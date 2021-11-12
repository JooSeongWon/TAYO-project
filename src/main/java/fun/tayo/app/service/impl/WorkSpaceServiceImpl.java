package fun.tayo.app.service.impl;

import fun.tayo.app.dao.WorkSpaceDao;
import fun.tayo.app.dto.*;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    private final WorkSpaceDao workSpaceDao;

    @Override
    public List<WorkSpace> getWorkSpaces(MemberSession memberSession) {
        return workSpaceDao.selectsByMemberId(memberSession.getId());
    }


    @Override
    @Transactional
    public ResponseData createWorkSpace(String name, int headCount, MemberSession memberSession) {
        //유효성 검증
        ResponseData responseData = validateInputData(name, headCount);
        if (!responseData.getResult()) {
            return responseData;
        }

        if(memberSession == null) {
            return new ResponseData(false, "잘못된 접근입니다.");
        }

        //db 삽입
        WorkSpace workSpace = new WorkSpace();
        workSpace.setMemberId(memberSession.getId());
        workSpace.setHeadCount(headCount);
        workSpace.setName(name);
        workSpace.setInvitationCode(createInvitationCode());

        workSpaceDao.insert(workSpace);
        workSpaceDao.insertTeamMember(new TeamMember(memberSession.getId(), workSpace.getId()));

        responseData.setMessage("생성완료");
        return responseData;
    }

    @Override
    public ResponseObject findDetailWorkSpaceOfMember(int workSpaceId, int memberId) {
        final WorkSpace workSpace = workSpaceDao.selectDetail(new TeamMember(memberId, workSpaceId));

        return (workSpace == null) ? new ResponseObject(false, "잘못된 접근입니다.") : new ResponseObject(true, workSpace);
    }

    @Override
    @Transactional
    public ResponseData changeInvitationCode(int workSpaceId, int memberId) {
        WorkSpace workSpaceParam = new WorkSpace();
        workSpaceParam.setId(workSpaceId);
        workSpaceParam.setMemberId(memberId);
        workSpaceParam.setInvitationCode(createInvitationCode());

        final int result = workSpaceDao.updateInvitationCode(workSpaceParam);
        return result == 0 ? new ResponseData(false, "잘못된 접근입니다.") : new ResponseData(true, workSpaceParam.getInvitationCode());
    }

    private String createInvitationCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0,20);
    }

    private ResponseData validateInputData(String name, int headCount) {
        if (!StringUtils.hasText(name) || headCount <= 0) {
            return new ResponseData(false, "이름과 팀원 수를 정확히 입력하세요!");
        }

        String nameRegex = "^[a-zA-Z0-9가-힣]{2,20}$";
        if (!Pattern.matches(nameRegex, name)) {
            return new ResponseData(false, "이름은 영문, 숫자, 한글 2~20자 사이로 입력하세요!");
        }

        if (headCount < 2 || headCount > 10) {
            return new ResponseData(false, "인원은 2~10인 까지 설정 가능합니다.");
        }

        return new ResponseData(true, null);
    }
}
