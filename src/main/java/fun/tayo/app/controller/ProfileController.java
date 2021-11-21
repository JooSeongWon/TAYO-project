package fun.tayo.app.controller;

import javax.servlet.http.HttpSession;

import fun.tayo.app.dto.ResponseData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        MemberSession member = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
        log.debug("session : {}", session);
        log.debug("/profile : {}", member.getId());

        //로그인된 사용자의 정보 조회
        Member memberInfo = profileService.info(member.getId());
        log.debug("조회결과 info {}", memberInfo);

        //사용자의 정보, 모델값 전달
        model.addAttribute("member", memberInfo);
        System.out.println("========" + memberInfo);

        return "user/member/profile";

    }

    @ResponseBody
    @PostMapping("/profile")
    public ResponseData updateProfile(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession, @RequestParam String target, @RequestParam String value) {

        log.debug("target {} value {}", target, value);

        final Member member = profileService.info(memberSession.getId());
        return profileService.update(member, target, value);
    }

}
