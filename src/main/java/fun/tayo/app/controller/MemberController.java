package fun.tayo.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/login")
	public String loginForm() {
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

	@RequestMapping(value = "/join", method=RequestMethod.GET)
	public void join() {}
	
	@RequestMapping(value = "/join", method=RequestMethod.POST)
	public String joinProc(MemberLoginParam login) {
		
		log.info("전달 파라미터 {}", login);

		//회원가입 처리 - LoginService 이용
		memberService.join(login);
		
		//메인페이지로 리다이렉트
		return "redirect:/login";		
		
	}	
	

}

