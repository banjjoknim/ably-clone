package com.softsquared.template.config;

//request에 들어오는 값들이 올바른 형식인지 check
//ex ) null, 길이 check 가능

public class FormatChecker {

    /**
     * request 값이 채워져 왔는지 확인
     */
    public boolean isFull(String request){
        boolean isfull=true;

        if( request.length()==0 || request == null){
            isfull = false;
        }
        return isfull;
    }

    /**
     * 전화번호 형식 확인
     */
    public boolean isPhoneNum(String num){
        boolean isPhone=true;

        if(!num.contains("-"))
            isPhone = false;
        if(!num.substring(0,3).equals("010"))
            isPhone = false;


        return isPhone;
    }


    /**
     * 이메일 형식 확인
     */
    public boolean isEmail(String email){
        boolean result=true;

        if(!email.contains("@"))
            result = false;

        return result;
    }
}
