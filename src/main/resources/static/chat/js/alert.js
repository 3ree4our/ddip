import {getTotalUnreadCount, getUserChatrooms} from "./api.js";

let connectStatus = false;
let stompClient = '';

const getConnect = async () => {
  const allProducts = await getUserChatrooms();

  console.log("기존 product찾는거에서 변경 allProducts", allProducts)
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e);
    console.log('productsId 이것도 안나ㅗㅇ네', productsId)
    if (!connectStatus) connect(productsId);

    const unreadCounts = await getTotalUnreadCount();
    console.log('unreadCounts', unreadCounts)
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
  const alertImageContainer = document.querySelector(`#alertImgContainer`)
  const alertCount = document.querySelector('#alertCount')

  if (unreadCounts === undefined) unreadCounts = await getTotalUnreadCount();

  console.log('unreadCounts', unreadCounts)
  if (unreadCounts > 0) {
    alertImageContainer.style.backgroundColor = 'red';
    alertCount.textContent = unreadCounts;
    alertCount.style.display = 'block';
  } else {
    alertImageContainer.style.backgroundColor = '';
    alertCount.style.display = 'none';
  }

}