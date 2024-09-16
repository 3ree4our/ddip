import {getAllProduct, getAllChatroom, getChatroomByProductId} from "./api.js";

let stompClient = '';
let connectStatus = false;
let focusChatroom = '';

export const getConnect = async () => {
  const allProducts = await getAllProduct();
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e.productId);
    if (!connectStatus) connect(productsId);
  }
}
getConnect();

function connect(productsId) {
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
    stompClient.subscribe(`/room/${productId}`, chatMessage => {
      const messageObj = JSON.parse(chatMessage.body);
      const roomElement = document.querySelector(`[data-product="${messageObj.roomId}"]`);
      roomElement.querySelector('p:last-child').innerText = messageObj.message;

      if (Number(messageObj.roomId) === Number(focusChatroom)) appendMessageTag(messageObj);
      else roomElement.style.backgroundColor = 'red'

    })
  }
}

//===========메시지 전송===============
document.querySelector("section.input-div textarea").addEventListener("keydown", e => {
  if (e.keyCode === 13 && !e.shiftKey) {
    e.preventDefault();
    sendMessage();
    e.target.value = '';
    e.target.focus();
  }
});

const sendMessage = () => {
  const messageInputEle = document.querySelector('section.input-div textarea');

  const headers = {
    'Authorization': 'Bearer ' + localStorage.getItem('access_token')
  };

  const messageObj = {
    message: messageInputEle.value
  }

  if (messageInputEle.value != '') {
    stompClient.send(`/messages/${focusChatroom}`, headers, JSON.stringify(messageObj));
  }

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
  if (messageObj.length === undefined) {
    const chatLi = createMessageTag(messageObj.type, messageObj.nickname, messageObj.message);
    document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
    document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
  } else {
    for (let message of messageObj) {
      // 첫 번째 채팅 => 대화 신청 중간 정렬 나중에 (서버에서 type을 center로 보내게 할 수 있을까?
      const chatLi = createMessageTag(message.type, message.sender.nickname, message.message);
      document.querySelector("#chatWrapper .chat:not(.format) ul").appendChild(chatLi);
      document.querySelector("#chatWrapper .chat").scrollTop = document.querySelector("section.chat").scrollHeight;
    }
  }
  const chatContainer = document.querySelector("#chatWrapper .chat");
  chatContainer.scrollTop = chatContainer.scrollHeight;
}

//===========================================================================================

const drawChatList = async () => {
  const allChatroom = await getAllChatroom();
  const chatRoomArea = document.querySelector('.chat-list');
  let productIds = [];
  let html = '';
  if (allChatroom.length === 0) {
    html += `<h3>게시글이 없습니다.</h3>`
  } else {
    for (let chat of allChatroom) {
      productIds.push(chat.productId);
      html += `<div class="user" data-product="${chat.productId}">`;
      html += `<img src="/img/trend/bs-1.jpg" alt="물품사진"/>`
      html += `<div><p>${chat.sender.nickname}</p>`
      html += `<p>${chat.message}</p></div></div>`
    }
  }
  connect(productIds);
  chatRoomArea.innerHTML = html;

  const chatrooms = document.querySelectorAll('.user');
  if (chatrooms.length > 0) {
    chatrooms.forEach(e => {
      e.addEventListener('click', async (e) => {

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
        const messageObj = await getChatroomByProductId('suwan', roomId);
        console.log('messageObj', messageObj)
        appendMessageTag(messageObj);
      })
    })
  }
}
drawChatList()

const addClassName = (targetEle) => {
  targetEle.style.backgroundColor = ''

  const elements = document.querySelector('.chat-list');
  const children = elements.children;
  for (let child of children) child.classList.remove('active')

  targetEle.classList.add('active');
}
