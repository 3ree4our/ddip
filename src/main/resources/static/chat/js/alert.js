import {getAllProduct} from "./api.js";

let connectStatus = false;
let stompClient = '';

const getConnect = async () => {
  const allProducts = await getAllProduct();
  if (allProducts.length > 0) {
    const productsId = allProducts.map(e => e.productId);
    if (!connectStatus) connect(productsId);
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
    })
  });
  connectStatus = true;
}

const subscribeToProduct = (productId) => {
  if (stompClient && stompClient.connected) {
    stompClient.subscribe(`/room/${productId}`, chatMessage => {
      const targetEle = document.querySelector('#alertImgContainer');
      targetEle.style.backgroundColor = 'red';
    })
  }
}