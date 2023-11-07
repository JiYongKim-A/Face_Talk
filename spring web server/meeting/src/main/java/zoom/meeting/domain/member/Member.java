package zoom.meeting.domain.member;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@RequiredArgsConstructor
public class Member {
    /**
     * ==회원==
     * <p>
     * 관리번호
     * login ID
     * login PW
     * 사용자 이름
     * 닉네임
     */

    private Long manageSeq;

    private String loginId;

    private String password;

    private String name;

    private String nickName;

    public Member(String loginId, String password, String name, String nickName) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }

    public Member(long manageSeq, String loginId, String password, String name, String nickName) {
        this.manageSeq = manageSeq;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }
}
