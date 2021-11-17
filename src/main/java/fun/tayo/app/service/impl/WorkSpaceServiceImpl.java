package fun.tayo.app.service.impl;

import fun.tayo.app.dao.WorkSpaceDao;
import fun.tayo.app.dto.*;
import fun.tayo.app.service.face.WorkSpaceService;
import fun.tayo.app.websocket.WorkSpaceHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


@Slf4j
@Service
@RequiredArgsConstructor
public class WorkSpaceServiceImpl implements WorkSpaceService {

    private final WorkSpaceDao workSpaceDao;
    private final WorkSpaceHandler workSpaceHandler;

    @Override
    public List<WorkSpace> getWorkSpaces(MemberSession memberSession) {
        final List<WorkSpace> workSpaces = workSpaceDao.selectsByMemberId(memberSession.getId());
        workSpaces.forEach(workspace -> workspace.setOnlineMemberNum(workSpaceHandler.getOnlineMemberNum(workspace.getId())));

        return workSpaces;
    }


    @Override
    @Transactional
    public ResponseData createWorkSpace(String name, int headCount, MemberSession memberSession) {
        //유효성 검증
        ResponseData responseData = validateInputData(name, headCount);
        if (!responseData.getResult()) {
            return responseData;
        }

        if (memberSession == null) {
            return new ResponseData(false, "잘못된 접근입니다.");
        }

        //db 삽입
        WorkSpace workSpace = new WorkSpace();
        workSpace.setMemberId(memberSession.getId());
        workSpace.setHeadCount(headCount);
        workSpace.setName(name);
        workSpace.setInvitationCode(createInvitationCode());

        workSpaceDao.insert(workSpace);
        workSpaceDao.insertTeamMember(new WorkSpaceAndMember(memberSession.getId(), workSpace.getId()));

        responseData.setMessage("ok");
        return responseData;
    }

    @Override
    public ResponseObject findDetailWorkSpaceOfMember(int workSpaceId, int memberId) {
        final WorkSpace workSpace = workSpaceDao.selectDetailOfMember(new WorkSpaceAndMember(memberId, workSpaceId));

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

    @Override
    @Transactional
    public ResponseData updateWorkSpace(int workSpaceId, int memberId, String name, int headCount) {

        if (headCount < getCurrentHeadCountFrom(workSpaceId)) {
            return new ResponseData(false, "현재 가입인원이 더 많습니다.");
        }

        WorkSpace workSpaceParam = new WorkSpace();
        workSpaceParam.setId(workSpaceId);
        workSpaceParam.setMemberId(memberId);
        workSpaceParam.setName(name);
        workSpaceParam.setHeadCount(headCount);

        final int result = workSpaceDao.update(workSpaceParam);
        return result == 0 ? new ResponseData(false, "잘못된 접근입니다.") : new ResponseData(true, "ok");
    }

    @Override
    @Transactional
    public ResponseData deleteWorkSpace(int workSpaceId, int memberId) {
        WorkSpaceAndMember workSpaceAndMember = new WorkSpaceAndMember(memberId, workSpaceId);
        final int result = workSpaceDao.delete(workSpaceAndMember);

        return result == 0 ? new ResponseData(false, "잘못된 접근입니다.") : new ResponseData(true, "ok");
    }

    @Override
    @Transactional
    public ResponseData joinWorkSpaceByInvCode(int memberId, String invitationCode) {
        final WorkSpace workSpace = workSpaceDao.selectDetailByInvCode(invitationCode);

        //유효성 검사
        if (workSpace == null) {
            return new ResponseData(false, "초대코드를 확인하세요.");
        }
        if (workSpace.getHeadCount() <= workSpace.getMembers().size()) {
            return new ResponseData(false, "정원이 가득 찼습니다.");
        }

        final List<WorkSpaceAndMember> members = workSpace.getMembers();
        for (WorkSpaceAndMember member : members) {
            if (member.getMemberId() == memberId) {
                return new ResponseData(false, "이미 가입한 팀입니다.");
            }
        }

        workSpaceDao.insertTeamMember(new WorkSpaceAndMember(memberId, workSpace.getId()));

        return new ResponseData(true, String.format("축하합니다!<br>팀 '%s'의 새 멤버가 되셨습니다.", workSpace.getName()));
    }

    @Override
    @Transactional
    public ResponseData expelTeamMember(int workSpaceId, int requesterId, int targetId) {
        if (workSpaceId == requesterId) {
            return new ResponseData(false, "처리할 수 없는 요청입니다.");
        }

        final ResponseObject detailWorkSpaceOfMember = findDetailWorkSpaceOfMember(workSpaceId, requesterId);
        if (!detailWorkSpaceOfMember.getResult()) {
            return new ResponseData(false, "처리할 수 없는 요청입니다.");
        }

        final WorkSpace workSpace = (WorkSpace) detailWorkSpaceOfMember.getObject();
        final List<WorkSpaceAndMember> members = workSpace.getMembers();

        for (WorkSpaceAndMember member : members) {
            if (member.getMemberId() == targetId) {
                final int result = workSpaceDao.deleteTeamMember(new WorkSpaceAndMember(targetId, workSpaceId));
                if (result == 0) return new ResponseData(false, "서버오류");

                return new ResponseData(true, "ok");
            }
        }

        return new ResponseData(false, "없는 회원입니다.");
    }

    @Override
    @Transactional
    public ResponseData exitTeam(int workSpaceId, int memberId) {
        final int result = workSpaceDao.deleteTeamMember(new WorkSpaceAndMember(memberId, workSpaceId));
        if (result == 0) return new ResponseData(false, "처리할 수 없는 요청입니다.");

        return new ResponseData(true, "ok");
    }

    @Override
    public String getMyWorkSpaceName(int workSpaceId, int memberId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("workSpaceId", workSpaceId);

        return workSpaceDao.selectWorkSpaceNameOfTeamMember(params);
    }

    private int getCurrentHeadCountFrom(int workSpaceId) {
        return workSpaceDao.selectCntTeamMember(workSpaceId);
    }

    private String createInvitationCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private ResponseData validateInputData(String name, int headCount) {
        if (!StringUtils.hasText(name) || headCount <= 0) {
            return new ResponseData(false, "이름과 팀원 수를 정확히 입력하세요!");
        }

        String nameRegex = "^[a-zA-Z0-9가-힣 ]{2,20}$";
        if (!Pattern.matches(nameRegex, name)) {
            return new ResponseData(false, "이름은 영문, 숫자, 한글 2~20자 사이로 입력하세요!");
        }

        if (headCount < 2 || headCount > 10) {
            return new ResponseData(false, "인원은 2~10인 까지 설정 가능합니다.");
        }

        return new ResponseData(true, null);
    }
}