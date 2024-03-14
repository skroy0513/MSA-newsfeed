# 소셜 네트워크 서비스
회원가입을 완료한 유저들이 게시글을 올리며 자유롭게 소통할 수 있는 서비스입니다.
일반 유저들은 게시글을 작성하며, 상대의 게시글에 댓글을 달거나 좋아요를 누를 수 있으며, 원하는 유저를 팔로우 할 수 있습니다.
나의 뉴스피드에는 내가 팔로우한 유저들의 게시글들을 불러옵니다.

## 개발환경
- JAVA 17
- Spring Boot
- MySql
- JPA
- Redis
- JWT
- Feign Client


## ERD
![Untitled](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/27a2510d-79d3-4017-bfd8-4b7b5d185990)


## 아키텍처
![newsfeed](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/d2a3801b-8c13-4a73-8b9e-5cfd2823ff81)


## 프로젝트 기능
### 유저기능 (User-Service)
- 회원가입 및 로그인
- 프로필 수정
### 활동기능 (Activity-Service)
- 게시글 작성
- 댓글 작성
- 좋아요
- 팔로우하기
### 뉴스피드기능 (Newsfeed-Service)
- 내가 팔로우한 사람들의 소식 받기
- 내 게시글에 대한 소식 받기(댓글, 좋아요)

<details>
<summary>API 보기</summary>
<div markdown="1">
  
![user-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/e244380f-0e8f-4934-9f12-0ea8c4f4a3a9)
![activity-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/4bc4965d-4255-432b-ba78-c0aa8adb547b)
![newsfeed-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/43f8c464-1c83-43dc-b37b-b313dabc20df)

</div>
</details>

## 트러블슈팅
- [이메일 인증](https://skroy0513.tistory.com/15)
  
- [api-gateway, JWT-Token](https://skroy0513.tistory.com/16)
 
- [RefreshToken을 통한 AccessToken재발급](https://skroy0513.tistory.com/17)

