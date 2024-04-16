package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "user")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uno")
    private Long id;

    @Column(name = "u_pwd", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "u_fname", length = 100)
    private String filename;

    @Column(name = "u_name", length = 20, nullable = false)
    private String name;

    @Column(name = "u_nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "u_phone", length = 11)
    private String phone;

    @Column(name = "u_email", length = 20)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", length = 10, nullable = false)
    private AuthType authType; // Added this to map to the new column

    @ManyToOne
    @JoinColumn(name = "rno", referencedColumnName = "rno") // Assuming 'rno' is mapped elsewhere correctly
    private RegionCode regionCode;

    @Transient
    private MultipartFile uploadFile;

	public Users orElseGet(Object object) {	
	    return null;
	}
}
