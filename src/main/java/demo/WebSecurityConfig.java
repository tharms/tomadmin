package demo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import demo.config.security.GaeAuthenticationFilter;
import demo.config.security.GoogleAccountsAuthenticationProvider;

import demo.user.GaeDatastoreUserRegistry;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/theme/**").and().debug(true);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

        String loginUrl = "/login";
        UserService userService = UserServiceFactory.getUserService();

        try {
            loginUrl = userService.createLoginURL("/tomadmin");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        http.authorizeRequests().antMatchers("/", "/info", "/version", "/_ah/warmup").permitAll().anyRequest()
            .authenticated().and().formLogin().loginPage(loginUrl).permitAll().and().logout().permitAll().and()
            .addFilterAfter(gaeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public GaeAuthenticationFilter gaeAuthenticationFilter() throws Exception {
        GaeAuthenticationFilter gaeAuthenticationFilter = new GaeAuthenticationFilter();
        gaeAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return gaeAuthenticationFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        List<AuthenticationProvider> authenticationProviderList = new ArrayList<AuthenticationProvider>();
        authenticationProviderList.add(customAuthenticationProvider());

        AuthenticationManager authenticationManager = new ProviderManager(authenticationProviderList);
        return authenticationManager;
    }

    @Bean
    public GoogleAccountsAuthenticationProvider customAuthenticationProvider() {
        GoogleAccountsAuthenticationProvider customAuthenticationProvider = new GoogleAccountsAuthenticationProvider();
        customAuthenticationProvider.setUserRegistry(new GaeDatastoreUserRegistry());
        return customAuthenticationProvider;
    }

}

/*
 * @Configuration
 * @EnableWebMvcSecurity
 * public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 *  @Override
 *  protected void configure(final HttpSecurity http) throws Exception {
 *      http.authorizeRequests().antMatchers("/", "/info", "/version", "/_ah/warmup").permitAll().anyRequest()
 *          .authenticated().and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
 *  }
 *
 *  @Autowired
 *  public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
 *      auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
 *  }
 *
 * }
 */
