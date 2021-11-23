package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class Board {
    private int id;
    private int rowNumber;
    private String title;
    private String content;//
    private Date writeDate;
    private String planDate;
    private int uploadFileId;
    private boolean read;

    private MemberSession member;

    public void setRead(char read) {
        this.read = read == 'Y';
    }
}
