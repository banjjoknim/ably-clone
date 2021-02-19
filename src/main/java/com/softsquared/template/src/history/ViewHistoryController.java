package com.softsquared.template.src.history;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("histories")
public class ViewHistoryController {

    private final ViewHistoryProvider viewHistoryProvider;

    @Autowired
    public ViewHistoryController(ViewHistoryProvider viewHistoryProvider) {
        this.viewHistoryProvider = viewHistoryProvider;
    }

}
