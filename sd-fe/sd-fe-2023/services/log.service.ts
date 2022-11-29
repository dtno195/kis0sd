import apiClient from '@/services/core/api-client'

export const getAllListLogFiles = () => apiClient.get(`/logs`)

export const downloadLogFile = (data: any) => apiClient.post(`/logs/download`, data, {responseType: 'blob'})