package fun.tayo.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberLoginParam;
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
	private final JavaMailSender mailSender;
	
	@GetMapping("/login")
	public String login() {

		return "user/member/login";

	}

	@ResponseBody
	@PostMapping("/login")
	public ResponseData login(@ModelAttribute MemberLoginParam memberLoginParam, HttpSession session) {
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

		if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

			return "redirect:/";

		}

		session.invalidate();

		return "redirect:/";
	}

	
	@GetMapping(value = "/consent")
	public String consent() {
		
		return "user/member/consent";
		
	}
	
	@GetMapping(value = "/join")
	public String join() {

		return "user/member/join";

	}

	@PostMapping(value = "/join")
	public String joinProc(@ModelAttribute("member") Member member) {

		boolean joinResult = memberService.join(member);

		if (joinResult) {
			memberService.create(member);

			log.info("회원가입 성공");
			return "redirect:/";

		} else {

			log.info("회원가입 실패");

			return "redirect:/user/member/join";
		}
	}

	@GetMapping("/login/kakao")
	public String kakaoLogin(@RequestParam(required = false) String code, HttpServletRequest request) {
		try {
			if (socialLoginService.login(code, request)) {
				
				log.info("로그인 성공!");
				
			} else {
				
				log.debug("request.getSession().getAttribute(\"kakaoemail\") {}", request.getSession().getAttribute(SessionConst.KAKAO_EMAIL));
				
				log.info("추가정보를 입력하세요!");
				
				request.getSession().getAttribute(SessionConst.KAKAO_EMAIL);
				
				return "user/member/kakao-join";
				
			}
		} catch (Exception e) {

			log.error("서버오류 소셜로그인 실패!");
		}

		return "redirect:/";
	}


	@PostMapping("/kakao-join") public String kakaoJoin(Member member) {
		log.debug("member {} ", member);
		
		boolean joinResult = memberService.kakaojoin(member);

		if (joinResult) {

			log.info("추가정보 입력 성공!");

			return "redirect:/";

		} else {

			log.info("추가정보 입력 실패!");

			return "redirect:/user/member/kakao-join";
		}
	}

//	@GetMapping("/sendMail")
//	public void sendMail() throws Exception {
//		
//        String subject = "메일";
//        String content = "메일 테스트 내용";
//        String from = "보내는이 아이디@도메인주소";
//        String to = "받는이 아이디@도메인주소";		
//        
//        MimeMessage mail = mailSender.createMimeMessage();
//        //true는 멀티파트 메세지를 사용하겠다는 의미
//        MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
//        
//		/* 단순한 텍스트 메세지만 사용시엔 아래의 코드도 사용 가능 */ 
//		/* MimeMessageHelper mailHelper = new MimeMessageHelper(mail,"UTF-8"); */
//        
//        mailHelper.setFrom(from);
//        
//        mailHelper.setTo(to);
//        mailHelper.setSubject(subject);
//        
//        // true는 html을 사용하겠다는 의미 
//        mailHelper.setText(content, true);
//        
//        //단순한 텍스트만 사용하신다면 다음의 코드를 사용 mailHelper.setText(content);
//        
//        mailSender.send(mail);
//        
//        
//	}
	

}



















