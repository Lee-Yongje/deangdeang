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
public class CommunityPhotoController {

	@Autowired
	private BoardService bs;

	@Autowired
	private UsersService us;

	@Autowired // 파일 경로찾기용
	private ResourceLoader resourceLoader;

	// 사진형 게시판 글 등록 페이지 가기
	@GetMapping("/member/community/photoBoardInsert/{b_code}")
	public String insertPage(@PathVariable int b_code, Model model) {
		model.addAttribute("b_code", b_code);
		model.addAttribute("b_name", bs.findBNameByBCode(b_code));
		return "/member/community/photoBoardInsert";
	}

	// 사진형 게시판 글 등록
	@PostMapping("/member/community/photoBoardInsert/{b_code}")
	public String photoBoardInsert(Board b, @PathVariable int b_code, HttpSession session) {

		// 현재시간
		LocalDateTime now = LocalDateTime.now();

		// 게시판번호에 따른 게시글번호 증가
		int bno = bs.getNextBno(b_code);

		// RegionCode 객체 생성 및 설정 (지역 있을 때)
		if (b.getRegionCode() != null) {
			RegionCode regionCode = new RegionCode();
			regionCode.setRno(b.getRegionCode().getRno());
			b.setRegionCode(regionCode);
		}

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
			System.out.println("path" + path);

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
		String url= "";
		switch(b_code) {
			case 1: url = "/community/boast"; break;
			case 5: url = "/community/report"; break;
		}
		return "redirect:"+url;
	}

	// 사진형 게시판 글 수정 페이지 가기
	@GetMapping("/member/community/photoBoardUpdate/{b_code}/{bno}")
	public String updatePage(@PathVariable int b_code, @PathVariable int bno, Model model) {
		Board b = bs.findBoardByBnoAndBCode(b_code, bno);
		model.addAttribute("b_name", bs.findBNameByBCode(b_code));
		model.addAttribute("b", b);
		return "/member/community/photoBoardUpdate";
	}

	// 중고거래 글 수정 update
	@PostMapping("/member/community/photoBoardUpdate/{b_code}/{bno}")
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

		if (newFname != null && !newFname.equals("") && !newFname.equals(oldFname)) {
			try {
				byte[] data = uploadFile.getBytes();
				FileOutputStream fos = new FileOutputStream(path + "/" + newFname);
				fos.write(data);
				fos.close();
				ob.setB_fname(newFname);
			} catch (IOException e) {
				System.out.println("파일등록예외발생:" + e.getMessage());
			}
		} else {
			ob.setB_fname(oldFname);
		}

		//지역 있을 떄
		if (b.getRegionCode() != null) {
			RegionCode regionCode = new RegionCode();
			regionCode.setRno(b.getRegionCode().getRno());
			ob.setRegionCode(regionCode);
		}
		ob.setB_content(b.getB_content());
		ob.setB_title(b.getB_title());
		ob.setB_price(b.getB_price());

		Board re = bs.update(ob);
		if (re != null && oldFname != null && newFname != null && newFname != "" && !oldFname.equals(newFname)) {
			File file = new File(path + "/" + oldFname);
			file.delete();
		}
		String url= "";
		switch(b_code) {
			case 1: url = "/community/boast"; break;
			case 5: url = "/community/report"; break;
		}
		return "redirect:"+url;
	}

	// 자랑게시판 조회
	@GetMapping("/community/boast")
	public void boastPage(@RequestParam(required = false) String search,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "reset", defaultValue = "0") String reset, HttpSession session, Model model) {

		int pageSize = 16;
		String vsearch = null;

		// 메인 누르면 검색했던 것 초기화 상태로 돌려놓기 위해
		if (reset.equals("1")) {
			session.removeAttribute("search");
		}

		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Board> list = bs.listUsedgood(1, pageable);

		//session에 값 있으면 가져와
		if (session.getAttribute("search") != null) {
			vsearch = (String) session.getAttribute("search");
		}

		//검색어가 있으면 session에 값 유지
		if (search != null) {
			vsearch = search;
			session.setAttribute("search", vsearch);
		}
		//검색
		if (vsearch != null) {
			list = bs.searchUsedgoodByTitle(1, vsearch,"", pageable);
		}

		//페이징
		int pagingSize = 5; // 페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
		int startPage = ((page - 1) / pagingSize) * pagingSize + 1;
		int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); // 5개씩 보여주기. 마지막 페이지는 마지막페이지까지

		model.addAttribute("b_name", "전국댕댕자랑");
		model.addAttribute("list", list);
		model.addAttribute("nowPage", page);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("totalPage", list.getTotalPages());
	}

	// 신고제보 조회
	@GetMapping("/community/report")
	public void reportPage(@RequestParam(required = false) String search, 
			@RequestParam(required = false) String region,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "0") String reset,
			HttpSession session, Model model) {

		int pageSize = 16; 
		String vsearch = null;
		String vregion = null;

		// 메인 누르면 검색했던 것 초기화 상태로 돌려놓기 위해
		if (reset.equals("1")) {
			session.removeAttribute("search");
			session.removeAttribute("region");
		}

		Pageable pageable = PageRequest.of(page - 1, pageSize);
		Page<Board> list = bs.listUsedgood(5, pageable);

		// session에 값 있으면 가져와
		if (session.getAttribute("search") != null) {
			vsearch = (String) session.getAttribute("search");
		}
		if (session.getAttribute("region") != null) {
			vregion = (String) session.getAttribute("region");
		}

		// 검색어가 있으면 session에 값 유지
		if (search != null) {
			vsearch = search;
			session.setAttribute("search", vsearch);
		}

		if (region != null) {
			if (region.equals("지역 전체")) {
				region = null;
			}
			vregion = region;
			session.setAttribute("region", vregion);
		}
		// 검색
		if (vsearch != null) {
			list = bs.searchUsedgoodByTitle(5, vsearch, "",pageable);
			if (vregion != null) {
				list = bs.searchUsedgoodByTitleAndRegion(5, vregion, vsearch, "",pageable);
			}
		} else if (vregion != null) {
			list = bs.searchBoardByRegion(5, vregion, pageable);
		}

		// 페이징
		int pagingSize = 5; // 페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
		int startPage = ((page - 1) / pagingSize) * pagingSize + 1;
		int endPage = Math.min(startPage + pagingSize - 1, list.getTotalPages()); // 5개씩 보여주기. 마지막 페이지는 마지막페이지까지

		model.addAttribute("b_name", "신고/제보게시판");
		model.addAttribute("list", list);
		model.addAttribute("nowPage", page);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("totalPage", list.getTotalPages());
		model.addAttribute("region",vregion);
		if(vregion==null) {
			model.addAttribute("region","지역 전체");
		}
	}

	// 사진게시판 상세 - 댕댕자/신고제보
	@GetMapping("/member/community/photoBoardDetail/{b_code}/{bno}")
	public String boastDetailPage(Model model, @PathVariable int b_code, @PathVariable int bno) {
		model.addAttribute("b", bs.detailBoard(bno, b_code));
		model.addAttribute("writer", bs.findBoardByBnoAndBCode(b_code, bno).getUser().getId());
		model.addAttribute("bno", bno);
		model.addAttribute("b_code", b_code);
		model.addAttribute("b_name", bs.findBNameByBCode(b_code));
		bs.updateHit(bno, b_code);
		return "/member/community/photoBoardDetail";
	}

	// 사진게시판 글삭제 => UsedgoodController에 통일

}
