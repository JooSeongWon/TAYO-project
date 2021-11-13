package fun.tayo.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notice {

	private int id;
	private Date writeDate; 
	private String content;
	
}
