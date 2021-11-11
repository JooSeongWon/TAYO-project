package fun.tayo.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseObject {
    private boolean result;
    private Object object;

    public ResponseObject(boolean result, Object object) {
        this.result = result;
        this.object = object;
    }

    public boolean getResult() {
        return result;
    }
}
