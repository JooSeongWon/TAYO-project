package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class UploadFile {
    private int id;
    private String savedName;
    private String originName;
    private int memberId;
}
