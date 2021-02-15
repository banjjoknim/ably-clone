package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    List<ProductOption> findProductOptionsByProductId(Long productId);
}
