package com.mjk.summary.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.mjk.summary.model.Session;
import com.mjk.summary.model.Users;
import com.mjk.summary.repository.SessionRepository;
import com.mjk.summary.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FirebaseAuth firebaseAuth;
    private final UsersRepository usersRepository;
    private final SessionRepository sessionRepository;

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

            user.setProfileImg(imageUrl); // user데이터에 프로필 이미지를 설정합니다
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred", e);
        }

        // DB에 데이터를 저장하고, 저장된 엔티티를 반환합니다
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

    /**
     * Firebase AccessToken의 유효성을 검사합니다
     * 
     * @param idToken firebase의 accessToken
     * @return 사용자 uid
     * @throws FirebaseAuthException
     */
    public String verifyFirebaseAccessToken(String idToken) throws FirebaseAuthException {
        FirebaseToken verifiedToken = firebaseAuth.verifyIdToken(idToken);
        return verifiedToken.getUid();
    }

    /**
     * 임의의 세션값을 생성하고, 그 값을 DB에 저장합니다
     * 
     * @param uid 사용자 uid
     * @return sessionId 생성된 세션 Id
     */
    public String generateSessionId(String uid) {
        try {
            Session session = new Session();
            Users user = usersRepository.findById(uid).orElse(null);
            String uuid = UUID.randomUUID().toString();
            LocalDateTime expirationDate = LocalDateTime.now().plusDays(1);

            session.setSessionId(uuid);
            session.setUid(uid);
            session.setExpiration(expirationDate);
            session.setUser(user);

            sessionRepository.save(session);

            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * uid값을 통해 사용자의 정보를 반환합니다
     * 
     * @param uid 사용자 uid
     * @return users테이블에 저장된 정보
     */
    public Users findUserById(String uid) {
        Users user = usersRepository.findById(uid).orElse(null);
        return user;
    }

    /**
     * SessionId를 통해 uid를 찾습니다
     * 
     * @param sessionId 세션ID
     * @return uid
     */
    public String findUidBySessionId(String sessionId) {
        Session session = sessionRepository.findBySessionId(sessionId);
        return session.getUid();
    }

    /**
     * 특정 Uid값을 가진 사용자를 삭제합니다
     * 
     * @param uid uid
     */
    public void DeleteAccountByUid(String uid) {
        usersRepository.deleteById(uid);
    }
}
