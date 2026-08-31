package dio.budget.budgetapp.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class Transaction {
    private TransactionId id;
    private String description;
    private Category category;
    private Subcategory subcategory;
    private BigDecimal amount;
    private LocalDate date;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private PaymentBrand paymentBrand;
    private UUID userId;
    private Instant createdAt;

    public Transaction(String description, Category category, Subcategory subcategory, BigDecimal amount, LocalDate date, PaymentMethod paymentMethod, PaymentBrand paymentBrand) {
        this.id = new TransactionId();
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.paymentBrand = paymentBrand;
        this.paymentDate = calculatePaymentDate();
    }

    private LocalDate calculatePaymentDate() {
        if (this.paymentMethod == PaymentMethod.DÉBITO_PIX || this.paymentMethod == PaymentMethod.DINHEIRO) {
            return this.date;
        } else if (this.paymentBrand.getClosingInvoiceDate() < this.paymentBrand.getInvoicePaymentDate()) {
            return this.date
                    .plusMonths(1)
                    .withDayOfMonth(this.paymentBrand.getInvoicePaymentDate());
        } else {
            return this.date
                    .plusMonths(2)
                    .withDayOfMonth(this.paymentBrand.getInvoicePaymentDate());
        }
    }

    @Builder(builderMethodName = "restore")
    public Transaction(TransactionId id, String description, Category category, Subcategory subcategory,
                       BigDecimal amount, LocalDate date, LocalDate paymentDate,
                       PaymentMethod paymentMethod, PaymentBrand paymentBrand,
                       UUID userId, Instant createdAt) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.subcategory = subcategory;
        this.amount = amount;
        this.date = date;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.paymentBrand = paymentBrand;
        this.userId = userId;
        this.createdAt = createdAt;
    }

}

