package zoom.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import zoom.meeting.config.DBConfig.JdbcTemplateConfig;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

@Import(JdbcTemplateConfig.class)
@SpringBootApplication(scanBasePackages = "zoom.meeting.web")
public class MeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }

    @Bean
    @Profile("memory")
    public InitData testDataInit(MemberRepository memberRepository, NoteRepository noteRepository, MessageRepository messageRepository) {
        return new InitData(memberRepository,noteRepository,messageRepository);
    }
}
