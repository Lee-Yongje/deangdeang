package com.example.demo.entity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hospital {

	 @JsonProperty("FCLTY_NM") // 1004 약국
	    private String fclty_NM;
	    
	    @JsonProperty("CTGRY_ONE_NM") // 반려동물업
	    private String ctgry_ONE_NM;
	    
	    @JsonProperty("CTGRY_TWO_NM") // 반려의료
	    private String ctgry_TWO_NM;
	    
	    @JsonProperty("CTGRY_THREE_NM") // 동물병원
	    private String ctgry_THREE_NM;
	    
	    @JsonProperty("CTPRVN_NM") // 경기도
	    private String ctprvn_NM;
	    
	    @JsonProperty("SIGNGU_NM") // 고양시 덕양구
	    private String signgu_NM;
	    
	    @JsonProperty("LEGALDONG_NM") // 동산동
	    private String legaldong_NM;
	    
	    @JsonProperty("LNBR_NO") // 352-1 번지
	    private String lnbr_NO;
	    
	    @JsonProperty("ROAD_NM") // 동세로
	    private String road_NM;
	    
	    @JsonProperty("BULD_NO") // 건물번호
	    private String buld_NO;
	    
	    @JsonProperty("LC_LA") // 위도
	    private double lc_LA;
	    
	    @JsonProperty("LC_LO") // 경도
	    private double lc_LO;
	    
	    @JsonProperty("ZIP_NO") // 우편번호
	    private int zip_NO;
	    
	    @JsonProperty("RDNMADR_NM") // 경기도 고양시 덕양구 동세로 19
	    private String rdnmadr_NM;
	    
	    @JsonProperty("LNM_ADDR") // 경기도 고양시 덕양구 동산동 352-1
	    private String lnm_ADDR;
	    
	    @JsonProperty("TEL_NO") // 전화번호
	    private String tel_NO;
	    
	    @JsonProperty("RSTDE_GUID_CN") // 휴일
	    private String rstde_GUID_CN;
	    
	    @JsonProperty("OPER_TIME") // 영업시간
	    private String oper_TIME;
	    
	    @JsonProperty("PARKNG_POSBL_AT") // 주차가능
	    private String parkng_POSBL_AT;
	    
	    @JsonProperty("UTILIIZA_PRC_CN")
	    private String utiliiza_PRC_CN;
	    
	    @JsonProperty("PET_POSBL_AT")
	    private String pet_POSBL_AT;
	    
	    @JsonProperty("PET_INFO_CN")
	    private String pet_INFO_CN;
	    
	    @JsonProperty("ENTRN_POSBL_PET_SIZE_VALUE")
	    private String entrn_POSBL_PET_SIZE_VALUE;
	    
	    @JsonProperty("PET_LMTT_MTR_CN")
	    private String pet_LMTT_MTR_CN;
	    
	    @JsonProperty("IN_PLACE_ACP_POSBL_AT")
	    private String in_PLACE_ACP_POSBL_AT;
	    
	    @JsonProperty("OUT_PLACE_ACP_POSBL_AT")
	    private String out_PLACE_ACP_POSBL_AT;
	    
	    @JsonProperty("FCLTY_INFO_DC")
	    private String fclty_INFO_DC;
	    
	    @JsonProperty("PET_ACP_ADIT_CHRGE_VALUE")
	    private String pet_ACP_ADIT_CHRGE_VALUE;
	    
	    @JsonProperty("LAST_UPDT_DE") // 마지막 업데이트 날짜
	    private int last_UPDT_DE;
	    
	    @JsonProperty("HMPG_URL") // 홈페이지 url
	    private String hmpg_URL; 


}
