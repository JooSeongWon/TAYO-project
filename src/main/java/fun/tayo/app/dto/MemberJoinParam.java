package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberJoinParam {

    private String name;
    private String email;
    private String password;
}
