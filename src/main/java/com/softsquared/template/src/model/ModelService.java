package com.softsquared.template.src.model;

import com.softsquared.template.DBmodel.Model;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.market.MarketRepository;
import com.softsquared.template.src.model.models.PatchModelReq;
import com.softsquared.template.src.model.models.PostModelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final MarketRepository marketRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository, MarketRepository marketRepository) {
        this.modelRepository = modelRepository;
        this.marketRepository = marketRepository;
    }

    public Long createModel(PostModelReq request) throws BaseException {
        Long marketIdFromToken = 3L; // todo: 토큰으로부터 마켓 ID 추출해야 함.
        if (marketRepository.findById(marketIdFromToken).isEmpty()) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        Model model = Model.builder()
                .name(request.getModelName())
                .image(request.getModelImage())
                .tall(request.getTall())
                .topSize(request.getTopSize())
                .bottomSize(request.getBottomSize())
                .shoeSize(request.getShoeSize())
                .marketId(marketIdFromToken)
                .build();
        modelRepository.save(model);

        return model.getId();
    }

    public Long updateModel(Long modelId, PatchModelReq request) throws BaseException {
        Long marketIdFromToken = 3L; // todo: 토큰으로부터 마켓 ID 추출해야 함.
        if (marketRepository.findById(marketIdFromToken).isEmpty()) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        Optional<Model> model = modelRepository.findById(modelId);
        if (model.isEmpty()) {
            throw new BaseException(NOT_FOUND_MODEL);
        }
        if (!model.get().getMarketId().equals(marketIdFromToken)) {
            throw new BaseException(NO_AUTHORITY);
        }
        model.ifPresent(selectedModel -> {
            selectedModel.setName(request.getModelName());
            selectedModel.setImage(request.getModelImage());
            selectedModel.setTall(request.getTall());
            selectedModel.setTopSize(request.getTopSize());
            selectedModel.setBottomSize(request.getBottomSize());
            selectedModel.setShoeSize(request.getShoeSize());
            modelRepository.save(selectedModel);
        });

        return modelId;
    }
}
