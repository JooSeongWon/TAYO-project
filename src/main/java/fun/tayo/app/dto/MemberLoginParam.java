package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberLoginParam {

    private String email;
    private String password;
}
