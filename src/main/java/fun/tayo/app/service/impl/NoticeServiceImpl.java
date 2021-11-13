package fun.tayo.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.NoticeDao;
import fun.tayo.app.dto.Notice;
import fun.tayo.app.service.face.NoticeService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
	
	private final NoticeDao noticeDao;

	@Override
	public List<Notice> noticeList(Paging paging) {
		return noticeDao.selectNotice(paging);
	}

	
	@Override
	public Paging getPaging(int currentPage) {
		return new Paging(noticeDao.selectCntAll(), currentPage);
	}


}
