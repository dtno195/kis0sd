import apiClient from '@/services/core/api-client'

export const getAllListCustomer = (data: any) => apiClient.get(`/customer?${data}`)

export const getOneCustomer = (id: any) => apiClient.get(`/customer/${id}`)

export const createCustomer = (data: any) => apiClient.post('/customer', data)

export const deleteCustomer = (id: any) => apiClient.delete(`/customer/${id}`)
