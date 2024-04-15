package com.example.demo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.demo.service.ScheduleService;

@Controller 
public class ScheduleController {
	
	
	//----------스케줄러----------

	
	@Autowired
	private ScheduleService ss;
	
    @GetMapping("/member/diary/scheduler")
    public String scheduler(Model model, @RequestParam(defaultValue = "101") long id) {
        List<Puppy> puppies = ss.getPuppyByUsersId(id);
        model.addAttribute("puppies", puppies);
        return "member/diary/scheduler";  // scheduler.html 페이지
    }
    
    

    @GetMapping("/get-schedule")
    @ResponseBody
    public List<Schedule> getSchedulesByDate(@RequestParam long id,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day) {
		LocalDate localDate = LocalDate.of(year, month + 1, day); // month는 0부터 시작하므로 +1
		Date date = Date.valueOf(localDate);
		return ss.getSchedulesByDate(id, date);
	}


}
    