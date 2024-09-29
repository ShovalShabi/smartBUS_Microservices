package club.smartbus.service;

import club.smartbus.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<UserDTO> registerUser(String company, UserDTO userDTO);

    Mono<UserDTO> userLogin (String company, String email, String password);
}
