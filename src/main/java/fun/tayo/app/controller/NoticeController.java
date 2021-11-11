package fun.tayo.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

	
	@RequestMapping(value ="/notice/notice", method=RequestMethod.GET)
	public String noticeList(Model model) {
		List<Notice> list = noticeService.noticeList();
		model.addAttribute("list", list);
		
//		for(Notice n : list) {
//		logger.debug("{}", n);
//		}
		return "user/notice/notice";	
	}
	
//	공지사항 리스트
	@ResponseBody
	@RequestMapping(value ="/notice/notice", method=RequestMethod.POST)
	public List<Notice> getNoticeList(){
		
		List<Notice> list = noticeService.noticeList();
//		for(Notice n : list) {
//			logger.debug("{}", n);
//		}
		log.debug("list{}",list);
		return list;
	}
	
}
