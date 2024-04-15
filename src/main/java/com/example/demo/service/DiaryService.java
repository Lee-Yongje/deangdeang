package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.DiaryDAO;
import com.example.demo.entity.Diary;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;
import com.example.demo.entity.Users;


@Service
public class DiaryService {
	
	@Autowired
	private DiaryDAO dao;
	
	
	//----------스케줄러----------
	public List<Puppy> getPuppyByUserId(int uno) {
        return dao.findPuppyByUno(uno);
    }
	
	// 특정 날짜 스케줄 가져오기
	public List<Schedule> getSchedulesByDate(int uno, LocalDate date) {
		  return dao.findSchedulesByDate(uno, date);
		}

	
	//----------집사일지----------
	
	// 게시글 번호
	public int getNextDno() {
		return dao.getNextDno();
	}

	
	// 유저별 전체 다이어리 조회
	public List<Diary> getDiariesByUno(int uno) {
	    return dao.findByUsersUno(uno);
	}
	
//	// 일지 상세 정보
//	public List<Diary> getDiaryDetailsByUnoAndDno(int uno, int dno){
//		return dao.findByUsersUnoAndDno(uno, dno);
//	}
	
	// 일지 상세
	public Diary getDiaryById(int dno) {
		return dao.findById(dno).orElse(null);
    }
	
	 
	// 일지 등록
    public void saveDiary(Diary diary) {
        dao.save(diary);
    }
    
    // 일지 수정
    public void updateDiary(Diary diary) {
    	dao.save(diary);
    }
    
    // 일지 삭제
    public void deleteDiary(int dno) {
        dao.deleteById(dno);
    }


	
}
