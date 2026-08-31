package dio.budget.budgetapp.domain;

import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;

import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    List<Transaction> findByUserId();

    List<Transaction> findAll();

    List<Transaction> findByCategory(Category category);

    List<ListByCategory> ListGroupByCategoryByUserId();
}
