package dio.budget.budgetapp.infrastructure.http.response;

import dio.budget.budgetapp.domain.Category;

import java.math.BigDecimal;

public record ListByCategory(Category category, BigDecimal amount)  {
}
