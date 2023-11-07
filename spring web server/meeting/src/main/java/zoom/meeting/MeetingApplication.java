package zoom.meeting;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import zoom.meeting.config.DBConfig.MyBatisConfig;
import zoom.meeting.domain.repositoryInterface.MemberRepository;
import zoom.meeting.domain.repositoryInterface.MessageRepository;
import zoom.meeting.domain.repositoryInterface.NoteRepository;

import javax.sql.DataSource;

@Slf4j
@Import(MyBatisConfig.class)
@SpringBootApplication(scanBasePackages = "zoom.meeting.web")
public class MeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }

    @Bean
    @Profile("memory")
    public InitData testDataInit(MemberRepository memberRepository, NoteRepository noteRepository, MessageRepository messageRepository) {
        return new InitData(memberRepository, noteRepository, messageRepository);
    }

    @Bean
    @Profile("local")
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Profile("local")
    DataSource dataSource() {
        return new HikariDataSource(hikariConfig());
    }
}
