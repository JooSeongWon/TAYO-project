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
	 * 채팅 메세지 부르기
	 * 
	 * @param questionId - 문의번호
	 * @return
	 */
	ResponseObject getMessageList(int questionChatId);
	
	/**
	 * 채팅방 찾기
	 * 
	 * @param questionChatId - 채팅방 번호
	 * @return
	 */
	QusetionChat selectChatRoom(int questionChatId);
	
	/**
	 * 메시지 저장
	 * 
	 * @param questionMessage - 전달받은 메세지
	 * @return
	 */
	int insertMessage(QuestionMessage questionMessage);
	
	/**
	 * 채팅 리스트 얻기
	 * 
	 * @return
	 */
	ResponseObject getList();




}
