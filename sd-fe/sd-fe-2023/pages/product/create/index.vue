<template>
  <div class="product-create-page">
    <app-tab-bar />
    <app-page-title title="Tạo sản phẩm" />

    <div class="card mb-5 mb-xl-10" v-loading="isLoading">
      <div class="card-body">
        <ValidationObserver ref="observer">
          <el-form
            :model="productForm"
            :rules="rules"
            ref="productForm"
            label-position="top"
          >
            <el-row :gutter="20">
              <el-col :span="8">
                <h3>Thông tin sản phẩm</h3>
                <hr />
                <ValidationProvider
                  v-slot="{ errors }"
                  :rules="{ required: true }"
                  name="code"
                >
                  <el-form-item label="Mã sản phẩm" prop="code">
                    <el-input
                      data-cy="code"
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="productForm.code"
                    />
                    <span class="el-form-item__error">
                      {{ errors[0] }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <ValidationProvider
                  v-slot="{ errors }"
                  :rules="{ required: true }"
                  name="name"
                >
                  <el-form-item label="Tên sản phẩm" prop="name">
                    <el-input
                      data-cy="name"
                      :class="errors.length > 0 ? 'is-invalid' : ''"
                      v-model="productForm.name"
                    />
                    <span class="el-form-item__error">
                      {{ errors[0] }}
                    </span>
                  </el-form-item>
                </ValidationProvider>

                <el-form-item label="Giá" prop="price">
                  <el-input-number
                    :data-cy="'price_' + index"
                    :controls="false"
                    :min="0"
                    v-model="productForm.price"
                  />
                </el-form-item>

                <el-form-item label="Nhãn hiệu" prop="brand">
                  <el-input v-model="productForm.brand" />
                </el-form-item>

                <el-form-item label="Ghi chú" prop="note">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2 }"
                    v-model="productForm.note"
                  />
                </el-form-item>
                <el-form-item label="Ảnh" prop="imageUrls">
                  <el-input
                    type="textarea"
                    :autosize="{ minRows: 2 }"
                    v-model="productForm.imageUrls"
                  />
                  <div class="tip">
                    <span
                      >Lưu một hoặc nhiều link, phân cách nhau bởi dấu |</span
                    >
                  </div>
                </el-form-item>
              </el-col>

              <el-col :span="16">
                <h3>Danh sách phân loại</h3>
                <hr />
                <el-button type="info" @click="addMoreType" data-cy="addProductType"> Thêm </el-button>

                <div
                  v-for="(productType, index) in productForm.listProduct"
                  :key="index"
                  class="form-list-item action-horizontal"
                >
                  <div class="flex-grow-1">
                    <el-row :gutter="20">
                      <el-col :span="12">
                        <ValidationProvider
                          v-slot="{ errors }"
                          rules="required"
                          :name="'size_' + index"
                        >
                          <el-form-item label="Phân loại" prop="size">
                            <el-input
                              :data-cy="'size_' + index"
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              v-model="productType.size"
                            />
                            <span class="el-form-item__error">
                              {{ errors[0] }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
                      </el-col>
                      <el-col :span="6">
                        <ValidationProvider
                          v-slot="{ errors }"
                          rules="required"
                          :name="'quantity_' + index"
                        >
                          <el-form-item label="Số lượng" prop="quantity">
                            <el-input-number
                              :data-cy="'quantity_' + index"
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              :controls="false"
                              :min="0"
                              :max="10000"
                              v-model="productType.quantity"
                            />
                            <span class="el-form-item__error">
                              {{ errors[0] }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
                      </el-col>
                      <el-col :span="6">
                        <ValidationProvider
                          v-slot="{ errors }"
                          :name="'price_' + index"
                        >
                          <el-form-item label="Giá" prop="price">
                            <el-input-number
                              :data-cy="'price_' + index"
                              :class="errors.length > 0 ? 'is-invalid' : ''"
                              :controls="false"
                              :min="0"
                              v-model="productType.price"
                            />
                            <span class="el-form-item__error">
                              {{ errors[0] }}
                            </span>
                          </el-form-item>
                        </ValidationProvider>
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
                      @click="deleteProductType(index)"
                      >Xóa</el-button
                    >
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </ValidationObserver>
      </div>
      <div class="card-footer d-flex justify-content-end py-4 px-8">
        <el-button @click="goBack">Quay lại</el-button>
        <el-button @click="resetForm">Làm mới</el-button>
        <el-button :disabled="isLoading" type="primary" @click="save" data-cy="save">Lưu</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { createProductGroup } from "@/services/product.service";

export default {
  name: "product-create",
  layout: "main-layout",
  data() {
    return {
      isLoading: false,
      productForm: {
        id: null,
        code: "",
        name: "",
        price: 0,
        brand: "",
        imageUrls: "",
        note: "",
        listProduct: [{ size: "", quantity: 0, price: 0 }],
      },
      rules: {
        code: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        name: [
          {
            required: true,
            trigger: "submit",
          },
        ],
        size: [
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
      },
    };
  },
  mounted() {},
  methods: {
    async save() {
      if (this.productForm.listProduct.length === 0) {
        this.$notify({
          type: "error",
          message: "Danh sách phân loại trống!",
          position: "bottom-right",
        });
        return;
      }
      const isValid = await this.$refs.observer.validate();
      if (isValid) {
        this.isLoading = true;
        createProductGroup(this.productForm)
          .then(() => {
            this.$notify({
              type: "success",
              message: "Tạo sản phẩm thành công!",
              position: "bottom-right",
            });
            this.$router.push("/product");
          })
          .finally(() => (this.isLoading = false));
      }
    },
    goBack() {
      this.$router.back();
    },
    deleteProductType(index) {
      this.productForm.listProduct.splice(index, 1);
    },
    resetForm() {
      this.productForm = {
        id: null,
        code: "",
        name: "",
        price: 0,
        brand: "",
        imageUrls: "",
        note: "",
        listProduct: [{ size: "", quantity: 0, price: 0 }],
      };
      this.$refs.observer.reset();
    },
    addMoreType() {
      this.productForm.listProduct.push({
        size: "",
        quantity: 0,
        price: 0,
      });
    },
  },
};
</script>
