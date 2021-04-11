package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Consumer} entity.
 */
public class ConsumerDTO implements Serializable {

    private Long id;

    @NotNull
    private String address;

    @Lob
    private byte[] imageProfil;

    private String imageProfilContentType;

    @NotNull
    private String mobilePhone;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImageProfil() {
        return imageProfil;
    }

    public void setImageProfil(byte[] imageProfil) {
        this.imageProfil = imageProfil;
    }

    public String getImageProfilContentType() {
        return imageProfilContentType;
    }

    public void setImageProfilContentType(String imageProfilContentType) {
        this.imageProfilContentType = imageProfilContentType;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConsumerDTO)) {
            return false;
        }

        ConsumerDTO consumerDTO = (ConsumerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, consumerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConsumerDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
