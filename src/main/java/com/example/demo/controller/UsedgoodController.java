package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.OptionalInt;

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
import org.springframework.web.bind.annotation.ModelAttribute;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsedgoodController {

	@Autowired
	private BoardService bs;
	
	@Autowired
	private UsersService us;

	@Autowired //경로찾기용
	private ResourceLoader resourceLoader;

	
	// 중고거래 조회
	@GetMapping("/usedgood/usedgood")
	public void usedgoodPage(String category, String search, String rno, 
							@RequestParam(defaultValue = "0")String reset,
	                         @RequestParam(defaultValue = "1") int page,
	                         HttpSession session, Model model) {
	    
	    //페이징 
	    int pageSize = 8; //한 페이지에 들어갈 아이템 수 실제로는 16개지만 일단 테스트용
	    Pageable pageable = PageRequest.of(page-1, pageSize); //(데이터 0부터 시작, 불러올 데이터 개수)
	    Page<Board> list = bs.listUsedgood(6, pageable); //페이저블을 매개변수로 포함해서 데이터 조회
	    
	    String v_category = null;
	    String v_search = null;
	    String v_rno = null;
	    
	    //중고장터 메인 누르면 검색했던 것 초기화 상태로 돌려놓기 위해
	    if(reset.equals("1")) {
	    	session.removeAttribute("search");
	    	session.removeAttribute("category");
	    	session.removeAttribute("rno");
	    }
	    //session에 검색값이 있으면 불러오기
	    if(session.getAttribute("search")!=null) {
	    	v_search=(String)session.getAttribute("search"); 
			v_category=(String)session.getAttribute("category");	
			if (session.getAttribute("category").equals("region")) {
				v_rno=(String)session.getAttribute("rno");	
			}else if(session.getAttribute("category").equals("b_title")) {
				session.removeAttribute("rno");
			}
	    }
	    
	    //입력된 검색어가 있으면 session에 값 유지
	    if(search!=null) {
	    	v_search = search;
	    	v_category = category;
	    	if(category.equals("region")) {
	    		v_rno =rno;
	    	}
	    	session.setAttribute("search", v_search);
	    	session.setAttribute("category", v_category);
			session.setAttribute("rno", v_rno);
	    }
	    

	    
	    //검색
	    if (v_category != null && v_category.equals("b_title") && v_search != null) {
	        list = bs.searchUsedgoodByTitle(6, v_search, pageable);
	    } else if(v_category != null && v_category.equals("region") && v_search != null){
	    	list = bs.searchUsedgoodByTitleAndRegion(6, v_rno, v_search, pageable);
	    }
	    
	    //페이징 
	    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
	    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
	    int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
	    
	    model.addAttribute("list",list);
	    model.addAttribute("nowPage",page);
	    model.addAttribute("startPage",startPage);
	    model.addAttribute("endPage",endPage);
	    model.addAttribute("totalPage",list.getTotalPages());
	}

	// 중고거래 상세
	@GetMapping("/member/usedgood/detail/{b_code}/{bno}")
	public String usedgoodDetailPage(@PathVariable int b_code, @PathVariable int bno, Model model) {
		model.addAttribute("b", bs.detailBoard(bno, b_code));
		model.addAttribute("writer",bs.findBoardByBnoAndBCode(b_code, bno).getUser().getId());
		model.addAttribute("bno",bno);
		model.addAttribute("b_code",b_code);
		bs.updateHit(bno, b_code);
		return "/member/usedgood/detail";
	}
	
	//사진형 게시판 글 삭제 delete
	@GetMapping("/member/community/photoBoardDelete/{b_code}/{bno}")
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
		String url= "";
		switch(b_code) {
			case 1: url = "/community/boast"; break;
			case 5: url = "/community/report"; break;
			case 6: url = "/usedgood/usedgood"; break;
		}
		return "redirect:"+url;
	}
	
	//중고거래 글 수정 페이지로 가기
	@GetMapping("/member/usedgood/update/{b_code}/{bno}")
	public String updateForm(@PathVariable int b_code, @PathVariable int bno, Model model) {
		Board b = bs.findBoardByBnoAndBCode(b_code, bno);
		System.out.println(b);
		model.addAttribute("b",b);
		return "/member/usedgood/update";
	}
	
	//중고거래 글 수정 update
	@PostMapping("/member/usedgood/update/{b_code}/{bno}")
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
		regionCode.setRno(b.getRegionCode().getRno());
		ob.setRegionCode(regionCode);
		ob.setB_content(b.getB_content());
		ob.setB_title(b.getB_title());
		ob.setB_price(b.getB_price());
		
		Board re = bs.update(ob);
		if(re!=null &&oldFname!=null &&newFname!=null&& newFname!="" &&!oldFname.equals(newFname)) {
			File file = new File(path+"/"+oldFname);
			file.delete();		
		}
		return "redirect:/member/usedgood/detail/"+b_code+"/"+bno;
	}
	
	//판매완료로 변경
	@GetMapping("/member/usedgood/sold/{b_code}/{bno}")
	public String changeSold(@PathVariable int b_code, @PathVariable int bno) {
		bs.usedgoodSold(b_code, bno);
		return "redirect:/member/usedgood/detail/"+b_code+"/"+bno;
	}
	
	
	// 중고거래 글 등록 페이지로 가기
	@GetMapping("/member/usedgood/write")
	public void usedgoodWritePage() {
	}
		
	// 중고거래 글 등록 insert
	@PostMapping("/member/usedgood/insert/{b_code}")
	public String insert(Board b, @PathVariable int b_code, HttpSession session) {

		// 현재시간
		LocalDateTime now = LocalDateTime.now();

		// 게시판번호에 따른 게시글번호 증가
		int bno = bs.getNextBno(b_code);

		// RegionCode 객체 생성 및 설정
		RegionCode regionCode = new RegionCode();
		regionCode.setRno(b.getRegionCode().getRno());
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

		Users user = (Users)session.getAttribute("userSession");
		Long userId = user.getId();
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
		return "redirect:/usedgood/usedgood";
	}
	
	
	
}
