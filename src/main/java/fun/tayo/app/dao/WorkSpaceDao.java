package fun.tayo.app.dao;

import fun.tayo.app.dto.WorkSpaceAndMember;
import fun.tayo.app.dto.WorkSpace;

import java.util.List;
import java.util.Map;

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
    void insertTeamMember(WorkSpaceAndMember workSpaceAndMember);

    /**
     * 가상공간의 자세한 정보
     */
    WorkSpace selectDetail(int workSpaceId);

    /**
     * 해당 멤버가 만든 가상공간의 자세한 정보
     */
    WorkSpace selectDetailOfMember(WorkSpaceAndMember workSpaceAndMember);

    /**
     * 초대코드로 가상공간 조회
     */
    WorkSpace selectDetailByInvCode(String invitationCode);

    /**
     * 해당 멤버가 만든 가상공간의 데이터의 초대코드 변경
     */
    int updateInvitationCode(WorkSpace workSpace);

    /**
     * 해당 멤버가 만든 가상공간의 headCount와 name만 업데이트
     */
    int update(WorkSpace workSpace);

    /**
     * 해당 workspace 인원 수 체크
     */
    int selectCntTeamMember(int workSpaceId);

    /**
     * 해당 멤버가 만든 가상공간 삭제
     */
    int delete(WorkSpaceAndMember workSpaceAndMember);

    /**
     * 팀멤버 삭제
     */
    int deleteTeamMember(WorkSpaceAndMember workSpaceAndMember);

    /**
     * 가입자의 가상공간 이름조회
     */
    String selectWorkSpaceNameOfTeamMember(Map<String, Integer> params);
}
