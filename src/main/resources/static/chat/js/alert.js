import {getTotalUnreadCount, getUserChatrooms} from "./api.js";

const chatListAEle = document.querySelector('#chatList');
let connectStatus = false;
let stompClient = '';

chatListAEle.onclick = (e) => {
  e.preventDefault();
  const accessToken = localStorage.getItem('access-token');

  if (accessToken === null) {
    location.href = '/member/login'
    alert('로그인을 먼저 진행해주세요.')
  } else {
    location.href = '/chat-list'
  }
}

const getConnect = async () => {
  const allProducts = await getUserChatrooms();
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e);
    if (!connectStatus) connect(productsId);

    const unreadCounts = await getTotalUnreadCount();
    updateUnreadMessageNotification(unreadCounts);
  }
}

getConnect();

const connect = (productsId) => {
  const socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);

  stompClient.connect({}, function (frame) {
    console.log('frame', frame);
    productsId.forEach(productId => {
      subscribeToProduct(productId);
    });
  });
  connectStatus = true;
}

const subscribeToProduct = (productId) => {
  if (stompClient && stompClient.connected) {
    stompClient.subscribe(`/room/${productId}`, chatMessage => {
      updateUnreadMessageNotification();
    })
  }
}

const updateUnreadMessageNotification = async (unreadCounts) => {
  const alertCount = document.querySelector('#alertCount')

  if (unreadCounts === undefined) unreadCounts = await getTotalUnreadCount();

  if (unreadCounts > 0) {
    alertCount.textContent = unreadCounts;
    alertCount.style.display = 'block';
  } else {
    alertCount.style.display = 'none';
  }

}