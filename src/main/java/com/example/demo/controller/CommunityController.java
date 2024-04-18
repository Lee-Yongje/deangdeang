package com.example.demo.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.LoginFormDTO;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardCode;
import com.example.demo.entity.BoardId;
import com.example.demo.entity.RegionCode;
import com.example.demo.entity.Users;
import com.example.demo.service.BoardService;
import com.example.demo.service.UsersService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommunityController {
	
	@Autowired
	private BoardService bs;
	
	@Autowired
	private UsersService us;
	
	@Autowired // 파일 경로찾기용
	private ResourceLoader resourceLoader;
	
	//----------게시판형(자유,질문,모임)----------
	// 게시판 조회
	@GetMapping("/community/board")
	public String boardPage(int b_code, HttpSession session, Model model) {

		String b_name = bs.findBNameByBCode(b_code);
		model.addAttribute("b_name", b_name);
		
		if( b_code == 4 ) {
			model.addAttribute("list", bs.findClubByBcode(b_code));
			return "/community/boardClub";
		}
		model.addAttribute("list", bs.findByBcode(b_code));
		return "/community/board";
	}
	
	// 게시물 상세보기
	@GetMapping("/member/community/boardDetail/{b_code}/{bno}")
    public String boardPage(@PathVariable int b_code, @PathVariable int bno, Model model) {
		
		String b_name = bs.findBNameByBCode(b_code);
		bs.updateHit(bno, b_code);
		
		model.addAttribute("b", bs.detailBoard(bno, b_code));
		model.addAttribute("b_name", b_name);
		return "/member/community/boardDetail";
    } 
	
	

    
    
    //---------게시판형, 사진형 공통(X)---------------
    // 글 작성 페이지 이동
    @GetMapping("/member/community/boardInsert")
    public String boardInsert(String b_name, HttpSession session, Model model) {
    	int b_code = bs.findBCodeByBName(b_name);
    	model.addAttribute("b_code", b_code);
    	model.addAttribute("b_name", b_name);
    	
    	if( b_code == 4) {
    		return "/member/community/boardClubInsert";
    	}
    	return "/member/community/boardInsert";
    }
    
    // 글 작성 등록
    @PostMapping("/member/community/boardInsert/{b_code}/{u_name}")
    public String boardInsert(Board b, @PathVariable int b_code, @PathVariable String u_name ) {
    	
    	// 현재시간
    	LocalDateTime now = LocalDateTime.now();
    	
    	// 게시판번호에 따른 게시글번호 증가
    	int bno = bs.getNextBno(b_code);
    	
    	// RegionCode 객체 생성 및 설정
    	RegionCode regionCode = new RegionCode();
    	if(b_code == 4) {
    		regionCode.setRno(b.getRegionCode().getRno());
    	}else {
    		regionCode = null;
    	}
    	b.setRegionCode(regionCode);    		

		// BoardCode 객체 생성 및 설정
		if (b.getBoardcode() == null) {
			b.setBoardcode(new BoardCode());
		}
		b.getBoardcode().setB_code(b_code);
		
    	// 복합키 처리
    	BoardId boardId = new BoardId();
    	boardId.setB_code(b_code);
    	boardId.setBno(bno);
    	b.setId(boardId);

    	b.setB_date(now);
    	b.setOngoing(1);
    	
    	// 로그인 세션유지시 us.findUnoByUName 으로 수정
//    	long userId = bs.findByUName(u_name);
    	long userId = (long)102;
    	if (b.getUser() == null) {
			b.setUser(new Users());
		}
		b.getUser().setId(userId);
    	
		// 파일 관련
		MultipartFile uploadFile = b.getUploadFile();
		String fname = b.getUploadFile().getOriginalFilename();
		String path = null;
		Resource resource = resourceLoader.getResource("classpath:/static/images"); // 절대경로 찾기
		try {
			path = resource.getFile().getAbsolutePath();
			System.out.println(path);

		} catch (IOException e) {
			System.out.println("경로 가져오는 중 예외 발생:" + e);
		}

		if (fname != null && !fname.equals("")) {
			try {
				byte[] data = uploadFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path + "/" + fname);
				fos.write(data);
				fos.close();
				b.setB_fname(fname);
				System.out.println(path);
			} catch (IOException e) {
				System.out.println("파일등록예외발생:" + e.getMessage());
			}
		}
		
		bs.insertUsedgood(b);
		
		return "redirect:/community/board?b_code="+b_code;
    }
    
}
    
