package demo.config.security;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.appengine.api.users.User;

import demo.user.ToolUser;

/**
 * A simple authentication provider which interacts with {@code User} returned by the GAE {@code UserService}, and also
 * the local persistent {@code UserRegistry} to build an application user principal.
 *
 * <p>If the user has been authenticated through google accounts, it will check if they are already registered and
 * either load the existing user information or assign them a temporary identity with limited access until they have
 * registered.
 *
 * <p>If the account has been disabled, a {@code DisabledException} will be raised.
 *
 * @author  Luke Taylor
 */
public class GoogleAccountsAuthenticationProvider implements AuthenticationProvider, MessageSourceAware {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        User googleUser = (User) authentication.getPrincipal();

        ToolUser user = new ToolUser(googleUser.getUserId(), googleUser.getNickname(), googleUser.getEmail());

        if (!user.isEnabled()) {
            throw new DisabledException("Account is disabled");
        }

        return new ToolUserAuthentication(user, authentication.getDetails());
    }

    /**
     * Indicate that this provider only supports PreAuthenticatedAuthenticationToken (sub)classes.
     */
    public final boolean supports(final Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setMessageSource(final MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
