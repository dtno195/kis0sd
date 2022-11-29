import apiClient from "@/services/core/api-client";

export const getAllListCustomerOrders = (data: any) =>
  apiClient.get(`/customer-order?${data}`);

export const getOneCustomerOrders = (id: any) =>
  apiClient.get(`/customer-order/${id}`);

export const changeStatusToExecute = (id: any) =>
  apiClient.post(`/customer-order/change-to-execute/${id}`);

export const changeStatusToCancel = (data: any) =>
  apiClient.post(`/customer-order/change-to-cancel/${data.id}`, data);

export const changeStatusToComplete = (id: any) =>
  apiClient.post(`/customer-order/change-to-complete/${id}`);

export const changeStatusToDelivering = (data: any) =>
  apiClient.put(`/customer-order/change-to-delivering`, data);

export const deliveryManyCustomerOrder = (data: any) =>
  apiClient.post(`/customer-order/delivery-many`, data);

export const completeManyCustomerOrder = (data: any) =>
  apiClient.post(`/customer-order/complete-many`, data);

export const cancelManyCustomerOrder = (data: any) =>
  apiClient.post(`/customer-order/cancel-many`, data);

export const saveCustomerOrders = (data: any) =>
  apiClient.post("/customer-order", data);

export const exchangeCustomerOrders = (data: any) =>
  apiClient.post("/customer-order/exchange", data);

export const saveCustomerOrderDelivery = (data: any) =>
  apiClient.post("/customer-order/delivery", data);

export const getCustomerOrderDeliveries = (id: any) =>
  apiClient.get(`/customer-order/delivery/${id}`);

export const cancelCustomerOrder = (id: any) =>
  apiClient.post(`/customer-order/cancel-order/${id}`);

export const cancelCustomerOrderDelivery = (id: any) =>
  apiClient.post(`/customer-order/delivery/cancel/${id}`);

export const markedAsHaveTrackingOrder = (data: any) =>
  apiClient.post(`/customer-order/marked-have-tracking-order-many`, data);

export const exportToExcelGHTK = (data: any) =>
  apiClient.get(`/customer-order/export-excel-to-ghtk?${data}`, {
    responseType: "blob",
  });

export const updateTrackingOrderDelivery = (data: any) =>
  apiClient.post(`/customer-order/delivery/update-tracking-order`, data);