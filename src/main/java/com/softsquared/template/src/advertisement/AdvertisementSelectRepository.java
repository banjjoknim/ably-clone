package com.softsquared.template.src.advertisement;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.DBmodel.QAdvertisement;
import com.softsquared.template.src.advertisement.model.GetAd;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdvertisementSelectRepository extends QuerydslRepositorySupport{
    private final JPAQueryFactory queryFactory;

    public AdvertisementSelectRepository(JPAQueryFactory queryFactory){
        super(Advertisement.class);
        this.queryFactory=queryFactory;
    }

    //onBoard==1인 광고들만 가져오기
    //onBoard ==1이면 페이지에 띄울 광고
    //onBoard ==0이면 페이지에 띄우지 않을 광고
    public List<GetAdRes> findByOnBoard(){
        QAdvertisement advertisement = QAdvertisement.advertisement;
        return queryFactory.select((Projections.constructor(GetAdRes.class,
                advertisement.adCode, advertisement.adImg, advertisement.adIndex)))
                .from(advertisement)
                .where(advertisement.isDeleted.eq(0), advertisement.isBoard.eq(1))
                .fetch();

    }
}
