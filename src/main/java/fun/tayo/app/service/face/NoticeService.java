package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Notice;

public interface NoticeService {

	public List<Notice> noticeList(Paging paging);
	
	/**
	 * 공지사항 조회 -> 현재 페이지 확인 
	 * @param currentPage
	 * @return paging(공지사항 수, currentPage)
	 */
	public Paging getPaging(int currentPage);

	public List<Notice> noticeList();

	public void write(Notice notice);

	public Notice getNotice(int noticeId);

	public void update(Notice notice);

	public void delete(Notice notice);
	
}
