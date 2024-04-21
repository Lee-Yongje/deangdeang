package com.example.demo.controller;

import java.io.File;
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
	// 자유, 질문 게시판 조회
	@GetMapping("/community/board/{b_code}")
	public String boardPage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@PathVariable int b_code,
			HttpSession session, Model model) {

		String b_name = bs.findBNameByBCode(b_code);
		// 한페이지에 5개씩 (테스트)
		int pageSize = 5; 
		Pageable pageable = PageRequest.of(page-1, pageSize);
		Page<List<Map<String ,Object>>> list = bs.findByBcode(b_code, pageable);

		 //페이징
	    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
	    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
	    int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
		
		model.addAttribute("b_name", b_name);
		model.addAttribute("b_code", b_code);
		model.addAttribute("list", list);
		model.addAttribute("nowPage",page);
	    model.addAttribute("startPage",startPage);
	    model.addAttribute("endPage",endPage);
	    model.addAttribute("totalPage",list.getTotalPages());
		return "/community/board";
	}
	
	// 모임 게시판 조회
	@GetMapping("/community/boardClub/{b_code}")
	public String boardClubPage(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@PathVariable int b_code,
			HttpSession session, Model model) {
		
		String b_name = bs.findBNameByBCode(b_code);
		// 한페이지에 5개씩 (테스트)
		int pageSize = 5; 
		Pageable pageable = PageRequest.of(page-1, pageSize);
		Page<List<Map<String ,Object>>> list = bs.findClubByBcode(b_code, pageable);
		
		 //페이징
	    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
	    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
	    int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
		
		model.addAttribute("b_name", b_name);
		model.addAttribute("b_code", b_code);
		model.addAttribute("list", list);
		model.addAttribute("nowPage",page);
	    model.addAttribute("startPage",startPage);
	    model.addAttribute("endPage",endPage);
	    model.addAttribute("totalPage",list.getTotalPages());
		return "/community/boardClub";
	}
	
	// 게시물 상세보기
	@GetMapping("/member/community/boardDetail/{b_code}/{bno}")
    public String boardPage(@PathVariable int b_code, @PathVariable int bno, Model model) {
		
		String b_name = bs.findBNameByBCode(b_code);
		bs.updateHit(bno, b_code);
		
		model.addAttribute("b", bs.detailBoard(bno, b_code));
		model.addAttribute("writer",bs.findBoardByBnoAndBCode(b_code, bno).getUser().getId());
		model.addAttribute("b_name", b_name);
		model.addAttribute("bno",bno);
		model.addAttribute("b_code",b_code);
		return "/member/community/boardDetail";
    } 
	
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
    	long userId = bs.findByUName(u_name);
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
		
		if( b_code == 4 ) {
			return "redirect:/community/boardClub/"+b_code;			
		}
		return "redirect:/community/board/"+b_code;
    }
    
    //모임완료시 ongoing 1 --> 0 변경
  	@GetMapping("/member/community/boardFinish/{b_code}/{bno}")
  	public String changeSold(@PathVariable int b_code, @PathVariable int bno) {
  		bs.usedgoodSold(b_code, bno);
  		return "redirect:/member/community/boardDetail/"+b_code+"/"+bno;
  	}
  	
  //사진형 게시판 글 삭제 delete
  	@GetMapping("/member/community/boardDelete/{b_code}/{bno}")
  	public String delete(@PathVariable int b_code, @PathVariable int bno) {
  		Board b = bs.findBoardByBnoAndBCode(b_code, bno);
  		int re = bs.deleteBoard(b_code, bno);
  		String fname = b.getB_fname();
  		
  		String path = null;
  		Resource resource = resourceLoader.getResource("classpath:/static/images"); // 절대경로 찾기
  		try {
  			path = resource.getFile().getAbsolutePath();
  		} catch (IOException e) {
  			System.out.println("경로 가져오는 중 예외 발생:" + e);
  		}
  		if(re!=0) {
  			File file = new File(path+"/"+fname);
  			file.delete();		
  		}
  		String url = "/community/board/"+b_code;
  		if( b_code ==4 ) {
  			url = "/community/boardClub/"+b_code;
  		}
  		return "redirect:"+url;
  	}
  	
  	// 글 수정 이동
  	@GetMapping("/member/community/boardUpdate/{b_code}/{bno}")
  	public String updateForm(@PathVariable int b_code, @PathVariable int bno, Model model) {
  		Board b = bs.findBoardByBnoAndBCode(b_code, bno);
		model.addAttribute("b",b);
		if( b_code == 4) {
			return"/member/community/boardClubUpdate";
		}
  		return "/member/community/boardUpdate";
  	}
  	
  	// 글 수정 
  	@PostMapping("/member/community/boardUpdate/{b_code}/{bno}")
  	public String update(Board b, @PathVariable int b_code, @PathVariable int bno) {
  		Board ob = bs.findBoardByBnoAndBCode(b_code, bno);
  		String oldFname = ob.getB_fname();
  		
  		// 파일 관련
  		MultipartFile uploadFile = b.getUploadFile();
  		String newFname = b.getUploadFile().getOriginalFilename();
  		String path = null;
  		Resource resource = resourceLoader.getResource("classpath:/static/images"); // 절대경로 찾기
  		try {
  			path = resource.getFile().getAbsolutePath();
  			System.out.println(path);
  		} catch (IOException e) {
  			System.out.println("경로 가져오는 중 예외 발생:" + e);
  		}

  		if (newFname != null && !newFname.equals("") &&!newFname.equals(oldFname)) {
  			try {
  				byte[] data = uploadFile.getBytes();
  				FileOutputStream fos = new FileOutputStream(path + "/" + newFname);
  				fos.write(data);
  				fos.close();
  				ob.setB_fname(newFname);
  			} catch (IOException e) {
  				System.out.println("파일등록예외발생:" + e.getMessage());
  			}
  		}else {
  			ob.setB_fname(oldFname);
  		}
  		
  		RegionCode regionCode = new RegionCode();
    	if(b_code == 4) {
    		regionCode.setRno(b.getRegionCode().getRno());
    	}else {
    		regionCode = null;
    	}
    	b.setRegionCode(regionCode);
  		ob.setRegionCode(regionCode);
  		ob.setB_content(b.getB_content());
  		ob.setB_title(b.getB_title());
  		ob.setB_price(b.getB_price());
  		
  		Board re = bs.update(ob);
  		if(re!=null &&oldFname!=null &&newFname!=null&& newFname!="" &&!oldFname.equals(newFname)) {
  			File file = new File(path+"/"+oldFname);
  			file.delete();		
  		}
  		return "redirect:/member/community/boardDetail/"+b_code+"/"+bno;
  	}
}
    
