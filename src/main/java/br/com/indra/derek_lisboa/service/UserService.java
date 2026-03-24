package br.com.indra.derek_lisboa.service;


import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.service.dto.UserDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import br.com.indra.derek_lisboa.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


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

    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getRole()
        );
    }
}