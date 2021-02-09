package com.softsquared.template.src.user;

import com.softsquared.template.utils.JwtService;
import com.softsquared.template.config.secret.Secret;
import com.softsquared.template.utils.AES128;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.src.user.models.*;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}