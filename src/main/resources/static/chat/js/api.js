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
};

// 사용 예시
// const updateUnreadCounts = async () => {
//   const unreadCounts = await getUnreadMessageCounts();
//   Object.entries(unreadCounts).forEach(([roomId, count]) => {
//     updateUnreadBadge(roomId, count);
//   });
// };