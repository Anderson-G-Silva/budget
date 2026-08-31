package dio.budget.budgetapp.application;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListTransactionByCategoryUseCase {
    private final TransactionRepository transactionRepository;

    public ListTransactionByCategoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> execute(Category category){
        return transactionRepository.findByCategory(category);
    }
}
