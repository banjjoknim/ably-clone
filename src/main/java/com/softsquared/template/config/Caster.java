package com.softsquared.template.config;

import java.util.Date;

/**
 * Response넘겨줄 때 데이터 정제하기 위해
 */
public class Caster {

    /**
     * 날짜
     * 2021.01.01
     */
    public String dateToString(Date date){
        if(date == null){
            return "noDate";
        }
        String str = date.toString();
        //날짜만 가져오기
        String[] ymd= (str.substring(0,11)).split("-");

        String result = ymd[0]+"."+ymd[1]+"."+ymd[2];

        return result;
    }

    /**
     * 배송 상태 변환
     */
    public String statusToString(char status){
        String result;
        switch (status) {
            case 'T' :
                result = "주문 접수";
                break;
            case 'R' :
                result = "배송 준비";
                break;
            case 'C' :
                result = "배송 완료";
                break;

            case 'D' :
                result = "취소/반품";
                break;

            default : result = "input error";
        }
        return result;
    }

}
