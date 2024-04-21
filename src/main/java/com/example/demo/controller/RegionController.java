package com.example.demo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Hospital;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegionController {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    // JSON 형식의 데이터 Hospital을 객체의 list로 반환하는 메소드
    private List<Hospital> loadHospitals() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:static/data/hospital.json");
		ObjectMapper objectMapper = new ObjectMapper();	
		List<Hospital> hospitals = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Hospital>>() {});
		hospitals = hospitals.stream()
			.filter(hospital -> "동물병원".equals(hospital.getCtgry_THREE_NM()))
			.collect(Collectors.toList());
		
		return hospitals;
    }
    
    //병원 조회
    @GetMapping("/region/hospital")
    public void getHospitals(Model model,
							@RequestParam(required = false) String search,
							@RequestParam(required = false) String region1,
							@RequestParam(required = false) String region2,
				            @RequestParam(defaultValue = "1") int page,
            HttpSession session) throws IOException {
    		
    		String vsearch = null;
    		String vregion1 = null;
    		String vregion2 = null;
    		
    		//전체 병원 조회
    		List<Hospital> hospitals = loadHospitals();
    		
    		//검색어로 검색
    		if(session.getAttribute("search")!=null) {
    			vsearch=(String)session.getAttribute("search"); 
    		}
    		if(search!=null) {
				session.setAttribute("search", search);
				vsearch = search;
			}
			
			//시도로 검색
			if(session.getAttribute("region1")!=null) {
    			vregion1=(String)session.getAttribute("region1"); 
    		}
    		if(region1!=null) {
    			if(region1.equals("none")) {
    				region1=null;
    			}
				session.setAttribute("region1", region1);
				vregion1 = region1;
			}
    		
    		//시군구로 검색
    		if(session.getAttribute("region2")!=null) {
    			vregion2=(String)session.getAttribute("region2"); 
    		}
    		if(region2!=null) {
    			if(region2.equals("none")) {
    				region2=null;
    			}
				session.setAttribute("region2", region2);
				vregion2 = region2;
			}
    		
			
			//hospitas에서 해당 시도를 하나하나 찾아서 다시 hospitals에 넣기
			List<Hospital> filteredHospitals = new ArrayList<>();
			if (vregion1 != null) {
			    for (Hospital hospital : hospitals) {
			        if (hospital.getCtprvn_NM() != null && hospital.getCtprvn_NM().contains(vregion1)) {
			            filteredHospitals.add(hospital);
			        }
			    }
			} else {
			    filteredHospitals.addAll(hospitals);
			}
			hospitals = filteredHospitals;
			
			//hospitals에서 해당 시군구를 하나하나 찾아서 다시 hospitals에 넣기
			List<Hospital> filteredHospitals2 = new ArrayList<>();
			if (vregion2 != null) {
			    for (Hospital hospital : hospitals) {
			        if (hospital.getSigngu_NM() != null && hospital.getSigngu_NM().contains(vregion2)) {
			            filteredHospitals2.add(hospital);
			        }
			    }
			} else {
			    filteredHospitals2.addAll(hospitals);
			}
			hospitals = filteredHospitals2;
			
			//hospitals에서 검색 건을 하나하나 찾아서 다시 hospitals에 넣기
			List<Hospital> filteredHospitals3 = new ArrayList<>();
			if (vsearch != null) {
			    for (Hospital hospital : hospitals) {
			        if (hospital.getFclty_NM() != null && hospital.getFclty_NM().contains(vsearch)) {
			            filteredHospitals3.add(hospital);
			        }
			    }
			} else {
			    filteredHospitals3.addAll(hospitals);
			}
			hospitals = filteredHospitals3;

			int size=10;
			int startIndex = (page-1) * size;
			int endIndex = Math.min(startIndex + size, hospitals.size());
			
			System.out.println("startIndex:"+startIndex);
			System.out.println("endIndex:"+endIndex);
			
			// 페이지에 해당하는 데이터만 추출
			List<Hospital> hospitalsPerPage = hospitals.subList(startIndex, endIndex);
			
			//페이징
		    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
		    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
		    int totalPage =  (int) Math.ceil((double) hospitals.size() /size);
		    int endPage = Math.min(startPage + pagingSize - 1, totalPage); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
		    
			
			model.addAttribute("list", hospitalsPerPage);
			model.addAttribute("nowPage", page);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("totalPage",totalPage);
			}
	    
    
    //병원 상세(지도 포함)
    @GetMapping("/region/hospitalDetail")
    public void hospitalDetailPage(Model model,
    								@RequestParam String phone,
    								@RequestParam String h_name) throws IOException {
    
    	List<Hospital> hospitals = loadHospitals();
    	Hospital h = null;
    	for (Hospital hospital : hospitals) {
    		if(phone!=null&&!phone.equals("")) {
	    	    if (phone.equals(hospital.getTel_NO())){
	    	        h = hospital;
	    	        model.addAttribute("h", h);
	    	        break;
	    	    }
    		}else { //전화번호가 없는 경우 처리
    			if( h_name.equals(hospital.getFclty_NM())){
    				h = hospital;
	    	        model.addAttribute("h", h);
	    	        break;
    			}
    		}
    	}
    	model.addAttribute("kakaoApiKey", kakaoApiKey);
    }
    
    
}
    
