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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("work-spaces/{workSpaceId}/board")
public class WorkSpaceBoardController {

    private final BoardService boardService;

    @GetMapping
    public String getBoard(
            @PathVariable int workSpaceId,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam int categoryId,
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
            @PathVariable int workSpaceId,
            @PathVariable int boardId,
            @RequestParam(defaultValue = "false") boolean noRead,
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
            @PathVariable int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
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
            @PathVariable int workSpaceId,
            @PathVariable int boardId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam String content
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

    @ResponseBody
    @DeleteMapping("/{boardId}/comments/{commentsId}")
    public ResponseData deleteComments(
            @PathVariable int workSpaceId,
            @PathVariable int boardId,
            @PathVariable int commentsId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            return new ResponseData(false, "권한이 없습니다.");
        }

        final boolean result = boardService.deleteComments(memberSession.getId(), workSpaceId, boardId, commentsId);
        if (!result) {
            return new ResponseData(false, "요청을 처리할 수 없습니다.");
        }

        return new ResponseData(true, "ok");
    }

    @GetMapping("/create")
    public String getCreateForm(
            @PathVariable int workSpaceId,
            @SessionAttribute(SessionConst.LOGIN_MEMBER) MemberSession memberSession
    ) {
        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            throw new RuntimeException();
        }

        return "user/work-space/board/input-form";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseData createNewPost(
            @PathVariable int workSpaceId,
            @SessionAttribute(SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam String planDateStart,
            @RequestParam String planDateEnd,
            @RequestParam int category,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam MultipartFile file
    ) throws IOException {

        //접속자가 팀멤버가 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            return new ResponseData(false, "권한이 없습니다.");
        }

        Map<String, Object> requestParams = new HashMap<>();
        if (category == BoardCategory.WORK_PLAN) {
            if (!StringUtils.hasText(planDateStart) || !StringUtils.hasText(planDateEnd)) {
                return new ResponseData(false, "일정 날짜를 입력하세요.");
            }

            requestParams.put("planDate", planDateStart.replace("-", "") +
                    "-" +
                    planDateEnd.replace("-", "")
            );
        }
        requestParams.put("categoryId", category);
        requestParams.put("title", title);
        requestParams.put("content", content);
        requestParams.put("file", file);
        requestParams.put("memberId", memberSession.getId());
        requestParams.put("workSpaceId", workSpaceId);

        final int newPostId = boardService.createNewPost(requestParams);

        if (newPostId == 0) { //검증오류
            return new ResponseData(false, "입력값을 확인하세요.");
        }
        return new ResponseData(true, String.valueOf(newPostId));
    }

    @GetMapping("/{boardId}/update")
    public String getUpdateForm(
            @PathVariable int workSpaceId,
            @PathVariable int boardId,
            @SessionAttribute(SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            Model model
    ) {
        //접속자가 팀멤버가 맞는지, 접속자가 작성한 글이 맞는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)
                || boardService.checkBoardNotWrittenFromMember(boardId, memberSession.getId())) {
            throw new RuntimeException();
        }

        //게시글
        final Board detail = boardService.getDetail(boardId, false, memberSession.getId(), workSpaceId);
        if (detail == null) {
            throw new RuntimeException();
        }

        model.addAttribute("board", detail);
        return "user/work-space/board/update-form";
    }

    @ResponseBody
    @PatchMapping("/{boardId}")
    public ResponseData updatePost(
            @PathVariable int workSpaceId,
            @PathVariable int boardId,
            @SessionAttribute(SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            @RequestParam(required = false) String planDateStart,
            @RequestParam(required = false) String planDateEnd,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) Boolean deleteFile,
            @RequestParam() int category
    ) throws IOException {

        //접속자가 팀멤버가 맞는지, 수정권한이 있는지 체크
        if (boardService.isNotTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)
                || boardService.checkBoardNotWrittenFromMember(boardId, memberSession.getId())) {
            return new ResponseData(false, "권한이 없습니다.");
        }

        Map<String, Object> requestParams = new HashMap<>();
        if (category == BoardCategory.WORK_PLAN) {
            if (StringUtils.hasText(planDateStart) && StringUtils.hasText(planDateEnd)) {
                requestParams.put("planDate", planDateStart.replace("-", "") +
                        "-" +
                        planDateEnd.replace("-", "")
                );
            }
        }

        requestParams.put("categoryId", category);
        requestParams.put("title", title);
        requestParams.put("content", content);
        requestParams.put("file", file);
        requestParams.put("memberId", memberSession.getId());
        requestParams.put("workSpaceId", workSpaceId);
        requestParams.put("boardId", boardId);
        requestParams.put("deleteFile", deleteFile);

        final boolean result = boardService.updatePost(requestParams);

        if (!result) { //검증오류
            return new ResponseData(false, "입력값을 확인하세요.");
        }
        return new ResponseData(true, "ok");
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
