document.addEventListener("DOMContentLoaded", function () {
  const modal = new bootstrap.Modal(document.getElementById("exampleModal"));
  const btn = document.getElementById("submit-btn");

  document
    .getElementById("summaryForm")
    .addEventListener("submit", async function (e) {
      e.preventDefault(); // 기본 폼 제출 방지
      const inputValue = document.getElementById("summary").value;
      const id = new URLSearchParams(location.search).get("id");

      btn.innerHTML = "채점중..";
      btn.disabled = true;

      await fetch("http://localhost:5500/evaluate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          id: id,
          inputValue: inputValue,
        }),
      })
        .then(async (response) => {
          return await response.json();
        })
        .then((data) => {
          const score = data.score;
          const reason = data.reason;
          document.getElementById("response-value").innerHTML = `
          점수 : ${score}<br>
          이유 : ${reason}<br>
          `;

          document.getElementById("score").value = score;
          document.getElementById("inputValue").value = inputValue;

          // 모달을 표시
          modal.show();
          btn.innerHTML = "제출";
          btn.disabled = false;
        })
        .catch((err) => {
          btn.innerHTML = "제출";
          btn.disabled = false;
          alert("에러가 발생했습니다. 다시 시도하세요");
        });
    });

  document.getElementById("saveForm").addEventListener("submit", function (e) {
    e.preventDefault();

    const id = new URLSearchParams(location.search).get("id");
    const sessionId = sessionStorage.getItem("sessionId");

    document.getElementById("paragraphId").value = id;

    if (sessionId) {
      document.getElementById("session").value = sessionId;

      this.submit();
    } else {
      alert("결과를 저장하려면 로그인 하세요!");
      return;
    }
  });
});
