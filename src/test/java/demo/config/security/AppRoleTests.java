package demo.config.security;

import static org.junit.Assert.assertEquals;

import static demo.config.security.AppRole.ADMIN;
import static demo.config.security.AppRole.NEW_USER;
import static demo.config.security.AppRole.USER;

import org.junit.Test;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author  Luke Taylor
 */
public class AppRoleTests {

    @Test
    public void getAuthorityReturnsRoleName() {
        GrantedAuthority admin = ADMIN;

        assertEquals("ROLE_ADMIN", admin.getAuthority());
    }

    @Test
    public void bitsAreCorrect() throws Exception {

        // If this fails, someone has modified the Enum and the Datastore is probably
        // corrupt!
        assertEquals(0, ADMIN.getBit());
        assertEquals(1, NEW_USER.getBit());
        assertEquals(2, USER.getBit());
    }
}
