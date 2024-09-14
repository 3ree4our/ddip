import {getAllProduct, getAllChatroom, getChatroomByProductId} from "./api.js";

const allChatroom = await getAllChatroom();
let stompClient = '';
let connectStatus = false;
let subscriptions = {};

export const getConnect = async () => {
  const allProducts = await getAllProduct();
  const chatList = [];
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e.productId);
    if (!connectStatus) connect(productsId);

    /* for (const e of allProducts) {
       const messageObj = await getChatroomByProductId('suwan', e.productId);
       chatList.push(messageObj);
     }
     drawChatList(chatList);*/
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
    subscriptions[productId] = stompClient.subscribe(`/room/${productId}`, chatMessage => {
      const messageObj = JSON.parse(chatMessage.body);
      const roomId = document.querySelector(`div[data-product="${messageObj.roomId}"] > p`)
      roomId.style.color = 'red';
    })
  }
}

function sendChat(productId) {
  const messageInputEle = document.querySelector('input[name="message"]');
  const messageObj = {
    message: messageInputEle.value
  }

  if (messageInputEle.value != '') {
    stompClient.send(`/messages/${productId}`, {}, JSON.stringify(messageObj));
    messageInputEle.value = '';
  }
}

const drawChatList = (allChatroom) => {
  const chatRoomArea = document.querySelector('.chat-list');
  let html = '';
  if (allChatroom.length === 0) {
    html += `<h3>게시글이 없습니다.</h3>`
  } else {
    for (let chat of allChatroom) {
      console.log('chat_ ', chat)
      if (chat.length !== undefined) {
        html += `<div id="user" data-product="${chat.productId}">`;
        html += `<img src="/img/trend/bs-1.jpg" alt="물품사진"/>`
        html += `<p>${chat.message}</p></div>`
      } else {
        html += `<div id="user" data-product="${chat.productId}">`;
        html += `<img src="/img/trend/bs-1.jpg" alt="물품사진"/>`
        html += `<p>${chat.message}</p></div>`
      }
    }
  }

  chatRoomArea.innerHTML = html;

  const chatrooms = document.querySelectorAll('#user');
  if (chatrooms.length > 0) {
    chatrooms.forEach(e => {
      e.addEventListener('click', async (e) => {
        let roomId = '';
        if (e.target.tagName !== 'DIV') {
          roomId = e.target.parentElement.dataset.product;
        } else {
          roomId = e.target.dataset.product;
        }
        const messageObj = await getChatroomByProductId('suwan', roomId);
        drawChat(messageObj)
      })
    })
  }
}
console.log('allChatroom', allChatroom)
drawChatList(allChatroom);


const drawChat = (messageObjs) => {
  const chatRoomEle = document.querySelector('#chatRoomArea');

  // 기존 메시지 모두 제거 (초기화)
  chatRoomEle.innerHTML = '';

  messageObjs.forEach(messageObj => {
    console.log('할 수 있을까', messageObj)
    const messagePEle = document.createElement('p');
    const submitBtnEle = document.createElement('button');
    messagePEle.classList.add(messageObj.mine ? 'sendMessage' : 'receiveMessage');
    messagePEle.textContent = messageObj.message;

    submitBtnEle.textContent = '전송';
    submitBtnEle.dataset.roomId = messageObj.productId;

    chatRoomEle.appendChild(messagePEle);
  });
};
