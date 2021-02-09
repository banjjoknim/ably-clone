package com.softsquared.template.src.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.DBmodel.QProduct;
import com.softsquared.template.DBmodel.QPurchase;
import com.softsquared.template.DBmodel.QPurchaseProduct;
import com.softsquared.template.src.user.models.GetUserPurchaseProduct;
import com.softsquared.template.src.user.models.GetUsersPurchase;
import com.softsquared.template.src.user.models.GetUsersPurchaseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserInfoSelectRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public UserInfoSelectRepository(JPAQueryFactory queryFactory){
        super(Advertisement.class);
        this.queryFactory=queryFactory;
    }


    /**
     * userId로 해당 회원의 구매 내역 조회
     * --> retrievePurchases
     */
    public List<GetUsersPurchase> findUsersPurchaseByUserId(long userId){
        QPurchase purchase = QPurchase.purchase;
        return queryFactory.select((Projections.constructor(GetUsersPurchase.class,
                purchase.purId, purchase.dateCreated)))
                .from(purchase)
                .where(purchase.userId.eq(userId))
                .orderBy(purchase.dateCreated.desc())
                .fetch();


    }

    /**
     * 해당 구매 내역에 포함되어 있는 제품들 조회
     */

    public List<GetUserPurchaseProduct> findProductByPurchaseId(long purId){
        QProduct product = QProduct.product;
        QPurchaseProduct purchaseProduct = QPurchaseProduct.purchaseProduct;

        return queryFactory.select((Projections.constructor(GetUserPurchaseProduct.class,
                product.id, purchaseProduct.purStatus, product.price,
                product.name, purchaseProduct.options)))
                .from(purchaseProduct)
                .join(product)
                .on(purchaseProduct.productId.eq(product.id))
                .where(purchaseProduct.purchaseId.eq(purId))
                .orderBy(purchaseProduct.dateCreated.desc())
                .fetch();
    }

    /**
     * 한 회원의 구매 현황 수 조회
     */
    public List<GetUsersPurchaseStatus> findStatusByUserId(long userId){
        QPurchaseProduct purchaseProduct =QPurchaseProduct.purchaseProduct;
        QPurchase purchase = QPurchase.purchase;
        return queryFactory.select((Projections.constructor(GetUsersPurchaseStatus.class,
                purchaseProduct.purStatus)))
                .from(purchaseProduct)
                .leftJoin(purchase)
                .on(purchase.purId.eq(purchaseProduct.purchaseId))
                .where(purchase.userId.eq(userId))
                .fetch();

    }
}

