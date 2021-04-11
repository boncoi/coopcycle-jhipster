package fr.polytech.info4.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Courier.
 */
@Entity
@Table(name = "courier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Courier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "working", nullable = false)
    private Boolean working;

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

    public Courier id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getWorking() {
        return this.working;
    }

    public Courier working(Boolean working) {
        this.working = working;
        return this;
    }

    public void setWorking(Boolean working) {
        this.working = working;
    }

    public byte[] getImageProfil() {
        return this.imageProfil;
    }

    public Courier imageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
        return this;
    }

    public void setImageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
    }

    public String getImageProfilContentType() {
        return this.imageProfilContentType;
    }

    public Courier imageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
        return this;
    }

    public void setImageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Courier mobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public User getUser() {
        return this.user;
    }

    public Courier user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public Courier cooperative(Cooperative cooperative) {
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
        if (!(o instanceof Courier)) {
            return false;
        }
        return id != null && id.equals(((Courier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Courier{" +
            "id=" + getId() +
            ", working='" + getWorking() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", imageProfilContentType='" + getImageProfilContentType() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            "}";
    }
}
