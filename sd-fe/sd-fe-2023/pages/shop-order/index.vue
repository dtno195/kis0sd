<template>
  <div class="shop-order-page">
    <keyboard-events :keyCode="13" @keyup="getListShopOrders" />
    <app-tab-bar />

    <el-tabs
      v-model="formSearch.status"
      @tab-click="getListShopOrders"
      class="card-tabs"
    >
      <el-tab-pane
        v-for="(tab, index) in listStatus"
        :key="index"
        :label="tab.label"
        :name="tab.value"
      >
        <div class="card mb-5 mb-xl-10">
          <div class="card-body">
            <el-form :model="formSearch" class="form" label-position="left">
              <div class="action-horizontal">
                <div class="flex-grow-1">
                  <el-row :gutter="20">
                    <el-col :xl="6" :span="6">
                      <el-form-item
                        label="Sản phẩm"
                        prop="productId"
                        class="fw-bold"
                      >
                        <el-select
                          class="width-max-25rem"
                          v-model="formSearch.productId"
                          filterable
                          remote
                          :remote-method="getAllProducts"
                          :loading="loadingProduct"
                          placeholder="Mã sản phẩm"
                        >
                          <el-option :value="null"> </el-option>
                          <el-option
                            v-for="p in listAllProducts"
                            :key="p.id"
                            :label="`${p.code} | ${p.name} | Phân loại: ${p.size}`"
                            :value="p.id"
                          />
                        </el-select>
                      </el-form-item>
                    </el-col>
                    <el-col :span="8">
                      <el-form-item label="Tìm kiếm" class="fw-bold">
                        <el-input
                          maxlength="100"
                          class="width-max-25rem"
                          v-model="formSearch.keySearch"
                          placeholder="Tên đơn hàng/ nhà vận chuyển/ nguồn hàng"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :span="10">
                      <el-form-item label="Thời gian tạo" class="fw-bold">
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
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="action-horizontal__action-container">
                  <el-button
                    icon="el-icon-search"
                    @click="getListShopOrders"
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
                >Danh sách đơn hàng nội bộ</span
              >
            </div>
            <div class="d-flex align-items-center app-actions">
              <el-button
                v-if="user.authority !== Role.USER"
                type="info"
                icon="el-icon-plus"
                @click="goToCreatePage"
              >
                Tạo mới
              </el-button>
            </div>
          </div>

          <div class="py-0 px-8" v-loading="isTableLoading">
            <el-table
              class="table-header-sticky"
              stripe
              cell-class-name="px-2 py-2 whitespace-nowrap"
              :data="listShopOrders"
            >
              <template slot="empty">
                <span
                  class="text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Không có dữ liệu
                </span>
              </template>
              <el-table-column prop="id" label="#ID" width="60">
                <template slot-scope="scope">
                  <a
                    href="javascript:;"
                    :data-cy="'order_' + scope.row.id"
                    @click="goToDetailShopOrder(scope.row)"
                  >
                    {{ scope.row.id }}
                  </a>
                </template>
              </el-table-column>
              <el-table-column prop="createdTime" label="Ngày tạo" width="100">
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
              <el-table-column
                prop="shopOrderName"
                label="Tên đơn hàng"
                min-width="250"
              />
              <el-table-column prop="listProduct" label="Sản phẩm" width="220">
                <template slot-scope="scope">
                  <ul style="margin: 0">
                    <li v-for="item of scope.row.orderItemList" :key="item.id">
                      <span>{{
                        `${item.productCode} - ${item.productSize}`
                      }}</span>
                      <span
                        :class="
                          item.quantity === item.deliveredQuantity
                            ? 'text-success'
                            : 'text-red'
                        "
                        >{{ `(x${item.quantity})` }}</span
                      >
                    </li>
                  </ul>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="Trạng thái" width="150">
                <template slot-scope="scope">
                  <span
                    class="text-status fw-bold"
                    :class="
                      scope.row.status === 'COMPLETE' ? 'color-success' : ''
                    "
                    type="text"
                    >{{ getStatus(scope.row.status) }}</span
                  >
                </template>
              </el-table-column>
              <!-- <el-table-column prop="totalPrice" label="Tổng đơn hàng" width="200"/> -->
              <el-table-column
                prop="deliveryDate"
                label="Ngày giao hàng dự kiến"
                width="150"
              >
                <template slot-scope="scope">
                  <el-tooltip
                    effect="dark"
                    :content="Utils.formatDate(scope.row.deliveryDate)"
                  >
                    <span>{{
                      Utils.getSimpleDateFormat(scope.row.deliveryDate)
                    }}</span>
                  </el-tooltip>
                </template>
              </el-table-column>
              <el-table-column
                prop="transporter"
                label="Nhà vận chuyển"
                width="150"
              />
              <el-table-column prop="source" label="Nguồn nhập" width="150" />
            </el-table>
          </div>

          <div
            class="card-footer border-0 d-flex justify-content-start pt-4 px-8"
          >
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
      </el-tab-pane>
    </el-tabs>

    <!-- Detail dialog -->
    <el-dialog
      title="Chi tiết đơn hàng"
      :visible.sync="isDialogDetail"
      width="1000px"
      @close="getListShopOrders"
    >
      <shop-order-detail v-if="isDialogDetail" :id="shopOrderId" />
    </el-dialog>
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import { ShopOrderStatus } from "@/services/core/constant";
import { getAllListShopOrders } from "@/services/shop-order.service";
import { getListProducts } from "@/services/product.service";
import { Role } from "@/services/core/constant";

export default {
  name: "shop-order-list",
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
      isTableLoading: false,
      loadingProduct: false,
      // Form
      formSearch: {
        startDate: "",
        endDate: "",
        status: "TOTAL",
        keySearch: "",
      },
      formDate: [],
      listStatus: [
        { value: "TOTAL", label: "Tất cả" },
        { value: ShopOrderStatus.EXECUTE, label: "Đã đặt hàng" },
        { value: ShopOrderStatus.DELIVERING, label: "Đang vận chuyển" },
        { value: ShopOrderStatus.COMPLETE, label: "Hoàn thành" },
        { value: ShopOrderStatus.CANCEL, label: "Đã hủy" },
      ],
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
      // Data
      listShopOrders: [],
      // Pagination
      totalElements: 0,
      page: 1,
      size: 30,
      // Dialog
      isDialogDetail: false,
      shopOrderId: null,
      listAllProducts: [],
    };
  },
  mounted() {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
    this.formDate = [start, end];
    this.getListShopOrders();
  },
  methods: {
    getStatus(status) {
      switch (status) {
        case ShopOrderStatus.EXECUTE:
          return "Đã đặt hàng";
        case ShopOrderStatus.DELIVERING:
          return "Đang vận chuyển";
        case ShopOrderStatus.COMPLETE:
          return "Hoàn thành";
        case ShopOrderStatus.CANCEL:
          return "Đã hủy";
      }
    },
    getAllProducts(searchText) {
      this.loadingProduct = true;
      getListProducts({
        name: searchText,
        code: searchText,
      })
        .then((res) => {
          this.listAllProducts = res.data?.content;
        })
        .finally(() => (this.loadingProduct = false));
    },
    goToCreatePage() {
      this.$router.push({ path: "/shop-order/create" });
    },
    goToDetailShopOrder(shopOrder) {
      // this.$router.push({
      //   name: "shop-order-detail-id",
      //   params: { id: shopOrder.id },
      // });
      this.isDialogDetail = true;
      this.shopOrderId = shopOrder.id;
    },
    changePage(p) {
      this.page = p;
      this.getListShopOrders();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getListShopOrders();
    },
    getListShopOrders() {
      this.isTableLoading = true;
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
      if (this.formSearch.status !== "TOTAL") {
        data.status = this.formSearch.status;
      }
      if (this.formSearch.keySearch) {
        data.keySearch = this.formSearch.keySearch;
      }
      if (this.formSearch.productId) {
        data.productId = this.formSearch.productId;
      }
      const param = new URLSearchParams(data);
      getAllListShopOrders(param)
        .then((res) => {
          if (res.data) {
            this.listShopOrders = res.data.content || [];
            this.totalElements = res.data.totalElements;
          }
        })
        .finally(() => (this.isTableLoading = false));
    },
    // async handleCancelOrder(id) {
    //   if (confirm(`Bạn chắc chắn muốn hủy đơn hàng #${id}?`)) {
    //     this.isTableLoading = true;
    //     try {
    //       await cancelShopOrder(id);
    //       this.$notify({
    //         type: "success",
    //         message: `Đơn hàng #${id} đã được hủy`,
    //         position: "bottom-right",
    //       });
    //     } finally {
    //       this.isTableLoading = false;
    //     }
    //   }
    // },
  },
};
</script>
