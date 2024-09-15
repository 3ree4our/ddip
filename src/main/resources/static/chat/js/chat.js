const conversationBtn = document.querySelector('#conversationApply');
const path = location.pathname;
const productId = path.substring(path.lastIndexOf('/') + 1);

let stompClient = '';

conversationBtn.addEventListener('click', async () => {
  const a = await connect(productId);
  sendChat(productId);
})


function connect(productId) {
  var socket = new SockJS("/ws-stomp");
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('대화 채팅 connect 생성: ' + frame);
    sendChat(productId);
    stompClient.subscribe(`/room/${productId}`);
  });
}

function sendChat(productId) {
  const messageObj = {
    message: "채팅방이 생성되었습니다."
  }

  stompClient.send(`/messages/${productId}`, {}, JSON.stringify(messageObj));

  location.href = `/chatrooms/${productId}`;
}