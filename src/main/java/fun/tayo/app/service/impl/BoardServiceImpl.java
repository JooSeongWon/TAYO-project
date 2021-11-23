package fun.tayo.app.service.impl;

import fun.tayo.app.common.util.Paging;
import fun.tayo.app.dao.BoardDao;
import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.PagingBoardAndMember;
import fun.tayo.app.service.face.BoardService;
import fun.tayo.app.service.face.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public boolean isTeamMemberInWorkSpace(int memberId, int workSpaceId) {
        return workSpaceService.getMyWorkSpaceName(workSpaceId, memberId) != null;
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
}
