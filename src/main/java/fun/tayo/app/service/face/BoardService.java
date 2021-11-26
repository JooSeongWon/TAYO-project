package fun.tayo.app.service.face;

import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.MemberSession;
import fun.tayo.app.dto.PagingBoardAndMember;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface BoardService {

    /**
     * 팀 멤버인지 확인
     */
    boolean isNotTeamMemberInWorkSpace(int memberId, int workSpaceId);

    /**
     * 해당 페이지의 게시글 목록 얻기, 페이징 객체에 페이지 정보를 담음
     */
    List<Board> getList(PagingBoardAndMember pagingBoardAndMember);

    /**
     * 해당 가상공간 카테고리의 해당 회원이 읽지않은 게시글이 있는지 체크
     */
    boolean checkNoRead(int memberId, int workSpaceId, int categoryId);

    /**
     * 게시글 상세보기
     */
    Board getDetail(int boardId, boolean noRead, int memberId, int workSpaceId);

    /**
     * 댓글 작성
     */
    int putComments(MemberSession member, int workSpaceId, int boardId, String content);

    /**
     * 댓글 삭제
     */
    boolean deleteComments(int memberId, int workSpaceId, int boardId, int commentsId);

    /**
     * 게시글 작성
     */
    int createNewPost(Map<String, Object> params) throws IOException;

    /**
     * 멤버 작성글 맞는지 체크
     */
    boolean checkBoardNotWrittenFromMember(int boardId, int memberId);

    /**
     * 글 수정
     */
    boolean updatePost(Map<String, Object> params) throws IOException;
}
