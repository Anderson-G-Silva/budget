package dio.budget.budgetapp.infrastructure.persistence.repository;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.PaymentBrand;
import dio.budget.budgetapp.domain.PaymentMethod;
import dio.budget.budgetapp.domain.Subcategory;
import dio.budget.budgetapp.infrastructure.http.response.ListByCategory;
import dio.budget.budgetapp.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, UUID> {

    @Query("SELECT t.category AS categoria, SUM(t.amount) AS total FROM transactions t WHERE t.userId = ?1 GROUP BY t.category")
    List<ListByCategory> ListGroupByCategoryByUserId(UUID id);

    List<TransactionEntity> findByUserId(UUID id);

    List<TransactionEntity> findAll();

    List<TransactionEntity> findAllByCategory(Category category);

    @Modifying
    @Query("""
    DELETE FROM transactions t
    WHERE t.userId = :userId
      AND (:category IS NULL OR t.category = :category)
      AND (:subcategory IS NULL OR t.subcategory = :subcategory)
      AND (:amount IS NULL OR t.amount = :amount)
      AND (:date IS NULL OR t.date = :date)
      AND (:paymentDate IS NULL OR t.paymentDate = :paymentDate)
      AND (:paymentMethod IS NULL OR t.paymentMethod = :paymentMethod)
      AND (:paymentBrand IS NULL OR t.paymentBrand = :paymentBrand)
    """)
    int deleteByFilters(@Param("userId") UUID userId,
                        @Param("category") Category category,
                        @Param("subcategory") Subcategory subcategory,
                        @Param("amount") BigDecimal amount,
                        @Param("date") LocalDate date,
                        @Param("paymentDate") LocalDate paymentDate,
                        @Param("paymentMethod") PaymentMethod paymentMethod,
                        @Param("paymentBrand") PaymentBrand paymentBrand);

    /*void deleteByUserIdAndCategoryAndSubcategoryAndAmountAndDateAndPaymentDateAndPaymentMethodAndPaymentBrand(
            UUID userId,
            Category category,
            Subcategory subcategory,
            BigDecimal amount,
            LocalDate date,
            LocalDate paymentDate,
            PaymentMethod paymentMethod,
            PaymentBrand paymentBrand
            );*/


}