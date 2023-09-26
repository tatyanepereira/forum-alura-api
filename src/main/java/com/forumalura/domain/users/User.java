package com.forumalura.domain.users;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "userId")
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 170)
    private String userName;
    @Column(nullable = false, length = 70,unique = true)
    private String email;
    @Column(nullable = false, length = 70)
    private String UserPassword;
    @Column(nullable = false, length = 10)
    private String roles;
    @Column(nullable = false)
    private boolean activeDate = true;
    public User (UserCreateDTO dto){
        this.userName = dto.name();
        this.email = dto.email();
        this.UserPassword = dto.password();
        this.roles = "ADMIN";
    }
    public User (UserUpdateDTO dto){
        this.userId = dto.id();
        this.userName = dto.name();
        this.email = dto.email();
        this.UserPassword = dto.password();
        this.roles = "ADMIN";
    }
    public UserDataResponse getDataResponse(){
        return new UserDataResponse(getUserId(),getUsername(),getEmail(),getUserPassword(),this.activeDate);
    }
    public void update(UserUpdateDTO dto){
        this.userName = dto.name();
        this.email = dto.email();
        this.UserPassword = dto.password();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (this.getRoles().compareToIgnoreCase("ADMIN")==0)
                ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"))
                : List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.UserPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
