package fun.tayo.app.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Member {

	private int id;
	private String phone;
	private String name;
	private String email;
	private String password;
	private Date createDate;
	private char grade;
	private char ban;
	private Integer profile;
	private String savedName;
	
}
