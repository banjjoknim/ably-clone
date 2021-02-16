package com.softsquared.template.config;

import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * 원 , 추가
     */
    public String priceString(int price){
        String[] str = (Integer.toString(price)).split("");
        ArrayList<String> arr = new ArrayList<>();
        String result="";
        for(int i= str.length-1;i>=0;i--){
            arr.add(str[i]);
            if(i%3==2)
                arr.add(",");
        }
        if(arr.get(arr.size()-1).equals(","))
            arr.remove(arr.size()-1);
        Collections.sort(arr, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -1;
            }
        });
        for(int i=0;i<arr.size();i++){
            result+=arr.get(i);
        }
        return result+"원";
    }

}
