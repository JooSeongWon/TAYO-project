package fun.tayo.app.dao;

import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.Comments;
import fun.tayo.app.dto.PagingBoardAndMember;

import java.util.List;
import java.util.Map;

public interface BoardDao {

    /**
     * 총 게시글 수 조회
     */
    int selectTotalCount(PagingBoardAndMember pagingBoardAndMember);

    /**
     * 해당 페이지의 목록 조회
     */
    List<Board> selectsByPagingBoardAndMember(PagingBoardAndMember pagingBoardAndMember);

    /**
     * 새글이 있는지 조회
     */
    int selectCntNewPost(Map<String, Object> params);

    /**
     * 게시글 상세조회
     */
    Board selectDetail(int boardId);

    /**
     * 게시글 읽음여부 조회
     */
    int selectCntReadCheck(Map<String, Object> params);

    /**
     * 게시글 읽음 처리
     */
    void insertReadCheck(Map<String, Object> params);

    /**
     * 댓글 작성
     */
    void insertComments(Comments comments);

    /**
     * 게시글 조회 (workSpace와 일치하는지 확인 용)
     */
    int selectCntBoardInWorkSpace(Map<String, Object> params);

    /**
     * 댓글 조회 (작성자 일치 확인 용)
     */
    int selectCntCommentsOfMember(Map<String, Object> params);

    /**
     * 댓글 삭제
     */
    int deleteComments(int commentsId);

    /**
     * 게시글 작성
     */
    void insert(Map<String, Object> params);

    /**
     * 파일 연결
     */
    void insertBoardFileLink(Map<String, Object> params);
}
