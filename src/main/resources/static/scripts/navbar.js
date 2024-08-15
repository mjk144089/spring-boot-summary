document.addEventListener("DOMContentLoaded", function () {
  const navbarAuth = document.querySelector(".navbar-nav.ms-auto");
  const sessionData = sessionStorage.getItem("user");

  if (sessionData) {
    // Parse user information from sessionStorage
    const user = JSON.parse(sessionData);

    // Create a new list item for the user's profile
    const profileItem = document.createElement("li");
    profileItem.className = "nav-item d-flex align-items-center";

    // Create the profile link with image and name
    profileItem.innerHTML = `
        <a class="nav-link d-flex align-items-center" href="#">
          <span class="me-2">${user.name}</span>
          <img src="${user.profileImg}" alt="Profile Image" width="30" height="30" class="rounded-circle" />
        </a>
      `;

    // Remove the existing login button
    const loginItem = navbarAuth.querySelector(".nav-item");
    if (loginItem) {
      navbarAuth.removeChild(loginItem);
    }

    // Add the profile link to the navbar
    navbarAuth.appendChild(profileItem);
  }
});
