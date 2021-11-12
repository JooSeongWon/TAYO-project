package fun.tayo.app.controller;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.dto.WorkSpace;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/work-spaces")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping
    public String workSpaces( //워크스페이스 목록
            Model model,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {

        final List<WorkSpace> workSpaceList = workSpaceService.getWorkSpaces(memberSession);
        model.addAttribute("workSpaceList", workSpaceList);

        return "user/work-space/list";
    }

    @ResponseBody
    @PostMapping
    public ResponseData createWorkSpace( //워크스페이스 생성
            @RequestParam String name,
            @RequestParam int headCount,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.createWorkSpace(name, headCount, memberSession);
    }

    @ResponseBody
    @PostMapping("/{workSpaceId}")
    public ResponseObject workSpaceDetail( //워크스페이스 정보
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.findDetailWorkSpaceOfMember(workSpaceId, memberSession.getId());
    }

    @ResponseBody
    @PostMapping("/{workSpaceId}/invitation-code")
    public ResponseData changeInvitationCode( //초대코드 갱신
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.changeInvitationCode(workSpaceId, memberSession.getId());
    }
}
