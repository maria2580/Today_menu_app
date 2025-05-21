# 오늘 뭐 먹지? - Today's Menu App

## 🌟 소개

**오늘 뭐 먹지?** 앱은 사용자가 일일 식단(점심, 저녁)을 확인하고, 사진을 공유하며, 메뉴에 대한 피드백을 남길 수 있는 안드로이드 애플리케이션입니다.

이 프로젝트는 IT 동아리 '프리미티브'의 2학년 학생 주도로 1학년 학생들과 함께 진행한 앱 스터디 연계 소규모 프로젝트입니다. 기본적인 기능 구현을 통해 안드로이드 앱 개발의 전반적인 과정을 학습하고 협업하는 것을 목표로 했습니다.

## ✨ 주요 기능

* **일일 식단 정보 제공:** 선택한 날짜의 점심 및 저녁 메뉴를 보여줍니다.
    * 공주대학교 기숙사 식당 웹사이트([dormi.kongju.ac.kr](https://dormi.kongju.ac.kr/))에서 메뉴 정보를 크롤링합니다.
* **날짜 네비게이션:**
    * '이전', '다음' 버튼을 통해 날짜를 쉽게 이동할 수 있습니다.
    * 날짜를 직접 선택할 수 있는 DatePicker 기능을 제공합니다.
* **식단 사진 공유:**
    * 서버에 저장된 식단 사진을 불러와 보여줍니다.
    * 사용자가 직접 촬영하거나 갤러리에서 식단 사진을 선택하여 서버에 업로드할 수 있습니다.
* **사용자 피드백:**
    * **좋아요/싫어요:** 각 날짜의 식단에 대해 좋아요 또는 싫어요를 표시할 수 있습니다. (로컬 DB 및 서버 연동)
    * **건의하기:** 특정 날짜의 식단에 대한 개선 사항이나 의견을 작성하여 서버로 전송할 수 있습니다.
* **댓글 기능:**
    * 각 날짜의 식단에 대한 댓글을 확인하고 작성할 수 있습니다.
    * 댓글은 서버에 저장되어 다른 사용자와 공유됩니다.
* **실시간 데이터 동기화:** `Observer` 패턴을 활용하여 메뉴, 사진, 댓글 등의 데이터 변경 시 UI가 동적으로 업데이트됩니다.

## 📸 스크린샷

* **메인 화면:**
  
 <div style="margin-left:100px"><img src="https://github.com/user-attachments/assets/a4ca6d72-347b-4531-841c-c49c66343426" width="200" ><img src="https://github.com/user-attachments/assets//94ac6e86-35ad-4be8-aefc-5e2babc70515" width="200"></div> 


* **건의하기 화면:**.
    ![건의사항 작성](https://github.com/user-attachments/assets/f5b3fcc1-e4cb-4cd7-bb5b-c93e28a06dd5)
    ![건의사항 전송](https://github.com/user-attachments/assets/0f9da268-845a-4253-80fd-a9f863b84295)



* **날짜 선택 화면:**
    ![날짜 선택1](https://github.com/user-attachments/assets/0deb0d74-db3d-4a3d-b117-681a837ce7d2)
    ![날짜 선택2](https://github.com/user-attachments/assets/140fe0c1-cf8c-43dc-9fd9-5a9042b818e3)
   ![날짜선택 이후](https://github.com/user-attachments/assets/bb5b2742-22fd-4188-b3bd-7839b360385c)


## ⚙️ 기술 스택

* **언어:** Java
* **플랫폼:** Android
* **네트워킹:** Retrofit2 (API 통신), Gson (JSON 파싱)
* **웹 크롤링:** Jsoup
* **로컬 데이터베이스:** SQLite
* **UI:** RecyclerView, ConstraintLayout, LinearLayout
* **비동기 처리:** Thread, Handler
* **데이터 관리:** Observable 패턴 (Custom `Data_on_changed` 클래스)

## 🧑‍💻 팀 및 기여 역할

본 프로젝트는 '프리미티브' 동아리 앱 스터디의 일환으로 진행되었습니다.

* **프로젝트 리더 및 주요 개발 (2학년):** [여기에 사용자분의 성함/닉네임을 적어주세요]
    * 프로젝트 총괄 및 아키텍처 설계
    * `MainActivity`의 핵심 로직 및 UI/UX 구현
    * `SubActivity` (건의사항) 기능 전체 구현
    * 서버 통신 (Retrofit, DTOs, API 인터페이스, 네트워크 스레드) 설정 및 구현
    * `Data_on_changed` 클래스 (Observable) 구현을 통한 데이터 관리 및 UI 동기화
    * 이미지 처리 (Bitmap <-> Base64 String 변환, 서버 업로드/다운로드)
    * Jsoup을 이용한 웹 크롤링 모듈(`MyThread`) 연동 및 개선
    * SQLite를 이용한 로컬 데이터(투표 상태 등) 관리
    * 1학년 팀원들이 개발한 V1 기능 통합 및 전체 시스템 안정화
    * 기타 모든 잔여 기능 구현 및 버그 수정

* **팀원 (1학년):**
    * **식단 정보 연동 (V1):** 예지
        * `getMenu_on_web`: Jsoup 크롤링 결과를 파싱하여 초기 메뉴 정보를 화면에 표시하는 부분 담당.
        * `getImageOnLocal`: 사용자가 로컬 기기에서 식단 사진을 선택하는 기능 구현.
    * **추천/비추천 기능 (V1):** 박시현
        * `sendVoteUp`, `sendVoteDown`: 메뉴에 대한 추천/비추천 선택 시 로컬 SQLite DB에 상태를 기록하는 기능 구현 (서버 연동 전 단계).
    * **건의사항 화면 전환 (V1):** 홍현지
        * `sendRequest`: '건의하기' 버튼 클릭 시 `SubActivity`로 날짜 정보를 넘겨주며 화면을 전환하는 기능 구현.
    * **댓글 기본 기능 (V1):** 세진
        * `getCommentStrings` (호출부): 특정 날짜의 댓글을 서버에서 가져오기 위한 네트워크 요청 시작 부분 담당.
        * `addComment` (호출부): 사용자가 작성한 댓글을 서버로 전송하기 위한 네트워크 요청 시작 부분 담당.
    * **댓글 UI 표시 (V1):** 박은비
        * `setCommentOnLayout`: 서버에서 가져온 댓글 문자열 배열을 `RecyclerView`를 통해 화면에 표시하는 기능 담당.

## 🛠️ 주요 기능 상세

* **식단 정보 로딩 (`getMenu_on_web`):**
    `MyThread` (Jsoup 사용)를 통해 공주대학교 기숙사 식당 웹사이트에서 특정 날짜의 주간 식단표 HTML을 가져옵니다.
    가져온 HTML에서 선택된 날짜의 점심, 저녁 메뉴를 파싱하여 `Data_on_changed` 객체에 저장하고, UI를 업데이트합니다.

* **이미지 업로드/다운로드:**
    사용자가 이미지를 선택하면(`getImageOnLocal`), 해당 이미지는 Bitmap으로 변환 후 Base64 문자열로 인코딩되어 Retrofit을 통해 서버로 전송됩니다 (`set_lunch_image_on_DB`, `set_dinner_image_on_DB`).
    화면에 이미지를 표시할 때는 서버로부터 Base64 인코딩된 이미지 문자열을 받아 Bitmap으로 디코딩한 후 `ImageView`에 설정합니다 (`get_lunch_image_on_DB`, `get_dinner_image_on_DB` 내부 로직 및 `Data_on_changed` 연동).

* **댓글 시스템:**
    사용자가 댓글을 작성하고 '전송' 버튼을 누르면 (`addComment`), 해당 내용은 Retrofit을 통해 서버로 전송됩니다.
    화면 진입 시 또는 날짜 변경 시 (`getCommentStrings`), 서버로부터 해당 날짜의 댓글 목록을 가져와 `Data_on_changed`를 통해 `RecyclerView`에 표시합니다 (`setCommentOnLayout`). 댓글 시간은 상대 시간(예: "5분 전", "1시간 전")으로 표시됩니다.

* **데이터 동기화 및 UI 업데이트 (`Data_on_changed`, `Observer`):**
    `Data_on_changed`는 Singleton으로 구현된 `Observable` 클래스입니다.
    식단 텍스트, 식단 이미지, 댓글 목록 등 주요 데이터가 변경될 때마다 `notifyObservers()`를 호출합니다.
    `MainActivity`는 `Observer`로 등록되어 있어, 데이터 변경 알림을 받으면 `update_changes_on_layout()` 메소드를 통해 UI를 최신 상태로 갱신합니다.

## 🚀 실행 방법 (참고)

1.  Android Studio에서 프로젝트를 엽니다.
2.  필요한 SDK 및 라이브러리가 설치되어 있는지 확인합니다.
3.  `network/URL_holder.java` 파일에 실제 운영 중인 백엔드 서버의 Base URL이 정확히 입력되어 있는지 확인합니다. (현재 코드: `http://140.245.76.30:10000/`)
4.  에뮬레이터 또는 실제 Android 기기에서 앱을 실행합니다.

---
