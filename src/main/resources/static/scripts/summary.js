document.addEventListener("DOMContentLoaded", function () {
  const modal = new bootstrap.Modal(document.getElementById("exampleModal"));
  const btn = document.getElementById("submit-btn");

  document
    .getElementById("summaryForm")
    .addEventListener("submit", function (e) {
      const inputValue = document.getElementById("summary").value;
      e.preventDefault(); // 기본 폼 제출 방지

      btn.innerHTML = "채점중..";
      btn.disabled = true;

      // 1초 대기
      setTimeout(() => {
        document.getElementById("response-value").innerHTML = "엣또";

        // 모달을 표시
        modal.show();
        btn.innerHTML = "제출";
        btn.disabled = false;
      }, 2000);
    });
});
