package fun.tayo.app.dao;

import java.util.List;
import java.util.Map;

import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;

public interface QuestionChatDao {
	
	/**
	 * 해당유저의 채팅방 수
	 * 
	 * @param memberSession
	 * @return
	 */
	int cntByMemberId(MemberSession memberSession);
	
	/**
	 * 채팅방 개설
	 * 
	 * @param memberSession
	 */
	void insertChatRoom(MemberSession memberSession);
	
	int selectChatRoom(MemberSession memberSession);
	
	QusetionChat selectChatRoomByQuestionId(int questionChatId);

	int insertChatMessage(QuestionMessage questionMessage);

	List<Map<Integer, Object>> selectChatList();

	List<Map<Integer, Object>> selectMessageByQId(int questionChatId);
	
	
	
	List<QuestionMessage> selectUnreadMessage(int questionChatId);

	void updateRead(int id);







}
