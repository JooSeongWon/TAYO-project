package fun.tayo.app.dao;

import fun.tayo.app.dto.Member;

public interface MemberDao {


	Member selectByEmail(String email);


	int selectCntByEmail(Member member);


	void insert(Member member);


}
