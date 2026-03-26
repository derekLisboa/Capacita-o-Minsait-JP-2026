package br.com.indra.derek_lisboa.user.service;


import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.user.model.User;
import br.com.indra.derek_lisboa.user.dto.UserDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import br.com.indra.derek_lisboa.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new InvalidUserException("O email é obrigatorio");
        }

        if (user.getRole() == null) {
            throw new InvalidUserException("O cargo é obrigatorio");
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidUserException("Usuario nao encontrado"));
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getRole()
        );
    }
}