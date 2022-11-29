<template>
  <div class="customer-order-page">
    <keyboard-events :keyCode="13" @keyup="getListCustomerOrders" />
    <app-tab-bar />

    <el-tabs
      v-model="formSearch.status"
      @tab-click="getListCustomerOrders"
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
                    <el-col :xl="6" :span="12">
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
                          :loading="formSearch.loadingProduct"
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
                    <el-col :xl="8" :span="12">
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
                    <el-col :xl="5" :span="8">
                      <el-form-item label="SĐT khách" class="fw-bold">
                        <el-input
                          maxlength="100"
                          class="width-max-15rem"
                          v-model="formSearch.receiverPhone"
                          placeholder="SĐT người nhận"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xl="5" :span="8">
                      <el-form-item label="Link chat" class="fw-bold">
                        <el-input
                          maxlength="100"
                          class="width-max-15rem"
                          v-model="formSearch.messengerUrl"
                          placeholder="Link chat"
                        />
                      </el-form-item>
                    </el-col>
                    <el-col :xl="12" :span="8">
                      <el-form-item
                        label="Thiếu thông tin cá nhân (tên/sđt/địa chỉ)"
                        class="fw-bold"
                      >
                        <el-checkbox
                          v-model="formSearch.isMissingInfo"
                          @change="changeSearchMissingInfo"
                        ></el-checkbox>
                      </el-form-item>
                    </el-col>
                    <el-col :xl="12" :span="8">
                      <el-form-item label="ID" class="fw-bold">
                        <el-input-number
                          :controls="false"
                          :min="1"
                          :max-length="50"
                          v-model="formSearch.id"
                        />
                      </el-form-item>
                    </el-col>
                  </el-row>
                  <el-row
                    v-if="formSearch.status === 'DELIVERING'"
                    :gutter="20"
                  >
                    <el-col>
                      <el-form-item>
                        <el-radio-group
                          v-model="formSearch.isShipAll"
                          @change="getListCustomerOrders"
                        >
                          <el-radio border :label="true">Giao toàn bộ</el-radio>
                          <el-radio border :label="false">Giao trước</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </div>
                <div class="action-horizontal__action-container">
                  <el-button
                    icon="el-icon-search"
                    @click="getListCustomerOrders"
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
              <span class="card-label fw-bolder fs-3">Danh sách đơn hàng</span>
            </div>
            <div class="d-flex align-items-center app-actions">
              <el-button
                v-if="formSearch.status === 'EXECUTE'"
                type="info"
                icon="el-icon-truck"
                :disabled="!listProductsSelection.length"
                @click="deliveryManyCustomerOrder"
              >
                Giao đơn
              </el-button>
              <el-button
                v-if="formSearch.status === 'DELIVERING'"
                type="info"
                icon="el-icon-truck"
                :disabled="!listProductsSelection.length"
                @click="markedAsHaveTrackingOrder"
              >
                Đánh dấu đã tạo vận đơn
              </el-button>
              <el-button
                v-if="formSearch.status === 'DELIVERING'"
                type="info"
                icon="el-icon-check"
                :disabled="!listProductsSelection.length"
                @click="completeManyCustomerOrder"
              >
                Hoàn thành đơn
              </el-button>
              <el-button
                v-if="
                  formSearch.status !== 'CANCEL' &&
                  formSearch.status !== 'COMPLETE'
                "
                type="info"
                icon="el-icon-close"
                :disabled="!listProductsSelection.length"
                @click="cancelManyCustomerOrder"
              >
                Hủy đơn
              </el-button>
              <el-button
                v-if="formSearch.status === 'DELIVERING'"
                type="info"
                icon="el-icon-truck"
                :disabled="listProductsSelection.length !== 1"
                @click="exchangeCustomerOrder"
              >
                Đổi/trả
              </el-button>
              <el-button
                type="info"
                icon="el-icon-download"
                @click="exportExcelGHTK"
              >
                Xuất Excel GHTK
              </el-button>
              <el-button
                type="info"
                icon="el-icon-plus"
                @click="goToCreatePage"
              >
                Tạo mới
              </el-button>
            </div>
          </div>

          <div v-loading="isTableLoading" class="py-0 px-8">
            <el-table
              class="table-header-sticky"
              stripe
              cell-class-name="px-2 py-2 whitespace-nowrap"
              :data="listCustomerOrders"
              :row-class-name="isReadyRowClassName"
              @selection-change="handleSelectionChange"
            >
              <template slot="empty">
                <span
                  class="text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  Không có dữ liệu
                </span>
              </template>

              <el-table-column type="selection" width="50" />
              <el-table-column prop="id" label="#ID" width="100">
                <template slot-scope="scope">
                  <a
                    href="javascript:;"
                    :data-cy="'order_' + scope.row.id"
                    @click="goToDetailCustomerOrder(scope.row)"
                  >
                    {{ scope.row.id }}
                  </a>
                </template>
              </el-table-column>
              <el-table-column label="Tên người nhận" width="150">
                <template slot-scope="scope">
                  <el-tooltip v-if="scope.row.note" :content="scope.row.note">
                    <el-link icon="el-icon-chat-dot-square" class="me-1" />
                  </el-tooltip>
                  <a
                    v-if="scope.row.messengerUrl"
                    :href="scope.row.messengerUrl"
                    :title="scope.row.messengerUrl"
                    target="_blank"
                  >
                    {{ scope.row.receiverName }}
                  </a>
                  <span v-else>{{ scope.row.receiverName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="receiverPhone"
                label="SĐT người nhận"
                width="120"
              />
              <el-table-column
                prop="receiverAddress"
                label="Địa chỉ nhận"
                min-width="200"
              />
              <el-table-column prop="listProduct" label="Sản phẩm" width="200">
                <template slot-scope="scope">
                  <ul style="margin: 0">
                    <li v-for="item of scope.row.orderItemList" :key="item.id">
                      <span>{{
                        `${item.productCode} - ${item.productSize}`
                      }}</span>
                      <span
                        :class="
                          item.quantity - item.deliveredQuantity <=
                          item.availableQuantity
                            ? 'text-success'
                            : 'text-red'
                        "
                        >{{ `(x${item.quantity})` }}</span
                      >
                    </li>
                  </ul>
                </template>
              </el-table-column>
              <el-table-column
                sortable
                prop="status"
                label="Trạng thái"
                width="150"
              >
                <template slot-scope="scope">
                  <span
                    class="text-status fw-bold"
                    :class="
                      scope.row.status === 'COMPLETE'
                        ? 'color-success'
                        : scope.row.status === 'DELIVERING'
                        ? scope.row.isHaveTrackingOrder
                          ? 'text-green'
                          : 'text-red'
                        : ''
                    "
                    type="text"
                    >{{ getStatus(scope.row) }}</span
                  >
                </template>
              </el-table-column>
              <el-table-column label="Tổng tiền" width="140">
                <template slot-scope="scope">
                  <span class="text-price">
                    {{ Utils.formatPrice(scope.row.totalPrice) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="deposit" label="Đặt cọc" width="140">
                <template slot-scope="scope">
                  <span class="text-price">
                    {{ Utils.formatPrice(scope.row.deposit) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="Còn thiếu" width="140">
                <template slot-scope="scope">
                  <span
                    :class="
                      !!(scope.row.totalPrice - scope.row.deposit)
                        ? 'text-price-left'
                        : 'text-price'
                    "
                  >
                    {{
                      Utils.formatPrice(
                        scope.row.totalPrice - scope.row.deposit
                      )
                    }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="Phí ship" width="100">
                <template slot-scope="scope">
                  <span>
                    {{
                      scope.row.isShopBearShippingFee ? "Freeship" : "Khách trả"
                    }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                sortable
                prop="username"
                label="Nhân viên"
                width="150"
              />
              <el-table-column
                sortable
                prop="createdTime"
                label="Ngày tạo"
                width="100"
              >
                <template slot-scope="scope">
                  <el-tooltip
                    :content="Utils.formatDate(scope.row.createdTime)"
                  >
                    <span>{{
                      Utils.getSimpleDateFormat(scope.row.createdTime)
                    }}</span>
                  </el-tooltip>
                </template>
              </el-table-column>
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

    <!-- dialog -->
    <el-dialog
      title="Xác nhận thông tin giao hàng"
      :visible.sync="isDialogAddDeliveryVisible"
      width="1000px"
    >
      <el-form
        :model="formOrder"
        :rules="rules"
        ref="formOrder"
        label-position="top"
      >
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="Tên người nhận" prop="receiverName">
              <el-input v-model="formOrder.receiverName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="SĐT người nhận" prop="receiverPhone">
              <el-input v-model="formOrder.receiverPhone" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Mã vận đơn" prop="trackingOrder">
              <el-input v-model="formOrder.trackingOrder" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Dự kiến giao hàng">
              <el-date-picker
                class="w-full"
                v-model="formOrder.deliveryDate"
                :picker-options="pickerOptions"
                type="date"
                placeholder="Chọn ngày giao hàng"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Địa chỉ nhận hàng" prop="receiverAddress">
              <el-input type="textarea" v-model="formOrder.receiverAddress" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Ghi chú">
              <el-input type="textarea" v-model="formOrder.note" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Phí ship">
              <!-- Shop hoặc khách hàng chịu phí ship -->
              <el-radio-group v-model="formOrder.isShopBearShippingFee">
                <el-radio border :label="false">Khách trả ship</el-radio>
                <el-radio border :label="true">Freeship</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              class="el-input el-input--small"
              label="Phí giao hàng"
              prop="shippingFee"
            >
              <my-currency-input v-model="formOrder.shippingFee" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item class="el-input el-input--small" label="Đặt cọc">
              <my-currency-input v-model="formOrder.deposit" />
            </el-form-item>
          </el-col>
        </el-row>
        <hr />
        <el-row>
          <el-col>
            <el-form-item label="Chọn danh sách sản phẩm về hàng">
              <div>
                <el-table stripe :data="orderItemLists" style="width: 100%">
                  <el-table-column prop="productCode" label="Mã" width="150" />
                  <el-table-column prop="productName" label="Tên" />
                  <el-table-column prop="productSize" label="PL" width="100" />
                  <el-table-column label="Số lượng" width="120">
                    <template slot-scope="scope">
                      <el-input-number
                        class="width-8rem"
                        :controls="false"
                        :min="0"
                        :max="scope.row.remainQuantity"
                        v-model="scope.row.quantity"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <hr />
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="isDialogAddDeliveryVisible = false">Hủy</el-button>
        <el-button :disabled="isTableLoading" type="primary" @click="handleAddDeliveryInfo('formOrder')"
          >Hoàn thành</el-button
        >
      </span>
    </el-dialog>

    <!-- Detail dialog -->
    <el-dialog
      title="Chi tiết đơn hàng"
      :visible.sync="isDialogDetail"
      width="1000px"
      @close="getListCustomerOrders"
    >
      <customer-order-detail v-if="isDialogDetail" :id="customerOrderId" />
    </el-dialog>

    <!-- Edit dialog -->
    <el-dialog
      title="Sửa đơn hàng"
      :visible.sync="isDialogEdit"
      width="1200px"
      append-to-body
    >
      <customer-order-edit
        v-if="isDialogEdit"
        :isExchange="true"
        :id="customerOrderId"
        @close="onCloseDialogEdit"
      />
    </el-dialog>
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import { CustomerOrderStatus } from "@/services/core/constant";
import {
  getAllListCustomerOrders,
  changeStatusToExecute,
  changeStatusToCancel,
  changeStatusToComplete,
  saveCustomerOrderDelivery,
  exportToExcelGHTK,
  deliveryManyCustomerOrder,
  completeManyCustomerOrder,
  cancelManyCustomerOrder,
  markedAsHaveTrackingOrder,
} from "@/services/customer-order.service";
import { saveAs } from "file-saver";
import { getListProducts } from "@/services/product.service";

export default {
  name: "customer-order-list",
  layout: "main-layout",
  data() {
    return {
      Utils: Utils,
      isTableLoading: false,
      // Form
      formSearch: {
        startDate: "",
        endDate: "",
        status: "TOTAL",
        isMissingInfo: false,
        isShipAll: true,
        loadingProduct: false,
      },
      formDate: [],
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
      listCustomerOrders: [],
      // Pagination
      totalElements: 0,
      page: 1,
      size: 30,
      // Other
      orderItemLists: [],
      listStatus: [
        { value: "TOTAL", label: "Tất cả" },
        { value: CustomerOrderStatus.EXECUTE, label: "Đã đặt hàng" },
        { value: CustomerOrderStatus.DELIVERING, label: "Đang vận chuyển" },
        { value: CustomerOrderStatus.COMPLETE, label: "Hoàn thành" },
        { value: CustomerOrderStatus.CANCEL, label: "Đã hủy" },
        { value: CustomerOrderStatus.EXCHANGE, label: "Đổi trả" },
      ],
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
        note: "",
      },
      rules: {
        receiverName: [
          {
            required: true,
            message: "Vui lòng nhập tên người nhận!",
            trigger: "blur",
          },
        ],
        receiverAddress: [
          {
            required: true,
            message: "Vui lòng nhập địa chỉ người nhận!",
            trigger: "blur",
          },
        ],
        receiverPhone: [
          {
            required: true,
            message: "Vui lòng nhập SĐT người nhận!",
            trigger: "blur",
          },
        ],
        shipperPhone: [
          {
            required: true,
            message: "Vui lòng nhập SĐT shipper!",
            trigger: "blur",
          },
        ],
        shipperName: [
          {
            required: true,
            message: "Vui lòng nhập tên shipper!",
            trigger: "blur",
          },
        ],
        shippingFee: [
          {
            required: true,
            message: "Vui lòng nhập phí ship!",
            trigger: "blur",
          },
        ],
      },
      // Dialog
      isDialogDetail: false,
      isDialogEdit: false,
      customerOrderId: null,
      isDialogAddDeliveryVisible: false,
      listAllProducts: [],
      listProductsSelection: [],
    };
  },
  mounted() {
    const end = new Date();
    const start = new Date();
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
    this.formDate = [start, end];
    this.getListCustomerOrders();
  },
  methods: {
    getAllProducts(searchText) {
      this.formSearch.loadingProduct = true;
      getListProducts({
        name: searchText,
        code: searchText,
      })
        .then((res) => {
          this.listAllProducts = res.data?.content;
        })
        .finally(() => (this.formSearch.loadingProduct = false));
    },
    getStatus(customerOrder) {
      switch (customerOrder.status) {
        case CustomerOrderStatus.EXECUTE:
          return "Đã đặt hàng";
        case CustomerOrderStatus.DELIVERING:
          if (!customerOrder.isShipAll) {
            return "Đang vận chuyển";
          }
          return customerOrder.isHaveTrackingOrder
            ? "Đã tạo vận đơn"
            : "Chưa tạo vận đơn";
        case CustomerOrderStatus.COMPLETE:
          return "Hoàn thành";
        case CustomerOrderStatus.CANCEL:
          return "Đã hủy";
        case CustomerOrderStatus.EXCHANGE:
          return "Đổi trả";
      }
    },
    handleSelectionChange(val) {
      this.listProductsSelection = val;
    },
    goToCreatePage() {
      this.$router.push({ path: "/customer-order/create" });
    },
    changePage(p) {
      this.page = p;
      this.getListCustomerOrders();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getListCustomerOrders();
    },
    async getListCustomerOrders($event) {
      try {
        this.isTableLoading = true;
        const param = this._getQueryParam();
        const res = await getAllListCustomerOrders(param);
        if (res.data) {
          this.listCustomerOrders = res.data.content || [];
          this.totalElements = res.data.totalElements;
          this.listProductsSelection = [];
        }
      } finally {
        this.isTableLoading = false;
      }
    },
    goToDetailCustomerOrder(order) {
      // this.$router.push({
      //   name: "customer-order-detail-id",
      //   params: { id: order.id },
      // });
      this.isDialogDetail = true;
      this.customerOrderId = order.id;
    },
    cancelCustomerOrder(order) {
      if (confirm(`Bạn chắc chắn muốn hủy đơn hàng #${order.id}?`)) {
        this.isTableLoading = true;
        changeStatusToCancel(order.id)
          .then(() => {
            this.getListCustomerOrders();
            this.$notify({
              type: "success",
              message: "Hủy đơn hàng thành công!",
              position: "bottom-right",
            });
          })
          .finally(() => {
            this.isTableLoading = false;
          });
      }
    },
    async deliveryManyCustomerOrder() {
      if (
        confirm(
          `Bạn chắc chắn muốn giao toàn bộ đơn hàng:` +
            this.listProductsSelection.map((item) => `\nId: ${item.id}`)
        )
      ) {
        this.isTableLoading = true;
        try {
          const request = this.listProductsSelection.map((item) => item.id);
          await deliveryManyCustomerOrder(request);
          this.$notify({
            type: "success",
            message: "Giao toàn bộ đơn hàng thành công!",
            position: "bottom-right",
          });
          this.getListCustomerOrders();
        } catch (err) {
          console.log(err);
        } finally {
          this.isTableLoading = false;
        }
      }
    },
    async markedAsHaveTrackingOrder() {
      if (
        confirm(
          `Bạn chắc chắn muốn đánh dấu các đơn hàng:` +
            this.listProductsSelection.map((item) => `\nId: ${item.id}`)
        )
      ) {
        this.isTableLoading = true;
        try {
          const request = this.listProductsSelection.map((item) => item.id);
          await markedAsHaveTrackingOrder(request);
          this.$notify({
            type: "success",
            message: "Thao tác thành công!",
            position: "bottom-right",
          });
          this.getListCustomerOrders();
        } catch (err) {
          console.log(err);
        } finally {
          this.isTableLoading = false;
        }
      }
    },
    async completeManyCustomerOrder() {
      if (
        confirm(
          `Bạn chắc chắn muốn hoàn thành toàn bộ đơn hàng:` +
            this.listProductsSelection.map((item) => `\nId: ${item.id}`)
        )
      ) {
        this.isTableLoading = true;
        try {
          const request = this.listProductsSelection.map((item) => item.id);
          await completeManyCustomerOrder(request);
          this.$notify({
            type: "success",
            message: "Hoàn thành toàn bộ đơn hàng thành công!",
            position: "bottom-right",
          });
          this.getListCustomerOrders();
        } catch (err) {
          console.log(err);
        } finally {
          this.isTableLoading = false;
        }
      }
    },
    async cancelManyCustomerOrder() {
      if (
        confirm(
          `Bạn chắc chắn muốn hủy toàn bộ đơn hàng:` +
            this.listProductsSelection.map((item) => `\nId: ${item.id}`)
        )
      ) {
        this.isTableLoading = true;
        try {
          const request = this.listProductsSelection.map((item) => item.id);
          await cancelManyCustomerOrder(request);
          this.$notify({
            type: "success",
            message: "Hủy toàn bộ đơn hàng thành công!",
            position: "bottom-right",
          });
          this.getListCustomerOrders();
        } catch (err) {
          console.log(err);
        } finally {
          this.isTableLoading = false;
        }
      }
    },
    async exchangeCustomerOrder() {
      if (
        confirm(
          `Bạn chắc chắn muốn đổi trả đơn hàng:` +
            this.listProductsSelection.map((item) => `\nId: ${item.id}`)
        )
      ) {
        this.customerOrderId = this.listProductsSelection[0].id;
        this.isDialogEdit = true;
      }
    },
    executeCustomerOrder(order) {
      this.isTableLoading = true;
      changeStatusToExecute(order.id)
        .then(() => {
          this.getListCustomerOrders();
          this.$notify({
            type: "success",
            message: "Xác nhận đơn hàng thành công!",
            position: "bottom-right",
          });
        })
        .finally(() => {
          this.isTableLoading = false;
        });
    },
    completeCustomerOrder(order) {
      if (confirm(`Xác nhận hoàn thành đơn hàng #${order.id}?`)) {
        this.isTableLoading = true;
        changeStatusToComplete(order.id)
          .then(() => {
            this.getListCustomerOrders();
            this.$notify({
              type: "success",
              message: "Hoàn thành đơn hàng!",
              position: "bottom-right",
            });
          })
          .finally(() => {
            this.isTableLoading = false;
          });
      }
    },
    checkIsNoneItems() {
      return this.orderItemLists.every((i) => i.quantity === 0 || !i.quantity);
    },
    handleAddDeliveryInfo(formName) {
      if (this.checkIsNoneItems()) {
        this.$notify({
          type: "error",
          message: "Danh sách sản phẩm giao bị trống!",
          position: "bottom-right",
        });
        return;
      }
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const listProducts = this.orderItemLists
            .filter((i) => i.quantity && i.quantity !== 0)
            .map((i) => {
              return {
                customerOrderId: this.formOrder.id,
                productId: i.productId,
                quantity: i.quantity,
              };
            });
          const data = {
            receiverName: this.formOrder.receiverName,
            receiverPhone: this.formOrder.receiverPhone,
            receiverAddress: this.formOrder.receiverAddress,
            trackingOrder: this.formOrder.trackingOrder,
            deliveryDate: this.formOrder.deliveryDate,
            isShopBearShippingFee: this.formOrder.isShopBearShippingFee,
            shippingFee: this.formOrder.shippingFee,
            deposit: this.formOrder.deposit,
            note: this.formOrder.note,
            orderId: this.formOrder.id,
            listProducts: listProducts,
          };
          this.deliveringCustomerOrder(data);
        }
      });
    },
    deliveringCustomerOrder(order) {
      this.isTableLoading = true;
      saveCustomerOrderDelivery(order)
        .then(() => {
          this.isDialogAddDeliveryVisible = false;
          this.getListCustomerOrders();
          this.$notify({
            type: "success",
            message: "Tạo mới giao hàng sản phẩm thành công!",
            position: "bottom-right",
          });
        })
        .finally(() => {
          this.isTableLoading = false;
        });
    },
    async exportExcelGHTK() {
      const param = this._getQueryParam();
      const res = await exportToExcelGHTK(param);
      saveAs(res.data, res.headers["file-name"]);
      this.$notify({
        type: "success",
        message: "Đang thực hiện download file",
        position: "bottom-right",
      });
    },
    changeSearchMissingInfo(value) {
      if (value) {
        this.formSearch.receiverPhone = "";
        this.formSearch.receiverName = "";
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
        startDateStr: startDate.toISOString(),
        endDateStr: endDate.toISOString(),
        sortBy: "createdTime",
        sortDesc: true,
      };
      if (this.formSearch.status !== "TOTAL") {
        data.status = this.formSearch.status;
      }
      if (this.formSearch.status === "DELIVERING") {
        data.isShipAll = this.formSearch.isShipAll;
      }
      if (this.formSearch.productId) {
        data.productId = this.formSearch.productId;
      }
      if (this.formSearch.receiverName) {
        data.receiverName = this.formSearch.receiverName;
      }
      if (this.formSearch.receiverPhone) {
        data.receiverPhone = this.formSearch.receiverPhone;
      }
      if (this.formSearch.messengerUrl) {
        data.messengerUrl = this.formSearch.messengerUrl;
      }
      if (this.formSearch.isMissingInfo) {
        data.isMissingInfo = this.formSearch.isMissingInfo;
      }
      if (this.formSearch.id) {
        data.id = this.formSearch.id;
      }
      return new URLSearchParams(data);
    },
    isReadyRowClassName({ row }) {
      const index = this.listCustomerOrders.indexOf(row);
      return (row.status === CustomerOrderStatus.EXECUTE ||
        row.status === CustomerOrderStatus.DELIVERING) &&
        row.isReady
        ? index < 10
          ? "top-10-ready-order-class"
          : "ready-order-class"
        : "";
    },
    onCloseDialogEdit() {
      this.getListCustomerOrders();
      this.isDialogEdit = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.label-order {
  width: 120px;
  display: inline-block;
  font-weight: bold;
}
.el-card {
  margin: 15px 0;
}

.card-list-order {
  min-height: 385px;
  border-radius: 0.125rem;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  background: #fff;
}
.text-red {
  color: red;
}
.text-green {
  color: green;
}
</style>
