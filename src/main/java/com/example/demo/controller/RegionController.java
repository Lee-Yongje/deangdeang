package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.LoginFormDTO;
import com.example.demo.entity.Hospital;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RegionController {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    // JSON 형식의 데이터 Hospital객체의 list로 반환하는 메소드
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {

    		List<Hospital> hospitals = loadHospitals();
    		
			if(search!=null) {
			hospitals = hospitals.stream()
				.filter(hospital -> search.equals(hospital.getCtprvn_NM()))
				.collect(Collectors.toList());
			}
			
			int startIndex = page * size;
			int endIndex = Math.min(startIndex + size, hospitals.size());
			
			// 페이지에 해당하는 데이터만 추출
			List<Hospital> hospitalsPerPage = hospitals.subList(startIndex, endIndex);
			
			//페이징
		    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
		    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
		    int endPage = Math.min(startPage + pagingSize - 1, hospitals.size()/size); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
		    
			
			System.out.println("hospitalsPerpage: "+hospitalsPerPage.get(0));
			model.addAttribute("list", hospitalsPerPage);
			model.addAttribute("nowPage", page);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			//?이건뭐지?
			model.addAttribute("totalPage", (int) Math.floor((double) hospitals.size() / size));
			}
	    
    
    //병원 상세(지도 포함)
    @GetMapping("/region/hospitalDetail")
    public void hospitalDetailPage(Model model, @RequestParam String phone) throws IOException {
    	List<Hospital> hospitals = loadHospitals();
    	Hospital h = null;
    	for (Hospital hospital : hospitals) {
    	    if (phone.equals(hospital.getTel_NO())) {
    	        h = hospital;
    	        System.out.println(h);
    	        model.addAttribute("h", h);
    	        break;
    	    }else {
    	    	// 해당 전화번호를 가진 hospital을 찾지 못한 경우 처리 (미완)
    	    }
    	}
    	
    	
    	model.addAttribute("kakaoApiKey", kakaoApiKey);
    }
    
    
}
    
