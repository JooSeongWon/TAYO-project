package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class Comments {
    private int id;
    private Date writeDate;
    private String content;
    private MemberSession member;
}
