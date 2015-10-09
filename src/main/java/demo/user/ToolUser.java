package demo.user;

import java.io.Serializable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import demo.config.security.AppRole;

/**
 * Custom user object for the application.
 *
 * @author  Luke Taylor
 */
public class ToolUser implements Serializable {
    private final String userId;
    private final String email;
    private final String nickname;
    private final String forename;
    private final String surname;
    private final Set<AppRole> authorities;
    private final boolean enabled;

    /**
     * Pre-registration constructor. Assigns the user the "NEW_USER" role only.
     */
    public ToolUser(final String userId, final String nickname, final String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.authorities = EnumSet.of(AppRole.USER);
        this.forename = null;
        this.surname = null;
        this.email = email;
        this.enabled = true;
    }

    /**
     * Post-registration constructor.
     */
    public ToolUser(final String userId, final String nickname, final String email, final String forename,
            final String surname, final Set<AppRole> authorities, final boolean enabled) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.authorities = authorities;
        this.forename = forename;
        this.surname = surname;
        this.enabled = enabled;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getForename() {
        return forename;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Collection<AppRole> getAuthorities() {
        return authorities;
    }

    @Override
    public String toString() {
        return "GaeUser{" + "userId='" + userId + '\'' + ", nickname='" + nickname + '\'' + ", forename='" + forename
                + '\'' + ", surname='" + surname + '\'' + ", authorities=" + authorities + '}';
    }
}
