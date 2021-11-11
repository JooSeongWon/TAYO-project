package fun.tayo.app.dao;

import java.util.List;

import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;

public interface QuestionChatDao {
	
	/**
	 * 
	 * @param memberSession
	 * @return
	 */
	int cntByMemberId(MemberSession memberSession);
	
	/**
	 * 
	 * @param memberSession
	 */
	void insertChatRoom(MemberSession memberSession);
	
	/**
	 * 
	 * @param memberSession
	 * @return
	 */
	int selectChatRoom(MemberSession memberSession);
	
	/**
	 * 
	 * @param roomId
	 * @return
	 */
	List<QuestionMessage> selectMessage(int roomId);



}
