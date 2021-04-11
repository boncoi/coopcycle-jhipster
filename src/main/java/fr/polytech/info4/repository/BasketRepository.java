package fr.polytech.info4.repository;

import fr.polytech.info4.domain.Basket;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Basket entity.
 */
@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    @Query(
        value = "select distinct basket from Basket basket left join fetch basket.idProducts",
        countQuery = "select count(distinct basket) from Basket basket"
    )
    Page<Basket> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct basket from Basket basket left join fetch basket.idProducts")
    List<Basket> findAllWithEagerRelationships();

    @Query("select basket from Basket basket left join fetch basket.idProducts where basket.id =:id")
    Optional<Basket> findOneWithEagerRelationships(@Param("id") Long id);
}
