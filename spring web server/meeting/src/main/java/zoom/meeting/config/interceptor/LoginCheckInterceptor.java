package zoom.meeting.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import zoom.meeting.config.session.sessionConst.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        String code = request.getParameter("code");


        if(session == null || session.getAttribute(SessionConst.LOGIN_SESSION_KEY)==null){
            log.info("미인증 사용자 요청");
            response.sendRedirect("/login?redirectURL="+requestURI);
            return false;
        }
        return true;
    }
}
