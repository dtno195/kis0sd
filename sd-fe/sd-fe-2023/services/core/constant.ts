export enum Role {
  ADMIN = 'ADMIN',
  MANAGER = 'MANAGER',
  USER = 'USER',
}

export enum ShopOrderStatus {
  EXECUTE = 'EXECUTE', // Đã đặt hàng
  DELIVERING = 'DELIVERING', // Đang vận chuyển
  COMPLETE = 'COMPLETE', // Hoàn thành
  CANCEL = 'CANCEL', // Đã hủy
}

export enum CustomerOrderStatus {
  EXECUTE = 'EXECUTE', // Đã đặt hàng
  DELIVERING = 'DELIVERING', // Đang vận chuyển
  COMPLETE = 'COMPLETE', // Hoàn thành
  CANCEL = 'CANCEL', // Đã hủy,
  EXCHANGE = 'EXCHANGE', // Đổi trả
}