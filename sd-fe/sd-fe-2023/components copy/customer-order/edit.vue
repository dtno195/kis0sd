<template>
  <div class="card mb-5 mb-xl-10">
    <div class="card-body list-vertical">
      <div>
        <ValidationObserver ref="observer">
          <el-form :model="formOrder" :rules="rules" label-position="top">
            <el-row :gutter="20">
              <el-col :span="8">
                <h3>Thông tin đơn hàng</h3>
                <hr />

                <ValidationProvider
                  v-slot="{ errors }"
                  :rules="{ required: true }"
                  name="messengerUrl"
                >
                  <el-form-item label="Link chat" prop="messengerUrl">
                    <el-autocomplete
                      class="w-full"
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="formOrder.messengerUrl"
                      :fetch-suggestions="querySearchCustomerByMessengerUrl"
                      :trigger-on-focus="false"
                      @select="handleSelectCustomer"
                    >
                      <template slot-scope="{ item }">
                        <div class="autocomplete-item">
                          <span class="fw-bold autocomplete-item-title">{{
                            `${item.name} - ${item.phone}`
                          }}</span>
                          <span class="fs-7 autocomplete-item-subtitle">{{
                            item.address
                          }}</span>
                        </div>
                      </template>
                    </el-autocomplete>
                    <span class="el-form-item__error">
                      {{ errors[0] ? "Vui lòng nhập link chat!" : "" }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <ValidationProvider
                  v-slot="{ errors }"
                  :rules="{ required: true }"
                  name="receiverName"
                >
                  <el-form-item label="Tên người nhận" prop="receiverName">
                    <el-input
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="formOrder.receiverName"
                    />
                    <span class="el-form-item__error">
                      {{ errors[0] ? "Vui lòng nhập tên người nhận!" : "" }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <ValidationProvider v-slot="{ errors }" name="receiverPhone">
                  <el-form-item label="SĐT người nhận" prop="receiverPhone">
                    <el-autocomplete
                      class="w-full"
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="formOrder.receiverPhone"
                      :fetch-suggestions="querySearchCustomerByPhone"
                      :trigger-on-focus="false"
                      @select="handleSelectCustomer"
                    >
                      <template slot-scope="{ item }">
                        <div class="autocomplete-item">
                          <span class="fw-bold autocomplete-item-title">{{
                            `${item.name} - ${item.phone}`
                          }}</span>
                          <span class="fs-7 autocomplete-item-subtitle">{{
                            item.address
                          }}</span>
                        </div>
                      </template>
                    </el-autocomplete>
                    <span class="el-form-item__error">
                      {{ errors[0] ? "Vui lòng nhập SĐT người nhận!" : "" }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <ValidationProvider v-slot="{ errors }" name="receiverAddress">
                  <el-form-item
                    label="Địa chỉ nhận hàng"
                    prop="receiverAddress"
                  >
                    <el-input
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="formOrder.receiverAddress"
                    />
                    <span class="el-form-item__error">
                      {{ errors[0] ? "Vui lòng nhập địa chỉ nhận hàng!" : "" }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <el-form-item label="Ngày giao hàng dự kiến">
                  <el-date-picker
                    class="w-full"
                    v-model="formOrder.deliveryDate"
                    :picker-options="pickerOptions"
                    type="date"
                    placeholder="Chọn ngày giao hàng"
                  >
                  </el-date-picker>
                </el-form-item>

                <el-form-item label="Phí ship">
                  <!-- Shop hoặc khách hàng chịu phí ship -->
                  <el-radio-group v-model="formOrder.isShopBearShippingFee">
                    <el-radio border :label="false">Khách trả ship</el-radio>
                    <el-radio border :label="true">Freeship</el-radio>
                  </el-radio-group>
                </el-form-item>

                <el-form-item label="Phí giao hàng">
                  <my-currency-input v-model="formOrder.shippingFee" />
                </el-form-item>

                <el-form-item label="Đặt cọc">
                  <my-currency-input v-model="formOrder.deposit" />
                </el-form-item>

                <el-form-item label="Ghi chú">
                  <el-input type="textarea" v-model="formOrder.note" />
                </el-form-item>
              </el-col>

              <el-col :span="16">
                <h3>Danh sách sản phẩm</h3>
                <hr />
                <el-button type="info" @click="addMoreProduct">
                  Thêm sản phẩm
                </el-button>
                <div class="warning">
                  <p><strong>Note:</strong> Vui lòng nhập để tìm kiếm sản phẩm</p>
                </div>
                <div
                  class="form-list-item action-horizontal"
                  v-for="(product, index) in orderItemList"
                  :key="index"
                >
                  <div class="flex-grow-1">
                    <el-row :gutter="20">
                      <el-col :xl="12" :span="8">
                        <ValidationProvider
                          v-slot="{ errors }"
                          :rules="{
                            isDuplicate: orderItemList,
                            required: true,
                          }"
                          :name="'productId_' + index"
                        >
                          <el-form-item label="Chọn sản phẩm" prop="productId">
                            <el-select
                              class="w-full"
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              v-model="product.productId"
                              filterable
                              remote
                              :remote-method="getAllProducts"
                              :loading="loadingProduct"
                              placeholder="Nhập mã sản phẩm"
                              @change="getSelectProduct(index)"
                            >
                              <el-option
                                v-for="p in listProducts"
                                :key="p.id"
                                :label="`${p.code} | ${p.name} | Phân loại: ${p.size}`"
                                :value="p.id"
                              />
                            </el-select>
                            <span class="el-form-item__error">
                              {{ errors[0] }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
                      </el-col>
                      <el-col :xl="4" :span="5">
                        <ValidationProvider
                          v-slot="{ errors }"
                          rules="required"
                          :name="'quantity_' + index"
                        >
                          <el-form-item label="Số lượng" prop="quantity">
                            <el-input-number
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              :controls="false"
                              :min="1"
                              :max="10000"
                              v-model="product.quantity"
                              @change="onchangeQuantity(index)"
                            />
                            <span class="el-form-item__error">
                              {{ errors[0] ? "Vui lòng nhập số lượng!" : "" }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
                      </el-col>
                      <el-col :xl="4" :span="5">
                        <ValidationProvider
                          v-slot="{ errors }"
                          rules="required"
                          :name="'price_' + index"
                        >
                          <el-form-item label="Đơn giá" prop="price">
                            <el-input-number
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              :controls="false"
                              :min="0"
                              v-model="product.price"
                              @change="onChangePrice(index)"
                            />
                            <span class="el-form-item__error">
                              {{
                                errors[0] ? "Vui lòng nhập giá sản phẩm!" : ""
                              }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
                      </el-col>
                      <el-col :xl="4" :span="6">
                        <el-form-item label="Tổng cộng">
                          <el-input readonly v-model="product.totalPriceText" />
                        </el-form-item>
                      </el-col>
                    </el-row>
                  </div>
                  <div
                    class="action-horizontal__action-container action-horizontal__action-container--center"
                  >
                    <el-button
                      size="mini"
                      type="danger"
                      icon="el-icon-delete"
                      @click="deleteProduct(index)"
                      >Xóa</el-button
                    >
                  </div>
                </div>

                <el-row :gutter="20">
                  <el-col class="text-price"
                    >Thành tiền:
                    {{ Utils.formatPrice(totalPriceOrder) }}</el-col
                  >
                </el-row>
              </el-col>
            </el-row>
          </el-form>
        </ValidationObserver>
      </div>
      <div class="card-footer d-flex justify-content-end py-4 px-8">
        <el-button @click="goBack">Quay lại</el-button>
        <el-button @click="resetForm">Làm mới</el-button>
        <el-button :disabled="isLoading" type="primary" @click="saveCustomerOrder()">Lưu</el-button>
      </div>
    </div>
  </div>
</template>
<script>
import { Utils } from "@/services/core/utils";
import {
  saveCustomerOrders,
  exchangeCustomerOrders,
  getOneCustomerOrders,
} from "@/services/customer-order.service";
import { getListProducts } from "@/services/product.service";
import { getAllListCustomer } from "@/services/customer.service";

export default {
  name: "CustomerOrderEdit",
  layout: "main-layout",
  props: ["id", "isExchange"],
  data() {
    return {
      Utils: Utils,
      isLoading: false,
      customerOrderId: this.id,
      listProducts: [],
      formOrder: {
        receiverName: "",
        receiverAddress: "",
        receiverPhone: "",
        shipperPhone: "",
        shipperName: "",
        shippingFee: "",
        messengerUrl: "",
        deposit: "",
        deliveryDate: "",
        listProducts: [{ id: "", quantity: 0, price: 0 }],
        note: "",
        isShopBearShippingFee: true,
      },
      orderItemList: [{ productId: "", quantity: 0, price: 0 }],
      pickerOptions: {
        disabledDate(time) {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          return time.getTime() < today;
        },
      },
      rules: {
        messengerUrl: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        receiverName: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        price: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        quantity: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        productId: [
          {
            required: true,
            trigger: "submit",
          },
        ],
      },
      totalPriceOrder: 0,
      // Pagination
      totalElements: 0,
      page: 1,
      size: 10,
      loadingProduct: false,
    };
  },
  mounted() {
    if (this.customerOrderId) {
      this.getDetailCustomerOrder();
    } else {
      this.$router.go(-1);
    }
  },
  methods: {
    getDetailCustomerOrder() {
      getOneCustomerOrders(this.customerOrderId).then((res) => {
        this.formOrder = res.data;
        const listSelectedProduct = [];
        this.orderItemList = this.formOrder.orderItemList
          .filter((i) => i.productId !== null)
          .map((product) => {
            product.totalPrice = (product.price || 0) * (product.quantity || 0);
            product.totalPriceText = Utils.formatPrice(product.totalPrice);
            listSelectedProduct.push({
              id: product.productId,
              name: product.productName,
              code: product.productCode,
              size: product.productSize,
            });
            return product;
          });
        this.listProducts = listSelectedProduct;
        const totalPrice = this.orderItemList.reduce((prev, curr) => {
          return prev + (curr.totalPrice || 0);
        }, 0);
        this.totalPriceOrder = totalPrice;
      });
    },
    getAllProducts(searchText) {
      this.loadingProduct = true;
      getListProducts({
        name: searchText,
        code: searchText,
      })
        .then((res) => {
          this.listProducts = res.data?.content;
        })
        .finally(() => (this.loadingProduct = false));
    },
    addMoreProduct() {
      this.orderItemList.push({
        productId: "",
        quantity: 0,
        price: 0,
      });
    },
    goBack() {
      this.$emit("close", this.customerOrderId);
    },
    resetForm() {
      this.formOrder = {
        customerOrderName: "",
        transporter: "",
        source: "",
        deliveryDate: "",
        listProducts: [{ id: "", quantity: 0, price: 0 }],
        note: "",
        isShopBearShippingFee: true,
      };
      this.$refs.observer.reset();
    },
    async saveCustomerOrder() {
      if (this.orderItemList.length === 0) {
        this.$notify({
          type: "error",
          message: "Danh sách sản phẩm trống!",
          position: "bottom-right",
        });
        return;
      }
      const isValid = await this.$refs.observer.validate();
      if (isValid) {
        this.formOrder.listProducts = this.orderItemList;
        this.isLoading = true;
        if (this.isExchange) {
          exchangeCustomerOrders(this.formOrder).then(() => {
            this.$notify({
              type: "success",
              message: "Đổi/trả đơn hàng thành công!",
              position: "bottom-right",
            });
            this.$emit("close");
          });
          return;
        }
        saveCustomerOrders(this.formOrder)
          .then(() => {
            this.$notify({
              type: "success",
              message: "Sửa đơn hàng thành công!",
              position: "bottom-right",
            });
            this.$emit("close");
          })
          .finally(() => (this.isLoading = false));
      }
    },
    getSelectProduct(index) {
      let product = this.orderItemList[index];
      let p = this.listProducts.find((i) => i.id === product.productId);
      product.price = p.price;
      product.name = p.name;
      product.totalPrice = p.price * product.quantity; //TODO
      product.totalPrice = (product.price || 0) * (product.quantity || 0);
      product.totalPriceText = Utils.formatPrice(product.totalPrice);
      const totalPrice = this.orderItemList.reduce((prev, curr) => {
        return prev + (curr.totalPrice || 0);
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    onchangeQuantity(index) {
      let p = this.orderItemList[index];
      p.totalPrice = (p.price || 0) * (p.quantity || 0);
      p.totalPriceText = Utils.formatPrice(p.totalPrice);
      const totalPrice = this.orderItemList.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    onChangePrice(index) {
      let p = this.orderItemList[index];
      p.totalPrice = (p.price || 0) * (p.quantity || 0);
      p.totalPriceText = Utils.formatPrice(p.totalPrice);
      const totalPrice = this.orderItemList.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    deleteProduct(index) {
      this.orderItemList.splice(index, 1);
      const totalPrice = this.orderItemList.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    async querySearchCustomerByPhone(queryString, cb) {
      const param = new URLSearchParams({
        page: this.page - 1,
        size: this.size,
        phone: queryString,
      });
      const res = await getAllListCustomer(param);
      if (res.data && res.data.content) {
        const listSuggest = res.data.content.map((item) => {
          item.value = `${item.name} - ${item.phone} - ${item.address}`;
          return item;
        });
        cb(listSuggest);
      }
    },
    async querySearchCustomerByMessengerUrl(queryString, cb) {
      const param = new URLSearchParams({
        page: this.page - 1,
        size: this.size,
        messengerUrl: queryString,
      });
      const res = await getAllListCustomer(param);
      if (res.data && res.data.content) {
        const listSuggest = res.data.content.map((item) => {
          item.value = `${item.name} - ${item.phone} - ${item.address}`;
          return item;
        });
        cb(listSuggest);
      }
    },
    handleSelectCustomer(item) {
      this.formOrder.receiverName = item.name;
      this.formOrder.receiverPhone = item.phone;
      this.formOrder.receiverAddress = item.address;
      this.formOrder.messengerUrl = item.messengerUrl;
    },
  },
};
</script>

<style lang="scss" scoped>
.form-product {
  border: 1px solid #ccc;
  margin: 10px 0 !important;
}
.text-price {
  text-align: end;
  margin: 10px 0;
  color: red;
}
.text-success {
  color: green;
}
.border-red {
  border: 1px solid red !important;
}
</style>
