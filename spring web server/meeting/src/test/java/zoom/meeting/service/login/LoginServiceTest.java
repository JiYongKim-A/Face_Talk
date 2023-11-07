package zoom.meeting.service.login;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryInterface.MemberRepository;

@Slf4j
@SpringBootTest
public class LoginServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoginService loginService;


    @Test
    @DisplayName("LoginServiceImplementV1 Test")
    public void loginServiceTest() {
        //given
        Member newMem = memberRepository.save(new Member("T1", "T1", "T1", "T1"));
        //when
        Member loginMem = loginService.login(newMem.getLoginId(), newMem.getPassword());
        //then
        Assertions.assertThat(newMem).isEqualTo(loginMem);

    }

    @AfterEach
    public void afterEach() {
        memberRepository.removeByLoginId("T1");
    }


}
