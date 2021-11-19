package fun.tayo.app.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fun.tayo.app.dao.ProfileDao;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.UploadFile;
import fun.tayo.app.service.face.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
	
	private final ProfileDao profileDao;
	
	@Override
	public Member info(int memberId) {
		log.debug("info() memberId : {}", memberId);
		
		return profileDao.selectMemberById(memberId);
	}
	
	@Override
	@Transactional
	public void update(MemberSession member, UploadFile file) {
		if("".equals(member.getName())) {
			member.setName("(이름없음)");
		}
		profileDao.update(member);
	}
	
}
