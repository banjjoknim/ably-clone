package com.softsquared.template.src.model;

import com.softsquared.template.DBmodel.Market;
import com.softsquared.template.DBmodel.Model;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.statusEnum.IsPublic;
import com.softsquared.template.src.market.MarketRepository;
import com.softsquared.template.src.model.models.GetModelRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.softsquared.template.config.BaseResponseStatus.NOT_FOUND_MARKET;
import static java.util.stream.Collectors.toList;

@Service
public class ModelProvider {

    private final ModelRepository modelRepository;
    private final MarketRepository marketRepository;

    @Autowired
    public ModelProvider(ModelRepository modelRepository, MarketRepository marketRepository) {
        this.modelRepository = modelRepository;
        this.marketRepository = marketRepository;
    }

    public List<GetModelRes> retrieveMarketModels(Long marketId) throws BaseException {
        Optional<Market> market = marketRepository.findById(marketId);
        if (market.isEmpty()) {
            throw new BaseException(NOT_FOUND_MARKET);
        }
        List<Model> models = modelRepository.findModelsByMarketIdAndIsPublic(marketId, IsPublic.PUBLIC);

        return models.stream()
                .map(model -> new GetModelRes(model.getName(), model.getImage(), model.getTall(),
                        model.getTopSize(), model.getBottomSize(), model.getShoeSize()))
                .collect(toList());
    }
}
