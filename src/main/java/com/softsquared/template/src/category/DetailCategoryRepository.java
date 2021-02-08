package com.softsquared.template.src.category;

import com.softsquared.template.DBmodel.DetailCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailCategoryRepository extends JpaRepository<DetailCategory, Long> {

    boolean existsByIdAndCategoryId(Long id, Long categoryId);
}
