package com.softsquared.template.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.softsquared.template.config.BaseException;
import com.softsquared.template.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import static com.softsquared.template.config.BaseResponseStatus.*;

@Service
public class JwtService {
    /**
     * JWT 생성
     * @param userId
     * @return String
     */
    public String createJwt(int userId) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /**
     * Header에서 X-ACCESS-TOKEN 으로 JWT 추출
     * @return String
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /**
     * JWT에서 userId 추출
     * @return int
     * @throws BaseException
     */
    public long getUserId() throws BaseException {
        // 1. JWT 추출
        String access_Token = getJwt();
        if (access_Token == null || access_Token.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        HashMap<String, Object> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        long userId=0;


        try {
                URL url = new URL(reqURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");

                conn.setRequestProperty("Authorization", "Bearer " + access_Token);

                int responseCode = conn.getResponseCode();
            if(responseCode!=200){
                throw new BaseException(INVALID_TOKEN);
            }
                System.out.println("responseCode : " + responseCode);
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));


                String line = "";
                String result = "";

                while ((line = br.readLine()) != null) {
                    result += line;
                }
                System.out.println("response body : " + result);

                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(result);

                userId= element.getAsJsonObject().get("id").getAsLong();

                System.out.println(userId);


            }catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // 3. userId 추출
            // return claims.getBody().get("userId", Integer.class);
            return userId;
        }
    }

