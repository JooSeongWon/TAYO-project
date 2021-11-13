package fun.tayo.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class QuestionMessage {
	private int id;
	private String content;
	private Date sendDate;
	private String read;
	private int memberId;
	private int questionChatId;
	private String name;
}
