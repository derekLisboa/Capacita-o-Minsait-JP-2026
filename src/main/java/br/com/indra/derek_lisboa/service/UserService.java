package br.com.indra.derek_lisboa.service;


import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.model.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import br.com.indra.derek_lisboa.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));
    }
}