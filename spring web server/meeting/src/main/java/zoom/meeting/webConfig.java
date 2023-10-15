package zoom.meeting;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import zoom.meeting.config.interceptor.LoginCheckInterceptor;

@Configuration
@RequiredArgsConstructor
public class webConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/signUp",
                        "/login",
                        "/logout",
                        "/css/**",
                        "/images/**",
                        "/js/**",
                        "/*.ico",
                        "/error",
                        "/saveData",
                        "/webfonts/**");
    }
}
