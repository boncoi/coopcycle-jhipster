package fr.polytech.info4.service.dto;

import fr.polytech.info4.domain.enumeration.CommandStatus;
import fr.polytech.info4.domain.enumeration.TypePayment;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Bill} entity.
 */
public class BillDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant date;

    @NotNull
    private TypePayment payment;

    @NotNull
    private CommandStatus status;

    @NotNull
    @DecimalMin(value = "0")
    private Float totalPrice;

    private BasketDTO idBasket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public TypePayment getPayment() {
        return payment;
    }

    public void setPayment(TypePayment payment) {
        this.payment = payment;
    }

    public CommandStatus getStatus() {
        return status;
    }

    public void setStatus(CommandStatus status) {
        this.status = status;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BasketDTO getIdBasket() {
        return idBasket;
    }

    public void setIdBasket(BasketDTO idBasket) {
        this.idBasket = idBasket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillDTO)) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, billDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", payment='" + getPayment() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", idBasket=" + getIdBasket() +
            "}";
    }
}
