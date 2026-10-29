package dio.budget.budgetapp.application;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.PaymentBrand;
import dio.budget.budgetapp.domain.PaymentMethod;
import dio.budget.budgetapp.domain.Subcategory;
import dio.budget.budgetapp.domain.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class DeleteByParametersUseCase {
    private static final DateTimeFormatter BR_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final TransactionRepository transactionRepository;

    public DeleteByParametersUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "delete-transaction",
            description = "Deleta transações do usuário autenticado que correspondam aos filtros informados: "
                    + "categoria, subcategoria, valor da transação, data da transação (yyyy-MM-dd), "
                    + "data de pagamento (yyyy-MM-dd), forma de pagamento e bandeira do cartão. "
                    + "Informe apenas os filtros que o usuário mencionou e omita os demais. "
                    + "Retorna a quantidade apagada."
    )
    @Transactional
    public String execute(
            @ToolParam(description = "Categoria", required = false) Category category,
            @ToolParam(description = "Subcategoria", required = false) Subcategory subcategory,
            @ToolParam(description = "Valor da transação", required = false) BigDecimal amount,
            @ToolParam(description = "Data da transação, formato yyyy-MM-dd. Omita se não foi citada", required = false) String date,
            @ToolParam(description = "Data de pagamento, formato yyyy-MM-dd. Omita se não foi citada", required = false) String paymentDate,
            @ToolParam(description = "Forma de pagamento", required = false) PaymentMethod paymentMethod,
            @ToolParam(description = "Bandeira do cartão", required = false) PaymentBrand paymentBrand) {

        LocalDate parsedDate;
        LocalDate parsedPaymentDate;
        try {
            parsedDate = parseDate(date);
            parsedPaymentDate = parseDate(paymentDate);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        if (category == null && subcategory == null && amount == null && parsedDate == null
                && parsedPaymentDate == null && paymentMethod == null && paymentBrand == null) {
            return "Nenhum filtro informado. Peça ao usuário para especificar qual transação apagar.";
        }

        int removed = transactionRepository.deleteByParameters(
                category, subcategory, amount, parsedDate, parsedPaymentDate, paymentMethod, paymentBrand);
        return removed + " transação(ões) apagada(s).";
    }

    private static LocalDate parseDate(String value) {
        if (value == null || value.isBlank() || value.equalsIgnoreCase("null")
                || value.equalsIgnoreCase("undefined")) {
            return null;
        }
        String v = value.trim();
        try {
            return LocalDate.parse(v);                         // yyyy-MM-dd
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(v, BR_FORMAT);          // dd/MM/yyyy
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException(
                        "Data inválida: '" + v + "'. Use o formato yyyy-MM-dd.");
            }
        }
    }

}
