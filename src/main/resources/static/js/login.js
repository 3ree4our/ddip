document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.querySelector('form');
  loginForm.addEventListener('submit', function (e) {
    e.preventDefault();

    fetch('/login', {
      method : 'POST',
      body   : new FormData(loginForm),
      headers: {
        'Accept': 'application/json'
      }
    })
        .then(response => {
          if (response.ok) {
            return response.json();
          }
          throw new Error('Login failed');
        })
        .then(data => {
          if (data.success) {
            const accessToken = data.accessToken; // 서버에서 보낸 토큰
            localStorage.setItem('access-token', accessToken);
            localStorage.setItem('nickname', data.nickname);
            localStorage.setItem('role', data.role); //[1]
            location.href = '/';
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
  });
});

/* Logout */
document.addEventListener('DOMContentLoaded', function () {
  function checkLoggedIn() {
    return localStorage.getItem('access-token') !== null;
  }

  function updateAuthLinks() {
    const loginLink = document.querySelector('.loginLink');
    const registerLink = document.querySelector('.registerLink');
    const logoutLink = document.querySelector('.logoutLink');

    if (checkLoggedIn()) {
      if (loginLink) loginLink.style.display = 'none';
      if (registerLink) registerLink.style.display = 'none';
      if (logoutLink) logoutLink.style.display = 'inline-block';
    } else {
      if (loginLink) loginLink.style.display = 'inline-block';
      if (registerLink) registerLink.style.display = 'inline-block';
      if (logoutLink) logoutLink.style.display = 'none';
    }
  }

  updateAuthLinks();

  const logoutLink = document.querySelector('.logoutLink');
  if (logoutLink) {
    logoutLink.addEventListener('click', function(e) {
      e.preventDefault();
      const accessToken = localStorage.getItem('access-token');

      if (!accessToken) {
        console.error('No access token found');
        return;
      }

      $.ajax({
        url: '/logout',
        type: 'POST',
        headers: {
          'Authorization': 'Bearer ' + accessToken,
        },
        xhrFields: {
          withCredentials: true  // 쿠키를 포함시키기 위해
        },
        success: function (result) {
          localStorage.removeItem('access-token');
          localStorage.removeItem('nickname');
          localStorage.removeItem('role');
          updateAuthLinks();
          location.href = '/';
        },
        error: function (xhr, status, error) {
          console.error('Logout failed:', error);
          alert('Logout failed. Please try again.');
        }
    });
  });
  }
});