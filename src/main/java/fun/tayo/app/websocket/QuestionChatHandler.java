package fun.tayo.app.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.QuestionMessage;
import fun.tayo.app.dto.QusetionChat;
import fun.tayo.app.service.face.MemberService;
import fun.tayo.app.service.face.QuestionChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class QuestionChatHandler extends TextWebSocketHandler {
	
	
    // 채팅방 목록 <방 번호, ArrayList<session> >이 들어간다.
    private Map<Integer, ArrayList<WebSocketSession>> RoomList = new ConcurrentHashMap<Integer, ArrayList<WebSocketSession>>();
    // session, 방 번호가 들어간다.
    private Map<WebSocketSession, Integer> sessionList = new ConcurrentHashMap<WebSocketSession, Integer>();
  
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final QuestionChatService questionChatService;
	private final MemberService memberservice;
	
	// 클라이언트가 서버로 연결 처리
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("참가자 입장햇어요 ㅠㅠㅠ제발입장시켜줏요");
		log.debug(session.getAttributes().get(SessionConst.LOGIN_MEMBER).toString());
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.debug("close!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if(sessionList.get(session) != null) {
            RoomList.get(sessionList.get(session)).remove(session);
            sessionList.remove(session);
        }
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		//전달 받은 메세지
		String msg = message.getPayload();
		log.debug("{}", msg);
		
		//Json -> Java
		QuestionMessage questionMessage = objectMapper.readValue(msg, QuestionMessage.class);
		
		MemberSession memberSession = (MemberSession) session.getAttributes().get(SessionConst.LOGIN_MEMBER);
		boolean admin = memberSession.isAdmin();
		String name = memberSession.getName();
		
    	//관리자 확인
		String GRADE = "N";
		if(admin) {
			GRADE = "A";
			questionMessage.setName("상담사");
			questionMessage.setRead("Y");
		} else {
    		questionMessage.setName(name);
    		questionMessage.setRead("N");
    	}
		
		Integer FILE_ID = memberservice.getProfile(questionMessage.getMemberId());
		
				
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("GRADE", GRADE);
		map.put("FILE", FILE_ID);
		
		Map<Object, Object> map2 = new HashMap<Object, Object>();
		
		map2.put("msg", questionMessage);
		map2.put("admin", map);
		
		//Java -> Json 
		String jsonMsg = objectMapper.writeValueAsString(map2);
		
		log.debug("jsonMsg {}", jsonMsg);
		
		
		// 받은 메세지에 담긴 채팅방번호로 해당 채팅방 찾기 --필요없게됐다
//		QusetionChat chatRoomId = questionChatService.selectChatRoom(questionMessage.getQuestionChatId());
		int chatRoomId = 0;
		
		if(questionMessage.getQuestionChatId()!= 0) {
			chatRoomId = questionMessage.getQuestionChatId();
		}
		
		//채팅방 오픈
		if(RoomList.get(chatRoomId)==null && questionMessage.getContent().equals("CHAT-OPEN")) {
            
			// 채팅방에 들어갈 sessionMember
            ArrayList<WebSocketSession> sessionMember = new ArrayList<>();
           
            sessionMember.add(session);
            
            // sessionList에 추가
            sessionList.put(session, chatRoomId);
            
            // RoomList에 추가
            RoomList.put(chatRoomId, sessionMember);
            
            System.out.println("채팅방추가");
		}
        // 채팅방이 존재 할 때
        else if(RoomList.get(chatRoomId) != null && questionMessage.getContent().equals("CHAT-OPEN")) {
            
            // RoomList에서 해당 방번호를 가진 방이 있는지 확인.
        	RoomList.get(chatRoomId).add(session);
        	
            // sessionList에 추가
        	sessionList.put(session, chatRoomId);
            
            System.out.println("생성된 채팅방으로 입장");
        }
		
        // 채팅 메세지 입력 시
        else if(RoomList.get(chatRoomId) != null && !questionMessage.getContent().equals("")) {
        	
    		int userId = memberSession.getId();
    		
    		if(questionMessage.getContent().equals("CHAT-OPEN")) {
    			return;
    		}
    		
        	questionMessage.setId(userId);
        	
            // 메세지에 보낼 내용 담기
            TextMessage textMessage = new TextMessage(jsonMsg);
            
            // 현재 session 수
            int sessionCount = 0;
 
            // 해당 채팅방의 session에 뿌려준다.
            for(WebSocketSession sess : RoomList.get(chatRoomId) ) {
                sess.sendMessage(textMessage);
                sessionCount++;
                System.out.println("뿌렷어요");
            }
            
            // 읽음확인
            if(sessionCount > 1) {
            	questionMessage.setRead("Y");
            }
            
            // 리스트에도 메세지를 뿌려준다
            if(RoomList.get(0)!=null) {
            	for(WebSocketSession sess : RoomList.get(0)) {
            		sess.sendMessage(textMessage);
            		System.out.println("리스트에뿌렷어요");
            	}
            }
            

            // DB에 저장한다.
            int result = questionChatService.insertMessage(questionMessage);
            
            if(result == 1) {
                System.out.println("메세지 전송 성공");
            }else {
                System.out.println("메세지 전송 실패!!!");
            }
            
        }
    	
	}
	
	
}
