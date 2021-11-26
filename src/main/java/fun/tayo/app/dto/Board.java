package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class Board {
    private int id;
    private int workSpaceId;
    private int rowNumber;
    private String title;
    private String content;
    private Date writeDate;
    private String planDate;
    private Integer uploadFileId;
    private String uploadFileName;
    private boolean read;
    private Integer commentsCnt;

    private MemberSession member;
    private List<Comments> commentsList;

    public void setRead(char read) {
        this.read = read == 'Y';
    }

    public String getPlanStartDate() {
        return formatPlanDate(0);
    }

    public String getPlanEndDate() {
        return formatPlanDate(9);
    }

    private String formatPlanDate(int offset) {
        return planDate.substring(offset, offset + 4) + "-" + planDate.substring(offset + 4, offset + 6) + "-" + planDate.substring(offset + 6, offset + 8);
    }


}
