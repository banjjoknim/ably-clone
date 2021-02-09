package com.softsquared.template.src.purchase;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.config.FormatChecker;
import com.softsquared.template.src.purchase.model.GetPurchaseRes;
import org.springframework.web.bind.annotation.*;
import static com.softsquared.template.config.BaseResponseStatus.*;


import java.util.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private PurchaseProvider purchaseProvider;
    private PurchaseService purchaseService;

    //request의 형식이 올바른지 check
    private FormatChecker formatChecker;

    public PurchaseController(PurchaseProvider purchaseProvider, PurchaseService purchaseService){
        this.purchaseProvider = purchaseProvider;
        this.purchaseService = purchaseService;

        formatChecker = new FormatChecker();
    }





}
