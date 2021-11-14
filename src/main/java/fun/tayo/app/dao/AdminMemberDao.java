package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Member;

public interface AdminMemberDao {

	List<Member> selectList(Paging paging);

	int selectCntAll();


//	List<Member> selectAdminMember(Paging paging);
//
//	int selectCntAll();

}
