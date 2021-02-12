package com.softsquared.template.src.favorite;

import com.softsquared.template.DBmodel.FavoriteProduct;
import com.softsquared.template.DBmodel.FavoriteProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FavoriteProductRepository extends JpaRepository<FavoriteProduct, Long> {

    @Query(value = "select fp from FavoriteProduct fp where fp.favoriteProductId = :favoriteProductId")
    Optional<FavoriteProduct> findByFavoriteProductId(@Param("favoriteProductId") FavoriteProductId favoriteProductId);

    @Transactional
    @Modifying
    @Query(value = "update FavoriteProduct fp set fp.liked = 'YES' where fp.favoriteProductId.userCode = :userCode and fp.favoriteProductId.productCode = :productCode")
    void updateFavoriteIsYes(@Param(value = "userCode") Long userCode, @Param(value = "productCode") Long productCode);

    @Transactional
    @Modifying
    @Query(value = "update FavoriteProduct fp set fp.liked = 'NO' where fp.favoriteProductId.userCode = :userCode and fp.favoriteProductId.productCode = :productCode")
    void updateFavoriteIsNo(@Param(value = "userCode") Long userCode, @Param(value = "productCode") Long productCode);

}
