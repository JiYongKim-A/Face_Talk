package zoom.meeting.service.login.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.login.LoginService;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class LoginServiceImplementV1 implements LoginService {
    private final MemberRepository memberRepository;
    @Override
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    @Override
    public void generateSession(HttpServletRequest req, String loginId, String nickName){
        HttpSession session = req.getSession();
        SessionForm loggedMemSession = new SessionForm(loginId, nickName);
        session.setAttribute(SessionConst.LOGIN_SESSION_KEY, loggedMemSession);
    }

    @Override
    public void logout(HttpSession session) {
        if(session != null){
            session.invalidate();
        }
    }
}
