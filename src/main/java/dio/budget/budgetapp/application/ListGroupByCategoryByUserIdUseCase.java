package dio.budget.budgetapp.application;

import dio.budget.budgetapp.domain.TransactionRepository;
import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListGroupByCategoryByUserIdUseCase {
    private final TransactionRepository transactionRepository;

    public ListGroupByCategoryByUserIdUseCase(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public List<ListByCategory> execute(){
        return transactionRepository.ListGroupByCategoryByUserId();
    }

}
