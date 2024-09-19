export const SERVER_API = 'http://localhost:8080';

const accessToken = localStorage.getItem('access_token');
const headers = {
  'Authorization': 'Bearer ' + localStorage.getItem('access_token')

};
// 1번 => suwan
export const getAllProduct = async () => {
  const response = await fetch(`${SERVER_API}/products`, {
    method: 'get',
    headers
  })
  return response.json();
}

export const getAllChatroom = async () => {
  const response = await fetch(`${SERVER_API}/chatrooms`, {
    method: 'get',
    headers
  });
  return response.json();
}

export const getChatroomByProductId = async (chatroomId) => {
  const response = await fetch(`${SERVER_API}/chatrooms/${chatroomId}`, {
    method: 'get',
    headers
  });
  return response.json();
}

export const createChatroom = async (productId) => {
  const response = await fetch(`${SERVER_API}/room/${productId}`, {
    method: 'get',
    headers
  })
  return response.json();
}

export const getUnreadMessageCounts = async (productIds) => {
  try {
    const token = localStorage.getItem('access_token');
    const requests = productIds.map(productId =>
        fetch(`${SERVER_API}/${productId}/unread-count`, {
          method : 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type' : 'application/json'
          }
        }).then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
        }).then(count => ({productId, count}))
    );

    const results = await Promise.all(requests);

    const unreadCounts = {};
    results.forEach(({productId, count}) => {
      unreadCounts[productId] = count;
    });

    return unreadCounts;
  } catch (error) {
    console.error('Failed to fetch unread message counts:', error);
    return {};
  }
}

export const markRead = async (productId) => {
  await fetch(`/${productId}/mark-read`, {
    method : 'POST',
    headers: {
      'Authorization': 'Bearer ' + localStorage.getItem('access_token')
    }
  });
}

export const imageUpload = async (formData) => {
  const result = await fetch(`${SERVER_API}/api/images/upload`, {
    method: 'POST',
    headers,
    body  : formData
  })

  return result.json();
}

export const getImageUrl = async (chatId) => {
  const result = await fetch(`${SERVER_API}/api/images/chat/${chatId}`)
  return result.json();
}