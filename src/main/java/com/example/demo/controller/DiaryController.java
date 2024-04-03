package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dto.LoginFormDTO;

@Controller
public class DiaryController {

    @GetMapping("/member/diary/scheduler")
    public void scheduler() {
    } 
    @GetMapping("/member/diary/schedulerWrite")
    public void schedulerWritePage() {
    }
    
    @GetMapping("/member/diary/diary")
    public void diaryPage() {
    }
    @GetMapping("/member/diary/diaryDetail")
    public void diaryDetailPage() {
    }
    @GetMapping("/member/diary/diaryWrite")
    public void diaryWritePage() {
    }
    
}
    
