package com.softsquared.template.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    // 1000 : 요청 성공
    SUCCESS(true, 1000, "요청에 성공하였습니다."),
    SUCCESS_READ_USERS(true, 1010, "회원 전체 정보 조회에 성공하였습니다."),
    SUCCESS_READ_USER(true, 1011, "회원 정보 조회에 성공하였습니다."),
    SUCCESS_POST_USER(true, 1012, "회원가입에 성공하였습니다."),
    SUCCESS_LOGIN(true, 1013, "로그인에 성공하였습니다."),
    SUCCESS_JWT(true, 1014, "JWT 검증에 성공하였습니다."),
    SUCCESS_DELETE_USER(true, 1015, "회원 탈퇴에 성공하였습니다."),
    SUCCESS_PATCH_USER(true, 1016, "회원정보 수정에 성공하였습니다."),
    SUCCESS_READ_SEARCH_USERS(true, 1017, "회원 검색 조회에 성공하였습니다."),

    //vivi
    SUCCESS_CHECK_DELIVERY_DESTINATION(true, 1100, "배송지를 입력해주세요."),
    SUCCESS_CHECK_REFUND_INFO(true, 1101, "환불받을 계좌를 입력해주세요"),


    // 2000 : Request 오류
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_USERID(false, 2001, "유저 아이디 값을 확인해주세요."),
    EMPTY_JWT(false, 2010, "JWT를 입력해주세요."),
    INVALID_TOKEN(false, 2011, "유효하지 않은 토큰입니다."),
    EMPTY_EMAIL(false, 2020, "이메일을 입력해주세요."),
    INVALID_EMAIL(false, 2021, "이메일 형식을 확인해주세요."),
    EMPTY_PASSWORD(false, 2030, "비밀번호를 입력해주세요."),
    EMPTY_CONFIRM_PASSWORD(false, 2031, "비밀번호 확인을 입력해주세요."),
    WRONG_PASSWORD(false, 2032, "비밀번호를 다시 입력해주세요."),
    DO_NOT_MATCH_PASSWORD(false, 2033, "비밀번호와 비밀번호확인 값이 일치하지 않습니다."),
    EMPTY_NICKNAME(false, 2040, "닉네임을 입력해주세요."),

    // colt
    NOT_FOUND_CATEGORY(false, 2050, "존재하지 않는 상위 카테고리입니다."),
    NOT_FOUND_DETAIL_CATEGORY(false, 2051, "존재하지 않는 하위 카테고리입니다."),
    NOT_FOUND_DETAIL_CATEGORY_BELONGED_CATEGORY(false, 2052, "해당 하위 카테고리의 상위 카테고리가 아닙니다."),
    FILTER_PRICE_MUST_BE_POSITIVE(false, 2053, "필터 설정 가격은 0보다 큰 정수여야 합니다."),
    FILTER_TALL_MUST_BE_POSITIVE(false, 2054, "필터 설정 키는 0보다 큰 정수여야 합니다."),
    NOT_FOUND_PRODUCT(false, 2055, "존재하지 않는 상품입니다."),
    NOT_FOUND_REVIEW(false, 2056, "존재하지 않는 리뷰입니다."),
    COMMENT_CAN_NOT_BE_EMPTY(false, 2057, "리뷰 내용을 입력해주세요."),
    SATISFACTION_CAN_NOT_BE_EMPTY(false, 2058, "만족도 평가를 입력해주세요."),
    PURCHASED_OPTIONS_CAN_NOT_BE_EMPTY(false, 2059, "구매 옵션을 입력해주세요."),
    SIZE_COMMENT_CAN_NOT_BE_EMPTY(false, 2060, "사이즈 평가를 입력해주세요."),
    COLOR_COMMENT_CAN_NOT_BE_EMPTY(false, 2061, "색상 평가를 입력해주세요."),
    PRODUCT_NAME_CAN_NOT_BE_EMPTY(false, 2062, "상품 이름을 입력해주세요."),
    CATEGORY_CAN_NOT_BE_EMPTY(false, 2063, "카테고리를 선택해주세요."),
    DETAIL_CATEGORY_CAN_NOT_BE_EMPTY(false, 2064, "상세 카테고리를 선택해주세요."),
    AGE_GROUP_CAN_NOT_BE_EMPTY(false, 2065, "연령대를 선택해주세요."),
    CLOTH_LENGTH_CAN_NOT_BE_EMPTY(false, 2066, "기장을 선택해주세요."),
    COLOR_CAN_NOT_BE_EMPTY(false, 2067, "색상을 선택해주세요."),
    FABRIC_CAN_NOT_BE_EMPTY(false, 2068, "소재감을 선택해주세요."),
    TALL_CAN_NOT_BE_EMPTY(false, 2069, "키를 입력해주세요."),
    FIT_CAN_NOT_BE_EMPTY(false, 2070, "핏감을 선택해주세요."),
    PRINT_CAN_NOT_BE_EMPTY(false, 2071, "프린트(무늬)를 선택해주세요."),
    MODEL_CAN_NOT_BE_EMPTY(false, 2072, "모델을 선택해주세요."),
    PRICE_CAN_NOT_BE_EMPTY(false, 2073, "가격을 입력해주세요."),
    DISCOUNT_RATE_CAN_NOT_BE_EMPTY(false, 2074, "할인율을 입력해주세요."),
    IS_ON_SALE_CAN_NOT_BE_EMPTY(false, 2075, "상품 판매 여부를 선택해주세요."),
    IS_PUBLIC_CAN_NOT_BE_EMPTY(false, 2076, "상품 공개 여부를 선택해주세요."),
    MARKET_NAME_CAN_NOT_BE_EMPTY(false, 2077, "마켓 이름을 입력해주세요."),
    DELIVERY_TYPE_CAN_NOT_BE_EMPTY(false, 2078, "배송 타입을 선택해주세요."),
    MARKET_TYPE_CAN_NOT_BE_EMPTY(false, 2079, "마켓 타입을 선택해주세요."),
    MARKET_IMAGE_CAN_NOT_BE_EMPTY(false, 2081, "마켓 이미지를 선택해주세요."),


    //vivi
    NOT_FOUND_USERS(false, 2080, "회원가입이 되어있지 않는 회원입니다."),



    //vivi
   // NOT_FOUND_USERS(false, 2500, "회원가입이 되어있지 않는 회원입니다."),
    EMPTY_PHONENUM(false, 2501, "핸드폰 번호를 입력해주세요."),

    NOT_FOUND_REFUND(false, 2502, "환불정보가 없습니"),


    INVALID_PHONENUM(false, 2550, "유효하지 않은 핸드폰 번호 형식입니다."),





    // 3000 : Response 오류
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    NOT_FOUND_USER(false, 3010, "존재하지 않는 회원입니다."),
    DUPLICATED_USER(false, 3011, "이미 존재하는 회원입니다."),
    FAILED_TO_GET_USER(false, 3012, "회원 정보 조회에 실패하였습니다."),
    FAILED_TO_POST_USER(false, 3013, "회원가입에 실패하였습니다."),
    FAILED_TO_LOGIN(false, 3014, "로그인에 실패하였습니다."),
    FAILED_TO_DELETE_USER(false, 3015, "회원 탈퇴에 실패하였습니다."),
    FAILED_TO_PATCH_USER(false, 3016, "개인정보 수정에 실패하였습니다."),

    //vivi
    FAILED_TO_GET_ADS(false, 3020, "광고 목록 조회에 실패하였습니다."),
    FAILED_TO_GET_CATEGORY(false, 3021, "카테고리 조회에 실패하였습니다."),
    FAILED_TO_GET_PURCHASE(false, 3022, "구매 내역 조회에 실패하였습니다."),
    FAILED_TO_GET_PURCHASE_PRODUCTS(false, 3023, "구매하신 제품 조회에 실패하였습니다."),
    FAILED_TO_GET_DELVIERY_DESTINATION(false, 3024, "배송지 조회에 실패하였습니다."),
    NOT_FOUND_DELIVERY_DESTINATION(false, 3025, "배송지를 입력해주세요."),
    FAILED_TO_DELETE_DELVIERY_DESTINATION(false, 3026, "배송지 조회에 실패하였습니다."),


    //colt
    NO_AUTHORITY(false, 3050, "권한이 없습니다."),
    DUPLICATED_MARKET_NAME(false, 3060, "중복된 마켓 이름입니다."),
    NOT_FOUND_MARKET(false, 3061, "존재하지 않는 마켓입니다."),


    // 4000 : Database 오류
    SERVER_ERROR(false, 4000, "서버와의 통신에 실패하였습니다."),
    DATABASE_ERROR(false, 4001, "데이터베이스 연결에 실패하였습니다.");

    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
