package dio.budget.budgetapp.domain;

import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    List<Transaction> findByUserId();

    List<Transaction> findAll();

    List<Transaction> findByCategory(Category category);

    List<ListByCategory> ListGroupByCategoryByUserId();

    int deleteByParameters(
            Category category,
            Subcategory subcategory,
            BigDecimal amount,
            LocalDate date,
            LocalDate paymentDate,
            PaymentMethod paymentMethod,
            PaymentBrand paymentBrand
    );
}
