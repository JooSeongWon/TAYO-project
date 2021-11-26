package fun.tayo.app.dao;

import java.util.Map;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.UploadFile;

public interface ProfileDao {

	Member selectMemberById(int memberId);

	void update(Member member);

	int selectCntByIdAndPassword(Map<String, Object> params);
	
	int selectCntHasPassword(int memberId);

	void updateProfile(UploadFile uploadFile);

}
