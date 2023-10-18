package zoom.meeting.service.signUp.implement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.signUp.SignUpService;

@Service
public class SignUpServiceImplementV1 implements SignUpService {

    private final MemberRepository memberRepository;
    private final TransactionTemplate txTemplate;

    public SignUpServiceImplementV1(MemberRepository memberRepository, PlatformTransactionManager txManager) {
        this.memberRepository = memberRepository;
        this.txTemplate = new TransactionTemplate(txManager);
    }

    @Override
    public boolean[] validation(String loginId, String password, String nickName) {
        // index = 0, ID 빈칸 확인
        // index = 1, nickName 빈칸 확인
        // index = 2, ID 중복
        // index = 3, ID password 동일 검증
        // index = 4, nickName 중복
        boolean[] overlapCheck = new boolean[5];

        txTemplate.executeWithoutResult((status) -> {
            try {
                overlapCheck[0] = loginId.contains(" ");
                overlapCheck[1] = nickName.contains(" ");
                overlapCheck[2] = memberRepository.allLoginId().contains(loginId);
                overlapCheck[3] = loginId.equals(password);
                overlapCheck[4] = memberRepository.allNickName().contains(nickName);
            } catch (Exception e) {
                throw  new IllegalStateException(e);
            }
        });

        for (boolean b : overlapCheck) {
            if(b){
                return overlapCheck;
            }
        }
        return null;
    }

    @Override
    public Member memberSignUp(Member member) {
        txTemplate.executeWithoutResult(status -> {
            try{
                memberRepository.save(member);
            }catch (Exception e){
                throw new IllegalStateException(e);
            }
        });
        return member;
    }
}
