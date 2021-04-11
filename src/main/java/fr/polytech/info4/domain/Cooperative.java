package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cooperative.
 */
@Entity
@Table(name = "cooperative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cooperative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "cooperative_name", nullable = false)
    private String cooperativeName;

    @OneToMany(mappedBy = "cooperative")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "products", "cooperative" }, allowSetters = true)
    private Set<Merchant> merchants = new HashSet<>();

    @OneToMany(mappedBy = "cooperative")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "cooperative" }, allowSetters = true)
    private Set<Courier> couriers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cooperative id(Long id) {
        this.id = id;
        return this;
    }

    public String getCooperativeName() {
        return this.cooperativeName;
    }

    public Cooperative cooperativeName(String cooperativeName) {
        this.cooperativeName = cooperativeName;
        return this;
    }

    public void setCooperativeName(String cooperativeName) {
        this.cooperativeName = cooperativeName;
    }

    public Set<Merchant> getMerchants() {
        return this.merchants;
    }

    public Cooperative merchants(Set<Merchant> merchants) {
        this.setMerchants(merchants);
        return this;
    }

    public Cooperative addMerchant(Merchant merchant) {
        this.merchants.add(merchant);
        merchant.setCooperative(this);
        return this;
    }

    public Cooperative removeMerchant(Merchant merchant) {
        this.merchants.remove(merchant);
        merchant.setCooperative(null);
        return this;
    }

    public void setMerchants(Set<Merchant> merchants) {
        if (this.merchants != null) {
            this.merchants.forEach(i -> i.setCooperative(null));
        }
        if (merchants != null) {
            merchants.forEach(i -> i.setCooperative(this));
        }
        this.merchants = merchants;
    }

    public Set<Courier> getCouriers() {
        return this.couriers;
    }

    public Cooperative couriers(Set<Courier> couriers) {
        this.setCouriers(couriers);
        return this;
    }

    public Cooperative addCourier(Courier courier) {
        this.couriers.add(courier);
        courier.setCooperative(this);
        return this;
    }

    public Cooperative removeCourier(Courier courier) {
        this.couriers.remove(courier);
        courier.setCooperative(null);
        return this;
    }

    public void setCouriers(Set<Courier> couriers) {
        if (this.couriers != null) {
            this.couriers.forEach(i -> i.setCooperative(null));
        }
        if (couriers != null) {
            couriers.forEach(i -> i.setCooperative(this));
        }
        this.couriers = couriers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cooperative)) {
            return false;
        }
        return id != null && id.equals(((Cooperative) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cooperative{" +
            "id=" + getId() +
            ", cooperativeName='" + getCooperativeName() + "'" +
            "}";
    }
}
