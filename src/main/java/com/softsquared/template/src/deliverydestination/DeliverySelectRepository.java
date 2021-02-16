package com.softsquared.template.src.deliverydestination;


import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.DeliveryDestination;
import com.softsquared.template.DBmodel.QDeliveryDestination;
import com.softsquared.template.src.deliverydestination.model.DeleteDelivery;
import com.softsquared.template.src.deliverydestination.model.GetDelivery;
import com.softsquared.template.src.deliverydestination.model.GetMainDelivery;
import com.softsquared.template.src.deliverydestination.model.GetMainDeliveryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeliverySelectRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public DeliverySelectRepository(JPAQueryFactory queryFactory) {
        super(DeliveryDestination.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 회원의 배송지 목록 찾기
     * for retrieveUserDelivery
     */
    public List<GetDelivery> findDeliveryByUserId(long userId) {
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

    /**
     * 회원의 기본 배송지 찾기
     */
    public List<GetMainDelivery> findMainDeliveryByUserId(long userId) {
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;

        return queryFactory.select((Projections.constructor(GetMainDelivery.class,
                deliveryDestination.userName, deliveryDestination.address,
                deliveryDestination.detailAddress, deliveryDestination.phoneNum,
                deliveryDestination.desId

        )))
                .from(deliveryDestination)
                .where(deliveryDestination.userId.eq(userId), deliveryDestination.status.eq(0),
                        deliveryDestination.isMain.eq(1))
                .orderBy(deliveryDestination.dateCreated.desc())
                .fetch();
    }

    /**
     * 삭제할 배송지 정보 가져오기
     */
    public List<DeleteDelivery> findDeliveryByDesId(long desId) {
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;

        return queryFactory.select((Projections.constructor(DeleteDelivery.class,
                deliveryDestination.userId, deliveryDestination.detailAddress,
                deliveryDestination.phoneNum, deliveryDestination.userName,
                deliveryDestination.address, deliveryDestination.dateUpdated,
                deliveryDestination.dateCreated)))
                .from(deliveryDestination)
                .where(deliveryDestination.desId.eq(desId),
                        deliveryDestination.status.eq(0))
                .fetch();

    }

    /**
     * 해당 유저가 기본 배송지를 가지고 있는지 검사하기 위함
     */
    public List<Integer> findIsMainByUserId(long userId) {
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;

        return queryFactory.select(deliveryDestination.isMain)
                .from(deliveryDestination)
                .where(deliveryDestination.userId.eq(userId),
                        deliveryDestination.status.eq(0))
                .fetch();


    }

    /**
     * desId로 정보 가져오기
     */
    public List<GetDelivery> findDeliveryInfoByDesId(long desId) {
        QDeliveryDestination deliveryDestination = QDeliveryDestination.deliveryDestination;

        return queryFactory.select((Projections.constructor(GetDelivery.class,
                deliveryDestination.userName, deliveryDestination.address,
                deliveryDestination.detailAddress, deliveryDestination.phoneNum,
                deliveryDestination.isMain)))
                .from(deliveryDestination)
                .where(deliveryDestination.status.eq(0), deliveryDestination.desId.eq(desId))
                .fetch();
    }
}
