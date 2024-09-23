/* SIGNUP */
document.addEventListener('DOMContentLoaded', function() {
  const form = document.querySelector('.checkout__form');

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const nicknameRegex = /^[가-힣a-zA-Z0-9]{2,10}$/;
  const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[\W])(?=\S+$).{8,20}$/;

  const emailField = document.querySelector('input[name="email"]');
  const nicknameField = document.querySelector('input[name="nickName"]');
  const passwordField = document.querySelector('input[name="newPassword"]');
  const confirmPasswordField = document.querySelector('input[name="password"]');
  const zipcodeField = document.querySelector('input[name="zipcode"]');
  const roadAddressField = document.querySelector('input[name="roadAddress"]');
  const detailedAddressField = document.querySelector('input[name="detailedAddress"]');
  const schoolField = document.querySelector('#result_select');

  emailField.addEventListener('input', validateEmail);
  nicknameField.addEventListener('input', validateNickname);
  passwordField.addEventListener('input', validatePassword);
  confirmPasswordField.addEventListener('input', validateConfirmPassword);

  form.addEventListener('submit', function(e) {
    e.preventDefault();

    if (!emailField.value.trim()) {
      alert('이메일은 필수 입력값입니다.');
      emailField.focus();
      return false;
    }

    if (!nicknameField.value.trim()) {
      alert('닉네임은 필수 입력값입니다.');
      nicknameField.focus();
      return false;
    }

    if (!passwordField.value.trim()) {
      alert('비밀번호는 필수 입력값입니다.');
      passwordField.focus();
      return false;
    }

    if (!confirmPasswordField.value.trim()) {
      alert('비밀번호 확인은 필수 입력값입니다.');
      confirmPasswordField.focus();
      return false;
    }

    if (!zipcodeField.value.trim()) {
      alert('우편번호는 필수 입력값입니다.');
      zipcodeField.focus();
      return false;
    }
    if (!roadAddressField.value.trim()) {
      alert('도로명주소는 필수 입력값입니다.');
      roadAddressField.focus();
      return false;
    }
    if (!detailedAddressField.value.trim()) {
      alert('상세주소는 필수 입력값입니다.');
      detailedAddressField.focus();
      return false;
    }
    if (!schoolField.value.trim()) {
      alert('학교명은 필수 입력값입니다.');
      schoolField.focus();
      return false;
    }

    if (validateEmail() && validateNickname() && validatePassword() &&
        validateConfirmPassword() &&
        zipcodeField.value.trim() && roadAddressField.value.trim() && detailedAddressField.value.trim() &&
        schoolField.value.trim()) {

      const formData = new FormData(form);

      fetch('/member/signUp', {
        method: 'POST',
        body: formData
      })
          .then(response => response.json())
          .then(data => {
            if (data.success) {
              location.href = '/member/login';
            } else {
              alert(data.message || '회원가입 실패~');
            }
          })
          .catch(error => {
            console.error('Error:', error);
            alert('회원가입 처리 중 오류 발생');
          });
    }
  });

  function validateEmail() {
    const email = emailField.value.trim();
    if (!email || !emailRegex.test(email)) {
      displayErrorMessage('email-error', '유효한 이메일 주소를 입력해주세요.');
      return false;
    } else {
      clearErrorMessage('email-error');
      return true;
    }
  }

  function validateNickname() {
    const nickname = nicknameField.value.trim();
    if (!nickname || !nicknameRegex.test(nickname)) {
      displayErrorMessage('nickname-error', '닉네임은 특수기호를 제외한 두 글자 이상의 글자여야 합니다.');
      return false;
    } else {
      clearErrorMessage('nickname-error');
      return true;
    }
  }

  function validatePassword() {
    const password = passwordField.value.trim();
    if (!password || !passwordRegex.test(password)) {
      displayErrorMessage('password-error', '비밀번호는 영문 대,소문자와 숫자, 특수기호가 포함된 8자 ~ 20자의 비밀번호여야 합니다.');
      return false;
    } else {
      clearErrorMessage('password-error');
      return true;
    }
  }

  function validateConfirmPassword() {
    const password = passwordField.value.trim();
    const confirmPassword = confirmPasswordField.value.trim();
    if (password !== confirmPassword) {
      displayErrorMessage('confirm-password-error', '비밀번호가 일치하지 않습니다.');
      return false;
    } else {
      clearErrorMessage('confirm-password-error');
      return true;
    }
  }

  function displayErrorMessage(elementId, message) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
      errorElement.textContent = message;
      errorElement.style.color = 'red';
    }
  }

  function clearErrorMessage(elementId) {
    const errorElement = document.getElementById(elementId);
    if (errorElement) {
      errorElement.textContent = '';
    }
  }
});