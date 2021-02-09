package com.softsquared.template.src.purchase;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.DBmodel.QPurchase;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import com.softsquared.template.src.purchase.model.GetPurchaseRes;
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


}
