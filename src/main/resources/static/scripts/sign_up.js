var pwCheck = false; // 비밀번호 일치여부
var pwRegex = false; // 비밀번호 정규식 검사

function setButtonState() {
  document.getElementById("sign-up-btn").disabled = !(pwCheck && pwRegex);
}

// 이메일 형식 검사
function validateEmail() {}

// 비밀번호 형식 검사
function validatePassword() {
  const regex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_])[a-zA-Z\d\W_]{8,20}$/;
  const inputValue = document.getElementById("password").value;
  const errorMessage = document.getElementById("password-regex");

  if (regex.test(inputValue)) {
    errorMessage.style.visibility = "hidden";
    pwRegex = true;
  } else {
    errorMessage.style.visibility = "visible";
    pwRegex = false;
  }
  setButtonState();
}

// 비밀번호 일치 검사
function validatePasswordCheck() {
  const password = document.getElementById("password").value;
  const passwordCheck = document.getElementById("password-check").value;
  const errorMessage = document.getElementById("password-check-text");

  if (password == passwordCheck) {
    errorMessage.style.visibility = "hidden";
    pwCheck = true;
  } else {
    errorMessage.style.visibility = "visible";
    pwCheck = false;
  }
  setButtonState();
}

// 비밀번호 input내용이 변경될 때 마다 함수 실행
document.getElementById("password").addEventListener("input", validatePassword);
document
  .getElementById("password-check")
  .addEventListener("input", validatePasswordCheck);
