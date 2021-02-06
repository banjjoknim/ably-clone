package com.softsquared.template.src.category;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.advertisement.model.GetAdRes;
import com.softsquared.template.src.category.model.GetCategory;
import com.softsquared.template.src.category.model.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CategoryProvider {
    public CategorySelectRepository categorySelectRepository;

    @Autowired
    public CategoryProvider(CategorySelectRepository categorySelectRepository){
        this.categorySelectRepository = categorySelectRepository;
    }

    public List<GetCategoryRes> retrieveCategorys() throws BaseException{
        List<GetCategory> categoryList;
        try{
            categoryList = categorySelectRepository.findAllCategory();
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(BaseResponseStatus.FAILED_TO_GET_CATEGORY);
        }

        return categoryToResList(categoryList);
    }

    public List<GetCategoryRes> categoryToResList ( List<GetCategory> list){
        List<GetCategoryRes> changedList;
        changedList = list.stream().map(GetCategory ->{
            long id = GetCategory.getCategoryCode();
            String name= GetCategory.getCategoryName();
            Date dateCreated = GetCategory.getDateCreated();
            String date= dateCreated.toString();
            boolean isNew = isNewCategory(date);
            return new GetCategoryRes(id,name,isNew);
        }).collect(Collectors.toList());

        return changedList;
    }

    //2020년 10월 이후 생긴 카테고리는 new처리하도록
    //만약 new 날짜의 기준을 바꾸고 싶다면 이 곳을 수정
    public boolean isNewCategory(String dateCreated){
        String[] date = dateCreated.split("-");
        boolean result;

        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);

        //2020년 10월 이후
        if(year>2020 || (year==2020 && month>=10)){
            result = true;
        }else
            result = false;
        return result;
    }

}
