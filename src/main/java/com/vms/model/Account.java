package com.vms.model;

import com.vms.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="account")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="account_type", nullable = false)
    private AccountType accountType;

    @OneToMany(mappedBy = "account")
    private List<Token> tokens;

    // TO REMOVE
    @ManyToMany(mappedBy = "authorizedAccounts")
    private List<Form> authorizedForms;

    // TO REMOVE
    @ManyToMany(mappedBy = "authorizedAccounts")
    private List<FormSection> authorizedFormSections;

    // TO REMOVE
    @OneToMany(mappedBy = "submitter")
    private List<FormSubmission> submittedFormSubmissions;

    @ManyToMany(mappedBy = "authorizedAccounts")
    private List<Workflow> authorizedWorkflows;

    @Override
    public String getUsername() {
        return email;
    }

    // UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority( accountType.name()));
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