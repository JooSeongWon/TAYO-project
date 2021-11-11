package fun.tayo.app.service.impl;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dao.WorkSpaceDao;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.WorkSpace;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    private final WorkSpaceDao workSpaceDao;

    @Override
    public List<WorkSpace> getWorkSpaces(HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if(session == null) return null;

        MemberSession member = (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER);
        return workSpaceDao.selectsByMemberId(member.getId());
    }
}
