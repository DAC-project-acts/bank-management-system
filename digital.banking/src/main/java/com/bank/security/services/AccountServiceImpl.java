package com.bank.security.services;

import com.bank.security.entities.AppRole;
import com.bank.security.entities.AppUser;
import com.bank.security.repositories.AppRoleRepository;
import com.bank.security.repositories.AppUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserRepository.save(appUser);
    }
    @Override
    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole =  appRoleRepository.findAppRoleByRoleName(rolename);
        appUser.getRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> listUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser update(AppUser appUser) {
    	   AppUser au = appUserRepository.findByUsername(appUser.getUsername());
        String pw = appUser.getPassword();
        System.out.println("sham ");
        System.out.println(au+" "+appUser);
        appUser.setUsername(appUser.getPassword());
        appUser.setPassword(passwordEncoder.encode(pw));
        appUser.setId(au.getId());
        return appUserRepository.save(appUser);
    }
}
