package com.softsquared.template.src.model;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.model.models.PostModelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping("")
    public BaseResponse<Long> postModel(@RequestBody PostModelReq request) {
        try {
            if (request.getModelName() == null) {
                throw new BaseException(MODEL_NAME_CAN_NOT_BE_EMPTY);
            }
            if (request.getModelImage() == null) {
                throw new BaseException(MODEL_IMAGE_CAN_NOT_BE_EMPTY);
            }
            if (request.getTall() == null) {
                throw new BaseException(MODEL_TALL_CAN_NOT_BE_EMPTY);
            }
            if (request.getTopSize() == null) {
                throw new BaseException(MODEL_TOP_SIZE_CAN_NOT_BE_EMPTY);
            }
            if (request.getBottomSize() == null) {
                throw new BaseException(MODEL_BOTTOM_SIZE_CAN_NOT_BE_EMPTY);
            }
            if (request.getShoeSize() == null) {
                throw new BaseException(MODEL_SHOE_SIZE_CAN_NOT_BE_EMPTY);
            }
            return new BaseResponse<>(SUCCESS, modelService.createModel(request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
