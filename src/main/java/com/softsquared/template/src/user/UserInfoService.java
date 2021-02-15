package com.softsquared.template.src.user;

import com.softsquared.template.DBmodel.UserInfo;
import com.softsquared.template.utils.JwtService;
import com.softsquared.template.config.secret.Secret;
import com.softsquared.template.utils.AES128;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.user.models.*;
import com.softsquared.template.utils.KakaoService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.sql.Timestamp;

import static com.softsquared.template.config.BaseResponseStatus.*;


@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoProvider userInfoProvider;
    private final KakaoService kakaoService;
    private final UserInfoSelectRepository selectRepository;
    private final JwtService jwtService;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService,
                           UserInfoSelectRepository userInfoSelectRepository,KakaoService kakaoService) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoProvider = userInfoProvider;
        this.jwtService = jwtService;
        this.kakaoService = kakaoService;
        this.selectRepository = userInfoSelectRepository;
    }
    /**
     * for login
     */
    public String createJWTToken(String kakaoToken) throws BaseException{
        long userId = kakaoService.userIdFromKakao(kakaoToken);
        String jwt = jwtService.createJwt(userId);
        return jwt;
    }

    /**
     * 회원 가입
     */
    @Transactional
    public String createUserInfo(PostUserInfoReq param, long userId) throws BaseException{

        UserInfo newUser;
        String userName = param.getNickname();

        String email = param.getEmail();
        String gender = param.getGender();
        String age = param.getAge();

        String phoneNum = param.getPhoneNum();
        int birthday = param.getBirthday();

        String dateCreated = (new Timestamp(System.currentTimeMillis())).toString();
        String dateUpdated = (new Timestamp(System.currentTimeMillis())).toString();

        newUser = new UserInfo(10,email,userName,phoneNum,birthday,
                0,0,"WELCOME",0,dateCreated,dateUpdated, gender, age);

        userInfoRepository.save(newUser);


        return Long.toString(userId);

    }

    /**
     * 회원 탈퇴
     */
    public Boolean deleteUserInfo(long userId) throws BaseException{
        DeleteUserInfo user;
        try{
            user = selectRepository.findDeleteUserByUserId(userId).get(0);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException(FAILED_TO_GET_USER);
        }
        String email = user.getEmail();
        String userNmae = user.getUserName();
        String phoneNum = user.getPhoneNum();
        int birthday = user.getBirthday();
        String height = user.getHeight();
        String weight = user.getWeight();
        String topSize = user.getTopSize();
        String bottomSize = user.getBottomSize();
        String shoeSize = user.getShoeSize();
        String refundBank = user.getRefundBank();
        String refundName= user.getRefundName();
        String refundAccount = user.getRefundAccount();
        int point = user.getPoint();
        int coupon = user.getCoupon();
        String userRank = user.getUserRank();
        int status = 1;
        String dateUpdated = user.getDateUpdated().toString();
        String dateCreated = user.getDateCreated().toString();
        String gender = user.getGender();
        String age = user.getAge();

        UserInfo deleteInfo = new UserInfo(userId, email,userNmae,phoneNum,birthday, height,weight,
                topSize,bottomSize,shoeSize,refundBank,refundName,refundAccount,point,coupon,
                userRank, status,dateUpdated,dateCreated,gender,age);

        userInfoRepository.save(deleteInfo);

        return true;
    }
}