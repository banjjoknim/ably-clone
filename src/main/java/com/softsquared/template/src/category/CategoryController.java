package com.softsquared.template.src.category;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.category.model.GetCategoryRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categorys")
public class CategoryController {

    private final CategoryProvider categoryProvider;

    @Autowired
    public CategoryController(CategoryProvider categoryProvider){
        this.categoryProvider = categoryProvider;
    }

    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategory()  {
        try {
            List<GetCategoryRes> list;
            list = categoryProvider.retrieveCategorys();
            return new BaseResponse<>(BaseResponseStatus.SUCCESS,list);
        }catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
