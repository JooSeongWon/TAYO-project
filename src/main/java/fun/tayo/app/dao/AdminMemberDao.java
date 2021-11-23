package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;

public interface AdminMemberDao {

	List<Member> selectList(Paging paging);

	int selectCntAll(Paging paramData);

	Member selectMemberById(int memberId);

	List<Member> selectList(Paging paging, Member member1);

	void updateMember(Member member);


//	List<Member> selectAdminMember(Paging paging);
//
//	int selectCntAll();

}
