package fun.tayo.app.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.Member;
import fun.tayo.app.service.face.MemberService;
import fun.tayo.app.service.face.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialLoginServiceImpl implements SocialLoginService{

	//멤버 서비스
	private final MemberService memberService;
	
	@Override
	public boolean login(String code, HttpServletRequest request) {
		
		//액세스토큰 받기 
		final String KakaoAccessToken = getKakaoAccessToken(code);
		
		//액세스토큰이 null일경우
		if(KakaoAccessToken == null) {
			
			log.error("액세스토큰 파싱 실패!");
			
			//로그인 실패
			throw new IllegalStateException("엑세스 토큰 파싱 실패");
		}

		final String email = getEmailFromKakao(KakaoAccessToken);
		
		//이메일이 null일 경우
		if(email == null) {
			
			log.error("이메일 파싱 실패!");
			
			//로그인 실패
			throw new IllegalStateException("이메일 파싱 실패");

		}
		
		final Member member = memberService.getMemberByEmail(email);
		
		//세션 받기
		HttpSession session = request.getSession();
		
		//미가입자
		if(member == null) {
			
			session.setAttribute(SessionConst.KAKAO_EMAIL, email);
			//로그인 실패
			return false;
		}
		
		
		//로그인 정보 받기
		memberService.setLogin(member, session);
		
		//로그인 성공
		return true;
	}
	
	
	private String getEmailFromKakao(String accessToken) {
		
		//카카오API URL
		String apiURL = "https://kapi.kakao.com/v2/user/me";
		
		try {
			
			URL url = new URL(apiURL);
			
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", "Bearer " + accessToken);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setDoOutput(true);
			
			return parseEmail(con);
			
		} catch (Exception e) {
			
			log.error("카카오 로그인 요청 오류", e);
			
		}

		return null;
	}

	private String parseEmail(HttpURLConnection con) throws IOException {
		
		String responseBody = parseResponseBody(con);

		if (responseBody == null) {
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		
		@SuppressWarnings("unchecked") final Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);

		return ((Map<String, Object>) map.get("kakao_account")).get("email").toString();
	}


	private String getKakaoAccessToken(String code) {
		
		String clientId = "d688ecbcd7678fc036df85dfda0efcf3"; //애플리케이션 클라이언트 아이디값;
		String redirectURI = "https%3a%2f%2flocalhost%3a8443%2flogin%2fkakao";


		String apiURL = "https://kauth.kakao.com/oauth/token";
		String params = "grant_type=authorization_code";
		
		params += "&client_id=" + clientId;
		params += "&redirect_uri=" + redirectURI;
		params += "&code=" + code;

		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
			writer.write(params);
			writer.flush();
			writer.close();
			os.close();


			return parseAccessToken(con);
		} catch (Exception e) {
			log.error("카카오 로그인 요청 오류", e);
		}

		return null;
	}

	private String parseAccessToken(HttpURLConnection con) throws IOException {
		String responseBody = parseResponseBody(con);

		if (responseBody == null) {
			return null;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked") final Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);

		return map.get("access_token").toString();
	}
	private String parseResponseBody(HttpURLConnection con) throws IOException {
		int responseCode = con.getResponseCode();
		BufferedReader br;

		if (responseCode == 200) { // 정상 호출
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} else {  // 에러 발생
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}

		String inputLine;
		StringBuilder res = new StringBuilder();
		while ((inputLine = br.readLine()) != null) {
			res.append(inputLine);
		}

		br.close();

		if (responseCode == 200) {
			return res.toString();
		}

		log.error("카카오 로그인 응답 에러 : {}", res);
		return null;
	}
}
