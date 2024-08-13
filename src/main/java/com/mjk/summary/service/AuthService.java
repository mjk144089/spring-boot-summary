package com.mjk.summary.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.mjk.summary.model.Users;
import com.mjk.summary.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FirebaseAuth firebaseAuth;
    private final UsersRepository usersRepository;

    private final WebClient webClient;

    /**
     * 사용자가 입력한 이메일과 비밀번호로 Firebase에 새로운 사용자를 생성합니다
     * 
     * @param email    사용자 이메일
     * @param password 사용자 비밀번호
     * @return userId 생성된 사용자의 uid
     */
    public String createUserByEmailAndPassword(String email, String password) {
        try {
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password);

            UserRecord userRecord = firebaseAuth.createUser(request);
            return userRecord.getUid();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 또는 적절한 예외 처리를 수행합니다.
        }
    }

    /**
     * form에 저장한 정보 + 수령한 uid를 MySQL DB에 저장합니다
     * 
     * @param user Users객체
     * @return 저장된 Users객체
     */
    public Users saveUser(Users user) {
        /** Node.js서버로 요청을 보내 프로필 이미지를 만듭니다 */
        String baseUrl = "http://localhost:5500/generate-image";
        try {
            String imageUrl = webClient.post()
                    .uri(baseUrl)
                    .bodyValue(Map.of("uid", user.getUid()))
                    .retrieve()
                    .onStatus(status -> status.is5xxServerError(),
                            clientResponse -> Mono.error(new RuntimeException("Server error occurred"))) // 서버 에러 처리
                    .bodyToMono(String.class)
                    .block(); // 동기적으로 결과를 기다립니다

            user.setProfileImg(imageUrl); // 응답에서 이미지 URL을 설정합니다
        } catch (Exception e) {
            // 기타 예외 처리
            System.err.println("An unexpected error occurred: " + e.getMessage());
            // 적절한 예외 처리를 수행합니다
            throw new RuntimeException("An unexpected error occurred", e);
        }

        return usersRepository.save(user);
    }

    /**
     * 이메일이 중복되는지 검사합니다
     * 
     * @param email 이메일
     * @return 중복 여부
     */
    public Boolean checkEmailDuplicate(String email) {
        return usersRepository.existsByEmail(email);
    }
}
