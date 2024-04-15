package com.example.demo.controller;

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

	// 중고거래 조회
	@GetMapping("/usedgood/usedgood")
	public void usedgoodPage(@RequestParam(required = false) String category,
	                         @RequestParam(required = false) String search,
	                         @RequestParam(required = false) String rno,
	                         @RequestParam(value = "page", defaultValue = "1") int page,
	                         @RequestParam(value = "reset", defaultValue = "0") String reset,
	                         HttpSession session,
	                         Model model) {
	    int pageSize = 8; //한 페이지에 들어갈 아이템 수 실제로는 16개지만 일단 테스트용
	    String vcategory = null;
	    String vsearch = null;
	    String vrno = null;
	    
	    System.out.println("넘어온search: "+search);
	    System.out.println("넘어온category: "+category);
	    System.out.println("넘어온rno: "+rno);
	    
	    
	    //중고장터 메인 누르면 검색했던 것 초기화 상태로 돌려놓기 위해
	    if(reset.equals("1")) {
	    	System.out.println("reset equals 1");
	    	session.removeAttribute("search");
	    	session.removeAttribute("category");
	    	session.removeAttribute("rno");
	    }
	    
	    Pageable pageable = PageRequest.of(page-1, pageSize);
	    Page<Board> list = bs.listUsedgood(6, pageable);
	    
	    //session에 값 있으면 가져와
	    if(session.getAttribute("search")!=null) {
	    	vsearch=(String)session.getAttribute("search"); 
			vcategory=(String)session.getAttribute("category");	
			if (session.getAttribute("category").equals("rno")) {
				vrno=(String)session.getAttribute("rno");	
			}
			if(session.getAttribute("category").equals("b_title")) {
				session.removeAttribute("rno");
			}
	    }

	    //검색어가 있으면 session에 값 유지
	    if(search!=null) {
	    	System.out.println("search 있음");
	    	vsearch = search;
	    	vcategory = category;
	    	if(category.equals("rno")) {
	    		vrno =rno;
	    	}
	    	session.setAttribute("search", vsearch);
	    	session.setAttribute("category", vcategory);
			session.setAttribute("rno", vrno);
	    }
	    

	    
	    //검색
	    if (vcategory != null && vcategory.equals("b_title") && vsearch != null) {
	        list = bs.searchUsedgoodByTitle(6, vsearch, pageable);
	    } else if(vcategory != null && vcategory.equals("rno") && vsearch != null){
	    	list = bs.searchUsedgoodByTitleAndRegion(6, vrno, vsearch, pageable);
	    }
	    
	    //페이징
	    int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
	    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
	    int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
	    
	    
	    System.out.println("vsearch: "+vsearch);
	    System.out.println("vcategory: "+vcategory);
	    System.out.println("vrno: "+vrno);
	    
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
		bs.updateHit(bno, b_code);
		return "/member/usedgood/detail";
	}

	// 중고거래 글 등록 페이지
	@GetMapping("/member/usedgood/write")
	public void usedgoodWritePage() {
	}

	@Autowired //경로찾기용
	private ResourceLoader resourceLoader;

	// 중고거래 글 등록 insert
	@PostMapping("/member/usedgood/insert/{b_code}") //사실 여긴 b_code말고 걍 6 때려넣으면 되지만 딴데서 쓰려고 일단 일케 함
	public String usedgoodInsert(Board b, @PathVariable int b_code) {

		// 현재시간
		LocalDateTime now = LocalDateTime.now();

		// 게시판번호에 따른 게시글번호 증가
		int bno = bs.getNextUsedgoodBno();

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

		// 로그인 세션유지 하게 되면 이거 세션에서 아이디 가져와서 하는 걸로 수정해야됨!! 일단 101로 넣어놓음
		int userId = 101;
		if (b.getUsers() == null) {
			b.setUsers(new Users());
		}
		b.getUsers().setUno(userId);

		// 파일 관련
		MultipartFile uploadFile = b.getUploadFile();
		String fname = b.getUploadFile().getOriginalFilename();
		String path = null;
//			String path = request.getServletContext().getRealPath("classpath:/static/images");실패
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
		
		//테스트용
		System.out.println("b" + b);
		System.out.println("bno: " + b.getId().getBno());
		System.out.println("b_code: " + b.getId().getB_code());

		bs.insertUsedgood(b);
		return "redirect:/usedgood/usedgood";
	}
}
