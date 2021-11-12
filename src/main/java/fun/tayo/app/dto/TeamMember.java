package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TeamMember {
    private int memberId;
    private int workSpaceId;
    private String memberName;

    public TeamMember() {
    }

    public TeamMember(int memberId, int workSpaceId) {
        this.memberId = memberId;
        this.workSpaceId = workSpaceId;
    }
}
