package fun.tayo.app.websocket;

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
	
	private final QuestionChatService questionChatService;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("참가자 입장햇어요 ㅠㅠㅠ제발입장시켜줏요");
		//Object memberSession = session.getAttributes().get(SessionConst.LOGIN_MEMBER);
		//isAdmin();
		
		//log.debug((String) memberSession);
		log.debug(session.getAttributes().get(SessionConst.LOGIN_MEMBER).toString());
		
		session.getAttributes().get(SessionConst.LOGIN_MEMBER).toString();
		super.afterConnectionEstablished(session);
	}
	
	
	
	
}
