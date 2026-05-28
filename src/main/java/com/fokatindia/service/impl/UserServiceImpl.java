package com.fokatindia.service.impl;

import com.fokatindia.dto.ForgetPasswordResponse;
import com.fokatindia.dto.LoginRequest;
import com.fokatindia.dto.RegisterRequest;
import com.fokatindia.dto.UserResponse;
import com.fokatindia.entity.Token;
import com.fokatindia.entity.User;
import com.fokatindia.entity.UserRole;
import com.fokatindia.repository.RoleRepository;
import com.fokatindia.repository.TokenRepository;
import com.fokatindia.repository.UserRepository;
import com.fokatindia.repository.UserRoleRepository;
import com.fokatindia.security.JwtTokenProvider;
import com.fokatindia.service.UserService;
import com.fokatindia.service.vendor.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();




    private final DocumentService documentService;

    // ================= USER REGISTER =================

    @Override
    public Mono<UserResponse> registerUser(RegisterRequest request) {

        return createUserWithRole(request, "USER");
    }

    // ================= VENDOR REGISTER =================

    @Override
    public Mono<UserResponse> registerVendor(RegisterRequest request) {

        return createUserWithRole(request, "VENDOR");
    }

    // ================= SUBVENDOR REGISTER =================

    @Override
    public Mono<UserResponse> registerSubVendor(RegisterRequest request) {

        return createUserWithRole(request, "SUBVENDOR");
    }

    // ================= COMMON CORE LOGIC =================

    private Mono<UserResponse> createUserWithRole(
            RegisterRequest request,
            String roleName
    ) {

        return userRepository.findByEmail(request.getEmail())

                .flatMap(existing ->
                        Mono.<UserResponse>error(
                                new RuntimeException("Email already exists")
                        )
                )

                .switchIfEmpty(

                        roleRepository.findByName(roleName)

                                .switchIfEmpty(
                                        Mono.error(
                                                new RuntimeException("Role not found")
                                        )
                                )

                                .flatMap(role -> {

                                    User user = mapToEntity(request);

                                    // ================= VENDOR =================

                                    if ("VENDOR".equals(roleName)) {

                                        user.setInvitationCode(
                                                generateInvitationCode()
                                        );
                                    }

                                    // ================= SUBVENDOR =================

                                    if ("SUBVENDOR".equals(roleName)) {

                                        return userRepository

                                                .findByInvitationCode(
                                                        request.getInvitationCode()
                                                )

                                                .switchIfEmpty(
                                                        Mono.error(
                                                                new RuntimeException(
                                                                        "Invalid invitation code"
                                                                )
                                                        )
                                                )

                                                .flatMap(vendor ->
                                                        saveUserRole(user, role.getRoleId())
                                                                .map(savedUser -> {

                                                                    String token =
                                                                            jwtTokenProvider.generateToken(
                                                                                    savedUser.getUserId(),
                                                                                    savedUser.getPhone(),
                                                                                    roleName
                                                                            );

                                                                    return mapToResponse(savedUser, token);
                                                                })
                                                );
                                    }

                                    return saveUserRole(user, role.getRoleId())
                                            .map(savedUser -> {

                                                String token =
                                                        jwtTokenProvider.generateToken(
                                                                savedUser.getUserId(),
                                                                savedUser.getPhone(),
                                                                roleName
                                                        );

                                                return mapToResponse(savedUser, token);
                                            });
                                })
                );
    }

    // ================= SAVE USER ROLE =================

    private Mono<User> saveUserRole(User user, Long roleId) {

        return userRepository.save(user)

                .flatMap(savedUser -> {

                    UserRole ur = new UserRole();

                    ur.setUserId(savedUser.getUserId());

                    ur.setRoleId(roleId);

                    return userRoleRepository.save(ur)

                            .thenReturn(savedUser);
                });
    }

    // ================= INVITATION CODE =================

    private String generateInvitationCode() {

        return "VEN-" + UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();
    }

    // ================= LOGIN =================

    @Override
    public Mono<UserResponse> login(LoginRequest request) {

        return userRepository.findByEmail(request.getEmail())

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .flatMap(user -> {

                    // CHECK STATUS

                    if ("DEACTIVE".equalsIgnoreCase(user.getStatus())) {

                        return Mono.error(
                                new RuntimeException(
                                        "Your account is deactivated. Contact support."
                                )
                        );
                    }

                    // PASSWORD CHECK

                    if (!passwordEncoder.matches(
                            request.getPassword(),
                            user.getPassword()
                    )) {

                        return Mono.error(
                                new RuntimeException("Invalid credentials")
                        );
                    }

                    // FETCH ROLE

                    return roleRepository.findRoleNameByUserId(user.getUserId())

                            .defaultIfEmpty("USER")

                            .flatMap(role ->

                                    // DIRECT SERVICE CALL
                                    documentService
                                            .getDocumentStatus(user.getUserId())

                                            .defaultIfEmpty("PENDING")

                                            .flatMap(documentStatus -> {

                                                // GENERATE JWT

                                                String jwtToken =
                                                        jwtTokenProvider.generateToken(
                                                                user.getUserId(),
                                                                user.getPhone(),
                                                                role
                                                        );

                                                // SAVE OR UPDATE TOKEN

                                                return tokenRepository.findByUserId(user.getUserId())

                                                        .flatMap(existingToken -> {

                                                            existingToken.setJwtToken(jwtToken);

                                                            existingToken.setFcmToken(
                                                                    request.getFcmToken()
                                                            );

                                                            return tokenRepository.save(existingToken);
                                                        })

                                                        .switchIfEmpty(

                                                                tokenRepository.save(
                                                                        createToken(
                                                                                user.getUserId(),
                                                                                jwtToken,
                                                                                request.getFcmToken()
                                                                        )
                                                                )
                                                        )

                                                        .flatMap(savedToken -> {

                                                            UserResponse response =
                                                                    mapToResponse(user, jwtToken);

                                                            response.setDocumentStatus(
                                                                    documentStatus
                                                            );

                                                            response.setInvitationCode(
                                                                    user.getInvitationCode()
                                                            );

                                                            return Mono.just(response);
                                                        });
                                            })
                            );
                });
    }

    // ================= CREATE TOKEN =================

    private Token createToken(
            Long userId,
            String jwtToken,
            String fcmToken
    ) {

        Token token = new Token();

        token.setUserId(userId);

        token.setJwtToken(jwtToken);

        token.setFcmToken(fcmToken);

        return token;
    }

    // ================= FORGOT PASSWORD =================

    @Override
    public Mono<ForgetPasswordResponse> forgotPassword(String email) {

        return userRepository.findByEmail(email)

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .flatMap(user -> {

                    if ("DEACTIVE".equalsIgnoreCase(user.getStatus())) {

                        return Mono.error(
                                new RuntimeException(
                                        "Your account is deactivated."
                                )
                        );
                    }

                    ForgetPasswordResponse response =
                            new ForgetPasswordResponse(
                                    user.getEmail(),
                                    "Reset link sent successfully"
                            );

                    return Mono.just(response);
                });
    }

    // ================= GET PROFILE =================

    @Override
    public Mono<UserResponse> getProfile(Long userId) {

        return userRepository.findById(userId)

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .map(user -> mapToResponse(user, null));
    }

    // ================= GET ALL USERS =================

    @Override
    public Flux<UserResponse> getAllUsers() {

        return userRepository.findAll()

                .map(user -> mapToResponse(user, null));
    }

    // ================= UPDATE PROFILE =================

    @Override
    public Mono<UserResponse> updateProfile(
            Long id,
            RegisterRequest request
    ) {

        return userRepository.findById(id)

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .flatMap(user -> {

                    if (request.getName() != null) {
                        user.setName(request.getName());
                    }

                    if (request.getEmail() != null) {
                        user.setEmail(request.getEmail());
                    }

                    if (request.getPhone() != null) {
                        user.setPhone(request.getPhone());
                    }

                    if (request.getPassword() != null &&
                            !request.getPassword().isBlank()) {

                        user.setPassword(
                                passwordEncoder.encode(request.getPassword())
                        );
                    }

                    if (request.getStatus() != null) {
                        user.setStatus(request.getStatus());
                    }

                    return userRepository.save(user);
                })

                .map(user -> mapToResponse(user, null));
    }

    // ================= DELETE USER =================

    @Override
    public Mono<Void> deleteUser(Long id) {

        return userRepository.findById(id)

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .flatMap(userRepository::delete);
    }

    // ================= DEACTIVATE USER =================

    @Override
    public Mono<UserResponse> deactivateUser(Long id) {

        return userRepository.findById(id)

                .switchIfEmpty(
                        Mono.error(new RuntimeException("User not found"))
                )

                .flatMap(user -> {

                    user.setStatus("DEACTIVE");

                    return userRepository.save(user);
                })

                .map(user -> mapToResponse(user, null));
    }

    // ================= ENTITY MAPPER =================

    private User mapToEntity(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPhone(request.getPhone());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setStatus("ACTIVE");

        user.setCreatedAt(LocalDateTime.now());

        return user;
    }

    // ================= RESPONSE MAPPER =================

    private UserResponse mapToResponse(
            User user,
            String token
    ) {

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                token,
                user.getStatus(),
                user.getDocumentStatus(),
                user.getInvitationCode()
        );
    }

    // ================= GET PERMISSIONS =================

    @Override
    public Mono<List<String>> getPermissions(Long userId) {

        return roleRepository.findPermissionsByUserId(userId)

                .collectList();
    }
}
//package com.fokatindia.service.impl;
//
//
//import com.fokatindia.dto.ForgetPasswordResponse;
//import com.fokatindia.dto.LoginRequest;
//import com.fokatindia.dto.RegisterRequest;
//import com.fokatindia.dto.UserResponse;
//import com.fokatindia.entity.Token;
//import com.fokatindia.entity.User;
//import com.fokatindia.entity.UserRole;
//import com.fokatindia.repository.RoleRepository;
//import com.fokatindia.repository.TokenRepository;
//import com.fokatindia.repository.UserRepository;
//import com.fokatindia.repository.UserRoleRepository;
//import com.fokatindia.security.JwtTokenProvider;
//import com.fokatindia.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService {
//    private final UserRoleRepository userRoleRepository;
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final WebClient webClient;
//    private final TokenRepository tokenRepository;
//    private final BCryptPasswordEncoder passwordEncoder =
//            new BCryptPasswordEncoder();
//
//
//
//
//    // ================= USER REGISTER =================
//    @Override
//    public Mono<UserResponse> registerUser(RegisterRequest request) {
//
//        return createUserWithRole(request, "USER");
//    }
//
//    // ================= VENDOR REGISTER =================
//    @Override
//    public Mono<UserResponse> registerVendor(RegisterRequest request) {
//
//        return createUserWithRole(request, "VENDOR");
//    }
//
//    // ================= SUBVENDOR REGISTER =================
//    @Override
//    public Mono<UserResponse> registerSubVendor(RegisterRequest request) {
//
//        return createUserWithRole(request, "SUBVENDOR");
//    }
//
//    // ================= COMMON CORE LOGIC =================
//    private Mono<UserResponse> createUserWithRole(
//            RegisterRequest request,
//            String roleName
//    ) {
//
//        return userRepository.findByEmail(request.getEmail())
//
//                .flatMap(existing ->
//                        Mono.<UserResponse>error(
//                                new RuntimeException("Email already exists")
//                        )
//                )
//
//                .switchIfEmpty(
//
//                        roleRepository.findByName(roleName)
//
//                                .switchIfEmpty(
//                                        Mono.error(
//                                                new RuntimeException("Role not found")
//                                        )
//                                )
//
//                                .flatMap(role -> {
//
//                                    User user = mapToEntity(request);
//
//                                    // ================= VENDOR =================
//                                    if ("VENDOR".equals(roleName)) {
//
//                                        user.setInvitationCode(
//                                                generateInvitationCode()
//                                        );
//                                    }
//
//                                    // ================= SUBVENDOR =================
//                                    if ("SUBVENDOR".equals(roleName)) {
//
//                                        return userRepository
//
//                                                .findByInvitationCode(
//                                                        request.getInvitationCode()
//                                                )
//
//                                                .switchIfEmpty(
//                                                        Mono.error(
//                                                                new RuntimeException(
//                                                                        "Invalid invitation code"
//                                                                )
//                                                        )
//                                                )
//
//                                                .flatMap(vendor ->
//                                                        saveUserRole(user, role.getRoleId())
//                                                                .map(savedUser ->{
//                                                                            String token =
//                                                                                    jwtTokenProvider.generateToken(
//                                                                                            user.getUserId(),
//                                                                                            user.getPhone(),
//                                                                                            roleName
//                                                                                    );
//
//                                                                   return   mapToResponse(savedUser, token);
//
//                                                                })
//
//                                                );
//                                    }
//
//                                    return saveUserRole(user, role.getRoleId())
//                                            .map(savedUser ->{
//                                                        String token =
//                                                                jwtTokenProvider.generateToken(
//                                                                        user.getUserId(),
//                                                                        user.getPhone(),
//                                                                        roleName
//                                                                );
//                                                        return  mapToResponse(savedUser, token);
//                                                    }
//
//                                            );
//                                })
//                );
//    }
//
//
//
//    private Mono<User> saveUserRole(User user, Long roleId) {
//
//        return userRepository.save(user)
//                .flatMap(savedUser -> {
//
//                    UserRole ur = new UserRole();
//                    ur.setUserId(savedUser.getUserId());
//                    ur.setRoleId(roleId);
//
//                    return userRoleRepository.save(ur)
//                            .thenReturn(savedUser);
//                });
//    }
//
//
//
//    // ================= INVITATION CODE =================
//    private String generateInvitationCode() {
//
//        return "VEN-" + UUID.randomUUID()
//                .toString()
//                .substring(0, 6)
//                .toUpperCase();
//    }
//
//    // ================= LOGIN =================
//
//    @Override
//    public Mono<UserResponse> login(LoginRequest request) {
//
//        return userRepository.findByEmail(request.getEmail())
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .flatMap(user -> {
//
//                    // CHECK STATUS
//                    if ("DEACTIVE".equalsIgnoreCase(user.getStatus())) {
//
//                        return Mono.error(
//                                new RuntimeException(
//                                        "Your account is deactivated. Contact support."
//                                )
//                        );
//                    }
//
//                    // PASSWORD CHECK
//                    if (!passwordEncoder.matches(
//                            request.getPassword(),
//                            user.getPassword()
//                    )) {
//
//                        return Mono.error(
//                                new RuntimeException("Invalid credentials")
//                        );
//                    }
//
//                    // FETCH ROLE
//                    return roleRepository.findRoleNameByUserId(user.getUserId())
//
//                            .defaultIfEmpty("USER")
//
//                            .flatMap(role -> {
//
//                                // CALL VENDOR SERVICE
//                                return webClient.get()
//
//                                        .uri(
//                                                "http://localhost:8001/restful/v1/api/documents/status/"
//                                                        + user.getUserId()
//                                        )
//
//                                        .retrieve()
//
//                                        .bodyToMono(String.class)
//
//                                        .defaultIfEmpty("PENDING")
//
//                                        .flatMap(documentStatus -> {
//
//                                            // GENERATE JWT
//                                            String jwtToken =
//                                                    jwtTokenProvider.generateToken(
//                                                            user.getUserId(),
//                                                            user.getPhone(),
//                                                            role
//                                                    );
//
//
//                                            // =================  TOKEN SAVE/UPDATE =================
//
//                                             return tokenRepository.findByUserId(user.getUserId())
//                                                     .flatMap(existingToken -> {
//                                                         // UPDATE EXISTING
//                                                         existingToken.setJwtToken(jwtToken);
//                                                         existingToken.setFcmToken(
//                                                                 request.getFcmToken()
//                                                         );
//
//                                                         return tokenRepository.save(existingToken);
//                                                     })
//                                                     .switchIfEmpty(
//                                                             // INSERT NEW
//                                                             tokenRepository.save(
//                                                                     createToken(
//                                                                             user.getUserId(),
//                                                                             jwtToken,
//                                                                             request.getFcmToken()
//                                                                     )
//                                                             )
//                                                     )
//                                                     .flatMap(savedToken ->{
//                                                         UserResponse response =
//                                                                 mapToResponse(user, jwtToken);
//
//                                                         response.setDocumentStatus(
//                                                                 documentStatus
//                                                         );
//
//                                                         response.setInvitationCode(
//                                                                 user.getInvitationCode()
//                                                         );
//
//                                                         return Mono.just(response);
//                                                     });
//
//                                        });
//                            });
//                });
//    }
//
//    private Token createToken(Long userId, String jwtToken, String fcmToken) {
//        Token token = new Token();
//
//        token.setUserId(userId);
//        token.setJwtToken(jwtToken);
//        token.setFcmToken(fcmToken);
//
//        return token;
//
//    }
//
//
//    // ================= FORGOT PASSWORD =================
//
//    @Override
//    public Mono<ForgetPasswordResponse> forgotPassword(String email) {
//
//        return userRepository.findByEmail(email)
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .flatMap(user -> {
//
//                    if ("DEACTIVE".equalsIgnoreCase(user.getStatus())) {
//
//                        return Mono.error(
//                                new RuntimeException(
//                                        "Your account is deactivated."
//                                )
//                        );
//                    }
//
//                    // TODO EMAIL SERVICE
//
//                    ForgetPasswordResponse response =
//                            new ForgetPasswordResponse(
//                                    user.getEmail(),
//                                    "Reset link sent successfully"
//                            );
//
//                    return Mono.just(response);
//                });
//    }
//
//    // ================= GET PROFILE =================
//
//    @Override
//    public Mono<UserResponse> getProfile(Long userId) {
//
//        return userRepository.findById(userId)
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .map(user -> mapToResponse(user, null));
//    }
//
//
//    @Override
//    public Flux<UserResponse> getAllUsers() {
//
//        return userRepository.findAll()
//                .map(user -> mapToResponse(user, null));
//    }
//
//
//    // ================= UPDATE PROFILE =================
//
//    @Override
//    public Mono<UserResponse> updateProfile(
//            Long id,
//            RegisterRequest request
//    ) {
//
//        return userRepository.findById(id)
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .flatMap(user -> {
//
//                    if (request.getName() != null) {
//                        user.setName(request.getName());
//                    }
//
//                    if (request.getEmail() != null) {
//                        user.setEmail(request.getEmail());
//                    }
//
//                    if (request.getPhone() != null) {
//                        user.setPhone(request.getPhone());
//                    }
//
//                    if (request.getPassword() != null &&
//                            !request.getPassword().isBlank()) {
//
//                        user.setPassword(
//                                passwordEncoder.encode(request.getPassword())
//                        );
//                    }
//
//                    if (request.getStatus() != null) {
//                        user.setStatus(request.getStatus());
//                    }
//
//                    return userRepository.save(user);
//                })
//
//                .map(user -> mapToResponse(user, null));
//    }
//
//    // ================= DELETE USER =================
//
//    @Override
//    public Mono<Void> deleteUser(Long id) {
//
//        return userRepository.findById(id)
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .flatMap(userRepository::delete);
//    }
//
//    // ================= DEACTIVATE =================
//
//    @Override
//    public Mono<UserResponse> deactivateUser(Long id) {
//
//        return userRepository.findById(id)
//
//                .switchIfEmpty(
//                        Mono.error(new RuntimeException("User not found"))
//                )
//
//                .flatMap(user -> {
//
//                    user.setStatus("DEACTIVE");
//
//                    return userRepository.save(user);
//                })
//
//                .map(user -> mapToResponse(user, null));
//    }
//
//    // ================= ENTITY MAPPER =================
//
//    private User mapToEntity(RegisterRequest request) {
//
//        User user = new User();
//
//        user.setName(request.getName());
//
//        user.setEmail(request.getEmail());
//
//        user.setPhone(request.getPhone());
//
//        user.setPassword(
//                passwordEncoder.encode(request.getPassword())
//        );
//
//        user.setStatus("ACTIVE");
//
//        user.setCreatedAt(LocalDateTime.now());
//
//        return user;
//    }
//
//    // ================= RESPONSE MAPPER =================
//
//    private UserResponse mapToResponse(
//            User user,
//            String token
//    ) {
//
//        return new UserResponse(
//                user.getUserId(),
//                user.getName(),
//                user.getEmail(),
//                user.getPhone(),
//                token,
//                user.getStatus(),
//                user.getDocumentStatus(),
//                user.getInvitationCode()
//        );
//    }
//
//    @Override
//    public Mono<List<String>> getPermissions(Long userId) {
//
//        return roleRepository.findPermissionsByUserId(userId)
//                .collectList();
//    }
//}