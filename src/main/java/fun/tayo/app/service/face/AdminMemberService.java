package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;

public interface AdminMemberService {

	List<Member> list(Paging paging);
	
	Paging getPaging(Paging paramData);

	Member getMember(int memberId);

	List<Member> list(Paging paging, Member member1);

	void update(Member member1);


//	List<Member> getList(Paging paging);
//
//	Paging getPaging(int currentPage);


}
