package zoom.meeting.service.login;

import zoom.meeting.domain.member.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;

public interface LoginService {
    Member login(String loginId,String password);

    void generateSession(HttpServletRequest req, String loginId, String nickName);

    void logout(HttpSession session);
}
