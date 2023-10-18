package zoom.meeting.service.signUp.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.signUp.SignUpService;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpServiceImplementV1 implements SignUpService {

    private final MemberRepository memberRepository;


    @Override
    public boolean[] validation(String loginId, String password, String nickName) {
        // index = 0, ID 빈칸 확인
        // index = 1, nickName 빈칸 확인
        // index = 2, ID 중복
        // index = 3, ID password 동일 검증
        // index = 4, nickName 중복
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
    public Member memberSignUp(Member member) {
        memberRepository.save(member);
        return member;
    }
}
