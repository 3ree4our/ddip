export const SERVER_API = 'http://192.168.0.3:8080';

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