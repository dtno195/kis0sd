<template>
  <div class="card mb-5 mb-xl-10">
    <div class="card-body list-vertical">
      <!-- Title + actions -->
      <div class="detail-title-container">
        <h6 class="flex-grow-1">Order ID: #{{ customerOrder.id }}</h6>
        <span class="text-price-total">
          {{ Utils.formatPrice(customerOrder.totalPrice) }}
        </span>
        <div class="d-flex align-items-center app-actions">
          <el-button
            type="info"
            icon="el-icon-truck"
            v-if="
              customerOrder.status === 'EXECUTE' ||
              customerOrder.status === 'DELIVERING' ||
              customerOrder.status === 'EXCHANGE'
            "
            data-cy="buttonAddDelivery"
            @click="addDelivery"
          >
            Giao đơn
          </el-button>
          <el-button
            type="success"
            icon="el-icon-check"
            v-if="customerOrder.status === 'DELIVERING' || customerOrder.status === 'EXCHANGE'"
            @click="completeCustomerOrder(customerOrder)"
          >
            Hoàn thành đơn
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-close"
            v-if="
              customerOrder.status !== 'COMPLETE' &&
              customerOrder.status !== 'CANCEL'
            "
            @click="openDialogCancelOrder"
          >
            Hủy đơn
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-edit-outline"
            v-if="customerOrder.status === 'EXECUTE' ||
              customerOrder.status === 'EXCHANGE' || (customerOrder.status === 'DELIVERING' && !customerOrder.isShipAll)"
            @click="gotoEditOrder"
            >Sửa</el-button
          >
        </div>
      </div>

      <!-- Status -->
      <el-steps v-if="customerOrder.status !== 'EXCHANGE'" :active="customerOrder.activeStatus">
        <el-step title="Đã đặt hàng" icon="el-icon-circle-check" />
        <el-step title="Đang giao hàng" icon="el-icon-truck" />
        <el-step
          v-if="!customerOrder.isCancel"
          title="Hoàn thành"
          icon="el-icon-finished"
        />
        <el-step v-else title="Đã hủy" icon="el-icon-close" />
      </el-steps>

      <!-- Thông tin -->
      <div>
        <h6>Thông tin</h6>
        <article class="card">
          <div class="card-body detail-info-container">
            <div class="flex-root">
              <p>
                <strong>Trạng thái: </strong>
                <span class="fw-bold"
                  :class="customerOrder.status === 'DELIVERING' ? (customerOrder.isHaveTrackingOrder ? 'text-green' : 'text-red') : ''"
                >{{
                  getStatus(customerOrder)
                }}</span>
              </p>
              <p><strong>Nhân viên: </strong> {{ customerOrder.username }}</p>
              <p>
                <strong>Ngày tạo: </strong>
                {{ Utils.formatDate(customerOrder.createdTime) }}
              </p>
              <p>
                <strong>Ngày giao hàng: </strong>
                {{ Utils.formatDate(customerOrder.deliveryDate) }}
              </p>
            </div>
            <div class="flex-root">
              <p>
                <strong>Người nhận: </strong>
                <a
                  v-if="customerOrder.messengerUrl"
                  :href="customerOrder.messengerUrl"
                  :title="customerOrder.messengerUrl"
                  target="_blank"
                >
                  {{ customerOrder.receiverName }}
                </a>
                <span v-else>{{ customerOrder.receiverName }}</span>
              </p>
              <p><strong>SĐT: </strong> {{ customerOrder.receiverPhone }}</p>
              <p>
                <strong>Địa chỉ: </strong>
                {{ customerOrder.receiverAddress }}
              </p>
              <p>
                <strong>Ghi chú: </strong>
                {{ customerOrder.note }}
              </p>
            </div>
            <div class="flex-root">
              <p>
                <strong>Mã vận đơn: </strong>
                {{ customerOrder.trackingOrder }}
              </p>
              <p>
                <strong>Phí ship: </strong>
                <span class="text-price">
                  {{ Utils.formatPrice(customerOrder.shippingFee) }}
                </span>
              </p>
              <p v-if="customerOrder.deposit">
                <strong>Đặt cọc: </strong>
                <span>{{ Utils.formatPrice(customerOrder.deposit) }}</span>
              </p>
              <p v-if="customerOrder.note">
                <strong>Ghi chú: </strong>
                <span>{{ customerOrder.note }}</span>
              </p>
            </div>
          </div>
        </article>
      </div>

      <!-- Danh sách sản phẩm -->
      <div>
        <h6>Danh sách sản phẩm</h6>
        <article class="card">
          <div class="card-body">
            <el-table
              stripe
              cell-class-name="px-2 py-2 whitespace-nowrap"
              :data="customerOrder.orderItemList"
              :summary-method="getSummaries"
              show-summary
            >
              <template slot="empty">
                <span
                  class="text-xs font-medium text-gray-500 uppercase tracking-wider"
                  >Không có sản phẩm nào</span
                >
              </template>
              <el-table-column
                prop="productCode"
                label="Mã sản phẩm"
                width="150"
              />
              <el-table-column
                prop="productName"
                label="Tên sản phẩm"
                min-width="200"
              />
              <el-table-column
                prop="productSize"
                label="Phân loại"
                width="80"
              />
              <el-table-column prop="quantity" label="Số lượng" width="80">
                <template slot-scope="scope">
                  <span
                    :class="
                      scope.row.quantity - scope.row.deliveredQuantity <=
                      scope.row.availableQuantity
                        ? 'text-success'
                        : 'text-price'
                    "
                    >{{ scope.row.quantity }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                prop="deliveredQuantity"
                label="Đã giao"
                width="80"
              >
                <template slot-scope="scope">
                  <span
                    :class="
                      scope.row.deliveredQuantity === scope.row.quantity
                        ? 'text-success'
                        : 'text-price'
                    "
                    >{{ scope.row.deliveredQuantity }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="Giá" width="150">
                <template slot-scope="scope">
                  <span>{{ Utils.formatPrice(scope.row.price) }}</span>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="totalPrice"
                label="Tổng cộng"
                width="150"
              >
                <template slot-scope="scope">
                  <span class="text-price">{{
                    Utils.formatPrice(scope.row.totalPrice)
                  }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </article>
      </div>

      <!-- Đợt giao hàng -->
      <div>
        <h6>Đợt giao hàng</h6>
        <article class="card">
          <div class="card-body">
            <el-timeline
              v-if="customerOrderDeliveries.length > 0"
              :reverse="false"
            >
              <el-timeline-item
                v-for="(delivery, index) in customerOrderDeliveries"
                :key="index"
                placement="top"
                :timestamp="Utils.getSimpleDateFormat(delivery.createdTime)"
              >
                <div>
                  <div v-if="!delivery.isCanceled">
                    <el-button
                      v-if="customerOrder.status === 'EXECUTE' ||
                        customerOrder.status === 'EXCHANGE' ||
                        (customerOrder.status === 'DELIVERING' && !customerOrder.isShipAll)"
                      class="m-2"
                      type="warning"
                      @click="updateDelivery(delivery)"
                      >Sửa</el-button
                    >
                    <el-button
                      v-if="
                        customerOrder.status === 'EXECUTE' ||
                        customerOrder.status === 'DELIVERING' ||
                        customerOrder.status === 'EXCHANGE'
                      "
                      class="m-2"
                      type="danger"
                      @click="cancelDelivery(delivery.id)"
                      >Hủy</el-button
                    >
                  </div>
                  <el-row v-else>
                    <el-col>
                      <el-button class="m-2" :disabled="true" type="default"
                        >Đã hủy</el-button
                      >
                    </el-col>
                  </el-row>
                  <el-row :gutter="20">
                    <el-col :span="16">
                      <el-table stripe :data="delivery.listProducts" border>
                        <el-table-column
                          prop="productCode"
                          label="Mã sản phẩm"
                          width="100"
                        />
                        <el-table-column
                          prop="productName"
                          label="Tên sản phẩm"
                          min-width="120"
                        />
                        <el-table-column
                          prop="productSize"
                          label="Phân loại"
                          width="75"
                        />
                        <el-table-column
                          prop="quantity"
                          label="SL"
                          width="50"
                        />
                        <el-table-column prop="price" label="Giá" width="120">
                          <template slot-scope="scope">
                            <span class="text-price">{{
                              Utils.formatPrice(scope.row.price)
                            }}</span>
                          </template>
                        </el-table-column>
                      </el-table>
                    </el-col>
                    <el-col :span="8">
                      <el-row :gutter="20">
                        <el-col :span="12">
                          <h6>Mã vận đơn:</h6>

                          <span v-if="isEditingTrackingOrder">
                            <el-input v-model="trackingOrder" />
                            <el-button
                              class="m-2"
                              type="warning"
                              @click="updateTrackingOrder(delivery)"
                              >Lưu</el-button
                            >
                            <el-button
                              class="m-2"
                              type="warning"
                              @click="isEditingTrackingOrder = false"
                              >Hủy</el-button
                            >
                          </span>
                          <span v-else>
                            {{ delivery.trackingOrder }}
                            <el-button
                              class="m-2"
                              type="warning"
                              @click="isEditingTrackingOrder = true; trackingOrder = delivery.trackingOrder;"
                              >Sửa</el-button
                            >
                          </span>

                        </el-col>
                        <el-col :span="12">
                          <h6>Ghi chú:</h6>
                          {{ delivery.note }}
                        </el-col>
                      </el-row>
                    </el-col>
                  </el-row>
                </div>
                <hr />
              </el-timeline-item>
            </el-timeline>
          </div>
        </article>
      </div>
    </div>

    <!-- dialog -->
    <el-dialog
      :title="isEdit ? 'Sửa đợt giao hàng' : 'Tạo mới giao hàng'"
      :visible.sync="isDialogAddDeliveryVisible"
      width="720px"
      append-to-body
    >
      <el-form
        :model="formDelivery"
        :rules="rules"
        ref="formDelivery"
        label-position="top"
      >
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="Tên người nhận" prop="receiverName">
              <el-input v-model="formDelivery.receiverName" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="SĐT người nhận" prop="receiverPhone">
              <el-input v-model="formDelivery.receiverPhone" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Mã vận đơn" prop="trackingOrder">
              <el-input v-model="formDelivery.trackingOrder" data-cy="trackingOrder" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="Dự kiến giao hàng">
              <el-date-picker
                class="w-full"
                v-model="formDelivery.deliveryDate"
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
              <el-input
                type="textarea"
                v-model="formDelivery.receiverAddress"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Ghi chú">
              <el-input type="textarea" v-model="formDelivery.note" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Phí ship">
              <!-- Shop hoặc khách hàng chịu phí ship -->
              <el-radio-group v-model="formDelivery.isShopBearShippingFee">
                <el-radio border :label="false">Khách trả ship</el-radio>
                <el-radio border :label="true">Freeship</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              class="el-input el-input--small"
              label="Phí giao hàng"
            >
              <my-currency-input v-model="formDelivery.shippingFee" />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item class="el-input el-input--small" label="Đặt cọc">
              <my-currency-input v-model="formDelivery.deposit" />
            </el-form-item>
          </el-col>
        </el-row>
        <hr />
        <el-row>
          <el-col>
            <el-form-item label="Chọn danh sách sản phẩm về hàng">
              <div v-if="!isEdit">
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
                        :data-cy="'quantity_' + scope.row.productCode + '_' + scope.row.productSize"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </div>
              <div v-else>
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
        <el-button
          :disabled="checkIsNoneItems() || isLoading"
          type="primary"
          @click="handleAddDeliveryInfo('formDelivery')"
          data-cy="save"
          >Hoàn thành</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      title="Hủy đơn hàng"
      :visible.sync="isDialogCancelOrderVisible"
      width="720px"
      append-to-body
    >
      <el-form>
        <el-row :gutter="20">
          <el-col>
            <el-form-item label="Ghi chú" prop="note">
              <el-input v-model="noteCancel" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col>
            <el-button @click="cancelCustomerOrder(customerOrder)"
              >Xác nhận hủy</el-button
            >
          </el-col>
        </el-row>
      </el-form>
    </el-dialog>

    <!-- Edit dialog -->
    <el-dialog
      title="Sửa đơn hàng"
      :visible.sync="isDialogEdit"
      width="1200px"
      append-to-body
    >
      <customer-order-edit v-if="isDialogEdit" :isExchange="false" :id="customerOrderId" @close="onCloseDialogEdit" />
    </el-dialog>
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import {
  getOneCustomerOrders,
  changeStatusToCancel,
  changeStatusToComplete,
  saveCustomerOrderDelivery,
  cancelCustomerOrderDelivery,
  getCustomerOrderDeliveries,
  updateTrackingOrderDelivery,
} from "@/services/customer-order.service";

export default {
  name: "CustomerOrderDetail",
  layout: "main-layout",
  props: ["id"],
  data() {
    return {
      Utils: Utils,
      customerOrderId: this.id,
      isDialogCancelOrderVisible: false,
      isDialogEdit: false,
      customerOrder: {
        shippingFee: 0,
      },
      customerOrderDeliveries: [],
      isDialogAddDeliveryVisible: false,
      checkListItems: [],
      pickerOptions: {
        disabledDate(time) {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          return time.getTime() < today;
        },
      },
      formDelivery: {
        receiverName: "",
        receiverAddress: "",
        receiverPhone: "",
        shipperPhone: "",
        shipperName: "",
        shippingFee: "",
        messengerUrl: "",
        deposit: "",
        deliveryDate: "",
        trackingOrder: "",
        isShopBearShippingFee: true,
        note: "",
      },
      noteCancel: "",
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
        trackingOrder: [
          {
            required: true,
            message: "Vui lòng nhập mã vận đơn!",
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
        note: [
          {
            required: true,
            message: "Vui lòng nhập ghi chú!",
            trigger: "blur",
          },
        ],
      },
      isLoading: false,
      isEdit: false,
      orderItemLists: [],
      formDelivery: {},
      trackingOrder: "",
      isEditingTrackingOrder: false,
    };
  },
  mounted() {
    if (this.customerOrderId) {
      this.getDetailCustomerOrder();
    } else {
      this.$emit("close");
    }
  },
  methods: {
    getDetailCustomerOrder() {
      this.isLoading = true;
      getOneCustomerOrders(this.customerOrderId)
        .then((res) => {
          this.customerOrder = res.data;
          const list = JSON.parse(
            JSON.stringify(this.customerOrder.orderItemList)
          );
          this.orderItemLists = list.map((i) => {
            if (i.quantity - i.deliveredQuantity > i.availableQuantity) {
              i.quantity = 0;
            }
            return i;
          });
          this.getListCustomerOrderDeliveries();
          this.customerOrder.isCancel = false;
          switch (res.data.status) {
            case "EXECUTE":
              this.customerOrder.activeStatus = 1;
              break;
            case "DELIVERING":
              this.customerOrder.activeStatus = 2;
              break;
            case "COMPLETE":
              this.customerOrder.activeStatus = 3;
              break;
            case "CANCEL":
              this.customerOrder.activeStatus = 3;
              this.customerOrder.isCancel = true;
              break;
            case "EXCHANGE":
              this.customerOrder.activeStatus = 3;
              break;
          }
        })
        .finally(() => (this.isLoading = false));
    },
    getListCustomerOrderDeliveries() {
      getCustomerOrderDeliveries(this.customerOrderId).then((res) => {
        this.customerOrderDeliveries = res.data;
      });
    },
    getStatus(customerOrder) {
      switch (customerOrder.status) {
        case "WAITING":
          return "Đơn chờ";
        case "EXECUTE":
          return "Đã đặt hàng";
        case "DELIVERING":
          if (!customerOrder.isShipAll) {
            return "Đang vận chuyển";
          }
          return customerOrder.isHaveTrackingOrder
            ? "Đã tạo vận đơn"
            : "Chưa tạo vận đơn";
        case "COMPLETE":
          return "Hoàn thành";
        case "CANCEL":
          return "Đã hủy";
        case "EXCHANGE":
          return "Đổi trả";
      }
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "Tổng đơn hàng";
          return;
        } else if (index === 3 || index === 4) {
          const values = data.map((item) => Number(item[column.property]));
          if (!values.every((value) => isNaN(value))) {
            const total = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = total;
          } else {
            sums[index] = "";
          }
        } else if (index === 6) {
          const values = data.map((item) => Number(item[column.property]));
          if (!values.every((value) => isNaN(value))) {
            const totalPrice = values.reduce((prev, curr) => {
              const value = Number(curr);
              if (!isNaN(value)) {
                return prev + curr;
              } else {
                return prev;
              }
            }, 0);
            sums[index] = Utils.formatPrice(totalPrice);
            this.customerOrder.totalPrice = sums[index];
          } else {
            sums[index] = "";
          }
        }
      });
      return sums;
    },
    openDialogCancelOrder() {
      this.isDialogCancelOrderVisible = true;
      this.noteCancel = "";
    },
    cancelCustomerOrder(order) {
      this.isLoading = true;
      const data = {
        id: order.id,
        status: "CANCEL",
        note: this.noteCancel,
      };
      if (this.noteCancel) {
        changeStatusToCancel(data)
          .then((res) => {
            this.isDialogCancelOrderVisible = false;
            this.getDetailCustomerOrder();
            this.$notify({
              type: "success",
              message: "Hủy đơn hàng thành công!",
              position: "bottom-right",
            });
          })
          .finally(() => {
            this.isLoading = false;
          });
      } else {
        alert("Vui lòng nhập lý do hủy đơn hàng!");
      }
    },
    completeCustomerOrder(order) {
      if (confirm("Bạn xác nhận hoàn thành đơn hàng này?")) {
        this.isLoading = true;
        changeStatusToComplete(order.id)
          .then(() => {
            this.getDetailCustomerOrder();
            this.$notify({
              type: "success",
              message: "Hoàn thành đơn hàng!",
              position: "bottom-right",
            });
          })
          .finally(() => {
            this.isLoading = false;
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
                CustomerOrderId: this.customerOrderId,
                productId: i.productId,
                quantity: i.quantity,
              };
            });
          const data = {
            id: this.formDelivery.id,
            receiverName: this.formDelivery.receiverName,
            receiverPhone: this.formDelivery.receiverPhone,
            receiverAddress: this.formDelivery.receiverAddress,
            trackingOrder: this.formDelivery.trackingOrder,
            deliveryDate: this.formDelivery.deliveryDate,
            isShopBearShippingFee: this.formDelivery.isShopBearShippingFee,
            shippingFee: this.formDelivery.shippingFee,
            deposit: this.formDelivery.deposit,
            note: this.formDelivery.note,
            orderId: this.customerOrderId,
            listProducts: listProducts,
          };
          this.deliveringCustomerOrder(data);
        }
      });
    },
    deliveringCustomerOrder(delivery) {
      this.isLoading = true;
      saveCustomerOrderDelivery(delivery)
        .then(() => {
          this.getDetailCustomerOrder();
          this.isDialogAddDeliveryVisible = false;
          this.$notify({
            type: "success",
            message: this.isEdit
              ? "Cập nhật giao hàng sản phẩm thành công!"
              : "Tạo mới giao hàng sản phẩm thành công!",
            position: "bottom-right",
          });
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
    addDelivery() {
      this.isDialogAddDeliveryVisible = true;
      this.isEdit = false;
      this.formDelivery = Object.assign({}, this.customerOrder);
      this.formDelivery.id = null;
    },
    updateDelivery(delivery) {
      this.isEdit = true;
      this.formDelivery = Object.assign({}, delivery);
      this.isDialogAddDeliveryVisible = true;
    },
    cancelDelivery(deliveryId) {
      if (confirm("Bạn có chắc muốn hủy đợt giao này?")) {
        cancelCustomerOrderDelivery(deliveryId).then((res) => {
          this.$notify({
            type: "success",
            message: "Hủy đợt giao thành công!",
            position: "bottom-right",
          });
          this.getDetailCustomerOrder();
        });
      }
    },
    gotoEditOrder() {
      this.isDialogEdit = true;
    },
    onCloseDialogEdit() {
      this.getDetailCustomerOrder();
      this.isDialogEdit = false;
    },
    updateTrackingOrder(delivery) {
      // if (confirm("Bạn xác nhận cập nhật mã vận đơn chứ?"))
      const data = {
        orderDeliveryId: delivery.id,
        trackingOrder: this.trackingOrder,
      };
      updateTrackingOrderDelivery(data).then((res) => {
        this.$notify({
          type: "success",
          message: "Cập nhật mã vận đơn thành công!",
          position: "bottom-right",
        });
        this.getDetailCustomerOrder();
        this.isEditingTrackingOrder = false;
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.card {
  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  -ms-flex-direction: column;
  flex-direction: column;
  min-width: 0;
  word-wrap: break-word;
  background-color: #fff;
  background-clip: border-box;
  border: 1px solid rgba(0, 0, 0, 0.1);
  border-radius: 0.1rem;
}

p {
  margin-top: 0;
  margin-bottom: 1rem;
}
.text-price {
  color: #ee4d2d;
}
.text-success {
  color: green;
}
.detail-text {
  width: 470px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 20px;
}
::v-deep {
  label.el-checkbox.detail-checkbox {
    width: 100%;
    display: flex;
    align-content: center;
    align-items: center;
  }
  span.el-checkbox__label {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-content: center;
    justify-content: space-between;
    align-items: center;
  }
  label.el-checkbox.detail-checkbox {
    height: 50px;
  }
  .el-table__header-wrapper tbody td.el-table__cell,
  .el-table__footer-wrapper tbody td.el-table__cell {
    font-weight: bold;
  }
  td.el-table_1_column_7.is-leaf.el-table__cell {
    color: #ee4d2d;
  }
}
.el-checkbox-group {
  max-height: 300px;
}
.text-detail-item {
  font-weight: bold;
}
.text-status {
  line-height: 24px;
  color: #ee4d2d;
  text-align: right;
  text-transform: uppercase;
  white-space: nowrap;
}
.text-red {
  color: red;
}
.text-green {
  color: green;
}
</style>
