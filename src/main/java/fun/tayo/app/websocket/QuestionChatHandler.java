package fun.tayo.app.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class QuestionChatHandler extends TextWebSocketHandler {
	
	private final Map<String, WebSocketSession> sessionMap = new HashMap<>();
	private final List<Map<String, Object>> sessionList = new ArrayList<Map<String, Object>>();
	
	private final QuestionChatService questionChatService;
	
	// 클라이언트가 서버로 연결 처리
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("참가자 입장햇어요 ㅠㅠㅠ제발입장시켜줏요");
		
		MemberSession memberSession = (MemberSession) session.getAttributes().get(SessionConst.LOGIN_MEMBER);
		
		String username = memberSession.toString();
		int userId = memberSession.getId();
		
		
		boolean admin = memberSession.isAdmin();
		if(admin) {
			
			
		} 
		
	
		
		
		log.debug(session.getAttributes().get(SessionConst.LOGIN_MEMBER).toString());
		
		session.getAttributes().get(SessionConst.LOGIN_MEMBER).toString();
		super.afterConnectionEstablished(session);
	}
	
	
	
	
}
