import moment from "moment";

export class Utils {

  static formatFileSize = (bytes: number, decimalPoint?: number) => {
    if (bytes == 0) return "0 Bytes";
    const k = 1000,
      dm = decimalPoint || 2,
      sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"],
      i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
  };

  static getSimpleDateFormat(date: Date | string) {
    if (date) {
      return moment(date).format("DD/MM");
    }
    return "";
  };

  static formatDate(date: Date | string) {
    if (date) {
      return moment(date).format("DD/MM/YYYY HH:mm:ss");
    }
    return "";
  };

  static formatPrice(price?: number) {
    return price?.toLocaleString("it-IT", {
      style: "currency",
      currency: "VND",
    });
  };

  static textToArray(text?: string, seperator: string = '|') {
    if (!text) {
      return [];
    }
    const result = text.split(seperator).map(item => item.trim());
    return result;
  }
}
