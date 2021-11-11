package fun.tayo.app.service.impl;

import fun.tayo.app.dao.WorkSpaceDao;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.dto.TeamMember;
import fun.tayo.app.dto.WorkSpace;
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
    public ResponseObject createWorkSpace(String name, int headCount, MemberSession memberSession) {
        //유효성 검증
        ResponseObject responseObject = validateInputData(name, headCount);
        if(!responseObject.getResult()) {
            return responseObject;
        }

        //db 삽입
        WorkSpace workSpace = new WorkSpace();
        workSpace.setMemberId(memberSession.getId());
        workSpace.setHeadCount(headCount);
        workSpace.setName(name);
        workSpace.setInvitationCode(createInvitationCode());

        workSpaceDao.insert(workSpace);
        workSpaceDao.insertTeamMember(new TeamMember(memberSession.getId(), workSpace.getId()));

        workSpace.setInvitationCode(null);
        workSpace.setMemberId(0);
        responseObject.setObject(workSpace);
        return responseObject;
    }

    private String createInvitationCode() {
        return UUID.randomUUID().toString().replace("-","");
    }

    private ResponseObject validateInputData(String name, int headCount) {
        if(!StringUtils.hasText(name) || headCount <= 0) {
            return new ResponseObject(false, "이름과 팀원 수를 정확히 입력하세요!");
        }

        String nameRegex = "^[a-zA-Z0-9가-힣]{2,20}$";
        if(!Pattern.matches(nameRegex, name)) {
            return new ResponseObject(false, "이름은 영문, 숫자, 한글 2~20자 사이로 입력하세요!");
        }

        if(headCount < 2 || headCount > 10) {
            return new ResponseObject(false, "인원은 2~10인 까지 설정 가능합니다.");
        }

        return new ResponseObject(true, null);
    }
}
