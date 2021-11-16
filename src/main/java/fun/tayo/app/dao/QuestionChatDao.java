package fun.tayo.app.dao;

import java.util.List;
import java.util.Map;

import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;

public interface QuestionChatDao {
	
	int cntByMemberId(MemberSession memberSession);
	
	void insertChatRoom(MemberSession memberSession);
	
	int selectChatRoom(MemberSession memberSession);
	
	List<QuestionMessage> selectMessage(int questionChatId);
	
	QusetionChat selectChatRoomByQuestionId(int questionChatId);

	int insertChatMessage(QuestionMessage questionMessage);

	List<Map<Integer, Object>> selectChatList();

	List<Map<Integer, Object>> selectAdminMessage(int questionChatId);







}
