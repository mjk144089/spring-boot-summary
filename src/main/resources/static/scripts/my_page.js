document.addEventListener("DOMContentLoaded", () => {
  const userData = sessionStorage.getItem("user");
  const sessionId = sessionStorage.getItem("sessionId");
  const logoutButton = document.getElementById("logout-button");
  const deleteAccountButton = document.getElementById("delete-account-button");

  logoutButton.addEventListener("click", function () {
    sessionStorage.clear();
    window.location.href = "/";
  });

  deleteAccountButton.addEventListener("click", function () {
    if (
      confirm(
        "모든 정보(사용자 정보, 문제 기록 등)가 영구히 삭제되며, 복구할 수 없습니다.\n정말로 회원탈퇴를 진행하시겠습니까?"
      )
    ) {
      sessionStorage.clear();
      window.location.href = `/accounts/delete-account?t=${sessionId}`;
    }
  });

  if (!userData) {
    location.href = "/error";
    return;
  } else {
    const user = JSON.parse(userData);
    const hello = document.getElementById("hello");

    // ~~님 안녕하세요!
    hello.innerHTML = `
        <img src="${user.profileImg}" alt="Profile Image" width="30" height="30" class="rounded-circle" />
        <span>${user.name}님, 안녕하세요!</span>
    `;
  }
});
