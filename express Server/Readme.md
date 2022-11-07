<h1>서버 실행시 수정사항</h1>

1. 서버 실행 전 사용할 포트 번호 지정
   * .env 파일의 port = (사용할 포트 번호) 작성

<br>

2. SSL 적용 파일 붙혀넣기
   * ca.crt
   * ca.key

<br>

3. Spring web Server의 주소 설정
   * .env 파일의 SPRING_SERVER = https://(web Server의 주소 및 포트) 작성

<br>

4. 서버실행
   * 터미널에서 ``` node server.js ``` 실행 