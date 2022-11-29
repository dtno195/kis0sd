<template>
  <div class="product-edit-page">
    <app-tab-bar />

    <app-page-title title="Sửa sản phẩm" />

    <div class="card mb-5 mb-xl-10" v-loading="isLoading">
      <div class="card-body">
        <div class="container">
          <el-form
            :model="productForm"
            :rules="rules"
            ref="productForm"
            label-width="120px"
          >
            <el-form-item label="Mã sản phẩm" prop="code">
              <el-input v-model="productForm.code" disabled />
            </el-form-item>
            <el-form-item label="Tên sản phẩm" prop="name">
              <el-input v-model="productForm.name" disabled />
            </el-form-item>
            <el-form-item label="Nhãn hiệu" prop="brand">
              <el-input v-model="productForm.brand" disabled />
            </el-form-item>
            <el-form-item label="Size" prop="size">
              <el-input v-model="productForm.size" />
            </el-form-item>
            <el-form-item label="Số lượng" prop="quantity">
              <el-input-number
                class="width-10rem input-number-class"
                :controls="false"
                :min="0"
                :max="10000"
                v-model="productForm.quantity"
              />
            </el-form-item>
            <el-form-item class="el-input el-input--small" label="Giá tiền">
              <my-currency-input v-model="productForm.price" />
              <!-- <currency-input class="input-price" v-model="productForm.price" /> -->
            </el-form-item>
            <el-form-item label="Ghi chú" prop="note">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2 }"
                v-model="productForm.note"
                disabled
              />
            </el-form-item>
            <el-form-item label="Ảnh" prop="imageUrls">
              <el-input
                type="textarea"
                :autosize="{ minRows: 2 }"
                v-model="productForm.imageUrls"
              />
              <div class="tip">
                <span>Lưu một hoặc nhiều link, phân cách nhau bởi dấu |</span>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="card-footer d-flex justify-content-end py-4 px-8">
        <el-button @click="goBack">Hủy</el-button>
        <el-button type="primary" @click="submitForm('productForm')"
          >Lưu</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { getOneProduct, updateProduct } from "@/services/product.service";

export default {
  name: "product-edit",
  layout: "main-layout",
  data() {
    return {
      isLoading: false,
      productForm: {
        productId: this.$route.params["id"],
        name: "",
        code: "",
        brand: "",
        size: "",
        note: "",
        quantity: 0,
        imageUrls: "",
      },
      rules: {
        code: [
          {
            required: true,
            message: "Vui lòng nhập mã sản phẩm!",
            trigger: "blur",
          },
          {
            min: 1,
            max: 30,
            message: "Độ dài không quá 30 ký tự!",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "Vui lòng nhập tên sản phẩm!",
            trigger: "blur",
          },
          {
            min: 1,
            max: 200,
            message: "Độ dài không quá 200 ký tự!",
            trigger: "blur",
          },
        ],
        size: [
          { required: true, message: "Vui lòng nhập size!", trigger: "blur" },
          {
            min: 1,
            max: 5,
            message: "Độ dài không quá 5 ký tự!",
            trigger: "blur",
          },
        ],
        price: [
          {
            required: true,
            message: "Vui lòng nhập giá tiền!",
            trigger: "blur",
          },
        ],
        quantity: [
          {
            required: true,
            message: "Vui lòng nhập số lượng!",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    async loadData() {
      try {
        this.isLoading = true;
        const res = await getOneProduct(this.productForm.productId);
        Object.assign(this.productForm, res.data);
        this.productForm.price = res.data.price;
      } finally {
        this.isLoading = false;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            this.isLoading = true;
            const res = await updateProduct(
              this.productForm.productId,
              this.productForm
            );
            this.$notify({
              type: "success",
              message: "Cập nhật sản phẩm thành công!",
              position: "bottom-right",
            });
            this.goBack();
          } finally {
            this.isLoading = false;
          }
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    goBack() {
      this.$router.back();
    },
  },
};
</script>
