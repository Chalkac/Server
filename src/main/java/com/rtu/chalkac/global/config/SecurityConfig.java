package com.rtu.chalkac.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.util.stream.Collectors;

@Slf4j
@Configuration
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/v1/category/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/comment/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/reply/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/video/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/v1/users/**").hasAnyRole("USER", "ADMIN")
						.anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                ); // JWT 인증 활성화
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(jwtAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtDecoder());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String issuer = "https://cognito-idp.ap-northeast-2.amazonaws.com/ap-northeast-2_AbvZMxTi4";
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuer));
        return jwtDecoder;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // "cognito:groups" 클레임을 읽어서 Spring Security 권한으로 변환
            return jwt.getClaimAsStringList("cognito:groups").stream()
                    .map(group -> (GrantedAuthority) group::toUpperCase)
                    .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() { // 추후 CORS 수정 필요
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000/");
        config.addAllowedOrigin("https://chalkacserver.site");
        config.addAllowedOrigin("http://chalkacserver.site");
        config.addAllowedOrigin("https://chalkac.site");
        config.addAllowedOrigin("http://chalkac.site");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
