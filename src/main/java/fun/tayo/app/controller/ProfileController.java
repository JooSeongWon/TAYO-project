package fun.tayo.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fun.tayo.app.dto.Member;
import fun.tayo.app.service.face.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ProfileController {
	
	private final ProfileService profileService;
	
	@RequestMapping("/profile")
	public String profile(HttpSession session, Model model) {
		
		String memberId = (String) session.getAttribute("memberId");
		log.debug("session : {}", session);
		log.debug("/profile : {}", memberId);
		
		//로그인된 사용자의 정보 조회
		Member info = profileService.info( memberId );
		log.debug("조회결과 info {}", info);
		
		//사용자의 정보, 모델값 전달
		model.addAttribute("info", info);
		
		return "user/member/profile";
		
	}
	
}
