package fun.tayo.app.service.face;

import fun.tayo.app.dto.Board;
import fun.tayo.app.dto.PagingBoardAndMember;

import java.util.List;

public interface BoardService {

    /**
     * 팀 멤버인지 확인
     */
    boolean isTeamMemberInWorkSpace(int memberId, int workSpaceId);

    /**
     * 해당 페이지의 게시글 목록 얻기, 페이징 객체에 페이지 정보를 담음
     */
    List<Board> getList(PagingBoardAndMember pagingBoardAndMember);

}
