package org.duo.duo.user;

import lombok.RequiredArgsConstructor;
import org.duo.duo.common.exception.UserException;
import org.duo.duo.common.exception.ErrorCode;
import org.duo.duo.riot.RiotClient;
import org.duo.duo.riot.dto.AccountDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RiotClient riotClient;

    @Value("${file.profile-dir}")
    private String profileDir;

    @Transactional
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserException(ErrorCode.DUPLICATE_USERNAME);
        }

        AccountDto accountDto = riotClient.getAccount(request.getRiotId(), request.getRiotTag());

        if (accountDto.getPuuid() == null) {
            throw new UserException(ErrorCode.RIOT_ID_NOT_FOUND);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .role(Role.USER)
                .riotId(request.getRiotId())
                .riotTag(request.getRiotTag())
                .build();

        User savedUser = userRepository.save(user);
        return UserResponse.from(savedUser);
    }

    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return UserResponse.from(user);
    }

    public User getUserEntity(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    @Transactional
    public void updateProfile(User user, String bio, MultipartFile profileImage) {
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String ext = profileImage.getOriginalFilename()
                        .substring(profileImage.getOriginalFilename().lastIndexOf("."));
                String fileName = UUID.randomUUID() + ext;
                Path dirPath = Paths.get(profileDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                }
                Files.write(dirPath.resolve(fileName), profileImage.getBytes());
                imagePath = "/uploads/profile/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 저장 실패", e);
            }
        }
        user.updateProfile(bio, imagePath);
        userRepository.save(user);
    }
}
