package fun.tayo.app.dao;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;

public interface ProfileDao {

	Member selectMemberById(int memberId);

	void update(MemberSession member);

}
