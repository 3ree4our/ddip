import {
  getAllProduct,
  getAllChatroom,
  getChatroomByProductId,
  getUnreadMessageCountsByProducts,
  imageUpload,
  markRead, SERVER_API, getImageUrl, completeDeal, cancelDeal, headers
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

  const socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('frame', frame);
    productsId.forEach(async (productId) => {

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

      if (messageObj.message === null || messageObj.message === undefined) return;

      if (messageObj.message === 'complete') {
        alert('거래가 완료되었습니다. 감사합니다.')

        location.reload();
        return;
      }

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
const createMessageTag = (LR_className, senderName, message, sendDate) => {
  const chatLi = document.querySelector("section.chat.format ul li").cloneNode(true);
  chatLi.classList.add(LR_className);
  chatLi.querySelector(".sender span").textContent = senderName;
  chatLi.querySelector(".message span").textContent = message;

  // 날짜 추가
  const dateSpanEle = document.createElement('span')
  dateSpanEle.className = 'message-date';
  dateSpanEle.textContent = formatDate(sendDate);
  chatLi.querySelector('.message').appendChild(dateSpanEle);

  return chatLi;
}

// 메시지 태그 추가
const appendMessageTag = (messageObj) => {
  let chatLi = '';

  if (Array.isArray(messageObj)) {
    // 이전 대화 불러오기의 경우
    messageObj.forEach(message => {
      chatLi = createMessageTag(message.type, message.sender.nickname, message.message, message.sendDate);
      appendImageToMessage(chatLi, message.chatImageIds);
      document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);

      if (message.status === 'PAID') {
        const chatInput = document.querySelector('section.input-div textarea');

        chatInput.disabled = true;
        chatInput.placeholder = '거래가 완료되어 더 이상 메시지를 보낼 수 없습니다.';

        return;
      }
    });
  } else {
    // 단일 메시지의 경우
    const savedName = localStorage.getItem('nickname');
    let type = messageObj.nickname === savedName ? 'right' : 'left';

    chatLi = createMessageTag(type, messageObj.nickname, messageObj.message, messageObj.sendDate);
    document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
    appendImageToMessage(chatLi, messageObj.imageUrls[0]);
    document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
  }

  if (messageObj?.imageUrls?.length > 0) {
    if (messageObj.imageUrls[0].statusCode === 400) {
      const chatInput = document.querySelector('section.input-div textarea');
      const btnContainerEle = document.getElementById('dealButtonContainer');

      if (btnContainerEle) btnContainerEle.replaceChildren('')

      chatInput.disabled = true;
      chatInput.placeholder = '거래가 완료되어 더 이상 메시지를 보낼 수 없습니다.';
      return;
    }

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
      //img.src = `${SERVER_API}/api/images/${imageId}`;
      if (typeof imageId === 'string') img.src = imageId;
      else img.src = imageId.s3Url;
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
  let productId = '';

  if (document.querySelector("#chatWrapper .chat:not(.format) ul").hasChildNodes()) {
    document.querySelector("#chatWrapper .chat:not(.format) ul").replaceChildren('')
  }

  if (e.target.tagName !== 'DIV') {
    if (e.target.tagName !== 'IMG') {
      const targetEle = e.target.parentElement.parentElement;
      productId = targetEle.dataset.product;
      addClassName(targetEle);
    } else {
      const targetEle = e.target.parentElement;
      productId = targetEle.dataset.product;
      addClassName(targetEle);
    }
  } else {
    const targetEle = e.target;
    productId = targetEle.dataset.product;
    addClassName(targetEle);
  }

  if (e.target.tagName === 'DEL') {
    const targetEle = e.target.parentElement.parentElement.parentElement;
    productId = targetEle.dataset.product;
    addClassName(targetEle);
  }

  // 상태 가져오기

  focusChatroom = productId;
  // 읽지 않은 메시지 수 초기화
  unreadMessages[productId] = 0;
  updateUnreadBadge(productId);
  await openChatRoom(productId);

  const messageObj = await getChatroomByProductId(productId);
  appendMessageTag(messageObj);

  if (messageObj[0].status === 'PAID') return;

  if (messageObj[0].type === 'left') {
    updateDealButtons(productId);
  } else {
    const btnContainerEle = document.getElementById('dealButtonContainer');
    if (btnContainerEle) btnContainerEle.replaceChildren('')
  }
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
    const unreadCounts = await getUnreadMessageCountsByProducts(allChatroomId);
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

  const messageText = chat.message.trim() === '' ? '이미지 전송' : chat.message;

  div.innerHTML = `
  <img src='${chat.image}' alt="물품사진"/>
  <div>
    <p>${name}</p>
    <p>${chat.status === 'PAID' ? `<del>${messageText}</del>` : messageText}</p>
  </div>
  <span class="unread-badge" style="display: none;"></span>
`;

  div.addEventListener('click', chatRoomClickHandler);

  return div;
}

const openChatImageModal = (images, index) => {
  const modal = document.getElementById('chatImageModal');
  //chatCurrentImages = images.map(url => `${SERVER_API}/api/images/${url}`);
  chatCurrentImages = images.map(url => `${url.s3Url}`); // s3 전용
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

const updateDealButtons = (productId) => {
  const dealButtonContainer = document.getElementById('dealButtonContainer');
  dealButtonContainer.innerHTML = `
    <button id="completeDealButton" data-product-id="${productId}">거래 완료</button>
    <button id="cancelDealButton" data-product-id="${productId}">거래 취소</button>
  `;

  document.getElementById('completeDealButton').onclick = () => handleDealComplete(productId);
  document.getElementById('cancelDealButton').onclick = () => handleCancelDeal(productId);
}

const handleDealComplete = async () => {
  const result = confirm('거래를 완료 하시겠습니까?');

  if (result) {
    try {
      // deal 상태를 PAID 로 변경 후 해당 채팅방에 deleteYn false로 변경해서 채팅방 없애기,
      // 혹은 해당 상품 id 구독 x => 채팅 금지
      const result = await completeDeal(focusChatroom);

      if (!result.ok) throw new Error('Failed to complete deal');

      alert('거래가 완료되었습니다.')

      const messageObj = {
        message: 'complete',
        images : false
      }

      stompClient.send(`/messages/${focusChatroom}`, headers, JSON.stringify(messageObj));

    } catch (error) {
      console.error('Error completing deal: ', error);
      alert('거래 완료 처리 중 오류가 발생했습니다.')
    }
  }
}

const handleCancelDeal = async (productId) => {
  console.log(`거래 취소: 상품 ID ${productId}`);

  const result = confirm('거래를 취소하겠습니까?');
  if (result) {
    try {
      // deal 상태를 BEFORE_DEAL로 변경하고 해당 product와 senderId로 등록된 채팅 deleteYn false로 변경
      // 다음 예약 유저에게 .. (이게 가능한가? 내 머리에) 알림 날리기 ..
      alert('거래가 취소되었습니다.')
      const result = await cancelDeal(focusChatroom);
      if (!result.ok) throw new Error('Failed to cancel deal');

      location.reload();
    } catch (error) {
      console.error(error)
      alert('거래 취소 처리 중 오류가 발생하였습니다.')
    }
  }
}

// 구독 해제 함수
const unsubscribeFromProduct = (productId) => {
  if (stompClient && stompClient.connected) {
    const subscription = stompClient.subscriptions[`/room/${productId}`];
    if (subscription) {
      subscription.unsubscribe();
      console.log(`Unsubscribed from room ${productId}`);
    }
  }
}

// UI 업데이트 함수
const updateUIAfterDealComplete = (roomId) => {
  const dealButtonContainer = document.getElementById('dealButtonContainer');
  dealButtonContainer.innerHTML = '<p>완료된 거래입니다.</p>';

  const chatRoomElement = document.querySelector(`[data-product="${roomId}"]`);
  if (chatRoomElement) {
    chatRoomElement.style.backgroundColor = '#e0e0e0';  // 회색 배경으로 변경
  }

  // 채팅 입력 비활성화
  const chatInput = document.querySelector('section.input-div textarea');
  if (chatInput) {
    chatInput.disabled = true;
    chatInput.placeholder = '거래가 완료되어 더 이상 메시지를 보낼 수 없습니다.';
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString);
  const now = new Date();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hours = date.getHours().toString().padStart(2, '0');
  const minutes = date.getMinutes().toString().padStart(2, '0');

  if (date.getFullYear() === now.getFullYear())
    return `${month}월 ${day}일 ${hours}:${minutes}`;


  // 올해 날짜인 경우
  if (date.getFullYear() === now.getFullYear())
    return `${month}월 ${day}일 ${hours}:${minutes}`;


  // 작년 이전 날짜인 경우
  const year = date.getFullYear();
  return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`;
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