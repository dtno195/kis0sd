import apiClient from '@/services/core/api-client'

export const getAllListUsers = (data: any) => apiClient.get(`/user?${data}`)

export const getOneUser = (id: any) => apiClient.get(`/user/${id}`)

export const createUser = (data: any) => apiClient.post('/user', data)

export const updateUser = (id: any, data: any) => apiClient.put(`/user/${id}`, data)

export const deleteUser = (id: any) => apiClient.delete(`/user/${id}`)
