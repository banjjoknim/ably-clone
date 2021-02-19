package com.softsquared.template.src.history;

import com.softsquared.template.DBmodel.ViewHistoryId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.history.models.PatchHistoryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.softsquared.template.config.BaseResponseStatus.PRODUCT_ID_CAN_NOT_BE_EMPTY;
import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/histories")
public class ViewHistoryController {

    private final ViewHistoryProvider viewHistoryProvider;
    private final ViewHistoryService viewHistoryService;

    @Autowired
    public ViewHistoryController(ViewHistoryProvider viewHistoryProvider, ViewHistoryService viewHistoryService) {
        this.viewHistoryProvider = viewHistoryProvider;
        this.viewHistoryService = viewHistoryService;
    }

    @PostMapping("/history")
    public BaseResponse<ViewHistoryId> patchViewHistory(@RequestHeader("X-ACCESS-TOKEN") String token, @RequestBody PatchHistoryReq request) {
        try {
            if (request.getProductId() == null) {
                throw new BaseException(PRODUCT_ID_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, viewHistoryService.updateViewHistory(token, request.getProductId()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
