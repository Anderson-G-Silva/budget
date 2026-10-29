package dio.budget.budgetapp.infrastructure.http;

import dio.budget.budgetapp.application.DeleteByParametersUseCase;
import dio.budget.budgetapp.application.FindAllUseCase;
import dio.budget.budgetapp.application.FindByUserIdUseCase;
import dio.budget.budgetapp.application.ListGroupByCategoryByUserIdUseCase;
import dio.budget.budgetapp.application.ListTransactionByCategoryUseCase;
import dio.budget.budgetapp.application.PersistTransactionUseCase;
import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.infrastructure.http.request.TransactionRequest;
import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;
import dio.budget.budgetapp.infrastructure.persistence.entity.TransactionEntity;
import dio.budget.util.RelativeDate;
import dio.budget.util.mapper.MapperTransaction;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final FindByUserIdUseCase findByUserIdUseCase;
    private final FindAllUseCase findAllUseCase;
    private final ListTransactionByCategoryUseCase listTransactionByCategoryUseCase;
    private final ListGroupByCategoryByUserIdUseCase listGroupByCategoryByUserIdUseCase;
    private final DeleteByParametersUseCase deleteByParametersUseCase;
    private final RelativeDate relativeDate;


    private final TranscriptionModel transcriptionModel; // retorna texto a partir do áudio
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;
    private final MapperTransaction mapperTransaction;


    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 FindByUserIdUseCase findByUserIdUseCase,
                                 FindAllUseCase findAllUseCase,
                                 ListTransactionByCategoryUseCase listTransactionByCategoryUseCase,
                                 ListGroupByCategoryByUserIdUseCase listGroupByCategoryByUserIdUseCase,
                                 DeleteByParametersUseCase deleteByParametersUseCase,
                                 RelativeDate relativeDate,
                                 TranscriptionModel transcriptionModel,
                                 @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                 ChatClient.Builder chatClientBuilder,
                                 TextToSpeechModel textToSpeechModel, MapperTransaction mapperTransaction) throws IOException {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.findByUserIdUseCase = findByUserIdUseCase;
        this.findAllUseCase = findAllUseCase;
        this.listTransactionByCategoryUseCase = listTransactionByCategoryUseCase;
        this.listGroupByCategoryByUserIdUseCase = listGroupByCategoryByUserIdUseCase;
        this.deleteByParametersUseCase = deleteByParametersUseCase;
        this.relativeDate = relativeDate;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, deleteByParametersUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
        this.mapperTransaction = mapperTransaction;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody TransactionRequest request) {
        persistTransactionUseCase.execute(request.toInput());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionEntity> findByUserId(){
        return findByUserIdUseCase.execute().stream()
                .map(mapperTransaction::modelToEntity)
                .toList();
    }

    @GetMapping("/category")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<ListByCategory> listGroupByCategory(){
        return listGroupByCategoryByUserIdUseCase.execute();
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionEntity> findAllByCategory(@PathVariable Category category){
        return listTransactionByCategoryUseCase.execute(category).stream()
                .map(mapperTransaction::modelToEntity)
                .toList();
    }



    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionEntity> findAll(){
        return findAllUseCase.execute()
                .stream()
                .map(mapperTransaction::modelToEntity)
                .toList();
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        var resource = file.getResource();
        var userMessage = transcriptionModel.transcribe(resource);
        LocalDate date = relativeDate.converter(userMessage, LocalDate.now());
        if(Objects.isNull(date)){date =  LocalDate.now();}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataEmString = date.format(formatter);
        userMessage = userMessage+" date="+dataEmString;
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = textToSpeechModel.call(result);
        resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);


    }

    @DeleteMapping (value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Resource> transcribeToDelete(@RequestParam("file") MultipartFile file) {
        var resource = file.getResource();
        var userMessage = transcriptionModel.transcribe(resource);
        LocalDate date = relativeDate.converter(userMessage, LocalDate.now());
        if(Objects.isNull(date)){date =  LocalDate.now();}
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataEmString = date.format(formatter);
        userMessage = userMessage+" date="+dataEmString;
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = textToSpeechModel.call(result);
        resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);


    }
}
