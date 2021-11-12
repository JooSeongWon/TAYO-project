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
     * 멤버가 만든 가상공간중 id와 일치하는것 자세히 조회
     */
    ResponseObject findDetailWorkSpaceOfMember(int workSpaceId, int memberId);

    /**
     * 초대코드 변경
     */
    ResponseData changeInvitationCode(int workSpaceId, int memberId);
}
