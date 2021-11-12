package fun.tayo.app.service.face;

import javax.servlet.http.HttpServletRequest;

public interface SocialLoginService {

	boolean login(String code, HttpServletRequest request);



}
