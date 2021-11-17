package fun.tayo.app.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dao.WorkSpaceDao;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.WorkSpace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("FieldCanBeLocal")
public class WorkSpaceHandler extends TextWebSocketHandler {

    private final String RELAY_CHAT = "chat";
    private final String RELAY_MOVE = "move";
    private final String RELAY_ENTER = "enter";
    private final String RELAY_JOIN = "join";
    private final String RELAY_LEAVE = "leave";
    private final String SYSTEM_DUPLICATE = "duplicate";

    private final Map<Integer, WorkSpaceRoom> roomMap = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final WorkSpaceDao workSpaceDao;

    //텍스트 메세지 릴레이
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final Message relayMessage = objectMapper.readValue(message.getPayload(), Message.class);
        final MemberSession memberSession = (MemberSession) session.getAttributes().get(SessionConst.LOGIN_MEMBER);
        final Integer sender = memberSession.getId();


        if (!sender.equals(relayMessage.sender)) {
            log.warn("sender 정보가 일치하지 않음 표시된사람[{}] 실제 보낸사람[{}]", relayMessage.sender, sender);
            return;
        }

        log.debug("프레임 [{}]", message.getPayload());

        switch (relayMessage.type) {
            //입장
            case RELAY_ENTER:
                enterMember(session, relayMessage, memberSession, sender);
                break;
            //채팅, 이동 릴레이
            case RELAY_MOVE:
                final Integer roomId = relayMessage.getRoomId();
                final TeamMember member = roomMap.get(roomId).getMember(sender);

                @SuppressWarnings("unchecked")
                Map<String, Object> messageMap = (Map<String, Object>) relayMessage.getMessage();

                member.user.setX((Integer) messageMap.get("x"));
                member.user.setY((Integer) messageMap.get("y"));
            case RELAY_CHAT:
                relayMessageToAllMemberInSameRoom(relayMessage);
                break;
        }
    }

    //같은방 모두에게 메세지 릴레이
    private void relayMessageToAllMemberInSameRoom(Message relayMessage) {
        try {
            Integer roomId = relayMessage.roomId;
            Integer memberId = relayMessage.sender;
            WorkSpaceRoom room = roomMap.get(roomId);
            final Collection<TeamMember> teamMembers = room.enteredMemberMap.values();

            for (TeamMember teamMember : teamMembers) {
                if (!teamMember.getMemberId().equals(memberId)) {
                    teamMember.sendMessage(relayMessage);
                }
            }
        } catch (Exception e) {
            log.error("프레임 릴레이 오류");
        }
    }

    //유저 입장
    private void enterMember(WebSocketSession session, Message relayMessage, MemberSession memberSession, Integer sender) throws IOException {
        if (!roomMap.containsKey(relayMessage.roomId)) { //방 없으면 생성
            if (!createRoom(relayMessage.roomId)) {
                log.error("가상공간 방 생성 실패");
                return;
            }
            log.debug("가상공간 방 생성 [{} {}]", relayMessage.roomId, roomMap.get(relayMessage.roomId).getName());
        }

        final WorkSpaceRoom room = roomMap.get(relayMessage.roomId);
        // 기존 접속 확인
        if (room.isEntered(sender)) {
            //기존 접속 종료
            final TeamMember member = room.getMember(sender);
            Message message = new Message();
            message.setType(SYSTEM_DUPLICATE);
            member.sendMessage(message);

            final WebSocketSession oldSession = member.session;
            if (session != oldSession) oldSession.close();
        }

        //입장 알림
        User user = new User(memberSession.getId(), memberSession.getName(), memberSession.getProfile());
        relayMessage.message = user;
        relayMessage.setType(RELAY_JOIN);

        final Collection<TeamMember> members = room.enteredMemberMap.values();
        for (TeamMember member : members) {
            member.sendMessage(relayMessage);
        }

        TeamMember teamMember = new TeamMember(session, user);

        //기존 회원정보 전달
        List<User> memberList = new ArrayList<>();

        Message sendMessage = new Message();
        sendMessage.setType(RELAY_ENTER);

        for (TeamMember member : members) {
            memberList.add(member.user);
        }
        sendMessage.setMessage(memberList);
        teamMember.sendMessage(sendMessage);

        //입장
        room.enter(teamMember);
        session.getAttributes().put("roomId", relayMessage.roomId);
    }

    //연결종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws IOException {
        Integer roomId = (Integer) session.getAttributes().get("roomId");
        Integer memberId = ((MemberSession) session.getAttributes().get(SessionConst.LOGIN_MEMBER)).getId();
        if (roomId != null) {
            //나가기
            final WorkSpaceRoom room = roomMap.get(roomId);
            room.leave(memberId, session);

            //방이 비었다면 방삭제
            if (room.isEmpty()) {
                roomMap.remove(roomId);
            } else { //접속 종료 메세지
                Message message = new Message();
                message.setType(RELAY_LEAVE);
                message.setSender(memberId);
                message.setRoomId(roomId);

                log.debug("접속 종료 방[{}] 유저[{}]", roomId, memberId);

                relayMessageToAllMemberInSameRoom(message);
            }
        }
    }

    public int getOnlineMemberNum(int workSpaceId) {
        return roomMap.containsKey(workSpaceId) ? roomMap.get(workSpaceId).getOnlineMemberNum() : 0;
    }

    //방생성
    private boolean createRoom(int workSpaceId) {
        //방 조회
        final WorkSpace workSpace = workSpaceDao.selectDetail(workSpaceId);
        if (workSpace == null) return false;

        WorkSpaceRoom room = new WorkSpaceRoom(workSpace);
        roomMap.put(workSpaceId, room);
        return true;
    }

    private static class WorkSpaceRoom {
        final WorkSpace workSpace;
        final Map<Integer, TeamMember> enteredMemberMap = new ConcurrentHashMap<>();

        public WorkSpaceRoom(WorkSpace workSpace) {
            this.workSpace = workSpace;
        }

        boolean isEntered(Integer memberId) {
            return enteredMemberMap.containsKey(memberId);
        }

        void enter(TeamMember teamMember) {
            enteredMemberMap.put(teamMember.getMemberId(), teamMember);
        }

        void leave(Integer memberId, WebSocketSession session) {
            final WebSocketSession targetSession = enteredMemberMap.get(memberId).session;
            if (targetSession == session) { //혹시 재입장 처리된 이후일지도 모르니 세션으로 비교
                enteredMemberMap.remove(memberId);
            }
        }

        boolean isEmpty() {
            return enteredMemberMap.isEmpty();
        }

        String getName() {
            return workSpace.getName();
        }

        TeamMember getMember(Integer memberId) {
            return enteredMemberMap.get(memberId);
        }

        int getOnlineMemberNum() {
            return enteredMemberMap.size();
        }
    }

    @RequiredArgsConstructor
    private static class TeamMember {
        final WebSocketSession session;
        final User user;

        public void sendMessage(Message message) {
            try {

                final String jsonText = objectMapper.writeValueAsString(message);
                TextMessage textMessage = new TextMessage(jsonText);
                session.sendMessage(textMessage);
            } catch (Exception e) {
                log.error("프레임 전송오류");
            }
        }

        public Integer getMemberId() {
            return user.getId();
        }
    }

    @Getter
    @Setter
    private static class User {
        private Integer id;
        private String name;
        private Integer profile;
        private int x = 30;
        private int y = 30;

        public User(Integer id, String name, Integer profile) {
            this.id = id;
            this.name = name;
            this.profile = profile;
        }
    }

    @Getter
    @Setter
    private static class Message {
        String type;
        Integer sender;
        Integer receiver;
        Integer roomId;
        Object message;
    }
}
