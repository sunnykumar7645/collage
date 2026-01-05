package com.jwt.jwtsecuritydemo.service;

import com.jwt.jwtsecuritydemo.DTO.SignUpRequestDTO;
import com.jwt.jwtsecuritydemo.model.type.AuthProviderType;
import com.jwt.jwtsecuritydemo.model.type.RoleType;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.jwt.jwtsecuritydemo.DTO.LoginRequestDTO;
import com.jwt.jwtsecuritydemo.DTO.LoginResponseDTO;
import com.jwt.jwtsecuritydemo.DTO.SignUpResponseDTO;
import com.jwt.jwtsecuritydemo.model.User;
import com.jwt.jwtsecuritydemo.repository.UserRepository;
import com.jwt.jwtsecuritydemo.util.AuthUtil;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        
        Authentication authentication = authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(),
                loginRequestDTO.getPassword() 
            )
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateToken(user);
        return new LoginResponseDTO(token, user.getId());
        
    }
    public User signupHelper(SignUpRequestDTO signupRequestDTO, AuthProviderType authProviderType, String providerId){
        if(userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user =  User.builder()
                .username(signupRequestDTO.getUsername())
                .providerType(authProviderType)
                .providerId(providerId)
//                .roles(Set.of(RoleType.USER))  // we are using it in another way which is not recommanded in production.
                .roles(signupRequestDTO.getRoles())
                .build();
        if (authProviderType == AuthProviderType.EMAIL){
            user.setPassword(passwordEncoder.encode(signupRequestDTO.getPassword()));
        }
//        basic user can create here and intanciated here.

        return userRepository.save(user);
    }

//    login controller
    public SignUpResponseDTO signup(SignUpRequestDTO signupRequestDTO) {
        User user = signupHelper(signupRequestDTO, AuthProviderType.EMAIL, null);
        return new SignUpResponseDTO(user.getId(), user.getUsername(), "New User created successfully!");
    }

    @Transactional
    public ResponseEntity<LoginResponseDTO> handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationId(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);
        User user = userRepository.findByProviderIdAndProviderType(providerId, providerType).orElse(null);
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        User emailUser = userRepository.findByUsername(email).orElse(null);
        if (user == null || emailUser == null){
//            signup flow
            String username = authUtil.determineUsernameFromOAuth2User(oAuth2User, registrationId, providerId);
            user = signupHelper(new SignUpRequestDTO(username, null,name, Set.of(RoleType.USER)), providerType, providerId);

        }
        else if(user != null){
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())){
                user.setUsername(email);
                userRepository.save(user);
            }
        }
        else{
            throw new BadCredentialsException("This email is already register with providerId " + emailUser.getProviderType());
        }
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(authUtil.generateToken(user), user.getId());
        return ResponseEntity.ok(loginResponseDTO);
    }
}
