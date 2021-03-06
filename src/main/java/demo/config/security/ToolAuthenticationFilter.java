package demo.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import org.springframework.util.Assert;

import org.springframework.web.filter.GenericFilterBean;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

import demo.user.ToolUser;

/**
 * @author  Tobias Harms
 */
public class ToolAuthenticationFilter extends GenericFilterBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> ads =
        new WebAuthenticationDetailsSource();
    private AuthenticationManager authenticationManager;
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User googleUser = null;
        try {
            googleUser = UserServiceFactory.getUserService().getCurrentUser();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        if (authentication != null && !loggedInUserMatchesGaeUser(authentication, googleUser)) {
            SecurityContextHolder.clearContext();
            authentication = null;
            ((HttpServletRequest) request).getSession().invalidate();
        }

        if (authentication == null) {
            if (googleUser != null) {
                logger.debug("Currently logged on to GAE as user " + googleUser);
                logger.debug("Authenticating to Spring Security");

                // User has returned after authenticating via GAE. Need to authenticate
                // through Spring Security.
                PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(googleUser, null);
                token.setDetails(ads.buildDetails((HttpServletRequest) request));

                try {
                    authentication = authenticationManager.authenticate(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (AuthenticationException e) {
                    failureHandler.onAuthenticationFailure((HttpServletRequest) request, (HttpServletResponse)
                        response, e);

                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean loggedInUserMatchesGaeUser(final Authentication authentication, final User googleUser) {
        assert authentication != null;

        if (googleUser == null) {

            // User has logged out of GAE but is still logged into application
            return false;
        }

        ToolUser toolUser = (ToolUser) authentication.getPrincipal();

        if (!toolUser.getEmail().equals(googleUser.getEmail())) {
            return false;
        }

        return true;

    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(authenticationManager, "AuthenticationManager must be set");
    }

    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setFailureHandler(final AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }
}
