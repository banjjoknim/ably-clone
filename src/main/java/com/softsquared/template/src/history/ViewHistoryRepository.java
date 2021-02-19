package com.softsquared.template.src.history;

import com.softsquared.template.DBmodel.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    @Query(value = "select vh from ViewHistory vh where vh.id.userId = :userId order by vh.dateUpdated desc ")
    List<ViewHistory> findLastViewedProduct(@Param("userId") Long userId);

    Optional<ViewHistory> findById_UserIdAndId_ProductId(Long userId, Long productId);
}
