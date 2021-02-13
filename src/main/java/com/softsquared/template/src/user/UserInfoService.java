package com.softsquared.template.src.user;

import com.softsquared.template.DBmodel.UserInfo;
import com.softsquared.template.utils.JwtService;
import com.softsquared.template.config.secret.Secret;
import com.softsquared.template.utils.AES128;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.user.models.*;
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
    private final JwtService jwtService;

    @Autowired
    public UserInfoService(UserInfoRepository userInfoRepository, UserInfoProvider userInfoProvider, JwtService jwtService) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoProvider = userInfoProvider;
        this.jwtService = jwtService;
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
}