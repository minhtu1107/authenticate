package com.taira.cntl.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {
	
	@Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		
		@Autowired
		private CustomLogoutSuccessHandler customLogoutSuccessHandler;

		@Override
        public void configure(HttpSecurity http) throws Exception {

            http
                .exceptionHandling()
//                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .logout()
                .logoutUrl("/oauth/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .and()
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
                .headers()
                .frameOptions().disable().and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/secure/**").authenticated();
        }
		
	}
	
	@Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;
		
//		@Autowired
//	    private DataSource dataSource;
		
		@Bean
		public JwtAccessTokenConverter accessTokenConverter() {
	        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	        converter.setSigningKey("private");
//	        converter.setVerifierKey("public");
	        return converter;
		}
	
	    @Bean
	    public JwtTokenStore tokenStore() {
	    	return new JwtTokenStore(accessTokenConverter());
//	        return new JdbcTokenStore(dataSource);
	//    	return new InMemoryTokenStore();
	    }
	
	    @Bean
	    public TokenEnhancer tokenEnhancer() {
	        return new CustomTokenEnhancer();
	    }
	    
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			
			TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		    tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		    
			endpoints
				.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancerChain)
//				.tokenEnhancer(tokenEnhancer())
//				.accessTokenConverter(accessTokenConverter())
				.authenticationManager(authenticationManager);;
		}
		
		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
			security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()").passwordEncoder(passwordEncoder())
				.allowFormAuthenticationForClients();
		}
		
		 @Value("${authentication.oauth.clientid}")
		 private String clientid;
		
		 @Value("${authentication.oauth.secret}")
		 private String clientSecret;
		
		 @Value("${authentication.oauth.tokenValidityInSeconds}")
		 private int tokenValidityInSeconds;
		
		 @Override
		 public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			 clients
				.inMemory()
				.withClient(clientid)
				.scopes("read", "write")
				.authorities("ROLE_USER")
				.authorizedGrantTypes("password", "refresh_token")
				.secret(passwordEncoder().encode(clientSecret))
				.accessTokenValiditySeconds(tokenValidityInSeconds);
		 }
		 
		@Bean
		public PasswordEncoder passwordEncoder(){
			return new BCryptPasswordEncoder(4);
		}
	}
}
