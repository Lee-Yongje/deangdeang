
<img width="1039" alt="main" src="https://github.com/sangwon0707/final/blob/main/dangdang_img.jpg"> 
<img width="1039" alt="db" src="https://github.com/sangwon0707/final/blob/main/db_image.png"> 

&nbsp;
&nbsp;

# 🐾 반려동물 관리 웹 애플리케이션 프로젝트

[🔗 프로젝트 기획/주제선정](https://github.com/sangwon0707/final/blob/main/%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%8C%E1%85%A6%E1%86%A8%E1%84%90%E1%85%B3%E1%84%80%E1%85%B5%E1%84%92%E1%85%AC%E1%86%A8.pdf)<br>
[🔗 프로젝트 DB/프로파일러](https://github.com/sangwon0707/final/blob/main/%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%8C%E1%85%A6%E1%86%A8%E1%84%90%E1%85%B3%E1%84%89%E1%85%A5%E1%86%AF%E1%84%80%E1%85%A8.pdf) <br>
[🔗 최종발표 자료](https://github.com/sangwon0707/final/blob/main/%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%8C%E1%85%A6%E1%86%A8%E1%84%90%E1%85%B3%E1%84%87%E1%85%A1%E1%86%AF%E1%84%91%E1%85%AD.pdf) <br>

## 🗓 작업 기간 및 인원
**기간:** 2024.03.27 ~ 2024.04.23  
**팀원:** 5명

## 📋 프로젝트 소개
강아지를 키우는 사람들이 온라인/오프라인으로 자유롭게 소통하고, 강아지와 함께하는 일상을 기록하고 관리할 수 있도록 하는 반려견 전용 통합 커뮤니티 서비스입니다. 이 서비스를 통해 반려동물의 건강과 일상을 체계적으로 관리하고, 최신 동물 관련 정보를 쉽게 접할 수 있습니다.

## 📋 프로젝트 목적
Spring Boot와 Spring Security를 활용하여 웹 애플리케이션을 개발하는 경험을 쌓고, MySQL 데이터베이스와 연동하여 데이터 관리 능력을 강화합니다. OAuth 2.0을 활용한 카카오 소셜 로그인 구현을 통해 OAuth 프로토콜에 대한 이해 및 실무 적용 능력을 증대시키고, 타임리프를 통합하여 프론트엔드와 백엔드의 연계 개발 경험을 확장합니다. 또한, 페이징 기능을 구현하여 사용자 경험 및 UI/UX 개선 능력을 향상시킵니다.

## 🚀 프로젝트 내용
- **최신 동물 뉴스 제공:** 뉴스 섹션을 통해 반려동물 관련 최신 정보를 신속하게 제공합니다.
- **동물병원 검색 및 위치 확인:** 동물병원을 검색하고 위치를 확인하는 기능을 통해 반려동물 의료 서비스 접근성을 향상시킵니다.
- **반려동물 관리 일지 작성:** 댕댕수첩 기능을 통해 반려동물의 일정을 관리하고 기록을 남길 수 있는 서비스를 제공합니다.
- **로그인 및 마이페이지 기능:** 사용자의 개인정보를 관리하고, 비밀번호 변경 및 반려동물 정보를 관리하는 기능을 제공합니다.
- **카카오 소셜 로그인 기능:** OAuth 2.0을 사용하여 카카오 로그인 기능을 구현, 사용자 편의성을 증대시킵니다.

## 🛠 주요 업무 및 상세 역할
- **데이터베이스 설계 및 관리:** MySQL을 활용하여 데이터베이스 설계 및 테이블 생성, 관리.
- **프론트엔드 구조 및 디자인 구현:** 타임리프를 활용한 동적인 웹 페이지 구현.
- **비즈니스 로직 구현:** Spring Boot와 Spring Security를 사용하여 MVC 패턴 기반의 사용자 인증 및 권한 관리.
- **AWS 배포:** Elastic Beanstalk를 활용하여 프로젝트를 AWS에 배포하여 운영 환경 구축.
- **REST API 사용:** 마이페이지, 반려동물 정보 제공 및 병원 검색 기능 등을 RESTful API로 구현하여 클라이언트와 서버 간 효율적인 데이터 통신을 구현.

## 💻 사용 언어 및 개발 환경
- **Front-End:** HTML5, CSS3, JavaScript, jQuery, Thymeleaf, Bootstrap
- **Back-End:** Java, Spring Framework, Spring Boot, Spring Security, Spring Data JPA, Lombok
- **Database:** MySQL, MySQL Workbench
- **Tools & Platforms:** STS4(Eclipse), GitHub, AWS

1. **로그인 및 회원가입 기능**
    - Spring Security를 통해 로그인 및 회원가입 기능을 제공합니다. 신규 사용자는 회원가입 페이지를 통해 MySQL DB에 등록되며, 카카오 사용자는 OAuth 2.0을 이용해 카카오 인증 서버와 통신 후 회원가입 진행 및 이후 자동 로그인 처리합니다.

2. **동물 관련 뉴스 제공**
    - Jsoup 라이브러리를 사용하여 동물 관련 뉴스를 크롤링하고, 뉴스 항목은 페이지 단위로 출력합니다.

3. **동물병원 검색 기능**
    - 전국 동물병원 공공데이터를 JSON 형식으로 받아와 제공하는 기능을 구현했습니다.
    - 시도, 시군구, 병원명으로 검색할 수 있으며, 검색 결과는 리스트 형태로 표시되고, 클릭 시 상세 정보 페이지로 연결됩니다.
    - 병원 상세 페이지에는 병원의 위치를 카카오 지도를 통해 시각적으로 제공하여 사용자 편의성을 증진시켰습니다.

4. **반려견 일정 관리 및 일지 작성 기능**
    - **댕댕스케줄:** 사용자는 반려견별로 일정을 추가하고 관리할 수 있습니다. 월별 캘린더를 통해 일정을 확인하고, 특정 날짜의 일정을 조회할 수 있습니다. 일정은 반려견의 색상으로 구분되며, 완료된 일정은 별도로 표시됩니다.
    - **집사일지:** 반려견의 일상과 특별한 순간을 기록할 수 있습니다. 일지는 월별로 조회할 수 있으며, 상세 페이지에서 일지 내용 확인 및 수정, 삭제가 가능하며 사진을 첨부할 수 있고, 파일은 서버에 저장됩니다.

5. **마이페이지 기능**
    - 사용자가 회원 정보를 수정하고 비밀번호를 변경할 수 있는 기능을 제공합니다.
    - **비밀번호 변경 기능은 카카오 로그인 사용자를 고려하여 구현하였습니다.**
        - **타임리프를 통한 필드 잠금 (프론트 제한):** 카카오로 로그인한 사용자는 `authType`이 `KAKAO`로 등록되어 비밀번호 변경 기능에서 입력 필드를 Thymeleaf를 이용하여 `disabled` 속성으로 잠가두고 유저에게는 비밀번호 변경 불가를 안내합니다.
        - **서버 측 검증 (백엔드 제한):** 비밀번호 변경 요청이 들어올 때, `authType`이 `STANDARD`인 경우에만 비밀번호를 변경할 수 있도록 서버 측에서 추가 검증을 수행합니다. 이를 통해 사용자가 프론트엔드를 강제로 수정하여 비밀번호 수정 필드를 열더라도, 서버에서 다시 한 번 확인하여 변경을 방지합니다. 이러한 처리 방법을 통해 카카오 로그인 사용자의 비밀번호 변경을 완전히 제한하였습니다.

6. **프론트엔드 구조 및 디자인 구현**
    - 타임리프(Thymeleaf)를 사용하여 동적인 웹 페이지를 구현합니다. HTML5, CSS3, JavaScript, jQuery, Bootstrap을 사용하여 반응형 디자인과 사용자 친화적인 UI/UX를 제공합니다.


7. **데이터베이스 설계 및 관리**
    - MySQL을 활용하여 데이터베이스를 설계하고 테이블을 생성 및 관리합니다. 효율적인 데이터 저장과 관리를 위해 최적화된 쿼리를 작성하고 인덱스를 설정하여 성능을 향상시킵니다.

8. **REST API 사용**
    - 마이페이지, 반려동물 정보 제공 및 병원 검색 기능 등을 RESTful API로 구현하여 클라이언트와 서버 간 효율적인 데이터 통신을 구현합니다. 이를 통해 애플리케이션의 확장성과 유지 보수성을 향상시킵니다.

9. **비즈니스 로직 구현**
    - Spring Framework와 Spring Boot를 활용하여 MVC 패턴에 기반한 비즈니스 로직을 구현합니다. Spring Security를 사용하여 사용자 인증 및 권한 관리를 처리합니다.

10. **AWS 배포**
    - Elastic Beanstalk를 사용하여 애플리케이션을 AWS에 배포하고 운영 환경을 구축합니다. 이를 통해 애플리케이션의 확장성과 가용성을 확보합니다.

