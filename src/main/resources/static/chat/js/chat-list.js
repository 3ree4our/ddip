import {
  getAllProduct,
  getAllChatroom,
  getChatroomByProductId,
  getUnreadMessageCounts,
  imageUpload,
  markRead, SERVER_API, getImageUrl
} from "./api.js";
import {clearImageSelection, getSelectedImagesData} from "./image.js";

let stompClient = '';
let connectStatus = false;
let focusChatroom = '';
let unreadMessages = {};

let chatCurrentImages = [];
let chatCurrentImageIndex = 0;

// 채팅방을 열때
const openChatRoom = async (productId) => {
  await getChatroomByProductId(productId);
  await markAsRead(productId);
}

// 읽음 처리 함수
const markAsRead = async (productId) => {
  await markRead(productId);
}

const updateUnreadCount = (roomId) => {
  if (!unreadMessages[roomId]) {
    unreadMessages[roomId] = 0;
  }
  unreadMessages[roomId]++;
  updateUnreadBadge(roomId);
}

const getConnect = async () => {
  const allProducts = await getAllProduct();
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e.productId);
    if (!connectStatus) connect(productsId);
  }
}

const connect = (productsId) => {
  if (stompClient && stompClient.connected) {
    return; // 이미 연결된 경우 중복 연결 방지
  }

  if (!productsId) {
    const pathname = location.pathname;
    const path = pathname.substring(pathname.lastIndexOf('/') + 1);
    productsId = path;
  }
  const socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('frame', frame);
    productsId.forEach(productId => {
      subscribeToProduct(productId);
    })
  });
  connectStatus = true;
}

const subscribeToProduct = (productId) => {
  if (stompClient && stompClient.connected) {
    stompClient.subscribe(`/room/${productId}`, async (chatMessage) => {
      const messageObj = JSON.parse(chatMessage.body);
      const selectedImages = getSelectedImagesData();
      const formData = new FormData();

      let imageUrls = [];

      const sleep = (sec) => {
        return new Promise(resolve => setTimeout(resolve, sec * 1000));
      }

      selectedImages.forEach(file => {
        formData.append('files', file);
      });

      if (selectedImages.length > 0) {
        formData.append('chatId', messageObj.messageId);
        const imageIds = await imageUpload(formData);

        imageUrls.push(imageIds);
      }

      if (messageObj.images) {
        showPlaceholder(document.querySelector('.chat ul li:last-child'));
        await sleep(1);
        removePlaceholder(document.querySelector('.chat ul li:last-child'));
      }

      if (imageUrls.length === 0) {
        const imageIds = await getImageUrl(messageObj.messageId);
        imageUrls.push(imageIds);
      }

      messageObj.imageUrls = imageUrls;
      clearImageSelection();

      let roomElement = document.querySelector(`[data-product="${messageObj.roomId}"]`);

      if (Number(messageObj.roomId) === Number(focusChatroom)) {
        await markAsRead(messageObj.roomId);
        appendMessageTag(messageObj);
      } else {
        updateUnreadCount(messageObj.roomId);
      }

      if (!roomElement) {
        const newChat = {
          productId: messageObj.roomId,
          sender   : {nickname: messageObj.nickname},
          message  : messageObj.message
        };

        roomElement = createChatRoomElement(newChat);
        const chatRoomArea = document.querySelector('.chat-list');
        chatRoomArea.insertBefore(roomElement, chatRoomArea.firstChild);
      } else {
        roomElement.querySelector('p:last-child').innerText = messageObj.message;
      }

      if (Number(messageObj.roomId) !== Number(focusChatroom))
        updateUnreadBadge(messageObj.roomId);

    })
  }
}
const showPlaceholder = (messageElement) => {
  const placeholder = document.createElement('p');
  placeholder.className = 'message-placeholder';
  placeholder.textContent = '이미지 전송 중...';
  messageElement.appendChild(placeholder);
};

const removePlaceholder = (messageElement) => {
  const placeholder = messageElement.querySelector('.message-placeholder');
  if (placeholder) placeholder.remove();
};

//===========메시지 전송===============
document.querySelector("section.input-div textarea").addEventListener("keydown", async (e) => {
  if (e.keyCode === 13 && !e.shiftKey) {
    e.preventDefault();
    await sendMessage();
  }
})

const sendMessage = async () => {
  const messageInputEle = document.querySelector('section.input-div textarea');
  const message = messageInputEle.value.trim();
  const selectedImages = getSelectedImagesData();

  if (message === '' && selectedImages.length === 0) return;

  const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('access-token')
  };

  try {
    if (message !== '' || selectedImages.length > 0) {
      let messageObj = {message, images: false}
      if (selectedImages.length > 0) messageObj = {message, images: true}
      stompClient.send(`/messages/${focusChatroom}`, headers, JSON.stringify(messageObj));
    }

  } catch (error) {
    console.error('error sending message or uploading images: ', error)
  }

  messageInputEle.value = '';
  messageInputEle.focus();
}


// 메시지 태그 생성
const createMessageTag = (LR_className, senderName, message) => {
  const chatLi = document.querySelector("section.chat.format ul li").cloneNode(true);
  chatLi.classList.add(LR_className);
  chatLi.querySelector(".sender span").textContent = senderName;
  chatLi.querySelector(".message span").textContent = message;
  return chatLi;
}

// 메시지 태그 추가
const appendMessageTag = (messageObj) => {
  console.log('시간을 추가해보자,', messageObj)
  let chatLi = '';

  if (Array.isArray(messageObj)) {
    // 이전 대화 불러오기의 경우
    messageObj.forEach(message => {
      chatLi = createMessageTag(message.type, message.sender.nickname, message.message);
      appendImageToMessage(chatLi, message.chatImageIds);
      document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
    });
  } else {
    // 단일 메시지의 경우
    const savedName = localStorage.getItem('nickname');
    let type = messageObj.nickname === savedName ? 'right' : 'left';

    chatLi = createMessageTag(type, messageObj.nickname, messageObj.message);
    document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
    appendImageToMessage(chatLi, messageObj.chatImageIds);
    document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
  }

  if (messageObj?.imageUrls?.length > 0) {
    const imageContainer = document.createElement('div');
    imageContainer.className = 'message-images';
    messageObj.imageUrls[0].forEach((url, index) => {
      const img = document.createElement('img');
      img.src = `${SERVER_API}/api/images/${url}`;
      img.alt = 'upload image';
      img.style.maxWidth = '200px';
      img.style.maxHeight = '200px';
      img.style.margin = '5px';
      img.style.cursor = 'pointer';
      img.onclick = () => openChatImageModal(messageObj.imageUrls[0], index);
      imageContainer.appendChild(img);
    })
    chatLi.querySelector('.message').appendChild(imageContainer);
  }
  const chatContainer = document.querySelector("#chatWrapper .chat");
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

//===========================================================================================
const appendImageToMessage = (chatLi, chatImageIds) => {
  if (chatImageIds && chatImageIds.length > 0) {
    const imageContainer = document.createElement('div');
    imageContainer.className = 'message-images';
    chatImageIds.forEach((imageId, index) => {
      const img = document.createElement('img');
      img.src = `${SERVER_API}/api/images/${imageId}`;
      //img.src = imageId; // s3 전용
      img.alt = 'upload image';
      img.style.cursor = 'pointer';
      img.onclick = () => openChatImageModal(chatImageIds, index);
      imageContainer.appendChild(img);
    })
    chatLi.querySelector('.message').appendChild(imageContainer);
  }
}

// 채팅방 클릭 이벤트 핸들러 수정
const chatRoomClickHandler = async (e) => {
  if (document.querySelector("#chatWrapper .chat:not(.format) ul").hasChildNodes()) {
    document.querySelector("#chatWrapper .chat:not(.format) ul").replaceChildren('')
  }
  let roomId = '';
  if (e.target.tagName !== 'DIV') {
    if (e.target.tagName !== 'IMG') {
      const targetEle = e.target.parentElement.parentElement;
      roomId = targetEle.dataset.product;
      addClassName(targetEle);
    } else {
      const targetEle = e.target.parentElement;
      roomId = targetEle.dataset.product;
      addClassName(targetEle);
    }
  } else {
    const targetEle = e.target;
    roomId = targetEle.dataset.product;
    addClassName(targetEle);
  }

  focusChatroom = roomId;
  // 읽지 않은 메시지 수 초기화
  unreadMessages[roomId] = 0;
  updateUnreadBadge(roomId);
  await openChatRoom(roomId);

  const messageObj = await getChatroomByProductId(roomId);
  appendMessageTag(messageObj);
}


const drawChatList = async () => {
  const allChatroom = await getAllChatroom();
  const chatRoomArea = document.querySelector('.chat-list');
  let productIds = [];

  if (allChatroom.length === 0) {
    chatRoomArea.innerHTML = `
      <div class="no-chats">
        <svg width="50" height="50" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M20 2H4C2.9 2 2 2.9 2 4V22L6 18H20C21.1 18 22 17.1 22 16V4C22 2.9 21.1 2 20 2ZM20 16H6L4 18V4H20V16Z" fill="#CCCCCC"/>
        </svg>
        <p>채팅 내역이 없습니다.</p>
      </div>
    `;
  } else {
    chatRoomArea.replaceChildren('')
    chatRoomArea.innerHTML = '';
    for (let chat of allChatroom) {
      productIds.push(chat.productId);
      const chatRoomElement = createChatRoomElement(chat);
      chatRoomArea.appendChild(chatRoomElement);
      updateUnreadBadge(chat.productId); // 각 채팅방에 대해 읽지 않은 메시지 배지 업데이트
    }
  }

  connect(productIds);
}

const addClassName = (targetEle) => {
  targetEle.style.backgroundColor = ''

  const elements = document.querySelector('.chat-list');
  const children = elements.children;
  for (let child of children) child.classList.remove('active')

  targetEle.classList.add('active');
}


const updateUnreadBadge = (roomId) => {
  const roomElement = document.querySelector(`[data-product="${roomId}"]`);
  if (!roomElement) return; // 해당 채팅방 요소가 없으면 함수 종료

  let badgeElement = roomElement.querySelector('.unread-badge');

  if (!badgeElement) {
    badgeElement = document.createElement('span');
    badgeElement.className = 'unread-badge';
    roomElement.appendChild(badgeElement);
  }

  const unreadCount = unreadMessages[roomId] || 0;
  if (unreadCount > 0) {
    badgeElement.textContent = unreadCount;
    badgeElement.style.display = 'inline';
  } else {
    badgeElement.style.display = 'none';
  }
}

const initializeUnreadCounts = async () => {
  const allChatroom = await getAllChatroom();
  const allChatroomId = allChatroom.map(e => e.productId);
  if (allChatroom.length > 0) {
    const unreadCounts = await getUnreadMessageCounts(allChatroomId);
    unreadMessages = unreadCounts; // 전역 unreadMessages 객체 업데이트
    Object.entries(unreadCounts).forEach(([productId, count]) => {
      updateUnreadBadge(productId);
    });
  }
}

const createChatRoomElement = (chat) => {
  const nickname = localStorage.getItem('nickname');
  const name = nickname === chat.productOwner.nickname ? chat.sender.nickname : chat.productOwner.nickname;
  const div = document.createElement('div');
  div.className = 'user';
  div.setAttribute('data-product', chat.productId);
  div.innerHTML = `
    <img src="/img/trend/bs-1.jpg" alt="물품사진"/>
    <div>
      <p>${name}</p>
      <p>${chat.message}</p>
    </div>
    <span class="unread-badge" style="display: none;"></span>
  `;
  div.addEventListener('click', chatRoomClickHandler);
  return div;
}

const openChatImageModal = (images, index) => {
  const modal = document.getElementById('chatImageModal');
  chatCurrentImages = images.map(url => `${SERVER_API}/api/images/${url}`);
  //chatCurrentImages = images.map(url => `${url}`); // s3 전용
  chatCurrentImageIndex = index;
  updateChatModalImage();
  modal.classList.add('show'); // 'show' 클래스 추가
}

const updateChatModalImage = () => {
  const modalImg = document.getElementById('chatModalImage');
  modalImg.src = chatCurrentImages[chatCurrentImageIndex];
  document.getElementById('chatImageCounter').textContent = `${chatCurrentImageIndex + 1} / ${chatCurrentImages.length}`;

  document.querySelector('.chat-modal-prev').style.display = chatCurrentImageIndex > 0 ? 'block' : 'none';
  document.querySelector('.chat-modal-next').style.display = chatCurrentImageIndex < chatCurrentImages.length - 1 ? 'block' : 'none';
}

const closeChatImageModal = () => {
  const modal = document.getElementById('chatImageModal');
  modal.classList.remove('show');
}

const nextChatImage = () => {
  if (chatCurrentImageIndex < chatCurrentImages.length - 1) {
    chatCurrentImageIndex++;
    updateChatModalImage();
  }
}

const prevChatImage = () => {
  if (chatCurrentImageIndex > 0) {
    chatCurrentImageIndex--;
    updateChatModalImage();
  }
}

// 이벤트 리스너 설정
document.querySelector('.chat-modal-close').addEventListener('click', closeChatImageModal);
document.querySelector('.chat-modal-prev').addEventListener('click', prevChatImage);
document.querySelector('.chat-modal-next').addEventListener('click', nextChatImage);
document.getElementById('chatImageModal').addEventListener('click', (event) => {
  if (event.target === event.currentTarget) {
    closeChatImageModal();
  }
});

getConnect();
drawChatList().then(() => {
  initializeUnreadCounts();
});