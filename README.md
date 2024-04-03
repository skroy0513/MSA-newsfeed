# Newsfeed
## 📜 프로젝트 소개
회원가입을 완료한 유저들이 게시글을 올리며 자유롭게 소통할 수 있는 서비스입니다. <br>
일반 유저들은 게시글을 작성하며, 상대의 게시글에 댓글을 달거나 좋아요를 누를 수 있으며, 원하는 유저를 팔로우 할 수 있습니다. <br>
나의 뉴스피드에는 내가 팔로우한 유저들의 게시글들을 불러옵니다.

## 💻 개발환경
- JAVA 17
- Spring Boot
- MySql
- JPA
- Redis
- JWT
- Feign Client

## 📝 ERD
![Untitled](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/27a2510d-79d3-4017-bfd8-4b7b5d185990)


## 📡 아키텍처
- 마이크로서비스 아키텍처(MSA)로 구현을 하였으며, 각 기능별로 서비스를 분리하여 기능 별 결합도를 낮추고 서비스의 확장성을 강화하였습니다.
- 각 서비스들은 REST API를 통해 서로 통신합니다.
  
![newsfeed](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/d2a3801b-8c13-4a73-8b9e-5cfd2823ff81)


## 💡 프로젝트 주요 기능
### 👤유저기능 (User-Service)
- 회원가입 및 로그인이 가능하며, JWT 토큰을 통한 인증/인가를 구현하였습니다.
- 프로필 관리를 통해 노출되는 닉네임과 프로필 사진을 설정할 수 있습니다.
### 🏃활동기능 (Activity-Service)
- 게시글을 작성할 수 있으며, 댓글과 좋아요로 게시글에 반응할 수 있습니다.
- 팔로우 기능으로 더 많은 유저들과 소통할 수 있습니다.
### 📰뉴스피드기능 (Newsfeed-Service)
- 팔로우한 유저의 게시글만 불러오는 기능입니다.
- 다양한 활동에 대한 알림을 수신할 수 있습니다.(댓글 알림, 팔로우 알림, 좋아요 알림 등)

<details>
<summary><b> 📚 API 문서</b></summary>
<div markdown="1">
  
![user-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/e244380f-0e8f-4934-9f12-0ea8c4f4a3a9)
![activity-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/4bc4965d-4255-432b-ba78-c0aa8adb547b)
![newsfeed-service](https://github.com/skroy0513/MSA-newsfeed/assets/117910568/43f8c464-1c83-43dc-b37b-b313dabc20df)

</div>
</details>

## 🛠️ 트러블슈팅 경험
- <b>이메일 인증</b><br>&nbsp;&nbsp;이메일 인증 방식은 링크 클릭과 인증 코드 입력으로 두가지 방식이 존재하는데, 사용자의 편의성을 고려하여 인증 링크를 클릭하는 방법을 선택하고 적용하였습니다. [자세히 보기](https://skroy0513.tistory.com/15)
  
- <b>api-gateway, JWT-Token</b><br>&nbsp;&nbsp;api-gateway의 filter를 통해 jwt 인증/인가를 구현함으로써 모든 서비스에 일관된 로직을 구현할 수 있고 확장성을 강화했으며, User-Service의 부담을 덜어냈습니다. [자세히 보기](https://skroy0513.tistory.com/16)
 
- <b>RefreshToken을 통한 AccessToken재발급</b><br>&nbsp;&nbsp;유저에게 발급한 accessToken이 탈취될 경우를 가정하여 만료시간을 짧게 하고 RefreshToken을 같이 발급하여해당 토큰을 Redis에 만료시간과 같이 저장함으로써 보안성을 강화하였습니다. [자세히 보기](https://skroy0513.tistory.com/17)

