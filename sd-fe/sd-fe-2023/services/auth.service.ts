import apiClient from '@/services/core/api-client'

export const login = (data: any) => apiClient.post('/auth/sign-in', data)

export const logout = (data: any) => apiClient.post('/auth/logout')

export const register = (data: any) => apiClient.post('/user', data)

export const forgotPassword = (data: any) => apiClient.post('/auth/forgot-password', data)

export const verifyToken = (data: any) => apiClient.post('/auth/forgot-password/verify-token', data)

export const updatePassword = (data: any) => apiClient.post('/auth/update-password', data)
