package ca.ulaval.glo4002.billing.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class AccountEntity implements Serializable
{
    private static final long serialVersionUID = 8411232795782403271L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ACCOUNT_ID")
    private long accountId;
    @Column(name = "CLIENT_ID", nullable = false, unique = true)
    private long clientId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private List<BillEntity> billEntities;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID")
    private List<PaymentEntity> paymentEntities;

    public AccountEntity()
    {
    }

    public AccountEntity(long accountId, long clientId, List<BillEntity> billEntities,
                         List<PaymentEntity> paymentEntities)
    {
        this.accountId = accountId;
        this.clientId = clientId;
        this.billEntities = billEntities;
        this.paymentEntities = paymentEntities;
    }

    public long getAccountId()
    {
        return this.accountId;
    }

    public long getClientId()
    {
        return this.clientId;
    }

    public List<BillEntity> getBillEntities()
    {
        return this.billEntities;
    }

    public List<PaymentEntity> getPaymentEntities()
    {
        return this.paymentEntities;
    }

    public void setAccountId(long accountId)
    {
        this.accountId = accountId;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public void setBillEntities(List<BillEntity> billEntities)
    {
        this.billEntities = billEntities;
    }

    public void setPaymentEntities(List<PaymentEntity> paymentEntities)
    {
        this.paymentEntities = paymentEntities;
    }
}
