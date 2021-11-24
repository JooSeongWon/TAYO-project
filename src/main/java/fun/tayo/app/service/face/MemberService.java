package fun.tayo.app.service.face;

import javax.servlet.http.HttpSession;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.ResponseData;

public interface MemberService {

	ResponseData login(MemberLoginParam memberLoginParam, HttpSession session);

	//이메일 인증 포함 회원가입
	boolean join(Member member) throws Exception;

	Member getMemberByEmail(String email);

	void setLogin(Member member, HttpSession session);

	boolean kakaojoin(Member member);
	
	Integer getProfile(Integer memberId);

	//이메일 인증

	// authstatus 1로 변경
	void updateAuthstatus(String email) throws Exception;


}
