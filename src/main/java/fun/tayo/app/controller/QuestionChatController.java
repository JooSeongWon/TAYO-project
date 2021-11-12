package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/question")
public class QuestionChatController {
	
	private final QuestionChatService questionChatService;
	
	@GetMapping(value = "/sample")
	public String sample() {
		
		return "user/question-chat/sample-script";
	}
	
	@ResponseBody
	@PostMapping(value = "/service/open")
	public int openChat(
			@SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
			) {
		
		try {
			return questionChatService.openChat(memberSession);
		} catch (Exception e) {
			log.error("questionChatService.openChat error ", e);
			return 0;
		}
	}
	
	
	
	@GetMapping(value = "/service/{questionId}")
	public String joinChatRoom(
			@PathVariable("questionId") int questionId,
			@SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
			QusetionChat qusetionchat,
			Model model			
			) {
		
			log.debug("info  {}" , questionId);
			
			//채팅방 메세지 받기
			List<QuestionMessage> mList = questionChatService.messageList(questionId);
			log.debug("{}" , mList);

//		qusetionchat.setId(questionId);
//		qusetionchat.setMemberId(memberSession.getId());
//		String chatName = memberSession.toString();
//		
		model.addAttribute("mList", mList);
		
		return "user/question-chat/question-chat";
	}
	
	
}
