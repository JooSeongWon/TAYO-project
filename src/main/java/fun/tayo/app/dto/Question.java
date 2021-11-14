package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Question {

	private int id;
	private String questionContent;
	private String answerContent;
}
