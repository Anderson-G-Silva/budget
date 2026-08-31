package dio.budget.budgetapp.infrastructure.persistence.entity;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.PaymentBrand;
import dio.budget.budgetapp.domain.PaymentMethod;
import dio.budget.budgetapp.domain.Subcategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Subcategory subcategory;

    @Column(nullable = false)
    @Positive
    private BigDecimal amount;

    @Column(nullable = false, updatable = false )
    private LocalDate date;

    @Column(nullable = false, updatable = false )
    private LocalDate paymentDate;

    @Column(nullable = false, updatable = false )
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, updatable = false )
    @Enumerated(EnumType.STRING)
    private PaymentBrand paymentBrand;

    @JdbcTypeCode(SqlTypes.CHAR)
    @CreatedBy
    @Column(nullable = false, updatable = false )
    private UUID userId;


    @Column(nullable = false, updatable = false )
    @CreationTimestamp
    private Instant createdAt;

    @PrePersist
    @PreUpdate
    private void consistency() {
        // Consistência subcategoria x categoria
        if (this.subcategory != null && this.subcategory.getParentCategory() != this.category) {
            throw new IllegalStateException(String.format(
                    "Inconsistência detectada! A subcategoria '%s' não pertence à categoria '%s'.",
                    subcategory, category
            ));
        }
        // Consistência meio de pagamento x banco/bandeira
        if (this.paymentBrand != null && this.paymentBrand.getParentMethod() != this.paymentMethod) {
            throw new IllegalStateException(String.format(
                    "Inconsistência detectada! O banco/bandeira '%s' não pertence ao meio de pagamento '%s'.",
                    paymentBrand, paymentMethod
            ));
        }
    }


}
