1. 시나리오 선택 - 콘서트 예약 서비스

   
2. Milestone
   ![image](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/34e607f1-7ab8-41f9-bcec-45c162e11ee3)

3. 시퀀스 다이어그램

   
   3-1) 토큰 발급 API
   
   ![토큰 발급 시퀀스 다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/69eb643c-a268-4d13-92d9-fc96a0e7c173)



   3-2) 토큰 상태 조회 API
   
  ![토큰 상태 조회 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/40173a72-8720-4be8-b8c1-af3a1ab46ce6)



  3-3) 예약 가능 날짜 조회 API
  
  ![예약 가능 날짜 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/4352026a-7fcb-4c18-b40c-a738d48953e7)



  3-4) 예약 가능 좌석 조회 API
  
  ![예약 가능 좌석 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/effc4880-6429-427c-ae2a-d616f8dca94b)



  3-5) 좌석 예약 요청 API
  
  ![좌석 예약 요청 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/04ecbbba-d727-4a1e-ae5e-5834fea0194a)



  3-6) 잔액 조회 API
  
  ![잔액 조회 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/f0f5e39b-2961-43f2-ab32-32785a8c64be)



  3-7) 잔액 충전 API
  
  ![잔액 충전 시퀀스다이어그램](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/d22f1512-5283-45b5-9948-a6d41617847e)



  3-8) 결제 API
  
  ![결제 시퀀스다이어그램 (1)](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/11cd8f48-249d-4532-ac1c-c1b2a4f305c4)




4. ERD
   ![콘서트예약](https://github.com/cjm2357/hhp-concert-resevation/assets/101917893/ebf4d4a0-bd78-43cb-8843-eeaf12711494)

   - 좌석테이블에서 state가 빈번하게 변경이 일어날 것이라 예상되어 좌석을 조회하고 업데이트하는 과정을 비관적락으로 처리예정
     -> 다른 유저가 좌석을 조회할때 해당 row의 lock이 걸려 예약한 유저가 예약정보를 조회할때 조회속도가 느릴것 같아 예약 테이블을 비정규화로 구성
   - 토큰 테이블에서 state가 WAITING인 row를 생성시간별로 정렬하여 대기번호 발급



5. API 명세서
   https://documenter.getpostman.com/view/34472505/2sA3dyhAjm#011b50ea-242d-42fd-8abc-4156007dff83



  



  



