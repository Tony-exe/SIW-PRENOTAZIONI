package it.uniroma3.siw.siw_prenotazioni.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.siw_prenotazioni.exception.UserNotFoundException;
import it.uniroma3.siw.siw_prenotazioni.model.User;
import it.uniroma3.siw.siw_prenotazioni.repository.UserRepository;

@Service 
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional (readOnly = true)
    public User getUser(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }
}
