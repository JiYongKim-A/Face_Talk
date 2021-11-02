package zoom.meeting.domain.member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class Member {
    /**
     * ==회원==
     *
     * 관리번호
     * login ID
     * login PW
     * 사용자 이름
     * 닉네임
     */

    private Long manageSeq;

    private String loginId;

    private String password;

    private  String name;

    private String nickName;

    public Member(String loginId, String password, String name, String nickName) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickName = nickName;
    }
}
