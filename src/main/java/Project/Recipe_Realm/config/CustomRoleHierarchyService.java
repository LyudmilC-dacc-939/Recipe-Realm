package Project.Recipe_Realm.config;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomRoleHierarchyService implements RoleHierarchy {
    @Override
    public Collection<? extends GrantedAuthority> getReachableGrantedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        List<GrantedAuthority> reachableAuthorities = new ArrayList<>(authorities);

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_MODERATOR"));
            reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MODERATOR"))) {
            reachableAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return reachableAuthorities;
    }
}
