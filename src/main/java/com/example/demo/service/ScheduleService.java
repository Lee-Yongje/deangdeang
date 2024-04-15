package com.example.demo.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.ScheduleDAO;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;


@Service
public class ScheduleService {
	
	@Autowired
	private ScheduleDAO dao;
	
	
	//----------스케줄러----------
	public List<Puppy> getPuppyByUsersId(long id) {
        return dao.findPuppyById(id);
    }
	
	// 특정 날짜 스케줄 가져오기
	public List<Schedule> getSchedulesByDate(long id, Date date) {
		  return dao.findSchedulesByDate(id, date);
		}
	
	
}