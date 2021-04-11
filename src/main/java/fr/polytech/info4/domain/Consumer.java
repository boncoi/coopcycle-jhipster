package fr.polytech.info4.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Consumer.
 */
@Entity
@Table(name = "consumer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Consumer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

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

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consumer id(Long id) {
        this.id = id;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public Consumer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImageProfil() {
        return this.imageProfil;
    }

    public Consumer imageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
        return this;
    }

    public void setImageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
    }

    public String getImageProfilContentType() {
        return this.imageProfilContentType;
    }

    public Consumer imageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
        return this;
    }

    public void setImageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Consumer mobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public User getUser() {
        return this.user;
    }

    public Consumer user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Consumer)) {
            return false;
        }
        return id != null && id.equals(((Consumer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Consumer{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", imageProfilContentType='" + getImageProfilContentType() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            "}";
    }
}
