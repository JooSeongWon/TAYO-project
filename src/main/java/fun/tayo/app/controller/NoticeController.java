package fun.tayo.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Notice;
import fun.tayo.app.service.face.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NoticeController {
	
	private final NoticeService noticeService;
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	
	@RequestMapping(value ="/notice", method=RequestMethod.GET)
	public String noticeList(Model model) {
	
		Paging paging = noticeService.getPaging(1);
		List<Notice> list = noticeService.noticeList(paging);
		model.addAttribute("list", list);
		model.addAttribute("paging", paging);
		log.debug("paging{}",paging);
		return "user/notice/notice";	
	}
	
//	공지사항 리스트
	@ResponseBody
	@RequestMapping(value ="/notice", method=RequestMethod.POST)
	public List<Notice> getNoticeList(int curPage){
		
		Paging paging = noticeService.getPaging(curPage);
		List<Notice> list = noticeService.noticeList(paging);
//		for(Notice n : list) {
//			logger.debug("{}", n);
//		}
//		log.debug("list{}",list);
//		log.debug("curPage{}",curPage);
		return list;
	}
	
	@RequestMapping(value="/admin/notice", method = RequestMethod.GET)
	public String adminNoticeList(Model model) {
		List<Notice> list = noticeService.noticeList();
		model.addAttribute("list", list);
		return "admin/notice/notice";
	}
	
	@RequestMapping(value="/admin/notice/write", method = RequestMethod.GET)
	public void noticeWrite() {}
	
	@RequestMapping(value="/admin/notice/write", method = RequestMethod.POST)
	public String noticeWriteProc(Notice notice) {
		
		noticeService.write(notice);
		return "redirect:/admin/notice";
	}
	
	@RequestMapping(value="/admin/notice/update/{noticeId}", method = RequestMethod.GET)
	public String noticeUpdate(@PathVariable int noticeId, Model model) {
		
		Notice notice = noticeService.getNotice(noticeId);
		model.addAttribute("notice", notice);
		
		return "/admin/notice/update";
	}
	
	@RequestMapping(value="/admin/notice/update/{noticeId}", method = RequestMethod.POST)
	public String noticeUpdateProc(Notice notice, @PathVariable int noticeId) {
		
		notice.setId(noticeId);
		noticeService.update(notice);
		
		return "redirect:/admin/notice";
	}
	
	@RequestMapping(value="/admin/notice/delete/{noticeId}", method = RequestMethod.GET)
	public String noticeDelete(Notice notice, @PathVariable int noticeId) {
		
		notice.setId(noticeId);
		noticeService.delete(notice);
		
		return "redirect:/admin/notice";
	}
}












