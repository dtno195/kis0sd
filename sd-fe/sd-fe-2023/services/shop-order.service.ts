import apiClient from "@/services/core/api-client";

export const getAllListShopOrders = (data: any) =>
  apiClient.get(`/shop-order?${data}`);

export const getOneShopOrders = (id: any) => apiClient.get(`/shop-order/${id}`);

export const saveShopOrders = (data: any) =>
  apiClient.post("/shop-order", data);

export const cancelShopOrder = (id: any) =>
  apiClient.post(`/shop-order/cancel-order/${id}`);

export const changeStatusToComplete = (id: any) =>
  apiClient.post(`/shop-order/change-to-complete/${id}`);

export const saveShopOrderDelivery = (data: any) =>
  apiClient.post("/shop-order/delivery", data);

export const getShopOrderDeliveries = (id: any) =>
  apiClient.get(`/shop-order/delivery/${id}`);

export const cancelShopOrderDelivery = (id: any) =>
  apiClient.post(`/shop-order/delivery/cancel/${id}`);
