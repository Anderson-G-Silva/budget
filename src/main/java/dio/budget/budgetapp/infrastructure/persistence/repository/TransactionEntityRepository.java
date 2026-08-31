package dio.budget.budgetapp.infrastructure.persistence.repository;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;
import dio.budget.budgetapp.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("SELECT t.category AS categoria, SUM(t.amount) AS total FROM transactions t WHERE t.userId = ?1 GROUP BY t.category")
    List<ListByCategory> ListGroupByCategoryByUserId(UUID id);

    List<TransactionEntity> findByUserId(UUID id);

    List<TransactionEntity> findAll();

    List<TransactionEntity> findAllByCategory(Category category);


}