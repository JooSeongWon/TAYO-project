package fun.tayo.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fun.tayo.app.dto.Question;
import fun.tayo.app.service.face.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;
	
	@RequestMapping(value="/question", method=RequestMethod.GET)
	public String questionList(Model model) {
		
		List<Question> list = questionService.questionList();
		model.addAttribute("list", list);
		log.debug("list", list);
		return "user/question/question";
	}
	
	@ResponseBody
	@RequestMapping(value ="/question", method=RequestMethod.POST)
	public List<Question> getQuestionList(){
		
		List<Question> list = questionService.questionList();
		
		return list;
	}
}














