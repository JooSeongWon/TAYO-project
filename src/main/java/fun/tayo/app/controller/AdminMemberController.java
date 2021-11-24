package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;
import fun.tayo.app.service.face.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Controller
public class AdminMemberController {
	
	private final AdminMemberService adminMemberService;
	
	@RequestMapping(value="/admin/members")
	public String list(Paging paramData, Model model) {
		
		//페이징 계산
		Paging paging = adminMemberService.getPaging( paramData );
		
		//멤버 목록 조회
		List<Member> list = adminMemberService.list(paging);
		
		model.addAttribute("paging", paging);
		model.addAttribute("list", list);
		
		return "admin/member/list";
	}
	
	@ResponseBody
	@RequestMapping(value="/admin/members/{memberId}", method = RequestMethod.POST)
	public String memberList( @PathVariable int memberId, Character grade, Character ban ) {
	
		if(grade != null) {
			adminMemberService.updateGrade(memberId, grade);
		} else {
			adminMemberService.updateBan(memberId, ban);
		}
	
		return "ok";
	}
	
	

}
