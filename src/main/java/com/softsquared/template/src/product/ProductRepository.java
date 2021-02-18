package com.softsquared.template.src.product;

import com.softsquared.template.DBmodel.Product;
import com.softsquared.template.config.statusEnum.IsPublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByCode(String code);

    Optional<Product> findFirstByIsPublicOrderByDateCreated(IsPublic isPublic);
}
