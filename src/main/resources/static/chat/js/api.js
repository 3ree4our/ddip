export const SERVER_API = 'http://localhost:8080';

export const headers = {
  'Authorization': 'Bearer ' + localStorage.getItem('access-token')
}

export const getAllProduct = async () => {
  const response = await fetch(`/products`, {
    method: 'get',
    headers
  })
  return response.json();
}

export const getAllChatroom = async () => {
  const response = await fetch(`/chatrooms`, {
    method: 'get',
    headers
  });
  return response.json();
}

export const getChatroomByProductId = async (chatroomId) => {
  const response = await fetch(`/chatrooms/${chatroomId}`, {
    method: 'get',
    headers
  });
  return response.json();
}

export const getUnreadMessageCountsByProducts = async (productIds) => {
  try {
    const requests = productIds.map(productId =>
        fetch(`/${productId}/unread-count`, {
          method: 'GET',
          headers
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

export const getTotalUnreadCount = async () => {
  try {
    const response = await fetch(`/chatrooms/total-unread-count`, {
      headers
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  } catch (error) {
    console.error('Failed to fetch total unread count', error);
    return 0;
  }
}

export const getUserChatrooms = async () => {
  const response = await fetch(`/user/chatrooms`, {
    headers
  });

  return response.json();
}

export const markRead = async (productId) => {
  await fetch(`/${productId}/mark-read`, {
    method: 'POST',
    headers
  });
}

export const imageUpload = async (formData) => {
  const result = await fetch(`/api/images/upload`, {
    method: 'POST',
    headers,
    body  : formData
  })

  return result.json();
}

export const getImageUrl = async (chatId) => {
  const result = await fetch(`/api/images/chat/${chatId}`)
  return result.json();
}

export const getDealStatus = async (productId) => {
  await fetch(`/deal/products/${productId}`)
}

export const completeDeal = async (productId) => {
  const response = await fetch(`/deal/${productId}/complete`, {
    method: 'POST',
    headers
  });

  return response;
}

export const cancelDeal = async (productId) => {
  const response = await fetch(`/deal/${productId}/cancel`, {
    method : 'POST',
    headers: {
      'Authorization': 'Bearer ' + localStorage.getItem('access-token')
    }
  });

  return response;
}