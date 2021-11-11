package fun.tayo.app.service.face;

import javax.servlet.http.HttpSession;

import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.ResponseData;

public interface MemberService {

	ResponseData login(MemberLoginParam memberLoginParam, HttpSession session);

	void join(MemberLoginParam login);

}
