package fun.tayo.app.dao;

import java.util.Map;

public interface TestDao {
    Map<String, Object> selectOneMember(int memberId);
}