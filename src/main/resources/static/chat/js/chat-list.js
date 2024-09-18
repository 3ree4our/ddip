import {
  getAllProduct,
  getAllChatroom,
  getChatroomByProductId,
  getUnreadMessageCounts,
  imageUpload,
  markRead, SERVER_API
} from "./api.js";
import {clearImageSelection, getSelectedImagesData} from "./image.js";

let stompClient = '';
let connectStatus = false;
let focusChatroom = '';
let unreadMessages = {};

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
      selectedImages.forEach(file => {
        formData.append('files', file);
      });
      if (selectedImages.length > 0) {
        formData.append('chatId', messageObj.messageId);
        const imageIds = await imageUpload(formData);
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
    'Authorization': 'Bearer ' + localStorage.getItem('access_token')
  };

  try {

    if (message !== '' || selectedImages.length > 0) {
      const messageObj = {message}
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
  console.log('messageObj 넘겨받는..!! ', messageObj)
  let chatLi = '';
  if (messageObj.length === undefined) {
    chatLi = createMessageTag(messageObj.type, messageObj.nickname, messageObj.message);
    document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
    document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
  } else {
    for (let message of messageObj) {
      chatLi = createMessageTag(message.type, message.sender.nickname, message.message);
      document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
      document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
    }
  }

  if (messageObj.imageUrls.length > 0) {
    const imageContainer = document.createElement('div');
    imageContainer.className = 'message-images';
    messageObj.imageUrls[0].forEach(url => {
      const img = document.createElement('img');
      img.src = `${SERVER_API}/api/images/${url}`;
      img.alt = 'upload image';
      img.style.maxWidth = '200px';
      img.style.maxHeight = '200px';
      img.style.margin = '5px';
      imageContainer.appendChild(img);
    })
    chatLi.querySelector('.message').appendChild(imageContainer);
  }
  const chatContainer = document.querySelector("#chatWrapper .chat");
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

//===========================================================================================

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
  console.log('messageObj', messageObj);
  appendMessageTag(messageObj);
}


const drawChatList = async () => {
  const allChatroom = await getAllChatroom();
  const chatRoomArea = document.querySelector('.chat-list');
  let productIds = [];

  if (allChatroom.length === 0) {
    chatRoomArea.innerHTML = '<h3>게시글이 없습니다.</h3>';
  } else {
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
  const div = document.createElement('div');
  div.className = 'user';
  div.setAttribute('data-product', chat.productId);
  div.innerHTML = `
    <img src="/img/trend/bs-1.jpg" alt="물품사진"/>
    <div>
      <p>${chat.sender.nickname}</p>
      <p>${chat.message}</p>
    </div>
    <span class="unread-badge" style="display: none;"></span>
  `;
  div.addEventListener('click', chatRoomClickHandler);
  return div;
}

getConnect();
drawChatList().then(() => {
  initializeUnreadCounts();
});