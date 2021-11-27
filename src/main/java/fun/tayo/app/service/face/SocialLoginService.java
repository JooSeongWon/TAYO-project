package fun.tayo.app.service.face;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface SocialLoginService {

	boolean login(String code, HttpServletRequest request) throws UnsupportedEncodingException;



}
