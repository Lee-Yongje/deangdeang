package com.example.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.DiaryDAO;
import com.example.demo.entity.Diary;


@Service
public class DiaryService {
	
	@Autowired
	private DiaryDAO dao;

	//----------집사일지----------
	
	// 게시글 번호
	public int getNextDno() {
		return dao.getNextDno();
	}

	
	// 유저별 전체 다이어리 조회
    public List<Diary> getDiariesById(Long id) {
        return dao.findByUsersId(id);
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

    
    public List<Diary> getDiariesByIdAndYearAndMonth(Long id, int year, int month) {
        return dao.findByYearAndMonthAndId(year, month, id);
    }


	
}
