package com.softsquared.template.src.payment;

import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.src.payment.model.PostPaymentReq;
import com.softsquared.template.src.purchase.PurchaseController;
import com.softsquared.template.src.purchase.PurchaseService;
import com.softsquared.template.src.purchase.model.PostPurchaseReq;
import com.softsquared.template.src.purchase.model.PostPurchaseRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

import static com.softsquared.template.config.BaseResponseStatus.SUCCESS;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PurchaseService purchaseService;
    @Autowired
    public PaymentController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }

    @ResponseBody
    @PostMapping("/complete")
    public BaseResponse<String> postPayment(@RequestBody PostPaymentReq param){
        String id = param.getUid();
        int price = param.getPrice();
        System.out.println(id+"  "+ param.getPrice());

        //데이터가 들어왔다고 가정
        long purId = 1;
        ArrayList<String> options =new ArrayList<>();
        options.add("블랙");
        ArrayList<Integer> num = new ArrayList<>();
        num.add(1);
        long desId =1;
        long paymentCode = 1;
        int totalPrice=price;

        PostPurchaseReq purchase = new PostPurchaseReq(purId, options,num,
                desId,paymentCode,totalPrice);
        try{
            PostPurchaseRes postPurchaseRes = purchaseService.createPurchase(purchase,345);
            return new BaseResponse<>(SUCCESS,Integer.toString(totalPrice));
        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }


}
