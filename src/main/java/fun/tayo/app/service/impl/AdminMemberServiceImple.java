package fun.tayo.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.AdminMemberDao;
import fun.tayo.app.dto.Member;
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
	public Member getMember(int memberId) {
		
		return adminMemberDao.selectMemberById(memberId);
	}

	@Override
	public List<Member> list(Paging paging, Member member1) {

		return adminMemberDao.selectList(paging, member1);
	}

	@Override
	public void update(Member member) {
		adminMemberDao.updateMember(member);
	}
	
	
//	
//	@Override
//	public List<Member> getList(Paging paging) {
//		return adminMemberDao.selectAdminMember(paging);
//	}
//	
//	@Override
//	public Paging getPaging(int currentPage) {
//		return new Paging(adminMemberDao.selectCntAll(), currentPage);
//	}
	
}
