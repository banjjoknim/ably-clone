package com.softsquared.template.src.purchase;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.DBmodel.QPurchase;
import com.softsquared.template.DBmodel.QUserInfo;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import com.softsquared.template.src.purchase.model.GetPurchaseRes;
import com.softsquared.template.src.user.models.GetUserMyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class PurchaseSelectRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public PurchaseSelectRepository(JPAQueryFactory queryFactory){
        super(Advertisement.class);
        this.queryFactory=queryFactory;
    }

    /**
     * 마이페이지에서의 구매 수
     */
    public long findPurchaseCountByUserId(long userId){
        QPurchase purchase = QPurchase.purchase;
        return queryFactory.select(purchase.purId)
                .where(purchase.userId.eq(userId))
                .from(purchase)
                .fetchCount();

    }
    /**
     * purId 리스트 가져오기
     */
    public List<Long> findPurIdList(){
        QPurchase purchase = QPurchase.purchase;
        return queryFactory.select(purchase.purId)
                .from(purchase)
                .fetch();
    }

}
