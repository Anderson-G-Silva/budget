package dio.budget.budgetapp.application.input;

import dio.budget.budgetapp.domain.Category;
import dio.budget.budgetapp.domain.PaymentBrand;
import dio.budget.budgetapp.domain.PaymentMethod;
import dio.budget.budgetapp.domain.Subcategory;
import org.springframework.ai.tool.annotation.ToolParam;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PersistTransactionInput(@ToolParam(description = "Descrição do gasto") String description,
                                      @ToolParam(description = "Categoria do gasto") Category category,
                                      @ToolParam(description = "Subcategoria do gasto")Subcategory subcategory,
                                      @ToolParam(description = "Valor do gasto ")BigDecimal amount,
                                      @ToolParam(description = "Data da realização do gasto")LocalDate date,
                                      @ToolParam(description = "Meio de pagamento do gasto")PaymentMethod paymentMethod,
                                      @ToolParam(description = "Banco ou bandeira do cartão que foi realizado o gasto")PaymentBrand paymentBrand) {
}
