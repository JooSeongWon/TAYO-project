package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.dto.Question;

public interface QuestionDao {

	public List<Question> selectQuestion();

	public void insertQuestion(Question question);

	public void updateQuestion(Question question);

	public Question selectQuestionById(int questionId);

	public void deleteQuestion(Question question);

}
