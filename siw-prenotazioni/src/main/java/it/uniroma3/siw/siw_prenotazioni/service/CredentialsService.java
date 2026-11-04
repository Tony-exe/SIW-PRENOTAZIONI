package it.uniroma3.siw.siw_prenotazioni.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_prenotazioni.exception.CredentialsNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.exception.EmailExistsException;
import it.uniroma3.siw.siw_prenotazioni.exception.UsernameExistsException;
import it.uniroma3.siw.siw_prenotazioni.model.Credentials;
import it.uniroma3.siw.siw_prenotazioni.model.Role;
import it.uniroma3.siw.siw_prenotazioni.model.User;
import it.uniroma3.siw.siw_prenotazioni.repository.CredentialsRepository;
import it.uniroma3.siw.siw_prenotazioni.repository.UserRepository;

@Service 
public class CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialsService(CredentialsRepository credentialsRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.credentialsRepository = credentialsRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional 
    public Credentials registra (User user, Credentials credentials){
        if(credentialsRepository.existsByUsername(credentials.getUsername())){
           throw(new UsernameExistsException(credentials.getUsername()));
        }
        if(userRepository.existsByEmail(user.getEmail())){
            throw(new EmailExistsException(user.getEmail()));
        }

        credentials.setUser(user);
        credentials.setRuolo(Role.USER);
        credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
        credentialsRepository.save(credentials);

        return credentials;
    }

    @Transactional (readOnly = true)
    public Credentials getCredentials(String username){
        return credentialsRepository.findByUsername(username).orElseThrow(() -> new CredentialsNotFoundException(username));
    }
}
