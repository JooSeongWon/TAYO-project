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
	 * @param memberSession - 개설유저
	 * @return
	 */
	int openChat(MemberSession memberSession);
	
	/**
	 * 사용자 페이지 
	 * 채팅 메세지 불러오기
	 * 
	 * @param questionId - 문의번호
	 * @return
	 */
	ResponseObject messageList(int questionChatId);
	
	/**
	 * 관리자 페이지 
	 * 채팅 메세지 불러오기
	 * 
	 * @param questionId - 문의번호
	 * @return
	 */
	ResponseObject adminMessageList(int questionChatId);

	QusetionChat selectChatRoom(int questionChatId);

	int insertMessage(QuestionMessage questionMessage);

	ResponseObject getList();




}
