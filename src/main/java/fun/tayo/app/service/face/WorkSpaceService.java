package fun.tayo.app.service.face;

import fun.tayo.app.dto.MemberSession;
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
    ResponseObject createWorkSpace(String name, int headCount, MemberSession memberSession);
}
