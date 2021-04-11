package fr.polytech.info4.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.polytech.info4.domain.Basket} entity.
 */
public class BasketDTO implements Serializable {

    private Long id;

    @NotNull
    private UUID idBasket;

    private UserDTO user;

    private Set<ProductDTO> idProducts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdBasket() {
        return idBasket;
    }

    public void setIdBasket(UUID idBasket) {
        this.idBasket = idBasket;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ProductDTO> getIdProducts() {
        return idProducts;
    }

    public void setIdProducts(Set<ProductDTO> idProducts) {
        this.idProducts = idProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BasketDTO)) {
            return false;
        }

        BasketDTO basketDTO = (BasketDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, basketDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BasketDTO{" +
            "id=" + getId() +
            ", idBasket='" + getIdBasket() + "'" +
            ", user=" + getUser() +
            ", idProducts=" + getIdProducts() +
            "}";
    }
}
