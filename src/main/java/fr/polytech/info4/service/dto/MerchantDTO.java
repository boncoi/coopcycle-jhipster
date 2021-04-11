package fr.polytech.info4.service.dto;

import fr.polytech.info4.domain.enumeration.MerchantType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Merchant} entity.
 */
public class MerchantDTO implements Serializable {

    private Long id;

    @NotNull
    private String merchantName;

    @NotNull
    private String address;

    @NotNull
    private MerchantType merchantType;

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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MerchantType getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(MerchantType merchantType) {
        this.merchantType = merchantType;
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
        if (!(o instanceof MerchantDTO)) {
            return false;
        }

        MerchantDTO merchantDTO = (MerchantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, merchantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MerchantDTO{" +
            "id=" + getId() +
            ", merchantName='" + getMerchantName() + "'" +
            ", address='" + getAddress() + "'" +
            ", merchantType='" + getMerchantType() + "'" +
            ", imageProfil='" + getImageProfil() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", user=" + getUser() +
            ", cooperative=" + getCooperative() +
            "}";
    }
}
