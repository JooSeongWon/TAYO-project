package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.dto.Question;

public interface QuestionService {

	public List<Question> questionList();

	public void write(Question question);

	public void update(Question question);

	public Question getQuestion(int questionId);

}
