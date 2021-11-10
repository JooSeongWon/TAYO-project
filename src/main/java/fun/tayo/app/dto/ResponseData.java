package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ResponseData {
	
    private final boolean result;
    private final String message;
    
    public ResponseData(boolean result, String message) {
        this.result = result;
        this.message = message;
    }
}
