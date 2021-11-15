package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;
import fun.tayo.app.service.face.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping(value="/admin/member")
public class AdminMemberController {
	
	private final AdminMemberService adminMemberService;
	
	@RequestMapping(value="/list")
	public void list(Paging paramData, Model model) {
		log.debug("/admin/list");
		
		//페이징 계산
		Paging paging = adminMemberService.getPaging( paramData );
		log.debug("{}", paging);
		
		//멤버 목록 조회
		List<Member> list = adminMemberService.list(paging);
		for(Member m : list) {
			log.debug("{}", m);
		}
		
		model.addAttribute("paging", paging);
		model.addAttribute("list", list);
	}
	
//	private final AdminMemberService adminMemberService;
	
//	@GetMapping("/admin")
//	public String memberList(Model model) {
//		
//		Paging paging = adminMemberService.getPaging(1);
//		
//		List<Member> memberlist = adminMemberService.getList(paging);
//		model.addAttribute("list", memberlist);
//		
//		return "admin/member/member";
//		
//	}

}
