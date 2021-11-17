package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/question")
public class AdminQuestionChatController {
	
	private final QuestionChatService questionChatService;
	
	@GetMapping("/service/open")
	public String questionOpen() {
		return "/admin/question-chat/admin-question-chat";
	}
	
	@ResponseBody
	@PostMapping("/service/list")
	public ResponseObject chatList() {
		return questionChatService.getList();
	}

	@ResponseBody
	@PostMapping("/service/{questionChatId}")
	public ResponseObject joinChatRoom(@PathVariable("questionChatId") int questionChatId) {
		return questionChatService.getMessageList(questionChatId);
	}
}
