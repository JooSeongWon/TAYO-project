package fun.tayo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		
		return "/admin/question-chat/question-chat";
	}
	
}
