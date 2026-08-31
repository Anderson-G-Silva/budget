package dio.budget.budgetapp.application;

import dio.budget.budgetapp.domain.Transaction;
import dio.budget.budgetapp.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindByUserIdUseCase {
    private final TransactionRepository transactionRepository;

    public FindByUserIdUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    //@Tool(name = "List-transactions-Logged_User", description = "Lista as transações financeiras do usuário logado")
    public List<Transaction> execute(){
        return transactionRepository.findByUserId();
    }

}
