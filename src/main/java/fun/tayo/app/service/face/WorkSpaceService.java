package fun.tayo.app.service.face;

import fun.tayo.app.dto.WorkSpace;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface WorkSpaceService {

    /**
     * 로그인 한 회원의 가상공간 목록 조회
     */
    List<WorkSpace> getWorkSpaces(HttpServletRequest request);
}
