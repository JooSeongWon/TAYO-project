package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.dto.Question;

public interface QuestionDao {

	public List<Question> selectQuestion();

}
