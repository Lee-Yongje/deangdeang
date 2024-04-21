package com.example.demo.entity;

import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Entity
@Table(name="puppy")
public class Puppy {
    @Id
    private int pno;
    private String p_name;
    private Date p_birth;
    private String p_fname;
    private String p_sex;
    private String p_neuter;
    private String p_color;

    @ManyToOne
    @JoinColumn(name="uno", referencedColumnName="uno")
    private Users user;

    @Transient
    private MultipartFile uploadFile;
}