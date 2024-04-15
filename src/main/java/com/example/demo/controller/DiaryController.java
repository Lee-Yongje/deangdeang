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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Users;
import com.example.demo.service.DiaryService;

@Controller 
public class DiaryController {
	
	@Autowired
	private DiaryService ds;
	
	//----------스케줄러----------

    
    @GetMapping("/member/diary/scheduler")
    public String scheduler(Model model, @RequestParam(defaultValue = "101") int uno) {
        List<Puppy> puppies = ds.getPuppyByUserId(uno);
        model.addAttribute("puppies", puppies);
        return "member/diary/scheduler";  // scheduler.html 페이지
    }
    
    

    @GetMapping("/get-schedule")
    @ResponseBody
    public List<Schedule> getSchedulesByDate(@RequestParam int uno, 
                                             @RequestParam int year,
                                             @RequestParam int month,
                                             @RequestParam int day) {
      LocalDate date = LocalDate.of(year, month + 1, day); // month는 0부터 시작하므로 +1
      return ds.getSchedulesByDate(uno, date);
    }

    
    
    
    @GetMapping("/member/diary/schedulerWrite")
    public String schedulerWirtePage(Model model) {
//        List<Puppy> puppies = ds.getPuppyByUserId(uno);
//        model.addAttribute("puppies", puppies);
        return "member/diary/scheduler";  // scheduler.html 페이지
    }
    
    
    
//    @GetMapping("/member/diary/schedulerWrite")
//    public void schedulerWritePage() {
//    }
    
    
	//----------다이어리----------
    
    @GetMapping("/member/diary/diary")
    public String diaryPage(Model model) {
        int uno = 101; // 현재 로그인한 사용자의 uno 임시값
        List<Diary> diaries = ds.getDiariesByUno(uno);
        model.addAttribute("diaries", diaries);
        return "member/diary/diary";
    }
    
    // 특정 다이어리 상세
//    @GetMapping("/member/diary/diaryDetail/{id}")
//    public String diaryDetail(@PathVariable("id") int dno, Model model) {
//        Diary diary = ds.getDiaryById(dno);
//        model.addAttribute("diary", diary);
//        return "member/diary/diaryDetail";
//    }
    
    @GetMapping("/member/diary/diaryDetail/{dno}")
    public String diaryDetail(@PathVariable("dno") int dno, Model model) {
        Diary diary = ds.getDiaryById(dno);
        if (diary != null) {
            model.addAttribute("diary", diary);
            return "member/diary/diaryDetail";
        } else {
            return "redirect:/diaryPage"; // 적절한 에러 페이지 또는 리스트 페이지로 리다이렉트
        }
    }

    
    
    @GetMapping("/member/diary/diaryDetail")
    public void diaryDetailPage() {
    }
    
    
    @GetMapping("/member/diary/diaryWrite")
    public String diaryWritePage(Model model) {
        model.addAttribute("diary", new Diary());
        return "member/diary/diaryWrite";
    }

    

    @PostMapping("/member/diary/insertDiary")
    public String insertDiary(@ModelAttribute Diary diary, @RequestParam("uploadFile") MultipartFile file) {
    	
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
        

        // Users 객체 생성 및 설정
        Users user = new Users();
        user.setUno(101); // 임시 사용자 ID(로그인 연동되면 uno 가져와야함)
        diary.setUsers(user); // Diary 객체에 Users 객체 설정
        
        // Diary 객체 저장
        ds.saveDiary(diary);

        // 저장 후 diary.html 페이지로 리다이렉션
        return "redirect:/member/diary/diary";
    }

    
    
    
    
//    @PostMapping("/insertDiary")
//    public String insertDiary(@ModelAttribute Diary diary, @RequestParam("file") MultipartFile file) {
//        ds.saveDiary(diary, file);
//        return "redirect:/member/diary/diary"; // 글 등록 후 리디렉션될 경로
//    }
    
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
                              @RequestParam("existingFileName") String existingFileName) {
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
                e.printStackTrace(); // 파일 저장 중 오류 발생 시 예외 처리
            }
            
            
    	} else {
            diary.setD_fname(existingFileName);
        }
    	
        diary.setD_date(LocalDateTime.now());

        // Users 객체 생성 및 설정
        Users user = new Users();
        user.setUno(101);  // 임시 사용자 ID(로그인 연동되면 uno 가져와야함)
        diary.setUsers(user); // Diary에 Users 설정
        
        diary.setDno(dno);
        ds.updateDiary(diary); 
        return "redirect:/member/diary/diaryDetail/" + dno; // 수정 후 상세페이지로 이동
    }
    
    // 다이어리 삭제
    @PostMapping("/member/diary/deleteDiary/{dno}")
    public String deleteDiary(@PathVariable("dno") int dno) {
        ds.deleteDiary(dno);
        return "redirect:/member/diary/diary";
    }

    
}
    