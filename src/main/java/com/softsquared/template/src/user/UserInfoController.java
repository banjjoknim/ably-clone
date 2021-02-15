package com.softsquared.template.src.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
import com.softsquared.template.config.BaseResponseStatus;
import com.softsquared.template.config.FormatChecker;
import com.softsquared.template.src.user.models.*;
import com.softsquared.template.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static com.softsquared.template.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/users")
public class UserInfoController {
    private final UserInfoProvider userInfoProvider;
    private final UserInfoService userInfoService;
    private final JwtService jwtService;
    private final FormatChecker formatChecker;

    @Autowired
    public UserInfoController(UserInfoProvider userInfoProvider,JwtService jwtService, UserInfoService userInfoService) {
        this.userInfoProvider = userInfoProvider;
        this.userInfoService = userInfoService;
        this.jwtService = jwtService;
        formatChecker = new FormatChecker();
    }

    /**
     * 나의 구매 내역 조회
     * 이후 토큰을 통한 본인 확인 구현 필요
     */
    @ResponseBody
    @GetMapping("/{userId}/purchases")
    public BaseResponse<GetUsersPurchaseRes> getUserPurchases (@PathVariable long userId){
        try{
            GetUsersPurchaseRes list = userInfoProvider.retrieveUserPurchases(userId);
            return new BaseResponse<>(SUCCESS,list);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @GetMapping("/check-tokens")
    public BaseResponse<Boolean> requestResponse(@RequestHeader("X-ACCESS-TOKEN") String token) {
        boolean isAvailable =true;
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String access_Token = token;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + access_Token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            if (responseCode != 200) {
                isAvailable = false;
                return new BaseResponse<>(INVALID_TOKEN,isAvailable);
            }


            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

        }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }

        try{
            if(isAvailable) {
                System.out.println("??");
                return new BaseResponse<>(SUCCESS, isAvailable);
            }
            else
                return new BaseResponse<>(INVALID_TOKEN, isAvailable);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(INVALID_TOKEN,isAvailable);
        }

    }


    @ResponseBody
    @GetMapping("/check-ids")
    public BaseResponse<Boolean> checkUserId(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try{
           long userId= jwtService.getUserId();
            GetUserInfo userEmail = userInfoProvider.retrieveIsUser(userId);
            return new BaseResponse<>(SUCCESS,true);

        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(NOT_FOUND_USER,false);
        }

    }

    /**
     * 회원 가입
     *
     */

    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> postUserInfo(@RequestHeader("X-ACCESS-TOKEN") String token,
                                                      @RequestBody PostUserInfoReq param){
        long userId;
        try{
            userId = jwtService.getUserId();
            System.out.println(userId);
        }catch(Exception e){
            e.printStackTrace();
            return new BaseResponse<>(INVALID_TOKEN);
        }

        if( !formatChecker.isFull(param.getNickname())){
            return new BaseResponse<>(EMPTY_NICKNAME);
        }
        if( !formatChecker.isFull(param.getPhoneNum())){
            return new BaseResponse<>(EMPTY_PHONENUM);
        }
        if( !formatChecker.isFull(param.getEmail())){
            return new BaseResponse<>(EMPTY_EMAIL);
        }


        //전화번호 확인
        if(!formatChecker.isPhoneNum(param.getPhoneNum()))
            return new BaseResponse<>(INVALID_PHONENUM);

        //이메일 확인
        if(!formatChecker.isEmail(param.getEmail()))
            return new BaseResponse<>(INVALID_EMAIL);


        try{
            String result = userInfoService.createUserInfo(param,userId);
            return new BaseResponse<>(SUCCESS, result);
        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 회원 탈퇴
     *
     */
    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<Boolean> deleteUserInfo(@RequestHeader("X-ACCESS-TOKEN") String token){
        long userId;
        try{
            userId = jwtService.getUserId();

        }catch(Exception e){
            return new BaseResponse<>(INVALID_TOKEN);
        }

        try{
            boolean result = userInfoService.deleteUserInfo(userId);
            return new BaseResponse<>(SUCCESS,result);
        }catch (BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 로그인
     * 카카오 토큰 받아오고 --> JWT토큰으로 바꿈
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<String> postLoginUser(@RequestHeader("X-ACCESS-TOKEN") String token){

        //token 없이 들어올 떄
        if (token == null || token.length() == 0) {
            return new BaseResponse(EMPTY_JWT);
        }

        try{
            String result = userInfoService.createJWTToken(token);
            return new BaseResponse<>(SUCCESS,result);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(FAILED_TO_LOGIN);
        }

    }
}