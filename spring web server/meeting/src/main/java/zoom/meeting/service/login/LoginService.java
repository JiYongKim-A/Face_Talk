package zoom.meeting.service.login;

import zoom.meeting.domain.member.Member;

public interface LoginService {
    Member login(String loginId,String password);
}
