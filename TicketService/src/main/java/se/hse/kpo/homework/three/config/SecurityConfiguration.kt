package se.hse.kpo.homework.three.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestTemplate

@Configuration
@EnableWebSecurity
open class SecurityConfiguration() {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .cors { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/order/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }

        return http.build()
    }

    @Bean
    open fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}