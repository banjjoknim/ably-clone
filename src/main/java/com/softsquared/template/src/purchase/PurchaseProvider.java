package com.softsquared.template.src.purchase;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.src.purchase.model.GetPurchaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PurchaseProvider {
    private final PurchaseSelectRepository purchaseSelectRepository;

    @Autowired
    public PurchaseProvider(PurchaseSelectRepository purchaseSelectRepository){
        this.purchaseSelectRepository =purchaseSelectRepository;
    }


}
