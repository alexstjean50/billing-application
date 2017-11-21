package ca.ulaval.glo4002.billing.persistence.entity;

import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class PaymentMethodEntity implements Serializable
{
    private static final long serialVersionUID = 912564085785197079L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAYMENT_METHOD_ID")
    private long id;
    @Column
    private String bankAccount;
    @Enumerated(EnumType.STRING)
    private PaymentMethodSource source;

    public PaymentMethodEntity()
    {
    }

    public PaymentMethodEntity(long id, String bankAccount, PaymentMethodSource source)
    {
        this.id = id;
        this.bankAccount = bankAccount;
        this.source = source;
    }

    public long getId()
    {
        return this.id;
    }

    public String getBankAccount()
    {
        return this.bankAccount;
    }

    public PaymentMethodSource getSource()
    {
        return this.source;
    }
}
