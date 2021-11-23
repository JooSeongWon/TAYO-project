package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	@RequestMapping(value="/admin/members/{memberId}", method = RequestMethod.POST)
	public String memberList(Paging paramData, Model model, @PathVariable int memberId, Member member) {
		
		//페이징 계산
		Paging paging = adminMemberService.getPaging( paramData );
		
		Member member1 = adminMemberService.getMember(memberId);
		
		//멤버 목록 조회
		List<Member> list = adminMemberService.list(paging, member1);
	
		member.setId(memberId);
		adminMemberService.update(member);
		
		model.addAttribute("paging", paging);
		model.addAttribute("list", list);
		
//		return "admin/member/list";
		return "forward:/admin/member/list";
	}
	
	
//	@RequestMapping(value="/admin/members/update")
//	public String memberUpdate(@PathVariable int memberId, Model model) {
//	
//		
//	}
	
	
	
//	@RequestMapping(method = RequestMethod.POST)
//	public List<Member> getMemberList(Paging paramData, Model model) {
////		log.debug("/admin/list");
//		
//		//페이징 계산
//		Paging paging = adminMemberService.getPaging( paramData );
////		log.debug("{}", paging);
//		
//		//멤버 목록 조회
//		List<Member> list = adminMemberService.list(paging);
////			for(Member m : list) {
////			log.debug("{}", m);
////		}
//		
//		return list;
//	}
	
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
