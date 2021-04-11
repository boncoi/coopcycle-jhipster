package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.polytech.info4.domain.enumeration.MerchantType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Merchant.
 */
@Entity
@Table(name = "merchant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "merchant_name", nullable = false)
    private String merchantName;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "merchant_type", nullable = false)
    private MerchantType merchantType;

    @Lob
    @Column(name = "image_profil")
    private byte[] imageProfil;

    @Column(name = "image_profil_content_type")
    private String imageProfilContentType;

    @NotNull
    @Column(name = "mobile_phone", nullable = false)
    private String mobilePhone;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "merchant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "merchant", "idBaskets" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "merchants", "couriers" }, allowSetters = true)
    private Cooperative cooperative;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Merchant id(Long id) {
        this.id = id;
        return this;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public Merchant merchantName(String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAddress() {
        return this.address;
    }

    public Merchant address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MerchantType getMerchantType() {
        return this.merchantType;
    }

    public Merchant merchantType(MerchantType merchantType) {
        this.merchantType = merchantType;
        return this;
    }

    public void setMerchantType(MerchantType merchantType) {
        this.merchantType = merchantType;
    }

    public byte[] getImageProfil() {
        return this.imageProfil;
    }

    public Merchant imageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
        return this;
    }

    public void setImageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
    }

    public String getImageProfilContentType() {
        return this.imageProfilContentType;
    }

    public Merchant imageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
        return this;
    }

    public void setImageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Merchant mobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public User getUser() {
        return this.user;
    }

    public Merchant user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public Merchant products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Merchant addProduct(Product product) {
        this.products.add(product);
        product.setMerchant(this);
        return this;
    }

    public Merchant removeProduct(Product product) {
        this.products.remove(product);
        product.setMerchant(null);
        return this;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setMerchant(null));
        }
        if (products != null) {
            products.forEach(i -> i.setMerchant(this));
        }
        this.products = products;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public Merchant cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Merchant)) {
            return false;
        }
        return id != null && id.equals(((Merchant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Merchant{" +
            "id=" + getId() +
            ", merchantName='" + getMerchantName() + "'" +
            ", address='" + getAddress() + "'" +
            ", merchantType='" + getMerchantType() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", imageProfilContentType='" + getImageProfilContentType() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            "}";
    }
}
