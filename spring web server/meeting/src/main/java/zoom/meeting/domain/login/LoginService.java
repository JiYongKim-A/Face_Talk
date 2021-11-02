package zoom.meeting.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;

    /**
     * Login Service
     * @param loginId
     * @param password
     * @return
     */


    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

    }
}
