package fun.tayo.app.service.face;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.UploadFile;

public interface ProfileService {

	Member info(int memberId);

	ResponseData update(Member member, String target, String value);

//	boolean checkPw(String memberPassword);

}
