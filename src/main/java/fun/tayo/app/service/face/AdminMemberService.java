package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.ResponseData;

public interface AdminMemberService {

	List<Member> list(Paging paging);
	
	Paging getPaging(Paging paramData);

	Member getMember(int memberId);

	List<Member> list(Paging paging, Member member1);

	void updateGrade(int memberId, char grade);
	
	void updateBan(int memberId, char ban);
	

//	List<Member> getList(Paging paging);
//
//	Paging getPaging(int currentPage);


}
