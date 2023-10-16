package zoom.meeting.service.signUp;

import zoom.meeting.domain.member.Member;

public interface SignUpService {

    boolean[] validation(String loginId, String password, String nickName);

    Member memberSignUp(Member member);
}
