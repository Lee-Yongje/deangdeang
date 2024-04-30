package com.example.demo.controller;


import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Users;
import com.example.demo.service.ScheduleService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ScheduleController {


    //----------스케줄러----------


    @Autowired
    private ScheduleService ss;


    // 강아지 목록 조회
    @GetMapping("/member/diary/scheduler")
    public String scheduler(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("userSession");
        Long userId = user.getId();
        List < Puppy > puppies = ss.getPuppyByUsersId(userId);
        model.addAttribute("puppies", puppies);
        return "member/diary/scheduler";
    }


    // 특정 사용자 스케줄러 출력
    @GetMapping("/getSchedule")
    @ResponseBody
    public List < Map < String, Object >> getSchedules(HttpSession session,
        @RequestParam int year,
        @RequestParam int month,
        @RequestParam(required = false) Integer day) {
        Users user = (Users) session.getAttribute("userSession");
        Long userId = user.getId();

        List < Schedule > schedules;
        if (day != null) {
            // 일별 스케줄을 요청(전달받은 월 그대로 사용)
            LocalDate date = LocalDate.of(year, month + 1, day); // 클라이언트에서는 0-11 범위로 월을 보내므로 +1
            schedules = ss.getSchedulesByDate(userId, Date.valueOf(date));
        } else {
            // 월별 스케줄을 요청(달력에 점으로 표시)(클라이언트에서 -1된 값을 보냈으므로 +1)
            schedules = ss.getMonthlySchedules(userId, year, month + 1);
        }

        List < Map < String, Object >> enrichedSchedules = new ArrayList < > ();
        for (Schedule schedule: schedules) {
            Map < String, Object > map = new HashMap < > ();
            map.put("sno", schedule.getSno());
            map.put("s_date", schedule.getS_date());
            map.put("s_content", schedule.getS_content());
            map.put("s_complete", schedule.getS_complete());
            map.put("p_color", schedule.getPuppy().getP_color()); // Puppy 색상 정보 추가
            enrichedSchedules.add(map);
        }
        return enrichedSchedules;

    }

    // 스케줄러 등록
    @GetMapping("/member/diary/schedulerWrite")
    public String schedulerWrite(Model model, HttpSession session) {
        Users user = (Users) session.getAttribute("userSession");
        Long userId = user.getId();
        List < Puppy > puppies = ss.getPuppyByUsersId(userId);
        model.addAttribute("puppies", puppies);
        model.addAttribute("uno", userId);
        return "member/diary/schedulerWrite";
    }



    @PostMapping("/schedule/save")
    public String saveSchedule(@ModelAttribute Schedule schedule,
						        @RequestParam("pno") int pno,
						        HttpSession session,
						        @RequestParam("s_date") String sDate, // 달력에서 날짜 선택한것 문자로 받기
						        Model model) {
        Users user = (Users) session.getAttribute("userSession");
        Long userId = user.getId();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(sDate); // java.util.Date 파싱하고
            schedule.setS_date(new java.sql.Date(utilDate.getTime())); // java.sql.Date로 변환
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int nextSno = ss.getNextSno();
        schedule.setSno(nextSno);
        ss.saveSchedule(schedule, userId, pno);
        return "redirect:/member/diary/scheduler";
    }


    // 내용 수정하기
    @PostMapping("/updateSchedule")
    public String updateSchedule(@RequestParam("sno") int sno, @RequestParam("s_content") String sContent) {
        ss.updateSchedule(sno, sContent);
        return "redirect:/member/diary/scheduler";
    }

    // 내용 삭제하기
    @PostMapping("/deleteSchedule")
    public @ResponseBody String deleteSchedule(@RequestParam("sno") int sno) {
        try {
            ss.deleteSchedule(sno);
            return "삭제되었습니다.";
        } catch (Exception e) {
            return "삭제 실패하였습니다.";
        }
    }


    //스케줄 완료 체크
    @PostMapping("/checkSchedule")
    @ResponseBody
    public Map < String, String > checkSchedule(@RequestParam("sno") int sno, @RequestParam("s_complete") String sComplete) {
        ss.checkSchedule(sno, sComplete);
        Map < String, String > response = new HashMap < > ();
        response.put("status", "success");
        response.put("message", "스케줄 상태 업데이트 성공");
        return response;
    }



}