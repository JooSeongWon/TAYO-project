package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamMember {
    private int memberId;
    private int workSpaceId;

    public TeamMember() {
    }

    public TeamMember(int memberId, int workSpaceId) {
        this.memberId = memberId;
        this.workSpaceId = workSpaceId;
    }
}
