package zoom.meeting.service.signUp;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import zoom.meeting.domain.member.Member;
import zoom.meeting.domain.repositoryImpl.JdbcTemplateMemberRepository;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.service.signUp.implement.SignUpServiceImplementV1;

import javax.sql.DataSource;

import static zoom.meeting.ConnectionConstForTest.*;

@Slf4j
@SpringBootTest
public class SignUpServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SignUpService signUpService;

    @TestConfiguration
    static class testConfig {

        @Bean
        DataSource dataSource() {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(URL);
            dataSource.setUsername(USERNAME);
            dataSource.setPassword(PASSWORD);
            dataSource.setPoolName("myPool");
            dataSource.setMaximumPoolSize(10);
            return dataSource;
        }

        @Bean
        JdbcTemplateMemberRepository memberRepository() {
            return new JdbcTemplateMemberRepository(dataSource());
        }


        @Bean
        SignUpService signUpService() {
            return new SignUpServiceImplementV1(memberRepository());
        }
    }

    @Test
    @DisplayName("SignUpServiceImplementV1 Test")
    public void signUpServiceTest() {
        //given
        Member newMember = signUpService.memberSignUp(new Member("T1", "T1", "T1", "T1"));
        //when
        Member findMember = memberRepository.findByLoginId("T1").get();
        //then
        Assertions.assertThat(newMember).isEqualTo(findMember);

    }

    @AfterEach
    public void afterEach() {
        memberRepository.removeByLoginId("T1");
    }


}
