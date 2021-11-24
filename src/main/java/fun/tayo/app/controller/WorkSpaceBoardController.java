package fun.tayo.app.controller;

import fun.tayo.app.common.BoardCategory;
import fun.tayo.app.common.SessionConst;
import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.PagingBoardAndMember;
import fun.tayo.app.dto.ResponseData;
import fun.tayo.app.service.face.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("work-spaces/board")
public class WorkSpaceBoardController {

    private final BoardService boardService;

    @GetMapping
    public String getBoard(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam int categoryId,
            @RequestParam int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            Model model
    ) {
        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            log.debug("팀멤버 아님 게시판 조회 권한 없음.");
            throw new RuntimeException("this isn't team member");
        }

        //페이징에따라 List 모델에 삽입
        Paging paging = new Paging();
        paging.setCurPage(pageNo);

        PagingBoardAndMember pagingBoardAndMember = new PagingBoardAndMember();
        pagingBoardAndMember.setWorkSpaceId(workSpaceId);
        pagingBoardAndMember.setMemberId(memberSession.getId());
        pagingBoardAndMember.setCategoryId(categoryId);
        pagingBoardAndMember.setPaging(paging);

        final List<Board> list = boardService.getList(pagingBoardAndMember);

        model.addAttribute("boardList", list);
        model.addAttribute("paging", paging);
        model.addAttribute("category", getCategoryName(categoryId));
        model.addAttribute("categoryId", categoryId);

        return "user/work-space/board/list";
    }

    @GetMapping("/{boardId}")
    public String getBoardDetail(
            @RequestParam(defaultValue = "false") boolean noRead,
            @RequestParam int workSpaceId,
            @PathVariable int boardId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            Model model
    ) {
        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            log.debug("팀멤버 아님 게시판 조회 권한 없음.");
            throw new RuntimeException("this isn't team member");
        }

        final Board detail = boardService.getDetail(boardId, noRead, memberSession.getId(), workSpaceId);
        if (detail == null) {
            log.debug("권한 없는 사용자 게시글 접근 혹은 존재하지 않는 게시글");
            throw new RuntimeException("no have access grant or not exist post");
        }

        model.addAttribute("board", detail);

        return "user/work-space/board/detail";
    }

    @ResponseBody
    @GetMapping("/read-status")
    public Map<String, Object> getReadStatus(
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam int workSpaceId,
            @RequestParam(required = false) Integer categoryId
    ) {
        Map<String, Object> resultMap = new HashMap<>();
        int memberId = memberSession.getId();

        if (categoryId == null) {
            resultMap.put(String.valueOf(BoardCategory.ISSUE), boardService.checkNoRead(memberId, workSpaceId, BoardCategory.ISSUE));
            resultMap.put(String.valueOf(BoardCategory.WORK_PLAN), boardService.checkNoRead(memberId, workSpaceId, BoardCategory.WORK_PLAN));
            resultMap.put(String.valueOf(BoardCategory.QNA), boardService.checkNoRead(memberId, workSpaceId, BoardCategory.QNA));
        } else {
            resultMap.put(categoryId.toString(), boardService.checkNoRead(memberId, workSpaceId, categoryId));
        }

        return resultMap;
    }

    @ResponseBody
    @PostMapping("/{boardId}/comments")
    public ResponseData putComments(
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam int workSpaceId,
            @RequestParam String content,
            @PathVariable int boardId
    ) {
        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            return new ResponseData(false, "권한이 없습니다.");
        }

        final int result = boardService.putComments(memberSession, workSpaceId, boardId, content);
        if (result == 0) {
            return new ResponseData(false, "요청을 처리할 수 없습니다.");
        }

        return new ResponseData(true, String.valueOf(result));
    }

    private String getCategoryName(int categoryId) {
        switch (categoryId) {
            case BoardCategory.ISSUE:
                return "Issue";
            case BoardCategory.WORK_PLAN:
                return "Work plan";
            case BoardCategory.QNA:
                return "QnA";
        }

        return null;
    }
}
