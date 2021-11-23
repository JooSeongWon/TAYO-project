package fun.tayo.app.service.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.common.util.MailHandler;
import fun.tayo.app.common.util.TempKey;
import fun.tayo.app.dao.MemberDao;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberLoginParam;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	//서블릿 컨텍스트 객체
	@Autowired ServletContext context;
	
	private final MemberDao memberDao;
	
	private JavaMailSender mailSender;
	
	public ResponseData login(MemberLoginParam param, HttpSession session) {
		
        if(!StringUtils.hasText(param.getEmail()) || !StringUtils.hasText(param.getPassword())){
        	
            return new ResponseData(false, "빈칸을 채워주세요");
        }

        final Member member = getMemberByEmail(param.getEmail());

        if(member == null || !member.getPassword().equals(param.getPassword())) {
        	
            return new ResponseData(false, "일치하는 회원정보가 없습니다.");
        }

        //로그인 완료
        setLogin(member, session);
        log.debug("profile {}", member.getProfile());
        
        return new ResponseData(true, "로그인 성공") ;		
		
        
	}

	public void setLogin(Member member, HttpSession session) {
		
		session.setAttribute(SessionConst.LOGIN_MEMBER, new MemberSession(member));
		
	}

	public Member getMemberByEmail(String email) {
		
		return memberDao.selectByEmail(email);
	}


	/*
	 * @Override public boolean join(Member member) {
	 * 
	 * //중복 ID 확인 if( memberDao.selectCntByEmail(member) > 0 ) { return false; }
	 * 
	 * //회원가입(삽입) memberDao.insert(member);
	 * 
	 * //회원가입 결과 확인 if( memberDao.selectCntByEmail(member) > 0 ) { return true; }
	 * 
	 * return false; }
	 */
	@Override
	public boolean join(Member member) {
				
		//중복 ID 확인
		if( memberDao.selectCntByEmail(member) > 0 ) {
			log.debug("중복된 이메일 입니다!!");
			
			
			//로그인 실패
			return false;
		}
		
		//회원가입(삽입)
		memberDao.insert(member);
		
		//회원가입 결과 확인
		if( memberDao.selectCntByEmail(member) > 0 ) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean kakaojoin(Member member) {
		
		//중복 ID 확인
		if( memberDao.selectCntByEmail(member) > 0 ) {
			log.debug("중복된 kakao이메일 입니다!!");
			
			//로그인 실패
			return false;
		}
		
		//회원가입(삽입)
		memberDao.kakaoinsert(member);
		
		//회원가입 결과 확인
		if( memberDao.selectCntByEmail(member) > 0 ) {
			
			log.debug("회원가입 결과 확인 성공!");
			return true;
			
		}
		
		//회원가입 실패
		return false;
	}
	
	public Integer getProfile(Integer memberId){
		return memberDao.selectProfileById(memberId);
		}
	
	@Override
	@Transactional
	public void create(Member member) {
		
//		memberDao.create(member);
//
//		// 임의의 authkey 생성
//		String authkey = new TempKey().getKey(50, false);
//
////		member.setAuthkey(authkey);
//		memberDao.updateAuthkey(member);
//
//		// mail 작성 관련 
//		MailHandler sendMail = new MailHandler(mailSender);
//
//		sendMail.setSubject("[Hoon's Board v2.0] 회원가입 이메일 인증");
//		sendMail.setText(new StringBuffer().append("<h1>[이메일 인증]</h1>")
//				.append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
////				.append("<a href='http://localhost:8080/user/joinConfirm?uid=")
////				.append(member.getUid())                                                 수정하기
//				.append("&email=")
//				.append(member.getEmail())
//				.append("&authkey=")
//				.append(authkey)
//				.append("' target='_blenk'>이메일 인증 확인</a>")
//				.toString());
//		sendMail.setFrom("관리자 ", "관리자명");
//		sendMail.setTo(member.getEmail());
//		sendMail.send();
//		
	}
	
}
