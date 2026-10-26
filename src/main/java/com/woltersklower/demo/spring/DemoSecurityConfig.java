package com.woltersklower.demo.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    private static final String LDAP_ROOT = "dc=springframework,dc=org";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/error", "/favicon.ico").permitAll()
                .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/user/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .permitAll());
        return http.build();
    }

    /**
     * Embedded LDAP server (UnboundID) loaded from users.ldif.
     * Port 0 picks a free port; the context source is wired to it automatically.
     */
    @Bean
    EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
        EmbeddedLdapServerContextSourceFactoryBean factory =
                EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
        factory.setRoot(LDAP_ROOT);
        factory.setLdif("classpath:users.ldif");
        factory.setPort(0);
        return factory;
    }

    @Bean
    AuthenticationManager authenticationManager(BaseLdapPathContextSource contextSource) {
        DefaultLdapAuthoritiesPopulator authorities =
                new DefaultLdapAuthoritiesPopulator(contextSource, "ou=roles");
        authorities.setGroupSearchFilter("member={0}");

        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserSearchBase("ou=people");
        factory.setUserSearchFilter("(uid={0})");
        factory.setLdapAuthoritiesPopulator(authorities);
        return factory.createAuthenticationManager();
    }

}
