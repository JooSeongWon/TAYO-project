package fun.tayo.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.UploadFile;
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
		
		MemberSession member = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
		log.debug("session : {}", session);
		log.debug("/profile : {}", member.getId());
		
		//로그인된 사용자의 정보 조회
		Member info = profileService.info( member.getId());
		log.debug("조회결과 info {}", info);
		
		//사용자의 정보, 모델값 전달
		model.addAttribute("info", info);
		System.out.println("========" + info);
		
		return "user/member/profile";
		
	}
	
	@RequestMapping("/update")
	public String updateProfile(UploadFile file, HttpSession session) {
		log.debug("프로필수정 : {}");
		
		MemberSession member = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
		
//		member.setId((int) session.getAttribute("id"));
		member.setName((String) session.getAttribute("name"));
//		member.setPhone((int) session.getAttribute("id"));
		
		
		
		log.debug("프로필수정 : {}", member);
		
		profileService.update(member, file);
		
		return "user/member/profile";
	}
	
}
