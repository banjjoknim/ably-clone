package com.softsquared.template.src.user;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.softsquared.template.config.Caster;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.purchase.PurchaseProvider;
import com.softsquared.template.src.review.ReviewProvider;
import com.softsquared.template.src.user.models.*;
import com.softsquared.template.utils.JwtService;
import com.softsquared.template.utils.KakaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class UserInfoProvider {
    private UserInfoSelectRepository userInfoSelectRepository;
    private final JwtService jwtService;
    private final KakaoService kakaoService;
    private final ReviewProvider reviewProvider;
    private final PurchaseProvider purchaseProvider;
    private Caster caster;


    @Autowired
    public UserInfoProvider(UserInfoSelectRepository userInfoSelectRepository,JwtService jwtService,
                            KakaoService kakaoService,ReviewProvider reviewProvider,
                            PurchaseProvider purchaseProvider) {
        this.userInfoSelectRepository = userInfoSelectRepository;
        this.jwtService = jwtService;
        this.kakaoService = kakaoService;
        this.reviewProvider = reviewProvider;
        this.purchaseProvider = purchaseProvider;
        this.caster = new Caster();
    }
    /**
     * userId를 통해서 해당 회원이 구매 내역 조회
     */
    public GetUsersPurchaseRes retrieveUserPurchases(long userId) throws BaseException{
        GetUsersPurchaseRes purchaseRes;
        List<GetUsersPurchase> purchaseList;

        //주문 목록
        List<GetUsersPurchasesTemp> tempList;

        //배송 상태 표시를 위해
       GetUsersPurchaseProductStatusRes productStatus;

        try{
            purchaseList = userInfoSelectRepository.findUsersPurchaseByUserId(userId);

        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE);
        }

        tempList = productListToTempList(purchaseList);

        productStatus = retrievePurchaseStatus(userId);

        purchaseRes = new GetUsersPurchaseRes(productStatus,tempList);

        return purchaseRes;
    }

    /**
     * 특정 구매 내역에 포함된 제품들 조회
     */
    public List<GetUsersPurchaseProductRes> retrievePurchaseProduct(long purId) throws BaseException{
        List<GetUserPurchaseProduct> productList ;

        try{
            productList = userInfoSelectRepository.findProductByPurchaseId(purId);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE_PRODUCTS);
        }

        for(int i=0; i<productList.size();i++){
            System.out.println(productList.get(i).getProdId()+ "  "+productList.get(i));
        }

        return changePurProductToRes(productList);
    }

    /**
     * 하나의 구매 내역의 (날짜+ 제품) 한번에 조회 가능하도록 구성
     */

    public List<GetUsersPurchasesTemp> productListToTempList(List<GetUsersPurchase> list) {
        List<GetUsersPurchasesTemp> changedList;

        changedList = list.stream().map( GetUsersPurchase ->{
            long purId = GetUsersPurchase.getPurId();
            String dateCreated = caster.dateToString(GetUsersPurchase.getDateCreated());

            List<GetUsersPurchaseProductRes> purProductList = null;
            try {
                purProductList = retrievePurchaseProduct(purId);
            }catch (BaseException e){
                e.printStackTrace();
                try {
                    throw new BaseException(e.getStatus());
                } catch (BaseException baseException) {
                    baseException.printStackTrace();
                }
            }


            return new GetUsersPurchasesTemp(purId, dateCreated, purProductList);

                }).collect(Collectors.toList());
        return changedList;

    }

    /**
     * 현재 주문 내역 수 현황
     */
    public GetUsersPurchaseProductStatusRes retrievePurchaseStatus(long userId) throws BaseException{
        List<GetUsersPurchaseStatus> statusList;

        int ready =0;
        int complete = 0;
        int delete =0;


        GetUsersPurchaseProductStatusRes result;
        try{
            statusList = userInfoSelectRepository.findStatusByUserId(userId);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_PURCHASE_PRODUCTS);
        }
        for(int i=0;i<statusList.size();i++){
            char status = statusList.get(i).getPurState();
            if(status=='R')
                ready++;
            else if(status == 'C')
                complete++;
            else
                delete++;
        }

        result = new GetUsersPurchaseProductStatusRes(ready, complete, delete);
        return result;
    }


    /**
     * for PurchaseController --> 환불 계좌 조회
     */
//    public Get


    /**
     * 회원가입 유무 확인
     */
    public GetUserInfo retrieveIsUser(long userId) throws BaseException{
        GetUserInfo userInfo ;
        try{
            userInfo= userInfoSelectRepository.findUserByUserId(userId).get(0);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(NOT_FOUND_USERS);
        }
        return userInfo;
    }


    /**
     * 환불계좌 정보
     */
    public GetUserRefund retrieveRefundInfo(long userId) throws BaseException{
        GetUserRefund refundInfo;
        try{
            refundInfo = userInfoSelectRepository.findRefundByUserId(userId).get(0);
        }catch(Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_USER);
        }
        return refundInfo;
    }


    /**
     * 회원의 마이페이지 조회
     */
    public GetUserMyPageRes retireveMyPage(long userId) throws BaseException{
        GetUserMyPage getUserMyPage;
        try{
            getUserMyPage = userInfoSelectRepository.findMyPageByUserId(userId).get(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_USER);
        }
        String name = getUserMyPage.getName();
        String rank = getUserMyPage.getRank();
        int point= getUserMyPage.getPoint();
        int coupon = getUserMyPage.getCoupon();

        long purchase = purchaseProvider.retrievePurchaseCount(userId);
        long review = reviewProvider.retrieveUserReview(userId);

        return new GetUserMyPageRes(name, rank,point,coupon,purchase,review);

    }

    /*********************************************changedList




    /**
     * GetUserPurchaseProduct --> GetUserPurchasesProductRes
     */
    public List<GetUsersPurchaseProductRes> changePurProductToRes (List<GetUserPurchaseProduct> list){
        List<GetUsersPurchaseProductRes> changedList;
        changedList = list.stream().map( GetUserPurchaseProduct ->{
                long prodId = GetUserPurchaseProduct.getProdId();
                String purState = caster.statusToString(GetUserPurchaseProduct.getPurState());
                int price = GetUserPurchaseProduct.getPrice();
                String prodName = GetUserPurchaseProduct.getProdName();
                String option = GetUserPurchaseProduct.getOption();

                return new GetUsersPurchaseProductRes(prodId,purState,price,prodName,option);
        }).collect(Collectors.toList());

        return changedList;
    }



}
