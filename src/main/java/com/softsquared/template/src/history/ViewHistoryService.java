package com.softsquared.template.src.history;

import com.softsquared.template.DBmodel.ViewHistory;
import com.softsquared.template.DBmodel.ViewHistoryId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Transactional
@Service
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final JwtService jwtService;

    @Autowired
    public ViewHistoryService(ViewHistoryRepository viewHistoryRepository, JwtService jwtService) {
        this.viewHistoryRepository = viewHistoryRepository;
        this.jwtService = jwtService;
    }

    public ViewHistoryId updateViewHistory(String token, Long productId) throws BaseException {
        Long userId = 0L;
        if (token != null) {
            userId = jwtService.getUserId();
        }
        Optional<ViewHistory> viewHistory = viewHistoryRepository
                .findById_UserIdAndId_ProductId(userId, productId);
        if (viewHistory.isPresent()) {
            ViewHistory entity = viewHistory.get();
            entity.setDateUpdated(new Date());
            viewHistoryRepository.save(entity);
            return new ViewHistoryId(userId, productId);
        }
        ViewHistory newViewHistory = ViewHistory.builder()
                .id(new ViewHistoryId(userId, productId))
                .build();
        viewHistoryRepository.save(newViewHistory);

        return newViewHistory.getId();
    }
}
