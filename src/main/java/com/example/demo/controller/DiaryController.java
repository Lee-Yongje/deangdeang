package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Users;
import com.example.demo.service.DiaryService;

import jakarta.servlet.http.HttpSession;

@Controller 
public class DiaryController {
	
	@Autowired
	private DiaryService ds;
    
	//----------집사일지----------
    

	@GetMapping("/member/diary/diary")
    public String redirectToCurrentMonth() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        return "redirect:/member/diary/diary/" + year + "/" + month;
    }

    // 특정 연도와 월을 지정하여 집사일지 목록 출력
    @GetMapping("/member/diary/diary/{year}/{month}")
    public String listDiary(@PathVariable("year") int year, @PathVariable("month") int month, Model model, HttpSession session) {
    	Users user = (Users) session.getAttribute("userSession");
        Long userId = user.getId();
        
        List<Diary> diaries = ds.getDiariesByIdAndYearAndMonth(userId, year, month);
        model.addAttribute("diaries", diaries);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month-1); // 0부터 시작하는 월 인덱스에 맞추기
        return "member/diary/diary";
    } 
    
    // 특정(dno) 집사일지 상세보기
    @GetMapping("/member/diary/diaryDetail/{dno}")
    public String detailDiary(@PathVariable("dno") int dno, Model model, HttpSession session) {
        Diary diary = ds.getDiaryById(dno);
        Users user = (Users) session.getAttribute("userSession");
        if (diary != null && diary.getUsers().getId().equals(user.getId())) {
            model.addAttribute("diary", diary);
            model.addAttribute("isUser", true);
        } else {
            model.addAttribute("isUser", false);
        }
        return "/member/diary/diaryDetail";
    }

    
    
    @GetMapping("/member/diary/detailDiary")
    public void detailDiaryPage() {
    }
    
    
    @GetMapping("/member/diary/diaryWrite")
    public String diaryWritePage(Model model) {
        model.addAttribute("diary", new Diary());
        return "member/diary/diaryWrite";
    }

    
    // 집사일지 등록
    @PostMapping("/member/diary/insertDiary")
    public String insertDiary(@ModelAttribute Diary diary, @RequestParam("uploadFile") MultipartFile file, HttpSession session) {
    	Users user = (Users) session.getAttribute("userSession");
        diary.setUsers(user);
        
    	// 현재 날짜를 지정
    	diary.setD_date(LocalDateTime.now());
    	
        // 파일 저장 경로 설정
    	String path = "/Users/sooyoung/git/ms/ms/final-main/src/main/resources/static/images";
        
        // 파일 업로드 처리
    	if (!file.isEmpty()) {
            try {
            	String fileName = file.getOriginalFilename();            	
                // 프로젝트 내 static/images 경로 설정
                String uploadImage = "src/main/resources/static/images";

                // 파일 저장 경로 설정
                Path uploadPath = Paths.get(uploadImage);

                // 경로가 존재하지 않으면 생성
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 파일 저장
                Path filePath = Paths.get(uploadImage, fileName);
                file.transferTo(filePath);

                // 파일명을 DB에 저장
                diary.setD_fname(fileName);
            } catch (IOException e) {
                e.printStackTrace(); // 예외 처리
            }
        }
        
        int nextDno = ds.getNextDno();
        diary.setDno(nextDno);
        
        diary.setUsers(user); // Diary 객체에 Users 객체 설정
        
        // Diary 객체 저장
        ds.saveDiary(diary);

        // 저장 후 diary.html 페이지
        return "redirect:/member/diary/diary";
    }

    
    @GetMapping("/member/diary/diaryUpdate/{dno}")
    public String diaryUpdatePage(@PathVariable("dno") int dno, Model model) {
        Diary diary = ds.getDiaryById(dno);
        if (diary != null) {
            model.addAttribute("diary", diary);
            return "member/diary/diaryUpdate";
        }
        return "redirect:/some-error-page";
    }
    
    @PostMapping("/member/diary/updateDiary/{dno}")
    public String updateDiary(@PathVariable int dno, @ModelAttribute Diary diary, 
                              @RequestParam("uploadFile") MultipartFile file,
                              @RequestParam("existingFileName") String existingFileName,
                              HttpSession session) {
    	
    	Users user = (Users) session.getAttribute("userSession");
        diary.setUsers(user);
    	if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            // 파일저장 경로
            diary.setD_fname(fileName); // 파일명 설정.
            
            String uploadDir = "src/main/resources/static/images";
            // 디렉토리가 존재하지 않으면 생성
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File savedFile = new File(uploadPath, fileName);
            try (FileOutputStream fos = new FileOutputStream(savedFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace(); 
            }
            
            
    	} else {
            diary.setD_fname(existingFileName);
        }
    	
        diary.setD_date(LocalDateTime.now());

        // Users 객체 생성 및 설정
        diary.setUsers(user); // Diary에 Users 설정
        
        diary.setDno(dno);
        ds.updateDiary(diary); 
        return "redirect:/member/diary/detailDiary/" + dno;
    }
    
    // 집사일지 삭제
    @PostMapping("/member/diary/deleteDiary/{dno}")
    public String deleteDiary(@PathVariable int dno, HttpSession session) {
    	Users user = (Users) session.getAttribute("userSession");
    	Diary diary = ds.getDiaryById(dno);
    	if (diary != null && diary.getUsers().getId().equals(user.getId())) {
            ds.deleteDiary(dno);
            return "redirect:/member/diary/diary"; 
        } else {
            return "redirect:/member/diary/unauthorized";
        }
    }
}



    

    