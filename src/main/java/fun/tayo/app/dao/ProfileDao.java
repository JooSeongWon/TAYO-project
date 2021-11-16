package fun.tayo.app.dao;

import fun.tayo.app.dto.Member;

public interface ProfileDao {

	Member selectMemberById(int memberId);

}
