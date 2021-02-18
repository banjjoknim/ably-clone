package com.softsquared.template.src.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    public PaymentController(){}

    @ResponseBody
    @PostMapping("/complete")
    public String postPayment(){
//        String imp_uid = extract_POST_value_from_url('imp_uid') //post ajax request로부터 imp_uid확인
//
//        payment_result = rest_api_to_find_payment(imp_uid) //imp_uid로 아임포트로부터 결제정보 조회
//        amount_to_be_paid = query_amount_to_be_paid(payment_result.merchant_uid) //결제되었어야 하는 금액 조회. 가맹점에서는 merchant_uid기준으로 관리

        System.out.println("hi kakaopay");
        return "kakako";
    }


}
