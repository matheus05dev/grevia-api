package com.projeto1cc.grevia.user.model;

import com.projeto1cc.grevia.user.model.enums.Role;
import com.projeto1cc.grevia.user.model.enums.Status;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldReturnAuthoritiesBasedOnRole() {
        User user = new User();
        user.setRole(Role.ADMIN);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void shouldReturnUsernameAsEmail() {
        User user = new User();
        user.setEmail("test@test.com");

        assertEquals("test@test.com", user.getUsername());
    }

    @Test
    void shouldBeEnabledWhenStatusIsActive() {
        User user = new User();
        user.setStatus(Status.Active);

        assertTrue(user.isEnabled());
    }

    @Test
    void shouldNotBeEnabledWhenStatusIsInactive() {
        User user = new User();
        user.setStatus(Status.Inactive);

        assertFalse(user.isEnabled());
    }

    @Test
    void accountStatusMethodsShouldAlwaysReturnTrue() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }
}
