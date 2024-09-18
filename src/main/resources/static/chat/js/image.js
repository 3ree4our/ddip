let selectedImages = [];
let currentImageIndex = 0;

const handleImageSelect = (event) => {
  const files = event.target.files;
  selectedImages = Array.from(files).slice(0, 10);
  updateImagePreviews();
}

const updateImagePreviews = () => {
  const previewContainer = document.querySelector('#imageContainer .preview-container');
  previewContainer.innerHTML = '';

  selectedImages.slice(0, 3).forEach((file, index) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = document.createElement('img');
      img.src = e.target.result;
      img.classList.add('preview-image');
      img.onclick = () => openModal(index);
      previewContainer.appendChild(img);
    };
    reader.readAsDataURL(file);
  });

  if (selectedImages.length > 3) {
    const countElement = document.createElement('div');
    countElement.classList.add('additional-count');
    countElement.textContent = `+${selectedImages.length - 3}`;
    countElement.onclick = () => openModal(3);
    previewContainer.appendChild(countElement);
  }
}

const openModal = (index) => {
  const modal = document.getElementById('imageModal');
  currentImageIndex = index;
  updateModalImage();
  modal.classList.add('show'); // 'show' 클래스 추가
};

const closeModal = () => {
  const modal = document.getElementById('imageModal');
  modal.classList.remove('show'); // 'show' 클래스 제거
};

const updateModalImage = () => {
  const modalImg = document.getElementById('modalImage');
  const reader = new FileReader();
  reader.onload = (e) => {
    modalImg.src = e.target.result;
    document.getElementById('imageCounter').textContent = `${currentImageIndex + 1} / ${selectedImages.length}`;

    // 이전/다음 버튼 가시성 조정
    document.querySelector('.prev').style.display = currentImageIndex > 0 ? 'block' : 'none';
    document.querySelector('.next').style.display = currentImageIndex < selectedImages.length - 1 ? 'block' : 'none';
  }
  reader.readAsDataURL(selectedImages[currentImageIndex]);
}

const nextImage = () => {
  currentImageIndex = (currentImageIndex + 1) % selectedImages.length;
  updateModalImage();
}

const prevImage = () => {
  currentImageIndex = (currentImageIndex - 1 + selectedImages.length) % selectedImages.length;
  updateModalImage();
}

// 이벤트 리스너 설정
document.querySelector('.close').onclick = closeModal;
document.querySelector('.prev').onclick = prevImage;
document.querySelector('.next').onclick = nextImage;
document.querySelector('.modal').addEventListener('click', e => {
  if (!e.target.closest('img, .prev, .next, .modal-nav')) closeModal();
});
document.getElementById('images').addEventListener('change', handleImageSelect);

const getSelectedImagesData = () => {
  return selectedImages
}

const clearImageSelection = () => {
  selectedImages = [];
  updateImagePreviews();
}

export {getSelectedImagesData, clearImageSelection};