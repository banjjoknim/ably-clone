package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query(value = "select pi.image from ProductImage pi where pi.productId = :productId and pi.type = 'THUMBNAIL'")
    List<String> findProductImageByProductId(@Param(value = "productId") Long productId);
}
