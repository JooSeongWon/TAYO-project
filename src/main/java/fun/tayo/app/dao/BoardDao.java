package fun.tayo.app.dao;

import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.PagingBoardAndMember;

import java.util.List;

public interface BoardDao {

    /**
     * 총 게시글 수 조회
     */
    int selectTotalCount(PagingBoardAndMember pagingBoardAndMember);

    /**
     * 해당 페이지의 목록 조회
     */
    List<Board> selectsByPagingBoardAndMember(PagingBoardAndMember pagingBoardAndMember);

}
