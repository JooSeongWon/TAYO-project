package fun.tayo.app.controller;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.PagingBoardAndMember;
import fun.tayo.app.service.face.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("work-spaces/board")
public class WorkSpaceBoardController {

    private final BoardService boardService;

    @GetMapping
    public String getBoard(
            @RequestParam int pageNo,
            @RequestParam int categoryId,
            @RequestParam int workSpaceId,
            @SessionAttribute(value = SessionConst.LOGIN_MEMBER) MemberSession memberSession,
            Model model
    ){
        //접속자가 팀멤버가 맞는지 체크
        if(!boardService.isTeamMemberInWorkSpace(memberSession.getId(), workSpaceId)) {
            throw new RuntimeException("팀멤버 아님 게시판 조회 권한 없음.");
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

        return "user/work-space/board/list";
    }
}
