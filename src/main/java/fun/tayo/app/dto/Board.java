package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter @Setter @ToString
public class Board {
    private int id;
    private int rowNumber;
    private String title;
    private String content;
    private Date writeDate;
    private String planDate;
    private Integer uploadFileId;
    private boolean read;
    private Integer commentsCnt;

    private MemberSession member;
    private List<Comments> commentsList;

    public void setRead(char read) {
        this.read = read == 'Y';
    }
}
