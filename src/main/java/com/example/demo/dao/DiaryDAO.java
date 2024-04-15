package com.example.demo.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Diary;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;

@Repository
public interface DiaryDAO extends JpaRepository<Diary, Integer> {
	
	//----------스케줄러----------	
	
	// 스케줄러 강아지 목록 출력
	@Query("select p from Puppy p join p.user u where u.uno = :uno")
	List<Puppy> findPuppyByUno(@Param("uno") int uno);


	
	@Query("select s from Schedule s where s.users.uno = :uno and s.s_date = :date")
	List<Schedule> findSchedulesByDate(@Param("uno") int uno, @Param("date") LocalDate date);

	
	
	
	//----------집사일지----------
	
//	// 일지 요약 정보 출력 (내용이 200자 이상일 경우 '...'으로 처리 => 오류로 html에 적용
//	@Query(value="select diary(d.dno, d.d_title, " +
//            "case when length(d.d_content) > 200 then concat(substring(d.d_content, 1, 200), '…') else d.d_content end, " +
//            "d.d_date, d.d_fname) from diary d where d.uno = ?1")
//    List<Diary> findDiarySummariesByUno(int uno);
	
	 
	//목록 출력
	@Query(value="select * from diary where uno=?1 order by d_date desc", nativeQuery=true)
	List<Diary> findByUsersUno(int uno);
	
//	@Query(value = "select dno, d_title, case when char_lenght(d_content) > 160 then concat(left(d_content, 160), '…') else d_content end as d_content, d_date, d_fname from diary wher uno = ?1 order by d_date desc", nativeQuery = true)
//	List<Diary> findByUsersUno(int uno);
	
	
	// 상세내용 출력
	List<Diary> findByUsersUnoAndDno(int uno, int dno);

	// 게시글 번호 추가
	@Query(value="select ifnull(max(dno),0) + 1 from diary", nativeQuery = true)
	public int getNextDno();



}