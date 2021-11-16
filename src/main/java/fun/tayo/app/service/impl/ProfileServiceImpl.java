package fun.tayo.app.service.impl;

import org.springframework.stereotype.Service;

import fun.tayo.app.dao.ProfileDao;
import fun.tayo.app.dto.Member;
import fun.tayo.app.service.face.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService {
	
	private final ProfileDao profileDao;
	
	@Override
	public Member info(String memberId) {
		log.debug("info() memberId : {}", memberId);
		
		return profileDao.selectMemberById(memberId);
	}
	
}
