package com.softsquared.template.src.category;

import com.softsquared.template.DBmodel.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetailCategoryRepository extends JpaRepository<DetailCategory, Long> {

    Optional<DetailCategory> findByIdAndCategoryId(Long id, Long categoryId);

    boolean existsById(Long id);
}
