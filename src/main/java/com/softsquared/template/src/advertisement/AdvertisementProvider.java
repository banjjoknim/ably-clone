package com.softsquared.template.src.advertisement;

import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.advertisement.model.GetAd;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class AdvertisementProvider {

    private final AdvertisementSelectRepository advertisementSelectRepository;

    @Autowired
    public AdvertisementProvider(AdvertisementSelectRepository advertisementSelectRepository){
        this.advertisementSelectRepository = advertisementSelectRepository;
    }

    //홈 화면에 띄울 광고 리스트 가져오기
    public List<GetAdRes> retrieveAd(String word) throws BaseException {
        List<GetAdRes> getAdResList ;
        try{
            getAdResList = advertisementSelectRepository.findByOnBoard();
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_ADS);
        }

        //adIndex(화면에 나타낼 광고들의 순서) 대로 정렬해서 넘겨주기
        //뽀 클라이언트의 편의....
        Collections.sort(getAdResList, new Comparator<GetAdRes>() {
            @Override
            public int compare(GetAdRes o1, GetAdRes o2) {
                if(o1.getAdIndex()<o2.getAdIndex())
                    return -1;
                else
                    return 1;
            }
        });
        return getAdResList;
    }



}
