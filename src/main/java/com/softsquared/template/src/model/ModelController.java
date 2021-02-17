package com.softsquared.template.src.model;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.model.models.PatchModelReq;
import com.softsquared.template.src.model.models.PostModelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            validateModelRequest(request.getModelName(), request.getModelImage(), request.getTall(),
                    request.getTopSize(), request.getBottomSize(), request.getShoeSize());
            return new BaseResponse<>(SUCCESS, modelService.createModel(request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @PatchMapping("/{modelId}")
    public BaseResponse<Long> patchModel(@PathVariable Long modelId, @RequestBody PatchModelReq request) {
        try {
            validateModelRequest(request.getModelName(), request.getModelImage(), request.getTall(),
                    request.getTopSize(), request.getBottomSize(), request.getShoeSize());
            return new BaseResponse<>(SUCCESS, modelService.updateModel(modelId, request));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("/{modelId}")
    public BaseResponse<Long> deleteModel(@PathVariable Long modelId) {
        try {
            return new BaseResponse<>(SUCCESS, modelService.deleteModel(modelId));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    private void validateModelRequest(String modelName, String modelImage, Integer tall, Integer topSize, Integer bottomSize, Integer shoeSize) throws BaseException {
        if (modelName == null) {
            throw new BaseException(MODEL_NAME_CAN_NOT_BE_EMPTY);
        }
        if (modelImage == null) {
            throw new BaseException(MODEL_IMAGE_CAN_NOT_BE_EMPTY);
        }
        if (tall == null) {
            throw new BaseException(MODEL_TALL_CAN_NOT_BE_EMPTY);
        }
        if (topSize == null) {
            throw new BaseException(MODEL_TOP_SIZE_CAN_NOT_BE_EMPTY);
        }
        if (bottomSize == null) {
            throw new BaseException(MODEL_BOTTOM_SIZE_CAN_NOT_BE_EMPTY);
        }
        if (shoeSize == null) {
            throw new BaseException(MODEL_SHOE_SIZE_CAN_NOT_BE_EMPTY);
        }
    }

}
