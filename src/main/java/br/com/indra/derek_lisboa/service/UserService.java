package br.com.indra.derek_lisboa.service;


import br.com.indra.derek_lisboa.exception.InvalidUserException;
import br.com.indra.derek_lisboa.model.User;
import br.com.indra.derek_lisboa.dto.UserDTO;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import br.com.indra.derek_lisboa.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
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