package fun.tayo.app.service.face;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.UploadFile;

public interface ProfileService {

	Member info(int memberId);

	void update(MemberSession member, UploadFile file);

}
