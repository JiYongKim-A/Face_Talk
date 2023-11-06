package zoom.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import zoom.meeting.config.DBConfig.JdbcTemplateConfig;

@Import(JdbcTemplateConfig.class)
@SpringBootApplication(scanBasePackages = "zoom.meeting.web")
public class MeetingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }
}
