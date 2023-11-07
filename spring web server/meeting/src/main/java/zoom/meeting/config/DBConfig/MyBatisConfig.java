package zoom.meeting.config.DBConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zoom.meeting.domain.repositoryImpl.myBatis.member.MemberRepositoryMapper;
import zoom.meeting.domain.repositoryImpl.myBatis.member.MyBatisMemberRepository;
import zoom.meeting.domain.repositoryImpl.myBatis.message.MessageRepositoryMapper;
import zoom.meeting.domain.repositoryImpl.myBatis.message.MyBatisMessageRepository;
import zoom.meeting.domain.repositoryImpl.namedParameterJdbcTemplate.NamedParameterNoteRepository;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;
import zoom.meeting.service.documentService.DocumentService;
import zoom.meeting.service.documentService.implement.DocumentServiceImplementV1;
import zoom.meeting.service.login.Implement.LoginServiceImplementV1;
import zoom.meeting.service.login.LoginService;
import zoom.meeting.service.message.MessageService;
import zoom.meeting.service.message.implement.MessageServiceImplementV1;
import zoom.meeting.service.signUp.SignUpService;
import zoom.meeting.service.signUp.implement.SignUpServiceImplementV1;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final DataSource dataSource;
    private final MemberRepositoryMapper memberRepositoryMapper;
    private final MessageRepositoryMapper messageRepositoryMapper;

    // repository config

    @Bean
    MemberRepository memberRepository() {
        return new MyBatisMemberRepository(memberRepositoryMapper);
    }

    @Bean
    MessageRepository messageRepository() {
        return new MyBatisMessageRepository(messageRepositoryMapper);
    }

    @Bean
    NoteRepository noteRepository() {
        return new NamedParameterNoteRepository(dataSource);
    }


    // service config
    @Bean
    LoginService loginService() {
        return new LoginServiceImplementV1(memberRepository());
    }

    @Bean
    SignUpService signUpService() {
        return new SignUpServiceImplementV1(memberRepository());
    }

    @Bean
    DocumentService documentService() {
        return new DocumentServiceImplementV1(noteRepository());
    }

    @Bean
    MessageService messageService() {
        return new MessageServiceImplementV1(messageRepository(), memberRepository());
    }
}
