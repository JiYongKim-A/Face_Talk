package zoom.meeting.service.signUp.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.signUp.SignUpService;

@Service
@RequiredArgsConstructor
public class SignUpServiceImplementV1 implements SignUpService {

    private final MemberRepository memberRepository;

    @Override
    public boolean[] validation(String loginId, String password, String nickName) {
        // index = 0, ID 중복
        // index = 1, ID password 동일
        // index = 2, nickName 중복
        boolean[] overlapCheck = new boolean[5];
        overlapCheck[0] = loginId.contains(" ");
        overlapCheck[1] = nickName.contains(" ");
        overlapCheck[2] = memberRepository.allLoginId().contains(loginId);
        overlapCheck[3] = loginId.equals(password);
        overlapCheck[4] = memberRepository.allNickName().contains(nickName);
        for (boolean b : overlapCheck) {
            if(b){
                return overlapCheck;
            }
        }
        return null;
    }

    @Override
    public void memberSignUp(Member member) {
        memberRepository.save(new Member(
                member.getLoginId(),
                member.getPassword(),
                member.getName(),
                member.getNickName()));

    }
}
