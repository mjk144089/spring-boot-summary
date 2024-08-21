import { app } from "/firebase-config.js";
import {
  getAuth,
  GoogleAuthProvider,
  signInWithPopup,
  signInWithEmailAndPassword,
} from "https://www.gstatic.com/firebasejs/10.12.5/firebase-auth.js";
const auth = getAuth(app);

var login_btn = document.getElementById("login-btn");

login_btn.addEventListener("click", (e) => {
  e.preventDefault();
  var email = document.getElementById("email").value;
  var password = document.getElementById("password").value;
  var inputHelp = document.getElementById("input-help");

  // 이메일 형식 검사를 위한 정규 표현식
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!email) {
    inputHelp.textContent = "이메일을 입력해 주세요";
    inputHelp.style.display = "block";
  } else if (!emailPattern.test(email)) {
    inputHelp.textContent = "유효한 이메일 주소를 입력해 주세요";
    inputHelp.style.display = "block";
  } else if (!password) {
    inputHelp.textContent = "비밀번호를 입력해 주세요";
    inputHelp.style.display = "block";
  } else {
    inputHelp.style.display = "none";
    login_btn.innerHTML = "로그인중";
    login_btn.disabled = true;

    signInWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        // 로그인 성공
        const user = userCredential.user;

        // IdToken을 가져와서 서버로 전송
        user.getIdToken().then(async (idToken) => {
          await fetch("http://localhost:8080/accounts/sign-in", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${idToken}`,
            },
            body: JSON.stringify({}),
          })
            .then(async (response) => await response.json())
            .then((data) => {
              if (data.sessionId) {
                sessionStorage.setItem("sessionId", data.sessionId);
                sessionStorage.setItem("user", JSON.stringify(data.user));

                location.href = "/";
              } else {
                location.href = "/error";
              }
            });
        });
      })
      .catch((error) => {
        // 로그인 실패
        const errorCode = error.code;
        const errorMessage = error.message;
        inputHelp.textContent = `해당 이메일을 가진 사용자가 없거나 비밀번호가 다릅니다`;
        inputHelp.style.display = "block";
        console.error("로그인 실패:", errorCode, errorMessage);
      })
      .finally(() => {
        // 버튼 상태 복원
        login_btn.innerHTML = "로그인";
        login_btn.disabled = false;
      });
  }
});
