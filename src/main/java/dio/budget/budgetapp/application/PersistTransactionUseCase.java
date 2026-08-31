package dio.budget.budgetapp.application;

import dio.budget.budgetapp.application.input.PersistTransactionInput;
import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class PersistTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public PersistTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Tool(name = "persist-transaction", description = "Persiste transações financeiras")
    public void execute(PersistTransactionInput input) {
        var transaction =  new Transaction(input.description(),
                input.category(),
                input.subcategory(),
                input.amount(),
                input.date(),
                input.paymentMethod(),
                input.paymentBrand());
        transactionRepository.save(transaction);
    }



}
