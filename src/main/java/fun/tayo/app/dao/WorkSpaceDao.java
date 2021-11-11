package fun.tayo.app.dao;

import fun.tayo.app.dto.WorkSpace;

import java.util.List;

public interface WorkSpaceDao {

    /**
     * 해당 멤버가 가입되어 있는 가상공간 전부 조회
     */
    List<WorkSpace> selectsByMemberId(int memberId);
}
