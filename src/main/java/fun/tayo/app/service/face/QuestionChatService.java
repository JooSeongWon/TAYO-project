package fun.tayo.app.service.face;

import java.util.List;

import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;

public interface QuestionChatService {
	
	/**
	 * 채팅방 개설
	 * 
	 * @param memberSession
	 * @return
	 */
	int openChat(MemberSession memberSession);
	
	/**
	 * 메세지 불러오기
	 * @param questionId
	 * @return
	 */
	ResponseObject messageList(int questionId);
	
	QusetionChat selectChatRoom(int questionChatId);

	int insertMessage(QuestionMessage questionMessage);

	ResponseObject getList();



}
