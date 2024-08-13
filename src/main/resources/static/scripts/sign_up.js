var pwCheck = false; // 비밀번호 일치여부
var pwRegex = false; // 비밀번호 정규식 검사
var emailDup = false; // 이메일 중복 여부

function setButtonState() {
  document.getElementById("sign-up-btn").disabled = !(
    pwCheck &&
    pwRegex &&
    emailDup
  );
}

// 이메일 중복 검사
async function duplicateEmail() {
  const email = document.getElementById("email").value;
  const emailHelp = document.getElementById("email-duplicate");

  await fetch(`http://localhost:8080/accounts/duplicate?email=${email}`, {
    method: "GET",
  }).then(async (res) => {
    var data = await res.text();
    if (data == "true") {
      emailDup = false; // 이메일이 중복됨
      emailHelp.innerHTML = "가입된 이메일이 존재합니다";
      emailHelp.style.visibility = "visible";
      emailHelp.style.color = "red";
    } else {
      emailDup = true;
      emailHelp.innerHTML = "해당 이메일로 가입이 가능합니다";
      emailHelp.style.visibility = "visible";
      emailHelp.style.color = "blue";
    }
    setButtonState();
  });
}

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

// 이메일 입력 조정
document.getElementById("email").addEventListener("input", () => {
  const emailHelp = document.getElementById("email-duplicate");
  emailHelp.style.visibility = "hidden";
  emailDup = false;
  setButtonState();
});
