package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Notice;

public interface NoticeService {

	public List<Notice> noticeList(Paging paging);
	
	public Paging getPaging(int currentPage);
	
}
