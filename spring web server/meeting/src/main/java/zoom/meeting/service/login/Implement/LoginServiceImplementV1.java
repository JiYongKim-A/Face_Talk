package zoom.meeting.service.login.Implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.login.LoginService;
import zoom.meeting.config.session.form.SessionForm;
import zoom.meeting.config.session.sessionConst.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class LoginServiceImplementV1 implements LoginService {
    private final MemberRepository memberRepository;
    // 트랜잭션 템플릿 사용
    private final TransactionTemplate txTemplate;

    public LoginServiceImplementV1(MemberRepository memberRepository, PlatformTransactionManager txManager) {
        this.memberRepository = memberRepository;
        this.txTemplate = new TransactionTemplate(txManager);
    }

    @Override
    public Member login(String loginId, String password) {
        Optional<Member> loginMember = txTemplate.execute((status) -> {
            try {
                return memberRepository.findByLoginId(loginId)
                        .filter(m -> m.getPassword().equals(password));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
        return loginMember.orElse(null);
    }

    @Override
    public void generateSession(HttpServletRequest req, String loginId, String nickName) {
        HttpSession session = req.getSession();
        SessionForm loggedMemSession = new SessionForm(loginId, nickName);
        session.setAttribute(SessionConst.LOGIN_SESSION_KEY, loggedMemSession);
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}
