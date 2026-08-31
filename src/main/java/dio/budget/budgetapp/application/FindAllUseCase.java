package dio.budget.budgetapp.application;

import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindAllUseCase {
    private final TransactionRepository transactionRepository;

    public FindAllUseCase(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    //@Tool(name = "List-All-transactions", description = "Lista todas as transações financeiras")
    public List<Transaction> execute(){
        return transactionRepository.findAll();
    }

}
