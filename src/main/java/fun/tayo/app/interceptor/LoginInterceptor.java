package fun.tayo.app.interceptor;

import fun.tayo.app.common.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            //ajax 요청일경우 500 error 떨구기
            if (request.getHeader("X-Requested-With") != null) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            } else { //일반요청 redirect
                response.sendRedirect("/login");
            }
            return false;
        }
        return true;
    }
}