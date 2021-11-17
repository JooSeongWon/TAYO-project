package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Notice;

public interface NoticeDao {

	public List<Notice> selectNotice(Paging paging);

	public int selectCntAll();

	public List<Notice> selectNotice();

	public List<Notice> selectNoticeList();

	public void insertNotice(Notice notice);

	public Notice selectNoticeById(int noticeId);

	public void updateNotice(Notice notice);

	public void deleteNotice(Notice notice);

}
