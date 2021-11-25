package fun.tayo.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String joinProc(@ModelAttribute("member") Member member) throws Exception {

		//이메일 인증까지 포함
		boolean joinResult = memberService.join(member);


		if (joinResult) {


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

	// 이메일 인증 확인하면 나오는 경로
	@RequestMapping(value = "/emailConfirm", method = RequestMethod.GET)
	public String emailConfirm(String email, Model model) throws Exception {

		// authstatus 권한 상태 1로 변경
		memberService.updateAuthstatus(email);

		// jsp에서 쓰기위해 model에 담음
		model.addAttribute("email", email);

		return "user/member/emailConfirm";
	}



	// 비밀번호 찾기
	@RequestMapping(value = "/findpw", method = RequestMethod.GET)
	public void findPwGET() throws Exception{
	}

	@RequestMapping(value = "/findpw", method = RequestMethod.POST)
	public void findPwPOST(@ModelAttribute Member member, HttpServletResponse response) throws Exception{
		
		memberService.findPw(response, member);
		
	}

}















