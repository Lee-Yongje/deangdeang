package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.BoardId;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Users;
import com.example.demo.service.CommentService;

import jakarta.servlet.http.HttpSession;


@Controller
public class CommentController {
//댓글 등록, 수정, 삭제
	
	@Autowired
	private CommentService cs;
	
  	// 댓글 등록
  	@PostMapping("/member/community/boardComment")
  	public String insertComment(Comment c, @RequestParam(defaultValue = "0") int pno, int bno, int b_code, HttpSession session) {
  		
  		int cno = cs.getNextCno();
  		c.setCno(cno);
  		// 현재시간
  		LocalDateTime now = LocalDateTime.now();
  		c.setC_date(now);
  		
  		// 작성자 번호
  		Users user = (Users)session.getAttribute("userSession");
  		Long userId = user.getId();
  		if (c.getUser() == null) {
			c.setUser(new Users());
		}
  		c.getUser().setId(userId);
  		
  		// 게시물번호, 게시판번호
  		BoardId boardId = new BoardId();
		boardId.setB_code(b_code);
		boardId.setBno(bno);
		c.setId(boardId);
		
		//원댓글 없을 시 기본 설정
		int c_ref = cno; //기본 ref는 cno랑 동일하게 설정
		int c_level = 0; //기본 레벨 0으로 설정 
		int c_pcno = -1; //원댓글 없으면 -1
		int c_step = 1; //원댓글의 댓글 순서는 첫번째 
		
		//원댓글이 있다면 (대댓글이라면 )
		if(pno!=0) {
			Comment pc = cs.getOldComment(pno); //원댓글 불러오기
			c_ref = pc.getC_ref(); //원댓글이랑 ref 동일하게 설정
			c_level = 1; //대댓글이라면 레벨 1로 설정.
			c_pcno = pc.getCno(); //부모댓글번호
			c_step = cs.getMaxCStep(b_code, bno, c_ref)+1;
		}
		c.setC_ref(c_ref);
		c.setC_level(c_level);
		c.setC_pcno(c_pcno);
		c.setC_step(c_step);
		
		
		cs.insert(c);
		System.out.println("댓글 등록 완료");
		if(b_code==6) {
  			return "redirect:/member/usedgood/detail/"+b_code+"/"+bno;
  		}else if(b_code == 1 ||b_code == 5) {
  			return "redirect:/member/community/photoBoardDetail/"+b_code+"/"+bno;
  		}
		return "redirect:/member/community/boardDetail/"+b_code+"/"+bno;
  	}
  
  	//댓글 수정
  	@PostMapping("/member/community/updateComment")
  	public String updateComment(Comment c ,int cno, int bno, int b_code) {
  		Comment oc = cs.getOldComment(cno);
  		oc.setC_content(c.getC_content());
  		cs.update(oc);
  		if(b_code==6) {
  			return "redirect:/member/usedgood/detail/"+b_code+"/"+bno;
  		}else if(b_code == 1 ||b_code == 5) {
  			return "redirect:/member/community/photoBoardDetail/"+b_code+"/"+bno;
  		}
  		return "redirect:/member/community/boardDetail/"+b_code+"/"+bno;
  	}
  	//댓글 삭제
	@GetMapping("/member/community/deleteComment")
  	public String deleteComment(int cno,  int bno, int b_code) {
  		Comment oc = cs.getOldComment(cno);
  		cs.delete(oc);
  		if(b_code==6) {
  			return "redirect:/member/usedgood/detail/"+b_code+"/"+bno;
  		}else if(b_code == 1 ||b_code == 5) {
  			return "redirect:/member/community/photoBoardDetail/"+b_code+"/"+bno;
  		}
  		return "redirect:/member/community/boardDetail/"+b_code+"/"+bno;
  	}
	
	//대댓글 달 때 원댓의 글쓴이 표시하기 위함
	@GetMapping("/member/community/getCommentWriter")
	@ResponseBody
	public String commentWriter(int cno) {
		String writer = cs.getOldComment(cno).getUser().getNickname();
		return writer;
	}
}
