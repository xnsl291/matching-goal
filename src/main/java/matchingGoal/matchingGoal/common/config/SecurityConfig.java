package matchingGoal.matchingGoal.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "api/auth/sign-in").permitAll()
                                .requestMatchers( "api/**").permitAll()  /// todo: need to be specified!
                                .anyRequest().authenticated()
                )

                /**
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/api/auth/sign-in")
                                .usernameParameter("username")
                                .passwordParameter("password")
                                .loginProcessingUrl("/api/auth/sign-in")
                                //.defaultSuccessUrl("/", true)
                )
                 **/
                ;

        return http.build();
    }
}
