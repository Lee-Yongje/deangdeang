package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.RegionCodeDAO;
import com.example.demo.entity.Board;
import com.example.demo.entity.BoardId;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Users;
import com.example.demo.service.BoardCodeService;
import com.example.demo.service.BoardService;
import com.example.demo.service.PuppyService;
import com.example.demo.service.UsersService;
import jakarta.servlet.http.HttpSession;

@Controller
public class MypageController {
	
	@Autowired
	private UsersService us;
	
	@Autowired
	private RegionCodeDAO rd;
	
	@Autowired
	private PuppyService ps;
	
	@Autowired
	private BoardService bs;
	
	@Autowired
	private BoardCodeService bcs;
	
	@Autowired
	private ResourceLoader resourceLoader;

	// 회원정보수정 GET
	@GetMapping("/member/mypage/changeInfo")
	public void changeInfoPage(Model model, HttpSession session) {
		Users users = (Users)session.getAttribute("userSession");
		Long uno = users.getId();
		model.addAttribute("u",us.findById(uno));
		model.addAttribute("region",rd.findAll());
    }
	
	// 회원정보수정 POST
	@PostMapping("/member/mypage/changeInfo")
	public String changeInfo(Users u, String rno, HttpSession session) {
		Users user = (Users)session.getAttribute("userSession");
		Long uno = user.getId();
		u.setId(uno);
		String viewPage = "redirect:/member/mypage/changeInfo";
	    String oldFname = u.getFilename();
	    Resource resource = resourceLoader.getResource("classpath:/static/images"); //절대경로 찾기
	    String fname = null;
	    String path = null;
	    MultipartFile uploadFile = u.getUploadFile();

	    // uploadFile이 null인지 확인
	    if (uploadFile != null) {
	        fname = uploadFile.getOriginalFilename();
	        if (fname != null && !fname.equals("")) {
	            try {
	            	path = resource.getFile().getAbsolutePath();
	                System.out.println("이미지 경로 : "+path);
	                FileOutputStream fos = new FileOutputStream(path + "/" + fname);
	                FileCopyUtils.copy(uploadFile.getBytes(), fos);
	                fos.close();
	                u.setFilename(fname);
	            } catch (Exception e) {
	            	System.out.println("예외발생 : "+e.getMessage());
	            }
	        }else if(oldFname != null && !oldFname.equals("")) {
	        	u.setFilename(oldFname);
	        }else {
	        	u.setFilename(null);
	        }
	    }
	    int re = us.updateInfo(u.getName(), u.getEmail(), u.getPhone(), u.getNickname(), u.getFilename(), rno, u.getId());
	    if(re == 1) {
	    	if(fname != null && !fname.equals("") && oldFname != null && !oldFname.equals("")) {
				File file = new File(path + "/"+oldFname);
				file.delete();
			}
	    }else {
	    	System.out.println("게시물 수정에 실패했습니다.");
	    }
	    return viewPage;
	}

	
	// 비밀번호수정 GET
    @GetMapping("/member/mypage/changePwd")
    public void changePwdPage(Model model, HttpSession session) {
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	String oldPwd = us.findById(uno).getPasswordHash();
    	model.addAttribute("oldPwd", oldPwd);
    }
    
    // 비밀번호 수정 POST
    @PostMapping("/member/mypage/changePwd")
    public String changePwd(String newPassword, HttpSession session) {
    	String viewPage = "redirect:/member/mypage/changePwd";
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	us.updatePwd(newPassword, uno);
    	return viewPage;
    }
    
    // 비밀번호 확인 POST
    @PostMapping("/region/check-password")
    public boolean checkPassword(String u_pwd, HttpSession session) {
    	System.out.println("111111111111111111111111");
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
        String dbPwd = us.findById(uno).getPasswordHash();
        return u_pwd.equals(dbPwd);
    }
    
    // 내 반려견 조회하기
    @GetMapping("/member/mypage/listPuppy")
    public void listPuppyForm(Model model, HttpSession session) {
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	List<Puppy> puppy = ps.findByUno(uno);
    	model.addAttribute("puppy", puppy);
    }
      
    // 반려견,등록,수정,삭제 GET
    @GetMapping({"/member/mypage/insertPuppy","/member/mypage/insertPuppy/{pno}"})
    public String insertPuppyPage(@PathVariable(required = false) Integer pno, Model model) { //pno가 null인지 아닌지 파악하기 위해 자료형을 Integer로 설정.
        int p = (pno != null) ? pno : 0; // pno가 null인 경우 기본값 0으로 설정
        String viewPage = "/member/mypage/insertPuppy";
        if(p != 0) {
            Puppy puppy = ps.findByPno(p);
            model.addAttribute("p", puppy);
        }
        return viewPage;
    }

    // 반려견 수정 POST
    @PostMapping("/member/mypage/insertPuppy")
    public String insertPuppy(Puppy p,HttpSession session,Model model) {
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	p.setUser(us.findById(uno));
    	List<Puppy> puppy = ps.findByUno(uno);
    	model.addAttribute("puppy", puppy);
    	String viewPage = "redirect:/member/mypage/listPuppy";
    	
    	p.setPno(ps.getNextPno());
    	Resource resource = resourceLoader.getResource("classpath:/static/images"); //절대경로 찾기

    	MultipartFile uploFile = p.getUploadFile();
    	String fname = uploFile.getOriginalFilename();
    	if(fname != null && !fname.equals("")) {
    		p.setP_fname(fname);
    	}else {
    		p.setP_fname(null);
    	}
    	
    	
    	Puppy insertCheck = ps.save(p);
    	
        	if(insertCheck != null && fname != null && !fname.equals("")) {
        		try {
        			String path = resource.getFile().getAbsolutePath();
        	    	System.out.println("이미지 경로 : "+path);   			  			
        			byte[]data = uploFile.getBytes();
        			FileOutputStream fos = new FileOutputStream(path+"/"+fname);
        			fos.write(data);
        			fos.close();
        		}catch (Exception e) {
    				System.out.println("예외발생 : "+e.getMessage());
    			}   		
        	}
    	return viewPage;
    }
    
    
    // 반려견 수정 POST
    @PostMapping("/member/mypage/updatePuppy")
    public String updatePuppy(Puppy p, HttpSession session) {
    	String viewPage = "redirect:/member/mypage/listPuppy";
    	
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	p.setUser(us.findById(uno));
    	
    	Puppy puppy = ps.findByPno(p.getPno());
    	String oldFname = puppy.getP_fname();
    	if(oldFname != null && !oldFname.equals("")) {
    		p.setP_fname(oldFname);
    	}else {
    		p.setP_fname(null);
    	}
    	
    	
    	Resource resource = resourceLoader.getResource("classpath:/static/images");//클래스패스 상의 static/images 디렉토리에 있는 리소스에 접근하기 위한 Resource 객체를 생성
    	String fname = null;
    	String path = null;    	
    	MultipartFile uploadFile = p.getUploadFile();
    	
    	if(uploadFile != null) {
    		fname = uploadFile.getOriginalFilename();
    		if(fname != null && !fname.equals("")) {
    			try {
    				path = resource.getFile().getAbsolutePath(); //getFile() 메서드는 해당 리소스를 나타내는 파일 객체를 반환. getFile().getAbsolutePath() 메서드는 파일 객체의 절대 경로를 문자열로 반환합니다. 이 절대 경로는 파일의 위치 
    				System.out.println("이미지 경로 : "+path);
    				FileOutputStream fos = new FileOutputStream(path+"/"+fname);
    				FileCopyUtils.copy(uploadFile.getBytes(), fos);
    				fos.close();
    				p.setP_fname(fname);
    			}catch (Exception e) {
					System.out.println("예외발생 : "+e.getMessage());
				}
    		}
    	}
    	Puppy updateCheck = ps.save(p);
    	if(updateCheck != null && oldFname != null && !oldFname.equals("") ) {
    		File file = new File(path+"/"+oldFname);
    		file.delete();
    	}else {
    		System.out.println("게시물 수정에 실패했습니다.");
    	}
    	return viewPage;
    }

    
    // 반려견 삭제 POST
    @PostMapping("/member/mypage/deletePuppy")
    public String deletePuppy(int pno) {
    	String viewPage = "redirect:/member/mypage/listPuppy";
    	Puppy puppy =  ps.findByPno(pno);
    	String fname = puppy.getP_fname();
    	String path = null;
    	Resource resource = resourceLoader.getResource("classpath:/static/images");
    	
    	
    	int re = ps.deletePuppy(pno);
    	if(re==1 && fname!=null) {
    		try {
    			path = resource.getFile().getAbsolutePath();
    			System.out.println("이미지 경로 : "+path);
    			File file = new File(path+"/"+fname);
    			file.delete();
    		}catch (Exception e) {
				System.out.println("예외처리 : "+e.getMessage());
			}
    	}
    	return viewPage;
    }

    

    // 내 글 조회하기 GET
    @GetMapping("/member/mypage/myPosts")
    public void myPostsPage(@RequestParam(value = "page",defaultValue = "1")int page, Model model, HttpSession session) {
    	Users user = (Users)session.getAttribute("userSession");
    	Long uno = user.getId();
    	
    	// 페이징 작업
    	int pageSize = 8; //한 페이지에 보여질 내 게시글 수
    	Pageable pageable = PageRequest.of(page-1, pageSize);
    	Page<Board> boards = bs.findByUno(uno,pageable); // Page는 List와 같은 기능을 하는데 아래에 있는 getTotalPages메소드를 사용할 수 있는 객체.
    	int pagingSize = 5; //페이징 몇개씩 보여줄 건지 ex) 1 2 3 4 5
	    int startPage =  ((page-1)/pagingSize) * pagingSize +1;
	    int endPage = Math.min(startPage + pagingSize - 1, boards.getTotalPages()); //5개씩 보여주기. 마지막 페이지는 마지막페이지까지
    	
    	List<String> boardNames = new ArrayList<>();
    	List<String> formattedDates = new ArrayList<>(); // 변경된 날짜 형식을 담을 리스트
    	
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    	
    	for(Board board : boards) {
    		String b_name = bcs.findById(board.getId().getB_code());
    		boardNames.add(b_name);
    		System.out.println("코드 확인 : "+board.getId().getB_code());
    		 // 게시물의 작성 날짜를 가져와서 원하는 형식으로 변환
            LocalDateTime createDate = board.getB_date();
            String formattedDate = createDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // DateTimeFormatter 클래스의 ofPattern 메서드를 사용하여 원하는 날짜 및 시간 형식을 지정 
            formattedDates.add(formattedDate); // 변환된 날짜를 리스트에 추가
    	}
    	
    	
    	model.addAttribute("boards", boards);
    	model.addAttribute("boardNames", boardNames);
    	model.addAttribute("formattedDates", formattedDates);
    	
    	model.addAttribute("nowPage",page);
	    model.addAttribute("startPage",startPage);
	    model.addAttribute("endPage",endPage);
	    model.addAttribute("totalPage",boards.getTotalPages());
    }
    
    @GetMapping("/member/mypage/detail/{b_code}/{bno}")
    public String detailBoard(@PathVariable int b_code, @PathVariable int bno) {
    	String viewPage = "/member/community/photoBoardDetail/"+b_code+"/"+bno;
    	if(b_code==4 || b_code==2 || b_code==3){
    		viewPage = "/member/community/boardDetail/"+b_code+"/"+bno;
    	}else if(b_code == 6) {
    		viewPage = "/member/usedgood/detail/"+b_code+"/"+bno;
    	}    	
    	return "redirect:"+viewPage;
    }

    
}
    
