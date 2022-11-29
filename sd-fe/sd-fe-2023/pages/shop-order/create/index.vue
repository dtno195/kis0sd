<template>
  <div class="shop-order-create-page">
    <app-tab-bar />
    <app-page-title title="Tạo đơn hàng nội bộ" />

    <div class="card mb-5 mb-xl-10" v-loading="isLoading">
      <div class="card-body">
        <ValidationObserver ref="observer">
          <el-form :model="formOrder" :rules="rules" label-position="top">
            <el-row :gutter="20">
              <el-col :span="8">
                <h3>Thông tin đơn hàng</h3>
                <hr />
                <el-form-item label="Tên đơn hàng">
                  <el-input v-model="formOrder.shopOrderName" data-cy="shopOrderName" />
                </el-form-item>
                <ValidationProvider
                  v-slot="{ errors }"
                  :rules="{ required: false }"
                  name="transporter"
                >
                  <el-form-item label="Nhà vận chuyển" prop="transporter">
                    <el-input
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="formOrder.transporter"
                      data-cy="transporter"
                    />
                    <span class="el-form-item__error">
                      {{ errors[0] ? "Vui lòng nhập nhà vận chuyển!" : "" }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <el-form-item label="Ngày giao hàng dự kiến">
                  <el-date-picker
                    class="w-full"
                    v-model="formOrder.deliveryDate"
                    :picker-options="pickerOptions"
                    type="date"
                    data-cy="deliveryDate"
                    placeholder="Chọn ngày giao hàng"
                  >
                  </el-date-picker>
                </el-form-item>

                <el-form-item label="Nguồn hàng">
                  <el-input v-model="formOrder.source" data-cy="source" />
                </el-form-item>

                <el-form-item label="Ghi chú">
                  <el-input type="textarea" v-model="formOrder.note" data-cy="note" />
                </el-form-item>
              </el-col>

              <el-col :span="16">
                <h3>Danh sách sản phẩm</h3>
                <hr />
                <el-button type="info" @click="addMoreProduct" data-cy="addProduct">
                  Thêm sản phẩm
                </el-button>

                <div
                  class="form-list-item action-horizontal"
                  v-for="(product, index) in formOrder.listProducts"
                  :key="index"
                >
                  <div class="flex-grow-1">
                    <el-row :gutter="20">
                      <el-col :xl="12" :span="8">
                        <ValidationProvider
                          v-slot="{ errors }"
                          :rules="{
                            isDuplicate: formOrder.listProducts,
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
                              :data-cy="'searchText_' + index"
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
                              :data-cy="'quantity_' + index"
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
        <el-button v-if="user.authority !== Role.USER" :disabled="isLoading" type="primary" @click="saveShopOrder()" data-cy="saveShopOrder">Lưu</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import { Role } from "@/services/core/constant";
import { saveShopOrders } from "@/services/shop-order.service";
import { getListProducts } from "@/services/product.service";

export default {
  name: "shop-order-create",
  layout: "main-layout",
  computed: {
    user() {
      const u = this.$store.state.auth.user;
      return u
        ? u
        : {
            username: "",
            firstName: "",
            lastName: "",
            authority: "",
          };
    },
  },
  data() {
    return {
      Role: Role,
      Utils: Utils,
      isLoading: false,
      listProducts: [],
      formOrder: {
        shopOrderName: "",
        deliveryDate: "",
        transporter: "",
        source: "",
        listProducts: [{ productId: "", quantity: 0, price: 0 }],
        note: "",
      },
      pickerOptions: {
        disabledDate(time) {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          return time.getTime() < today;
        },
      },
      rules: {
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
      loadingProduct: false,
    };
  },
  mounted() {},
  methods: {
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
      this.formOrder.listProducts.push({
        productId: "",
        quantity: 0,
        price: 0,
      });
    },
    goBack() {
      this.$router.go(-1);
    },
    resetForm() {
      this.formOrder = {
        shopOrderName: "",
        transporter: "",
        source: "",
        deliveryDate: "",
        listProducts: [{ productId: "", quantity: 0, price: 0 }],
        note: "",
      };
      this.$refs.observer.reset();
    },
    async saveShopOrder() {
      if (this.formOrder.listProducts.length === 0) {
        this.$notify({
          type: "error",
          message: "Danh sách sản phẩm trống!",
          position: "bottom-right",
        });
        return;
      }
      const isValid = await this.$refs.observer.validate();
      if (isValid) {
        this.isLoading = true;
        saveShopOrders(this.formOrder)
          .then(() => {
            this.$notify({
              type: "success",
              message: "Tạo đơn hàng thành công!",
              position: "bottom-right",
            });
            this.$router.push("/shop-order");
          })
          .finally(() => (this.isLoading = false));
      }
    },
    getSelectProduct(index) {
      let product = this.formOrder.listProducts[index];
      let p = this.listProducts.find((i) => i.id === product.productId);
      product.price = p.price;
      product.name = p.name;
      product.totalPrice = p.price * product.quantity; //TODO
      product.totalPrice = (product.price || 0) * (product.quantity || 0);
      product.totalPriceText = Utils.formatPrice(product.totalPrice);
      const totalPrice = this.formOrder.listProducts.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    onchangeQuantity(index) {
      let p = this.formOrder.listProducts[index];
      p.totalPrice = (p.price || 0) * (p.quantity || 0);
      p.totalPriceText = Utils.formatPrice(p.totalPrice);
      const totalPrice = this.formOrder.listProducts.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    onChangePrice(index) {
      let p = this.formOrder.listProducts[index];
      p.totalPrice = (p.price || 0) * (p.quantity || 0);
      p.totalPriceText = Utils.formatPrice(p.totalPrice);
      const totalPrice = this.formOrder.listProducts.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
    },
    deleteProduct(index) {
      this.formOrder.listProducts.splice(index, 1);
      const totalPrice = this.formOrder.listProducts.reduce((prev, curr) => {
        return prev + curr.totalPrice;
      }, 0);
      this.totalPriceOrder = totalPrice;
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
.border-red {
  border: 1px solid red !important;
}
</style>
