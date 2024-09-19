const conversationBtn = document.querySelector('#conversationApply');
const path = location.pathname;
const productId = path.substring(path.lastIndexOf('/') + 1);

let stompClient = '';

const getAuthHeader = () => {
  const token = localStorage.getItem('access-token');
  if (!token) {
    console.error('No access token found');
    return null;
  }

  const headers = {
    'Authorization': `Bearer ${token}`
  };

  return headers;
}

conversationBtn.addEventListener('click', async () => {
  const a = await connect(productId);
  sendChat(productId);
})


function connect(productId) {
  var socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect(getAuthHeader(), function (frame) {
    sendChat(productId);
    stompClient.subscribe(`/room/${productId}`);
  });
}

function sendChat(productId) {
  const messageObj = {
    message: "할롱~"
  }

  stompClient.send(`/messages/${productId}`, getAuthHeader(), JSON.stringify(messageObj));

  location.href = `/chat-list`;
}