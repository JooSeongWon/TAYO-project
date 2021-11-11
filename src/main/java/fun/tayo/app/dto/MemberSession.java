package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
public class MemberSession {

	private int id;
	private String email;
	private Integer profile;
	private String name;
	private char grade;
	
	public MemberSession() {}
	
	public MemberSession(Member member) {
		this.id = member.getId();
		this.email = member.getEmail();
		this.name = member.getName();
		this.grade = member.getGrade();
		this.profile = member.getProfile();
	}
	
	public boolean isAdmin() {
		return grade == 'A' || grade == 'S';
	}
	
	@Override
	public String toString() {
		return String.format("%s #%04d", name, id);
	}
	
	public boolean hasProfile() { return this.profile != null;}
}
