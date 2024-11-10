- [x]  Junit를 이용한 테스트 코드 작성법 이해
- [x]  Spring Security를 이용한 Filter에 대한 이해
- [x]  JWT와 구체적인 알고리즘의 이해
- [x]  PR 날려보기
- [x]  리뷰 바탕으로 개선하기
- [x]  EC2에 배포해보기

---


### **Spring Security 기본 이해**

#### **Filter란 무엇인가요? (with Interceptor, AOP)**
- **Filter**는 서블릿 요청과 응답을 처리하는 객체로, 요청이 서블릿에 도달하기 전이나 응답이 클라이언트로 반환되기 전에 작업을 수행. 인증, 권한 부여, 로깅 등에 사용.
  
  - **Interceptor**: Spring MVC에서 요청 전후로 작업을 수행. `@RequestMapping` 메서드 호출 전에 또는 후에 처리.
  
  - **AOP**: 공통 기능을 모듈화하여 코드 중복을 줄이는 방식. `@Before`, `@After` 어노테이션으로 메서드 실행 전후 처리.

#### **Spring Security란?**
- **Spring Security**는 애플리케이션의 인증(authentication)과 권한 부여(authorization)를 관리하는 보안 프레임워크. 인증은 사용자가 누구인지 확인하고, 권한 부여는 사용자가 무엇을 할 수 있는지 결정.
  
  - **인증(Authentication)**: 사용자가 누구인지 확인.
  
  - **권한 부여(Authorization)**: 사용자가 무엇을 할 수 있는지 결정.

---

### **JWT 기본 이해**

#### **JWT란?**
- **JWT (JSON Web Token)**는 JSON 객체를 사용해 정보를 안전하게 전달하는 인증 방식. 인증과 권한 부여에 주로 사용.
  
  1. **헤더(Header)**: 토큰 타입과 서명 알고리즘 지정.
  2. **페이로드(Payload)**: 사용자 정보 또는 인증 정보 포함.
  3. **서명(Signature)**: 토큰 위변조 방지.

  - **Self-contained**: 필요한 모든 정보를 토큰에 담고 있어 서버 상태 저장 필요 없음.
  
  - **Stateless**: 서버가 상태를 저장하지 않음.
  
  - **Compact**: 텍스트 기반의 짧은 문자열로 전송 속도 빠름.

  JWT는 인증된 사용자에게 발급되어 API 요청 시 `Authorization` 헤더에 담아 전송. 서버는 JWT를 통해 사용자 인증 및 권한 확인


---
  ### **테스트 시나리오 작성**

  1. **Access Token 발행**
   - 정상적인 로그인 요청에 대해 Access Token을 발급 받는다.
   - 반환된 Access Token은 만료 시간이 포함되어 있고, 유효한 값을 가져야 한다.
   - 잘못된 로그인 정보로 Access Token 발급을 시도하면, 에러가 발생한다.

2. **Refresh Token 발행**
   - 유효한 Refresh Token을 사용하여 새로운 Access Token을 발급 받는다.
   - 만료된 Refresh Token을 사용하여 Access Token을 발급받으려 하면, 에러가 발생한다.

3. **Access Token 검증**
   - 만료된 Access Token을 사용하여 보호된 리소스를 요청하면 사용자의 기존 Refresh Token을 통해 Access Token을 새로 발급 받는다.
   - 만료된 Refresh Token을 사용하여 Access Token을 갱신하면, 에러가 발생한다.
   - 유효한 Access Token을 사용하여 요청을 보내면, 정상적으로 응답이 돌아온다.

4. **Refresh Token 검증**
   - 만료된 Refresh Token을 사용하여 Access Token을 갱신하면, 에러가 발생한다.
   - 유효한 Refresh Token을 사용하여 Access Token을 갱신하면, 새로운 Access Token이 발급된다.

5. **토큰을 이용한 인증**
   - 유효한 Access Token을 포함하여 보호된 리소스를 요청하면 정상 응답이 반환된다.
   - 만료된 Access Token을 포함한 요청을 보내면 에러가 발생한다.
   - 만료된 Access Token을 사용할 경우, Refresh Token을 이용하여 새로운 Access Token을 발급받고 정상 응답을 받는다.
