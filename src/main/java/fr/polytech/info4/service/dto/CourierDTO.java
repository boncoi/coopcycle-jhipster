package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Courier} entity.
 */
public class CourierDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean working;

    @Lob
    private byte[] imageProfil;

    private String imageProfilContentType;

    @NotNull
    private String mobilePhone;

    private UserDTO user;

    private CooperativeDTO cooperative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWorking() {
        return working;
    }

    public void setWorking(Boolean working) {
        this.working = working;
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

    public CooperativeDTO getCooperative() {
        return cooperative;
    }

    public void setCooperative(CooperativeDTO cooperative) {
        this.cooperative = cooperative;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourierDTO)) {
            return false;
        }

        CourierDTO courierDTO = (CourierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourierDTO{" +
            "id=" + getId() +
            ", working='" + getWorking() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", user=" + getUser() +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
