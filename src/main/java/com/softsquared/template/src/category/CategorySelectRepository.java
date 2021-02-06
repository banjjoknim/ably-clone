package com.softsquared.template.src.category;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.softsquared.template.DBmodel.Advertisement;
import com.softsquared.template.DBmodel.Category;
import com.softsquared.template.DBmodel.QCategory;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import com.softsquared.template.src.category.model.GetCategory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class CategorySelectRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CategorySelectRepository(JPAQueryFactory queryFactory){
        super(Category.class);
        this.queryFactory=queryFactory;
    }

    public List<GetCategory> findAllCategory(){
        QCategory category = QCategory.category;
        return queryFactory.select((Projections.constructor(GetCategory.class,
                category.id, category.name, category.dateCreated, category.dateUpdated)))
                .from(category)
                .fetch();

    }

}
