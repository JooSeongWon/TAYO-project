package fun.tayo.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.AdminMemberDao;
import fun.tayo.app.dto.Member;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.AdminMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminMemberServiceImple implements AdminMemberService {
	
	private final AdminMemberDao adminMemberDao;
	
	@Override
	public List<Member> list(Paging paging) {
		
		return adminMemberDao.selectList(paging);
	}
	
	@Override
		public Paging getPaging(Paging paramData) {
			
			int totalCount = adminMemberDao.selectCntAll(paramData);
			
			Paging paging = new Paging(totalCount, paramData.getCurPage());
			paging.setSearch(paramData.getSearch());
		
			return paging;
		}

	@Override
	public void updateGrade(int memberId, char grade) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", memberId);
		params.put("grade", grade);
		
		adminMemberDao.updateGrade(params);
	}

	@Override
	public void updateBan(int memberId, char ban) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", memberId);
		params.put("ban", ban);
		
		adminMemberDao.updateBan(params);
	}
	
	
	
}
