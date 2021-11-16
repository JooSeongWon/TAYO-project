package fun.tayo.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fun.tayo.app.dao.QuestionChatDao;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionChatServiceImpl implements QuestionChatService{
	private final QuestionChatDao questionChatDao;
	
	@Override
	public int openChat(MemberSession memberSession) {
		
		//채팅방 개설 확인
		int result = questionChatDao.cntByMemberId(memberSession);
		
		if( result < 1 ) { //개설된 채팅방이 없을 경우
			questionChatDao.insertChatRoom(memberSession);
		}
		
		int questionId = questionChatDao.selectChatRoom(memberSession);
		
		return questionId;
	}
	
	@Override
	public ResponseObject messageList(int questionChatId) {
		//지난 메세지 불러오기
		List<QuestionMessage> mList = questionChatDao.selectMessage(questionChatId);
		ResponseObject responseObject = new ResponseObject(true, mList);
		
		return responseObject;
	}
	
	//여기예요 여기
	@Override
	public ResponseObject adminMessageList(int questionChatId) {
		List<Map<Integer, Object>> mList = questionChatDao.selectAdminMessage(questionChatId);
		ResponseObject responseObject = new ResponseObject(true, mList);
		return responseObject;
	}
	
	@Override
	public QusetionChat selectChatRoom(int questionChatId) {
		return questionChatDao.selectChatRoomByQuestionId(questionChatId);
	}
	
	@Override
	public int insertMessage(QuestionMessage questionMessage) {
		return questionChatDao.insertChatMessage(questionMessage);
	}
	
	@Override
	public ResponseObject getList() {
		//채팅 리스트 불러오기
		List<Map<Integer, Object>> chatList = questionChatDao.selectChatList();
		ResponseObject responseObject = new ResponseObject(true, chatList);
		
		return responseObject;

	}

}
