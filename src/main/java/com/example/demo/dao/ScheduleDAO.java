package com.example.demo.dao;

import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Puppy;
import com.example.demo.entity.Schedule;

@Repository
public interface ScheduleDAO extends JpaRepository<Schedule, Integer> {
    @Query("select p from Puppy p join p.user u where u.id = :id")
    List<Puppy> findPuppyById(@Param("id") Long id);

    @Query("select s from Schedule s where s.users.id = :id and s.s_date = :date")
    List<Schedule> findSchedulesByDate(@Param("id") Long id, @Param("date") Date date);
}