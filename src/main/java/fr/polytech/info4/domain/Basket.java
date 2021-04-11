package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Basket.
 */
@Entity
@Table(name = "basket")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Basket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "id_basket", nullable = false)
    private UUID idBasket;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_basket__id_product",
        joinColumns = @JoinColumn(name = "basket_id"),
        inverseJoinColumns = @JoinColumn(name = "id_product_id")
    )
    @JsonIgnoreProperties(value = { "merchant", "idBaskets" }, allowSetters = true)
    private Set<Product> idProducts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Basket id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getIdBasket() {
        return this.idBasket;
    }

    public Basket idBasket(UUID idBasket) {
        this.idBasket = idBasket;
        return this;
    }

    public void setIdBasket(UUID idBasket) {
        this.idBasket = idBasket;
    }

    public User getUser() {
        return this.user;
    }

    public Basket user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getIdProducts() {
        return this.idProducts;
    }

    public Basket idProducts(Set<Product> products) {
        this.setIdProducts(products);
        return this;
    }

    public Basket addIdProduct(Product product) {
        this.idProducts.add(product);
        product.getIdBaskets().add(this);
        return this;
    }

    public Basket removeIdProduct(Product product) {
        this.idProducts.remove(product);
        product.getIdBaskets().remove(this);
        return this;
    }

    public void setIdProducts(Set<Product> products) {
        this.idProducts = products;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Basket)) {
            return false;
        }
        return id != null && id.equals(((Basket) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Basket{" +
            "id=" + getId() +
            ", idBasket='" + getIdBasket() + "'" +
            "}";
    }
}
