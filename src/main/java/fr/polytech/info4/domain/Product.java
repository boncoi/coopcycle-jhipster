package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.polytech.info4.domain.enumeration.ProductType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private UUID productID;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price", nullable = false)
    private Float price;

    @Lob
    @Column(name = "product_image")
    private byte[] productImage;

    @Column(name = "product_image_content_type")
    private String productImageContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "products", "cooperative" }, allowSetters = true)
    private Merchant merchant;

    @ManyToMany(mappedBy = "idProducts")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "idProducts" }, allowSetters = true)
    private Set<Basket> idBaskets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getProductID() {
        return this.productID;
    }

    public Product productID(UUID productID) {
        this.productID = productID;
        return this;
    }

    public void setProductID(UUID productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductType getProductType() {
        return this.productType;
    }

    public Product productType(ProductType productType) {
        this.productType = productType;
        return this;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Float getPrice() {
        return this.price;
    }

    public Product price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public byte[] getProductImage() {
        return this.productImage;
    }

    public Product productImage(byte[] productImage) {
        this.productImage = productImage;
        return this;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getProductImageContentType() {
        return this.productImageContentType;
    }

    public Product productImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
        return this;
    }

    public void setProductImageContentType(String productImageContentType) {
        this.productImageContentType = productImageContentType;
    }

    public Merchant getMerchant() {
        return this.merchant;
    }

    public Product merchant(Merchant merchant) {
        this.setMerchant(merchant);
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Set<Basket> getIdBaskets() {
        return this.idBaskets;
    }

    public Product idBaskets(Set<Basket> baskets) {
        this.setIdBaskets(baskets);
        return this;
    }

    public Product addIdBasket(Basket basket) {
        this.idBaskets.add(basket);
        basket.getIdProducts().add(this);
        return this;
    }

    public Product removeIdBasket(Basket basket) {
        this.idBaskets.remove(basket);
        basket.getIdProducts().remove(this);
        return this;
    }

    public void setIdBaskets(Set<Basket> baskets) {
        if (this.idBaskets != null) {
            this.idBaskets.forEach(i -> i.removeIdProduct(this));
        }
        if (baskets != null) {
            baskets.forEach(i -> i.addIdProduct(this));
        }
        this.idBaskets = baskets;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productID='" + getProductID() + "'" +
            ", productName='" + getProductName() + "'" +
            ", productType='" + getProductType() + "'" +
            ", price=" + getPrice() +
            ", productImage='" + getProductImage() + "'" +
            ", productImageContentType='" + getProductImageContentType() + "'" +
            "}";
    }
}
