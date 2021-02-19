package com.softsquared.template.src.history;

import com.softsquared.template.DBmodel.ViewHistoryId;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.history.models.PostHistoryReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.softsquared.template.config.BaseResponseStatus.PRODUCT_ID_CAN_NOT_BE_EMPTY;
import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/histories")
public class ViewHistoryController {

    private final ViewHistoryService viewHistoryService;

    @Autowired
    public ViewHistoryController(ViewHistoryService viewHistoryService) {
        this.viewHistoryService = viewHistoryService;
    }

    @PostMapping("")
    public BaseResponse<ViewHistoryId> postViewHistory(@RequestHeader(value = "X-ACCESS-TOKEN", required = false) String token, @RequestBody PostHistoryReq request) {
        try {
            if (request.getProductId() == null) {
                throw new BaseException(PRODUCT_ID_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, viewHistoryService.createViewHistory(token, request.getProductId()));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
