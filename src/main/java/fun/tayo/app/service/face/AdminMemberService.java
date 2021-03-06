package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;

public interface AdminMemberService {

	List<Member> list(Paging paging);
	
	Paging getPaging(Paging paramData);

	void updateGrade(int memberId, char grade);
	
	void updateBan(int memberId, char ban);
	

}
