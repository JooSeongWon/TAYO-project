package fun.tayo.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import fun.tayo.app.dao.NoticeDao;
import fun.tayo.app.dao.QuestionDao;
import fun.tayo.app.dto.Question;
import fun.tayo.app.service.face.QuestionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService{

	private final QuestionDao questionDao;
	
	@Override
	public List<Question> questionList() {

		return questionDao.selectQuestion();
	}

}
