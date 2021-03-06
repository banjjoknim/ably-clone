package com.softsquared.template.src.model;

import com.softsquared.template.DBmodel.Model;
import com.softsquared.template.config.statusEnum.IsPublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    List<Model> findModelsByMarketIdAndIsPublic(Long marketId, IsPublic isPublic);
}
