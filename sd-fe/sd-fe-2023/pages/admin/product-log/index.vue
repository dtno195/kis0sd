<template>
  <div class="warehouse-page">
    <keyboard-events :keyCode="13" @keyup="getListWarehouseLogs" />
    <app-tab-bar />
    <app-page-title title="Lịch sử kho hàng" />

    <div class="card mb-5 mb-xl-10">
      <div class="card-body">
        <el-form :model="formSearch" class="form" label-position="left">
          <div class="action-horizontal">
            <div class="flex-grow-1">
              <el-row :gutter="20">
                <el-col :xl="8" :span="12">
                  <el-form-item label="Chọn sản phẩm" class="fw-bold">
                    <el-select
                      class="width-max-25rem"
                      v-model="formSearch.productId"
                      filterable
                      remote
                      :remote-method="getAllProducts"
                      :loading="isTableLoading"
                      placeholder="Nhập mã sản phẩm"
                    >
                      <el-option
                        v-for="p in formListProducts"
                        :key="p.id"
                        :label="p.label"
                        :value="p.id"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :xl="8" :span="12">
                  <el-form-item label="Nhân viên" class="fw-bold">
                    <el-input
                      class="width-max-25rem"
                      v-model="formSearch.username"
                      placeholder="Nhập tên nhân viên"
                    />
                  </el-form-item>
                </el-col>
                <el-col :xl="8" :span="12">
                  <el-form-item label="Thời gian" class="fw-bold">
                    <el-date-picker
                      v-model="formDate"
                      type="daterange"
                      align="right"
                      :clearable="false"
                      unlink-panels
                      range-separator="-"
                      start-placeholder="Start date"
                      end-placeholder="End date"
                      :picker-options="pickerOptions"
                    >
                    </el-date-picker>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="action-horizontal__action-container">
              <el-button
                icon="el-icon-search"
                @click="getListWarehouseLogs"
                type="primary"
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
          <span class="card-label fw-bolder fs-3"
            >Lịch sử cập nhật kho hàng</span
          >
        </div>
      </div>

      <div class="py-0 px-8" v-loading="isTableLoading">
        <el-table
          class="table-header-sticky"
          stripe
          cell-class-name="px-2 py-2 whitespace-nowrap"
          :data="listWarehouseLogs"
        >
          <template slot="empty">
            <span
              class="text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Không có dữ liệu
            </span>
          </template>
          <el-table-column prop="createdTime" label="Thời gian" width="100">
            <template slot-scope="scope">
              <el-tooltip
                effect="dark"
                :content="Utils.formatDate(scope.row.createdTime)"
              >
                <span>{{
                  Utils.getSimpleDateFormat(scope.row.createdTime)
                }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="username" label="Nhân viên" width="150" />
          <el-table-column prop="code" label="Mã sản phẩm" width="200" />
          <el-table-column prop="name" label="Tên sản phẩm" />
          <el-table-column prop="type" label="Phân loại" width="100" />
          <el-table-column prop="action" label="Hành động" width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.action === 'CREATE'"> Tạo mới </span>
              <span v-else-if="scope.row.action === 'CREATE-MULTI'">
                Tạo mới nhiều sp
              </span>
              <span v-else-if="scope.row.action === 'CHANGE'"> Thay đổi </span>
              <span v-else-if="scope.row.action === 'IMPORT'"> Import </span>
              <span v-else-if="scope.row.action === 'DELETE'"> Xóa </span>
            </template>
          </el-table-column>
          <el-table-column label="Thay đổi" width="200">
            <template slot-scope="scope">
              <span v-if="scope.row.action === 'CHANGE'">
                {{ scope.row.oldQuantity }} -> {{ scope.row.newQuantity }}
              </span>
              <span
                v-if="
                  scope.row.action === 'CREATE' ||
                  scope.row.action === 'CREATE-MULTI' ||
                  scope.row.action === 'IMPORT'
                "
              >
                {{ scope.row.newQuantity }}
              </span>
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
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import { getListProducts } from "@/services/product.service";
import { getAllListWarehouseLogs } from "@/services/warehouse.service";

export default {
  name: "product-log-list",
  layout: "main-layout",
  middleware: "isAdmin",
  data() {
    return {
      Utils: Utils,
      isTableLoading: false,
      pickerOptions: {
        shortcuts: [
          {
            text: "Tuần trước",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "Tháng trước",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "3 tháng trước",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      // Form
      formSearch: {
        minPrice: 0,
        maxPrice: 999999999,
        brand: "",
        sizeProduct: "",
        name: "",
        status: "AVAILABLE",
      },
      formDate: [],
      formListProducts: [],
      // Data
      listWarehouseLogs: [],
      // Pagination
      totalElements: 0,
      page: 1,
      size: 30,
    };
  },
  mounted() {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
    this.formDate = [start, end];
    this.getListWarehouseLogs();
  },
  methods: {
    changePage(p) {
      this.page = p;
      this.getListWarehouseLogs();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getListWarehouseLogs();
    },
    async getListWarehouseLogs() {
      this.isTableLoading = true;
      try {
        const param = this._getQueryParam();
        const res = await getAllListWarehouseLogs(param);
        this.listWarehouseLogs = res?.data?.content || [];
        this.totalElements = res?.data?.totalElements;
      } finally {
        this.isTableLoading = false;
      }
    },
    _getQueryParam() {
      const startDate = this.formDate[0];
      const endDate = this.formDate[1];
      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(23, 59, 59, 999);
      const data = {
        page: this.page - 1,
        size: this.size,
        sortBy: "createdTime",
        sortDesc: true,
        startDateStr: startDate.toISOString(),
        endDateStr: endDate.toISOString(),
      };
      if (this.formSearch.username) {
        data.username = this.formSearch.username;
      }
      if (this.formSearch.name) {
        data.name = this.formSearch.name;
      }
      if (this.formSearch.productId) {
        data.productId = this.formSearch.productId;
      }
      if (this.formSearch.startDateStr) {
        data.startDateStr = this.formSearch.startDateStr;
      }
      if (this.formSearch.endDateStr) {
        data.endDateStr = this.formSearch.endDateStr;
      }
      return new URLSearchParams(data);
    },
    // Dialog
    getAllProducts(searchText) {
      this.isTableLoading = true;
      getListProducts({
        name: searchText,
        code: searchText,
      })
        .then((res) => {
          this.formListProducts = [
            { value: null, label: "Tất cả" },
            ...res.data?.content.map((item) => {
              item.label = `${item.code} | ${item.name} | Phân loại: ${item.size}`;
              return item;
            }),
          ];
        })
        .finally(() => (this.isTableLoading = false));
    },
  },
};
</script>
