package zoom.meeting.config.DBConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zoom.meeting.domain.repositoryImpl.jdbcTemplate.JdbcTemplateMemberRepository;
import zoom.meeting.domain.repositoryImpl.jdbcTemplate.JdbcTemplateMessageRepository;
import zoom.meeting.domain.repositoryImpl.jdbcTemplate.JdbcTemplateNoteRepository;
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
public class JdbcTemplateConfig {

    // DB config
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }

    // repository config
    @Bean
    MemberRepository memberRepository() {
        return new JdbcTemplateMemberRepository(dataSource());
    }

    @Bean
    MessageRepository messageRepository() {
        return new JdbcTemplateMessageRepository(dataSource());
    }

    @Bean
    NoteRepository noteRepository() {
        return new JdbcTemplateNoteRepository(dataSource());
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
