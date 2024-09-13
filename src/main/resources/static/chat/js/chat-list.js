import {getAllProduct, getAllChatroom, getChatroomByProductId} from "./api.js";

const path = location.pathname;
const productId = path.substring(path.lastIndexOf('/') + 1);

const allChatroom = await getAllChatroom();
const chatroom = await getChatroomByProductId('suwan', productId);
let stompClient = '';
let connectStatus = false;
let subscriptions = {};
let productsId = [];

console.log('send', allChatroom)
export const getConnect = async () => {
  const allProducts = await getAllProduct();
  console.log('product', allProducts)
  if (allProducts.length > 0) {
    allProducts.forEach(e => productsId.push(e.productId));
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
    subscriptions[productId] = stompClient.subscribe(`/room/${productId}`, chatMessage => {
      const messageObj = JSON.parse(chatMessage.body);
      //drawChat(messageObj);
      //TODO 해당 방에 그리기 ..
      const roomId = document.querySelector(`[data-product="${messageObj.roomId}"] > p`)
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
  const chatRoomArea = document.querySelector('#center');
  let html = '';
  if (allChatroom.length === 0) {
    html += `<h3>게시글이 없습니다.</h3>`
  } else {
    for (let chat of allChatroom) {
      html += `<div id="user" data-product="${chat.productId}" data-sender="${chat.senderId}">`;
      html += `<img src="/img/trend/bs-1.jpg" alt="물품사진"/>`
      html += `<p>${chat.message}</p></div>`
    }
  }

  chatRoomArea.innerHTML = html;

  const chatrooms = document.querySelectorAll('#user');
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
drawChatList(allChatroom);


const drawChat = (messageObjs) => {
  const chatRoomEle = document.querySelector('#chatRoomArea');

  // 기존 메시지 모두 제거 (초기화)
  chatRoomEle.innerHTML = '';

  messageObjs.forEach(messageObj => {
    const messageEle = document.createElement('p');
    messageEle.classList.add(messageObj.mine ? 'sendMessage' : 'receiveMessage');
    messageEle.textContent = messageObj.message;
    chatRoomEle.appendChild(messageEle);
  });
};
