package com.example.demo.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;

import jakarta.transaction.Transactional;

@Repository
public interface ScheduleDAO extends JpaRepository<Schedule, Integer> {
	
	//----------스케줄러----------	
	
	// 스케줄러 강아지 목록 출력
	@Query("select p from Puppy p join p.user u where u.id = ?1")
	public List<Puppy> findPuppyById(Long id);
	
	
	@Query("select s from Schedule s where s.users.id = ?1 and s.s_date = ?2")
	public List<Schedule> findSchedulesByDate(Long id, Date date);

	
	@Query("select s from Schedule s where s.users.id = ?1 and s.s_date between ?2 and ?3")
	public List<Schedule> findSchedulesByMonth(Long id, Date startOfMonth, Date endOfMonth);
    
	 
	@Query(value = "select ifnull(max(sno),0)+1 from schedule", nativeQuery = true)
	public int getNextSno(); 
	
	
	@Modifying
	@Query("update Schedule s set s.s_content = ?2 where s.sno = ?1")
	public void updateContentById(int sno, String content);

	
	@Query(value = "select * from Schedule where uno = ?1 and month(s_date) = ?2 and year(s_date) = ?3", nativeQuery = true)
	public List<Schedule> findByYearAndMonth(Long uno, int month, int year);

	@Modifying
	@Query(value = "delete from schedule where pno=?", nativeQuery = true)
	@Transactional
	public void deleteSchedule(int pno);
	
	
}
