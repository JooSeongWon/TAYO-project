package fun.tayo.app.service.face;

import javax.servlet.http.HttpSession;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.ResponseData;

public interface MemberService {

	ResponseData login(MemberLoginParam memberLoginParam, HttpSession session);

	boolean join(Member member);

	Member getMemberByEmail(String email);

	void setLogin(Member member, HttpSession session);


}
