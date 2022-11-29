<template>
  <div class="product-page">
    <keyboard-events :keyCode="13" @keyup="getListProductGroup" />
    <app-tab-bar />

    <div class="card mb-5 mb-xl-10">
      <div class="card-body">
        <el-form :model="formSearch" class="form" label-position="left">
          <div class="action-horizontal">
            <div class="flex-grow-1">
              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="Mã sản phẩm" class="fw-bold">
                    <el-input
                      maxlength="100"
                      class="width-max-15rem"
                      v-model="formSearch.code"
                      placeholder="Nhập mã sản phẩm"
                      data-cy="productCode"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="Tên sản phẩm" class="fw-bold">
                    <el-input
                      maxlength="100"
                      class="width-max-15rem"
                      v-model="formSearch.name"
                      placeholder="Nhập tên sản phẩm"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <el-form-item label="Size" class="fw-bold">
                    <el-input
                      maxlength="5"
                      class="width-max-10rem"
                      v-model="formSearch.type"
                      placeholder="Nhập size"
                    />
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="action-horizontal__action-container">
              <el-button
                icon="el-icon-search"
                @click="getListProductGroup"
                type="primary"
                data-cy="searchProduct"
              >
                Tìm kiếm
              </el-button>
            </div>
          </div>
        </el-form>
      </div>
    </div>

    <div class="card mb-5 mb-xl-10">
      <div class="card-header border-0 pt-5">
        <div class="card-title align-items-start flex-column">
          <span class="card-label fw-bolder fs-3">Danh sách sản phẩm</span>
        </div>
        <div class="d-flex align-items-center app-actions">
          <el-button
            v-if="user && (user.authority == Role.ADMIN || user.authority == Role.MANAGER)"
            type="info"
            @click="openAddQuantity"
          >
            Nhập sản phẩm
          </el-button>
          <el-button
            type="info"
            icon="el-icon-upload2"
            @click="handleFileUpload"
          >
            Nhập Excel

            <input
              id="id-import-excel"
              ref="importExcel"
              v-show="false"
              type="file"
              @change="_importExcel($event)"
            />
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="_exportExcel">
            Xuất Excel
          </el-button>
          <el-button type="info" icon="el-icon-plus" @click="goToCreatePage">
            Tạo mới
          </el-button>
        </div>
      </div>

      <div class="py-0 px-8" v-loading="isTableLoading">
        <el-table
          class="table-header-sticky"
          stripe
          cell-class-name="px-2 py-2 whitespace-nowrap"
          :data="listProductGroup"
          row-key="key"
          :default-sort="listProductGroupSorting"
          @sort-change="onSortChange"
          v-if="!isTableLoading"
        >
          <template slot="empty">
            <span
              class="text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Không có dữ liệu
            </span>
          </template>

          <el-table-column
            prop="code"
            sortable="custom"
            label="Mã sản phẩm"
            width="150"
          />
          <el-table-column
            prop="quantity"
            sortable="custom"
            label="Số lượng"
            width="100"
          />
          <el-table-column
            prop="availableQuantity"
            sortable="custom"
            label="Available"
            width="100"
          />
          <el-table-column
            prop="deliveringQuantity"
            sortable="custom"
            class-name="text-price"
            label="Đang về"
            width="100"
          />
          <el-table-column
            prop="requiredQuantity"
            sortable="custom"
            class-name="text-price"
            label="Cần order"
            width="100"
          />
          <el-table-column
            prop="shippingQuantity"
            sortable="custom"
            class-name="text-price"
            label="Đang giao"
            width="100"
          />
          <el-table-column
            prop="sold"
            sortable="custom"
            label="Đã bán"
            width="100"
          />
          <el-table-column
            prop="brand"
            sortable="custom"
            label="Nhãn hiệu"
            width="150"
          />
          <el-table-column
            prop="name"
            sortable="custom"
            label="Tên sản phẩm"
            min-width="120"
          />
          <el-table-column
            prop="price"
            sortable="custom"
            label="Giá tiền"
            width="150"
          >
            <template slot-scope="scope">
              {{ Utils.formatPrice(scope.row.price) }}
            </template>
          </el-table-column>
          <el-table-column prop="imageUrls" label="Ảnh" width="100">
            <template slot-scope="scope">
              <div
                class="image-container"
                v-if="isParent(scope.row) && scope.row.imageUrls"
              >
                <el-image
                  class="image-preview"
                  :src="Utils.textToArray(scope.row.imageUrls)[0]"
                  :preview-src-list="Utils.textToArray(scope.row.imageUrls)"
                >
                </el-image>
                <div
                  class="image-more"
                  v-if="Utils.textToArray(scope.row.imageUrls).length > 1"
                >
                  +{{ Utils.textToArray(scope.row.imageUrls).length - 1 }}
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="note" label="Ghi chú" min-width="200">
            <template slot-scope="scope">
              <div v-if="isParent(scope.row)">
                {{ scope.row.note }}
              </div>
            </template>
          </el-table-column>
          <el-table-column
            v-if="user && (user.authority == Role.ADMIN || user.authority == Role.MANAGER)"
            label="Hành động"
            width="140"
          >
            <template slot-scope="scope">
              <div v-if="!isParent(scope.row)">
                <el-tooltip content="Cập nhật">
                  <el-button
                    size="mini"
                    type="info"
                    icon="el-icon-edit"
                    @click="handleEditProduct(scope.row)"
                  ></el-button>
                </el-tooltip>
                <!-- Không cần chức năng này -->
                <!-- <el-tooltip content="Xóa">
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="handleRemoveProduct(scope.row)"
                  />
                </el-tooltip> -->
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="card-footer border-0 d-flex justify-content-start pt-4 px-8">
        <el-pagination
          layout="sizes, prev, pager, next"
          @current-change="changePage"
          @size-change="handleSizeChange"
          :total="totalElements"
          :page-size="size"
          class="border"
        >
        </el-pagination>
      </div>
    </div>
    <el-dialog
      title="Nhập kho sản phẩm"
      :visible.sync="isDialogAddWarehouseVisible"
      width="40%"
    >
      <div v-loading="isTableLoading">
        <div class="container">
          <el-form
            :model="warehouseForm"
            :rules="rules"
            ref="warehouseForm"
            label-width="120px"
          >
            <el-form-item label="Chọn sản phẩm" prop="productId">
              <el-select
                class="width-35rem"
                v-model="warehouseForm.id"
                filterable
                remote
                :remote-method="getAllProducts"
                :loading="isTableLoading"
                placeholder="Nhập mã sản phẩm"
              >
                <el-option
                  v-for="p in listProductsDialog"
                  :key="p.id"
                  :label="`${p.code} | ${p.name} | Phân loại: ${p.size}`"
                  :value="p.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="Số lượng" prop="quantity">
              <el-input-number
                class="width-10rem input-number-class"
                :controls="false"
                :min="0"
                :max="10000"
                v-model="warehouseForm.quantity"
              />
            </el-form-item>
          </el-form>
        </div>
        <div class="card-footer d-flex justify-content-end py-4 px-8">
          <el-button @click="isDialogAddWarehouseVisible = false"
            >Hủy</el-button
          >
          <el-button :disabled="isTableLoading" type="primary" @click="submitForm('warehouseForm')"
            >Lưu</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Role } from "@/services/core/constant";
import { Utils } from "@/services/core/utils";
import {
  findPagingProductGroup,
  getListProducts,
  deleteProduct,
  exportExcel,
  importExcel,
} from "@/services/product.service";
import { saveMultiWarehouse } from "@/services/warehouse.service";
import { saveAs } from "file-saver";
export default {
  name: "product-list",
  layout: "main-layout",
  data() {
    return {
      Utils: Utils,
      Role: Role,
      isTableLoading: false,
      user: null,
      // Form
      formSearch: {
        brand: "",
        type: "",
        name: "",
      },
      warehouseForm: {
        id: "",
        warehouseId: "",
        code: "",
        size: "",
        quantity: 0,
        name: "",
      },
      // Data
      listProductGroup: [],
      listProductGroupSorting: { prop: "code", order: "ascending" },
      // Pagination
      totalElements: 0,
      page: 1,
      size: 30,
      // Other
      rules: {
        id: [
          {
            required: true,
            message: "Vui lòng chọn 1 sản phẩm!",
            trigger: "change",
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
      listProductsDialog: [],
      isDialogAddWarehouseVisible: false,
    };
  },
  mounted() {
    if (this.$store.state.auth.user) {
      this.user = this.$store.state.auth.user;
    }
    this.getListProductGroup();
  },
  methods: {
    // Dialog
    getAllProducts(searchText) {
      this.isTableLoading = true;
      getListProducts({
        name: searchText,
        code: searchText,
      })
        .then((res) => {
          this.listProductsDialog = res.data?.content;
        })
        .finally(() => (this.isTableLoading = false));
    },
    openAddQuantity() {
      this.isDialogAddWarehouseVisible = true;
      this.warehouseForm = Object.assign({});
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.isTableLoading = true;
          saveMultiWarehouse([this.warehouseForm])
            .then(() => {
              this.$notify({
                type: "success",
                message: "Thêm kho sản phẩm thành công!",
                position: "bottom-right",
              });
              this.getListProductGroup();
              this.isDialogAddWarehouseVisible = false;
            })
            .finally(() => (this.isTableLoading = false));
        }
      });
    },
    goToCreatePage() {
      this.$router.push({ path: "/product/create" });
    },
    handleEditProduct(item) {
      this.$router.push({ path: `/product/edit/${item.id}` });
    },
    handleRemoveProduct(item) {
      if (confirm("Bạn chắc chắn muốn xóa sản phẩm này?")) {
        deleteProduct(item.id).then(() => {
          this.$notify({
            type: "success",
            message: "Xóa thành công!",
            position: "bottom-right",
          });
          this.getListProductGroup();
        });
      }
    },
    changePage(p) {
      this.page = p;
      this.getListProductGroup();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getListProductGroup();
    },
    async onSortChange(event) {
      this.isTableLoading = true;
      try {
        const param = this._getQueryParam({
          sortBy: event.prop,
          sortDesc: event.order !== "ascending",
        });
        const res = await findPagingProductGroup(param);
        if (res.data && res.data.content) {
          this.listProductGroup = res.data.content.map((item) => {
            item.key = item.id;
            item.children.map((type) => {
              type.code = type.size;
              type.key = `${type.id}-${type.size}`;
              return type;
            });
            return item;
          });
          this.totalElements = res.data.totalElements;
        }
        this.listProductGroupSorting = event;
      } finally {
        this.isTableLoading = false;
      }
    },
    async getListProductGroup() {
      this.isTableLoading = true;
      try {
        const param = this._getQueryParam();
        const res = await findPagingProductGroup(param);
        if (res.data) {
          this.listProductGroup = res.data.content.map((item) => {
            item.key = item.id;
            item.children.map((type) => {
              type.code = type.size;
              type.key = `${type.id}-${type.size}`;
              return type;
            });
            return item;
          });
          this.totalElements = res.data.totalElements;
        }
      } finally {
        this.isTableLoading = false;
      }
    },
    handleFileUpload() {
      document.getElementById("id-import-excel").click();
    },
    async _importExcel(event) {
      const file = event.target.files[0];
      let formData = new FormData();
      formData.append("file", file);
      this.isTableLoading = true;
      const res = await importExcel(formData);
      this.$notify({
        type: "success",
        message: "Import sản phẩm thành công!",
        position: "bottom-right",
      });
      this.isTableLoading = false;
      this.getListProductGroup();
    },
    async _exportExcel() {
      const param = this._getQueryParam();
      const res = await exportExcel(param);
      // response.data is a blob type
      saveAs(res.data, `export_product_${moment().format("YYYYMMDD")}.xlsx`);
      this.$notify({
        type: "success",
        message: "Đang thực hiện download file",
        position: "bottom-right",
      });
    },
    isParent(item) {
      return !item.size;
    },
    _getQueryParam(sort) {
      const data = {
        page: this.page - 1,
        size: this.size,
        sortBy: sort?.sortBy || "code",
        sortDesc: sort?.sortDesc || false,
      };
      if (this.formSearch.brand) {
        data.brand = this.formSearch.brand;
      }
      if (this.formSearch.type) {
        data.type = this.formSearch.type;
      }
      if (this.formSearch.name) {
        data.name = this.formSearch.name;
      }
      if (this.formSearch.code) {
        data.code = this.formSearch.code;
      }
      return new URLSearchParams(data);
    },
  },
};
</script>
<style lang="scss" scoped>
.image-container {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  & > .image-more {
    position: absolute;
    top: 5px;
    right: 5px;
    width: 20px;
    height: 20px;
    border-radius: 4px;
    font-weight: 500;
    font-size: 12px;
    line-height: 1;
    background-color: grey;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
.image-preview {
  width: 70px;
  height: 70px;
  border-radius: 4px;
  cursor: pointer;
}
</style>
