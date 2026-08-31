package dio.budget.budgetapp.infrastructure.http.request;

import dio.budget.budgetapp.application.input.PersistTransactionInput;
import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.PaymentBrand;
import dio.budget.budgetapp.domain.PaymentMethod;
import dio.budget.budgetapp.domain.Subcategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(String description,
                                 Category category,
                                 Subcategory subcategory,
                                 BigDecimal amount,
                                 LocalDate date,
                                 PaymentMethod paymentMethod,
                                 PaymentBrand paymentBrand) {

    public PersistTransactionInput toInput() {

        return new PersistTransactionInput(description,
                category,
                subcategory,
                amount,
                date,
                paymentMethod,
                paymentBrand);
    }
}
