package fun.tayo.app.dao;

import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberJoinParam;

public interface MemberDao {


	Member selectByEmail(String email);


	int selectCntByEmail(Member member);

	//이메일 인증 포함
	void insert(Member member);
	

	Integer selectProfileById(Integer memberId);

	// 이메일 인증
	// 디비에 authkey저장
	void createAuthkey(String email, String authkey);

	// 이메일 인증 후 authstatus 1로  변경
	void updateAuthstatus(String email);

	//임시비밀번호 발급
//	void updateTemporaryPw(String email, String temporaryPw);

	
	//이메일 체크
//	String emailCheck(String email);

	//입력한 이메일로 멤버테이블 조회
	Member memberCheck(String email);

	//변경된 비밀번호 DB저장
	void updatePassword(Member member);


	void kakaoinsert(Member member);

	
}
