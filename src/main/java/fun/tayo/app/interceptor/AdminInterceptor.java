package fun.tayo.app.interceptor;

import fun.tayo.app.common.SessionConst;
import fun.tayo.app.dto.MemberSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        final HttpSession session = request.getSession(false);
        final boolean isLogin = session != null && session.getAttribute(SessionConst.LOGIN_MEMBER) != null;
        final MemberSession memberSession = isLogin ? (MemberSession) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
        final boolean isAdmin = isLogin && memberSession.isAdmin();

        if (isAdmin) return true;

        //ajax 요청일경우 500 error 떨구기 (세션만료 AJAX)
        if (request.getHeader("X-Requested-With") != null) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else { //일반요청일경우 404 error로 페이지 감추기 (세션만료 혹은 직접 접근시도)
            response.sendError(HttpStatus.NOT_FOUND.value());
        }
        return false;
    }
}