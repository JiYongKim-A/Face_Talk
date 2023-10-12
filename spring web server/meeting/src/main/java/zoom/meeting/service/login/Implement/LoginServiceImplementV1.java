package zoom.meeting.service.login.Implement;

import lombok.RequiredArgsConstructor;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.login.LoginService;

@RequiredArgsConstructor
public class LoginServiceImplementV1 implements LoginService {
    private final MemberRepository memberRepository;
    @Override
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
