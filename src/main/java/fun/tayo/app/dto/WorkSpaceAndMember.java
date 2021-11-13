package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class WorkSpaceAndMember {
    private int memberId;
    private int workSpaceId;
    private String memberName;

    public WorkSpaceAndMember() {
    }

    public WorkSpaceAndMember(int memberId, int workSpaceId) {
        this.memberId = memberId;
        this.workSpaceId = workSpaceId;
    }
}
