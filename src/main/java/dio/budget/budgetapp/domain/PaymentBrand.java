package dio.budget.budgetapp.domain;

import lombok.Getter;

@Getter
public enum PaymentBrand {
    DINHEIRO(PaymentMethod.DINHEIRO,0,0),
    SANTANDER(PaymentMethod.DÉBITO_PIX,0,0),
    BB(PaymentMethod.DÉBITO_PIX,0,0),
    CEF(PaymentMethod.DÉBITO_PIX,0,0),
    VISA(PaymentMethod.CRÉDITO,25,5),
    MASTERCARD(PaymentMethod.CRÉDITO,5,15),
    ELO(PaymentMethod.CRÉDITO,15,25);

    private final int closingInvoiceDate;
    private final int invoicePaymentDate;
    private PaymentMethod  parentMethod;

    PaymentBrand(PaymentMethod parentMethod, int closingInvoiceDate, int invoicePaymentDate ) {
        this.parentMethod = parentMethod;
        this.closingInvoiceDate = closingInvoiceDate;
        this.invoicePaymentDate = invoicePaymentDate;
    }
}
