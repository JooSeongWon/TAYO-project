package fun.tayo.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.MemberService;
import fun.tayo.app.service.face.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

	private final MemberService memberService;
	private final SocialLoginService socialLoginService;

	@GetMapping("/login")
	public String login() {
		
		return "user/member/login";
		
	}

	@ResponseBody
	@PostMapping("/login")
	public ResponseData login (
			@ModelAttribute MemberLoginParam memberLoginParam, HttpSession session
			) {
		try {

			return memberService.login(memberLoginParam, session);

		} catch (Exception e) {

			log.error(e.getMessage());

			return new ResponseData(false, "서버오류");
		}
		
	}
	
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
    	
        final HttpSession session = request.getSession(false);
        
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
        	
            return "redirect:/";
            
        }

        session.invalidate();
        
        return "redirect:/";
    }

    
	@GetMapping(value = "/join")
	public String join() {
		
		return "user/member/join";
		
	}
	
	
	@PostMapping(value = "/join")
	public String joinProc(Member member) {
		
		boolean joinResult = memberService.join(member);
		
		if(joinResult) {
			
			log.info("회원가입 성공");
			
			return "redirect:/";
			
		} else {
			
			log.info("회원가입 실패");
			
			return "redirect:/member/join";
		}
	}
	
	@GetMapping("/login/kakao")
	public String kakaoLogin(
			@RequestParam(required = false) String code,
            HttpServletRequest request			
			) {
	       try {
	            if (socialLoginService.login(code, request)) {
	                log.info("로그인 성공!");
	            } else {
	                log.info("로그인 실패! - 미가입자!");
	            }
	        } catch (Exception e) {
	            log.error("서버오류 소셜로그인 실패!");
	        }

	        return "redirect:/";
	}
	
	
	
	
}



