package fun.tayo.app.controller;

import javax.mail.Session;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.FileService;
import fun.tayo.app.service.face.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ProfileController {

    private final ProfileService profileService;
    
    @GetMapping("/profile")
    public String showCheckPasswordForm(HttpSession session) {
    	
    	MemberSession memberSession = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
    	if(session.getAttribute(SessionConst.PROFILE_CHECK_PW) != null ) {
    		session.setAttribute(SessionConst.PROFILE_CHECK_PW, null);
    	} 
    	
    	if(profileService.isSocial(memberSession.getId())) { //소셜회원
    		session.setAttribute(SessionConst.PROFILE_CHECK_PW, true);
    		return "redirect:/profile/update";
    	} 
    	
    	//비밀번호 체크 필요
    	return "user/member/profile";
    }
    
    @ResponseBody
    @PostMapping("/profile")
    public ResponseData checkPassword(HttpSession session, String password) {
    	
    	MemberSession memberSession = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
    	
    	if(profileService.checkPassword(memberSession.getId(), password)) {
    		session.setAttribute(SessionConst.PROFILE_CHECK_PW, true);
    		return new ResponseData(true, "ok");
    	} 
    	
		return new ResponseData(false, "비밀번호가 일치하지 않습니다.");
    	
    }
    

    @GetMapping("/profile/update")
    public String profileForm(HttpSession session, Model model) {
    	
    	//url 직접 접근 방지
    	if(session.getAttribute(SessionConst.PROFILE_CHECK_PW) == null) {
    		return "redirect:/profile";
    	}

    	MemberSession member = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
    	log.debug("session : {}", session);
    	log.debug("/profile : {}", member.getId());

    	//로그인된 사용자의 정보 조회
    	Member memberInfo = profileService.info(member.getId());
    	log.debug("조회결과 info {}", memberInfo);

    	//사용자의 정보, 모델값 전달
    	model.addAttribute("member", memberInfo);
    	System.out.println("========" + memberInfo);

    	return "user/member/update";

    }

    @ResponseBody
    @PostMapping("/profile/update")
    public ResponseData updateProfile(
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam String target,
            @RequestParam String value
    ) {

        log.debug("target {} value {}", target, value);

        final Member member = profileService.info(memberSession.getId());
        return profileService.update(memberSession, member, target, value);
    }

	@ResponseBody
    @PostMapping("/profile/fileupload")
    public ResponseData updateFile(
			@RequestParam(value = "uploadFile") MultipartFile upFile ,
			@SessionAttribute(value = SessionConst.LOGIN_MEMBER)MemberSession memberSession
	) throws IOException {

		return profileService.fileUpload(upFile, memberSession);
    }

}
