export const SERVER_API = 'http://localhost:8080';

// 1번 => suwan
export const getAllProduct = async (member = 1) => {
  const accessToken = localStorage.getItem('access_token');
  const response = await fetch(`${SERVER_API}/${member}/products`,{
    method:'get',
    headers:{
      "Authorization": `Bearer ${accessToken}`
    }
  })
  return response.json();
}

export const getAllChatroom = async (member = 'suwan') => {
  const response = await fetch(`${SERVER_API}/${member}/chatrooms`);
  return response.json();
}

export const getChatroomByProductId = async (member = 'suwan', chatroomId) => {
  const response = await fetch(`${SERVER_API}/${member}/chatrooms/${chatroomId}`);
  return response.json();
}

export const createChatroom = async (productId) => {
  const response = await fetch(`${SERVER_API}/room/${productId}`)
  return response.json();
}