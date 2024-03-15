package matchingGoal.matchingGoal.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors().configurationSource(source).and()
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/**", "/api/auth/sign-in").permitAll()
                .requestMatchers("/api/**").permitAll())
            .build();
//            .cors(AbstractHttpConfigurer::disable)
//
//  .formLogin((formLogin) ->
//          formLogin
//                  .loginPage("/api/auth/sign-in")
//                  .usernameParameter("username")
//                   .passwordParameter("password")
//                   .loginProcessingUrl("/api/auth/sign-in")
//                  .defaultSuccessUrl("/", true)
//  )



    }



}
