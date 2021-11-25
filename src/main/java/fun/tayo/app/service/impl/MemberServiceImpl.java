package fun.tayo.app.service.impl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
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
	private final JavaMailSender mailSender;
	
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
	@Transactional
	@Override
	public boolean join(Member member) throws Exception {
				
		//중복 ID 확인
		if( memberDao.selectCntByEmail(member) > 0 ) {
			log.debug("중복된 이메일 입니다!!");
			
			
			//로그인 실패
			return false;
		}
		
		//회원가입(삽입)
		memberDao.insert(member);
		
		//이메일 인증
		// 임의의 authkey 생성
		String authkey = new TempKey().getKey(10, false);

		// 인증키 DB에 저장
	    memberDao.createAuthkey(member.getEmail(), authkey);
	    
	    // 메일 발송
	    MailHandler sendMail = new MailHandler(mailSender);

	    sendMail.setSubject("[Tayo 회원가입 서비스 이메일 인증 입니다.]");
	    sendMail.setText(new StringBuffer().append("<h1>Tayo 가입 메일인증 입니다</h1>")
	        .append("<a href=https://localhost:8443/emailConfirm?email=")
	        .append(member.getEmail()).append("&key=").append(authkey)
	        .append("' target='_blenk'>가입 완료를 위해 이메일 이곳을 눌러주세요</a>").toString());
	    sendMail.setFrom("MetarBusTayo@gmail.com", "Tayo");
	    sendMail.setTo(member.getEmail());
	    sendMail.send();
		member.setAuthkey(authkey);
		
		return true;
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
	
	
	//이메일 인증 시 authstatus값 1로 변경
	@Override
	public void updateAuthstatus(String email) throws Exception {
		
		memberDao.updateAuthstatus(email);
	}
	
	//비밀번호 찾기
	@Override
	public void findPw(HttpServletResponse response, Member member) {
		
		
		
	}
	
	
}













