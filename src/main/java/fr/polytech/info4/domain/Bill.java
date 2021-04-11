package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.polytech.info4.domain.enumeration.CommandStatus;
import fr.polytech.info4.domain.enumeration.TypePayment;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment", nullable = false)
    private TypePayment payment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommandStatus status;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total_price", nullable = false)
    private Float totalPrice;

    @JsonIgnoreProperties(value = { "user", "idProducts" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Basket idBasket;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bill id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDate() {
        return this.date;
    }

    public Bill date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public TypePayment getPayment() {
        return this.payment;
    }

    public Bill payment(TypePayment payment) {
        this.payment = payment;
        return this;
    }

    public void setPayment(TypePayment payment) {
        this.payment = payment;
    }

    public CommandStatus getStatus() {
        return this.status;
    }

    public Bill status(CommandStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public Float getTotalPrice() {
        return this.totalPrice;
    }

    public Bill totalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Basket getIdBasket() {
        return this.idBasket;
    }

    public Bill idBasket(Basket basket) {
        this.setIdBasket(basket);
        return this;
    }

    public void setIdBasket(Basket basket) {
        this.idBasket = basket;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", payment='" + getPayment() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalPrice=" + getTotalPrice() +
            "}";
    }
}
