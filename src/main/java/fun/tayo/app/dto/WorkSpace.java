package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class WorkSpace {
    private int id;
    private int memberId;
    private String name;
    private String invitationCode;
    private int headCount;
    private List<WorkSpaceAndMember> members;
    private int onlineMemberNum;

    public boolean isOwner(int memberId) {
        return this.memberId == memberId;
    }
}