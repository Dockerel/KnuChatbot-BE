1. (MySQL) image 객체에는 Base64 문자열을 해시화한 값 + s3 이미지 url 저장
2. (S3) 해시화한 값(파일명)과 Base64 문자열 디코딩해서 이미지 형태로 저장

# member

## todo

* /api/test
* /api/test
*
* /api/member/check-email ✅
* /api/member/signup ✅
* /api/member/login
* /api/member/info
* /api/member/delete
*
* /api/front-ai-response
*
* /api/chat/user-question/:historyId
*
* /api/history/new-history
* /api/history/show-all
* /api/history/show-questions/:historyId
* /api/history/rename/:historyId/:historyName
* /api/history/delete/:historyId

&rarr; controller단에서 valid 확인하도록, 지금은 validate 조건이 모두 entity에 있음