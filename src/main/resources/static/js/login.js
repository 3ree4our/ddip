document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.querySelector('form');
  loginForm.addEventListener('submit', function (e) {
    e.preventDefault();

    const formData = new FormData(loginForm);
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
            window.location.href = '/';
          }
        })
        .catch(error => {
          console.error('Error:', error);
        });
  });
});

function updateUIWithNickname(nickname) {
  const authLinks = document.querySelectorAll('.header__right__auth');
  authLinks.forEach(link => {
    link.innerHTML = `<a href="#">${nickname}</a>`;
  });
}