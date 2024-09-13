export const SERVER_API = 'http://localhost:8080';

const accessToken = localStorage.getItem('access_token');

// 1번 => suwan
export const getAllProduct = async (member = 'suwan') => {
  const response = await fetch(`${SERVER_API}/${member}/products`, {
    method : 'get',
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
  return response.json();
}

export const getAllChatroom = async (member = 'suwan') => {
  const response = await fetch(`${SERVER_API}/${member}/chatrooms`, {
    method : 'get',
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  });
  return response.json();
}

export const getChatroomByProductId = async (member = 'suwan', chatroomId) => {
  const response = await fetch(`${SERVER_API}/${member}/chatrooms/${chatroomId}`, {
    method : 'get',
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  });
  return response.json();
}

export const createChatroom = async (productId) => {
  const response = await fetch(`${SERVER_API}/room/${productId}`, {
    method : 'get',
    headers: {
      "Authorization": `Bearer ${accessToken}`
    }
  })
  return response.json();
}

// 모든 채팅은 productId로 chat 테이블을 뒤져서 찾은 뒤 반환하기
// 그리고 개인 채팅은 .. 어떻게? ..