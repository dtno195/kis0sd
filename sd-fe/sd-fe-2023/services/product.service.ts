import apiClient from '@/services/core/api-client'

export const getListProducts = (data: any) => apiClient.get(`/product?${new URLSearchParams(data)}`)

export const getOneProduct = (id: any) => apiClient.get(`/product/${id}`)

export const createProduct = (data: any) => apiClient.post('/product', data)

export const updateProduct = (id: any, data: any) => apiClient.put(`/product/${id}`, data)

export const deleteProduct = (id: any) => apiClient.delete(`/product/${id}`)

export const exportExcel = (data: any) => apiClient.get(`/product/export?${data}`, {
  responseType: 'blob'
})

export const importExcel = (data: any) => apiClient.post('/product/import', data, { headers: { 'Content-Type': 'multipart/form-data' }})

export const findPagingProductGroup = (data: any) => apiClient.get(`/product-group?${data}`)

export const createProductGroup = (data: any) => apiClient.post('/product-group', data)

export const deleteProductGroup = (id: any) => apiClient.delete(`/product-group/${id}`)