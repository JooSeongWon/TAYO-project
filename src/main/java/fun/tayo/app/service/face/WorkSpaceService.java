package fun.tayo.app.service.face;

import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.dto.WorkSpace;

import java.util.List;

public interface WorkSpaceService {

    /**
     * 로그인 한 회원의 가상공간 목록 조회
     */
    List<WorkSpace> getWorkSpaces(MemberSession memberSession);

    /**
     * 가상공간 생성
     */
    ResponseData createWorkSpace(String name, int headCount, MemberSession memberSession);

    /**
     * (멤버가 만든 가상공간인지 체크 포함) workspace 조회 - 가입멤버 포함
     */
    ResponseObject findDetailWorkSpaceOfMember(int workSpaceId, int memberId);

    /**
     * (멤버가 만든 가상공간인지 체크 포함) 초대코드 변경
     */
    ResponseData changeInvitationCode(int workSpaceId, int memberId);

    /**
     * (멤버가 만든 가상공간인지 체크 포함) name, headCount 업데이트
     */
    ResponseData updateWorkSpace(int workSpaceId, int memberId, String name, int headCount);

    /**
     * (멤버가 만든 가상공간인지 체크 포함) workspace 삭제
     */
    ResponseData deleteWorkSpace(int workSpaceId, int memberId);

    /**
     * 초대코드로 팀 가입하기
     */
    ResponseData joinWorkSpaceByInvCode(int memberId, String invitationCode);

    /**
     * (멤버가 만든 가상공간인지 체크 포함) 멤버 추방하기
     */
    ResponseData expelTeamMember(int workSpaceId, int requesterId, int targetId);

    /**
     * 가상공간 탈퇴
     */
    ResponseData exitTeam(int workSpaceId, int memberId);
}
