package fun.tayo.app.dao;

import fun.tayo.app.dto.TeamMember;
import fun.tayo.app.dto.WorkSpace;

import java.util.List;

public interface WorkSpaceDao {

    /**
     * 해당 멤버가 가입되어 있는 가상공간 전부 조회
     */
    List<WorkSpace> selectsByMemberId(int memberId);

    /**
     * 새 가상공간 생성
     */
    void insert(WorkSpace workSpace);

    /**
     * 새 팀맴버 등록
     */
    void insertTeamMember(TeamMember teamMember);
}
