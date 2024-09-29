package club.smartbus.controller;

import club.smartbus.dto.UserDTO;
import club.smartbus.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private final AuthService authService;

    @PostMapping(
            path = {"/register/{company}"},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> registerUser(@PathVariable String company,
                                      @RequestBody UserDTO userDTO) {
        log.info("Registering user for company: {}, with data: {}", company, userDTO);
        return authService.registerUser(company, userDTO);
    }

    @PostMapping(
            path = {"/auth/{company}"},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> userLogin(@PathVariable String company,
                                   @RequestParam String email,
                                   @RequestParam String password) {
        return authService.userLogin(company, email, password);
    }

}