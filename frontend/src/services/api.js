import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

export const setAuthToken = (token) => {
  api.defaults.headers.common.Authorization = token ? `Bearer ${token}` : '';
};

export const fetchAnalytics = async (hotelId, startDate, endDate) => {
  const response = await api.get(`/analytics/hotel/${hotelId}`, {
    params: { startDate, endDate }
  });
  return response.data;
};

export default api;
