package com.softsquared.template.src.model;

import com.softsquared.template.DBmodel.Model;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.market.MarketRepository;
import com.softsquared.template.src.model.models.PostModelReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_MARKET;

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
}
