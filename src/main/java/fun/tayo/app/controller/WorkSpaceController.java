package fun.tayo.app.controller;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.dto.ResponseObject;
import fun.tayo.app.dto.WorkSpace;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/work-spaces")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    //워크스페이스 목록
    @GetMapping
    public String workSpaces(
            Model model,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {

        final List<WorkSpace> workSpaceList = workSpaceService.getWorkSpaces(memberSession);
        model.addAttribute("workSpaceList", workSpaceList);

        return "user/work-space/list";
    }

    //워크스페이스 생성
    @ResponseBody
    @PostMapping
    public ResponseData createWorkSpace(
            @RequestParam String name,
            @RequestParam int headCount,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.createWorkSpace(name, headCount, memberSession);
    }

    //워크스페이스 정보
    @ResponseBody
    @PostMapping("/{workSpaceId}")
    public ResponseObject workSpaceDetail(
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.findDetailWorkSpaceOfMember(workSpaceId, memberSession.getId());
    }

    //워크스페이스 수정(덮어쓰기)
    @ResponseBody
    @PutMapping("/{workSpaceId}")
    public ResponseData updateWorkSpace(
            @PathVariable int workSpaceId,
            @RequestBody Map<String, Object> params,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.updateWorkSpace(
                workSpaceId,
                memberSession.getId(),
                params.get("name").toString(),
                (int) params.get("headCount")
        );
    }

    //워크스페이스 삭제
    @ResponseBody
    @DeleteMapping("/{workSpaceId}")
    public ResponseData deleteWorkSpace(
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.deleteWorkSpace(workSpaceId, memberSession.getId());
    }

    //초대코드 갱신
    @ResponseBody
    @PostMapping("/{workSpaceId}/invitation-code")
    public ResponseData changeInvitationCode(
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.changeInvitationCode(workSpaceId, memberSession.getId());
    }

    //초대코드로 가입
    @ResponseBody
    @PostMapping("/invitation/join")
    public ResponseData changeInvitationCode(
            @RequestParam String invitationCode,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.joinWorkSpaceByInvCode(memberSession.getId(), invitationCode);
    }

    //팀원 추방
    @ResponseBody
    @DeleteMapping("/{workSpaceId}/members/{memberId}")
    public ResponseData expelMember(
            @PathVariable int workSpaceId,
            @PathVariable(value = "memberId") int targetId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.expelTeamMember(workSpaceId, memberSession.getId(), targetId);
    }

    //팀 탈퇴
    @ResponseBody
    @DeleteMapping("/{workSpaceId}/members")
    public ResponseData expelMember(
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        return workSpaceService.exitTeam(workSpaceId, memberSession.getId());
    }

    //워크스페이스 입장
    @GetMapping("/{workSpaceId}")
    public String enterWorkSpace(
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            Model model
    ) {

        model.addAttribute("name", "test");
        return "user/work-space/work-space";
    }
}
