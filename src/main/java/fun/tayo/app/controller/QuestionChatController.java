package fun.tayo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class QuestionChatController {
	
	private final QuestionChatService questionChatService;
	
	@RequestMapping(value = "/question/chat")
	public String Qmain() {
		log.debug("/question/chat");
		return "user/question-chat/question-chat";
	} 
}
