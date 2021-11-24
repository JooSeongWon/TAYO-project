package fun.tayo.app.service.impl;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.BoardDao;
import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.Comments;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.PagingBoardAndMember;
import fun.tayo.app.service.face.BoardService;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final WorkSpaceService workSpaceService;
    private final BoardDao boardDao;

    //한페이지에 보여질 게시글 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_LIST_COUNT = 15;
    //보여질 페이지 수
    @SuppressWarnings("FieldCanBeLocal")
    private final int PAGING_PAGE_COUNT = 5;

    @Override
    public boolean isNotTeamMemberInWorkSpace(int memberId, int workSpaceId) {
        return workSpaceService.getMyWorkSpaceName(workSpaceId, memberId) == null;
    }

    @Override
    public List<Board> getList(PagingBoardAndMember pagingBoardAndMember) {
        final Paging paging = pagingBoardAndMember.getPaging();

        paging.setListCount(PAGING_LIST_COUNT);
        paging.setPageCount(PAGING_PAGE_COUNT);
        paging.setTotalCount(boardDao.selectTotalCount(pagingBoardAndMember));
        paging.makePaging();

        return boardDao.selectsByPagingBoardAndMember(pagingBoardAndMember);
    }

    @Override
    @Transactional
    public Board getDetail(int boardId, boolean noRead, int memberId, int workSpaceId) {
        final Board board = boardDao.selectDetail(boardId);

        if(board == null || board.getWorkSpaceId() != workSpaceId) {
            return null;
        }

        if(noRead) {
            Map<String, Object> params = new HashMap<>();
            params.put("memberId", memberId);
            params.put("boardId", boardId);

            final int readCheck = boardDao.selectCntReadCheck(params);
            if(readCheck == 0) {
                boardDao.insertReadCheck(params);
            }
        }

        return board;
    }

    @Override
    public boolean checkNoRead(int memberId, int workSpaceId, int categoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("workSpaceId", workSpaceId);
        params.put("categoryId", categoryId);

        int result = boardDao.selectCntNewPost(params);
        return result != 0;
    }

    @Override
    @Transactional
    public int putComments(MemberSession member, int workSpaceId, int boardId, String content) {
        if(!StringUtils.hasText(content)) {
            return 0;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("workSpaceId", workSpaceId);
        params.put("boardId", boardId);

        int checkGrant = boardDao.selectCntBoardInWorkSpace(params);
        if(checkGrant == 0) {
            return 0;
        }

        Comments comments= new Comments();
        comments.setContent(content);
        comments.setMember(member);
        comments.setBoardId(boardId);

        boardDao.insertComments(comments);
        return comments.getId();
    }
}
