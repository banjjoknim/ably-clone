package com.softsquared.template.src.deliverydestination;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.DeliveryDestination;
import com.softsquared.template.DBmodel.QDeliveryDestination;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliverySelectRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public DeliverySelectRepository(JPAQueryFactory queryFactory){
        super(DeliveryDestination.class);
        this.queryFactory=queryFactory;
    }
    /**
     * 회원의 배송지 목록 찾기
     * for retrieveUserDelivery
     */
    public List<GetDelivery> findDeliveryByUserId(long userId){
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;

        return queryFactory.select((Projections.constructor(GetDelivery.class,
                deliveryDestination.userName, deliveryDestination.address,
                deliveryDestination.detailAddress, deliveryDestination.phoneNum,
                deliveryDestination.isMain
        )))
                .from(deliveryDestination)
                .where(deliveryDestination.userId.eq(userId), deliveryDestination.status.eq(0))
                .orderBy(deliveryDestination.dateCreated.desc())
                .fetch();
    }
}
