//카테고리
const categoryIcon = document.querySelector('.category-icon');
const dropdown = categoryIcon.nextElementSibling;
let isDropdownOpen = false;

// 카테고리 아이콘 클릭 시 드롭다운 토글
categoryIcon.addEventListener('click', function(event) {
    event.preventDefault(); // 기본 링크 동작 방지
    isDropdownOpen = !isDropdownOpen; // 상태 토글
    dropdown.style.display = isDropdownOpen ? 'block' : 'none'; // 드롭다운 보이기/숨기기
});

// 드롭다운에서 마우스 아웃 시 드롭다운 숨기기
dropdown.addEventListener('mouseleave', function() {
    isDropdownOpen = false; // 상태 초기화
    dropdown.style.display = 'none'; // 드롭다운 숨기기
});

// 드롭다운 외부 클릭 시 드롭다운 숨기기
document.addEventListener('click', function(event) {
    if (!categoryIcon.contains(event.target) && !dropdown.contains(event.target)) {
        isDropdownOpen = false; // 상태 초기화
        dropdown.style.display = 'none'; // 드롭다운 숨기기
    }
});



//배너
let currentSlide = 0;

function moveSlide(direction) {
    const slides = document.querySelector('.slides');
    const totalSlides = document.querySelectorAll('.slide').length;

    currentSlide += direction;

    if (currentSlide < 0) {
        currentSlide = totalSlides - 1;
    } else if (currentSlide >= totalSlides) {
        currentSlide = 0;
    }

    const offset = -currentSlide * 100; // -100%씩 이동
    slides.style.transform = `translateX(${offset}%)`;
}

// 자동 슬라이드 기능 (옵션)
setInterval(() => {
    moveSlide(1);
}, 5000); // 5초마다 슬라이드 변경