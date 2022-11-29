import apiClient from '@/services/core/api-client'

export const getAllListWarehouses = (data: any) => apiClient.get(`/warehouse?${data}`)

export const getOneWarehouses = (id: any) => apiClient.get(`/warehouse/${id}`)

export const saveWarehouse = (data: any) => apiClient.post('/warehouse', data)

export const saveMultiWarehouse = (data: any) => apiClient.post('/product/add-multi', data)

export const getAllListWarehouseLogs = (data: any) => apiClient.get(`/product/logs?${data}`)