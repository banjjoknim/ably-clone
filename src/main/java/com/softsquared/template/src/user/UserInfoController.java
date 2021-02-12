package com.softsquared.template.src.user;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.BaseResponse;
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
    public BaseResponse<String> requestResponse(@RequestHeader("X-ACCESS-TOKEN") String token) {
        String isAvailable ="avaliable";
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String access_Token = token;
        try {
            try {
                URL url = new URL(reqURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Authorization", "Bearer " + access_Token);

                int responseCode = conn.getResponseCode();
                System.out.println("responseCode : " + responseCode);

                if (responseCode != 200)
                    isAvailable = "unAvaliable";


                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

//            JsonParser parser = new JsonParser();
//            JsonElement element = parser.parse(result);
//
//            String properties = element.getAsJsonObject().get("id").getAsString();
//            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
//
//            //String nickname = properties.getAsJsonObject().get("id").getAsString();
//            String email = kakao_account.getAsJsonObject().get("email").getAsString();
//
//            System.out.println(properties);


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return new BaseResponse<>(SUCCESS,isAvailable);

        }catch (Exception e){
            e.printStackTrace();
            return new BaseResponse<>(FAILED_TO_DELETE_USER,isAvailable);
        }

    }


    @ResponseBody
    @GetMapping("/check-ids")
    public BaseResponse<GetUserInfo> checkUserId(@RequestHeader("X-ACCESS-TOKEN") String token) {
        try{
           long userId= jwtService.getUserId();
            GetUserInfo userEmail = userInfoProvider.retrieveIsUser(userId);
            return new BaseResponse<>(SUCCESS,userEmail);

        }catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse<>(FAILED_TO_GET_USER);
        }

    }

    /**
     * 회원 가입
     *
     */

//    @ResponseBody
//    @PostMapping("/check-ids")
//    public BaseResponse<PostUserInfoRes> postUserInfo(@RequestHeader("X-ACCESS-TOKEN") String token,
//                                                      @RequestBody PostUserInfoReq param){
//        if(param.getPhoneNum().length()==0 || param.getPhoneNum()==null){
//            return new BaseResponse<>(NO_)
//        }
//        try{
//
//        }
//    }

}