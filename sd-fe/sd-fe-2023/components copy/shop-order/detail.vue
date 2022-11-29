<template>
  <div class="card mb-5 mb-xl-10">
    <div class="card-body list-vertical">
      <!-- Title + actions -->
      <div class="detail-title-container">
        <h6 class="flex-grow-1">Order ID: #{{ shopOrder.id }}</h6>
        <div v-if="user.authority !== Role.USER" class="d-flex align-items-center app-actions">
          <el-button
            type="info"
            icon="el-icon-box"
            v-if="
              shopOrder.status === 'EXECUTE' ||
              shopOrder.status === 'DELIVERING'
            "
            data-cy="buttonAddDelivery"
            @click="buttonAddDelivery"
            >Thêm đợt giao</el-button
          >
          <el-button
            type="success"
            icon="el-icon-check"
            v-if="shopOrder.status === 'DELIVERING'"
            @click="completeOrder(shopOrder)"
          >
            Hoàn thành đơn
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-close"
            v-if="
              shopOrder.status !== 'CANCEL'
            "
            @click="handleCancelOrder(shopOrderId)"
          >
            Hủy đơn
          </el-button>
          <el-button
            type="warning"
            icon="el-icon-edit-outline"
            v-if="
              shopOrder.status === 'EXECUTE' ||
              shopOrder.status === 'DELIVERING'
            "
            @click="gotoEditOrder"
            >Sửa</el-button
          >
        </div>
      </div>

      <!-- Status -->
      <el-steps :active="shopOrder.activeStatus">
        <el-step title="Đã đặt hàng" icon="el-icon-circle-check" />
        <el-step title="Đang vận chuyển" icon="el-icon-truck" />
        <el-step
          v-if="!shopOrder.isCancel"
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
                <span
                  class="text-status fw-bold"
                  :class="
                    shopOrder.status === 'COMPLETE' ? 'color-success' : ''
                  "
                  type="text"
                  >{{ getStatus(shopOrder.status) }}</span
                >
              </p>
              <p>
                <strong>Ngày giao hàng: </strong>
                {{ Utils.formatDate(shopOrder.deliveryDate) }}
              </p>
            </div>
            <div class="flex-root">
              <p>
                <strong>Tên: </strong>
                {{ shopOrder.shopOrderName }}
              </p>
              <p>
                <strong>Ghi chú: </strong>
                {{ shopOrder.note }}
              </p>
            </div>
            <div class="flex-root">
              <p>
                <strong>Nhà vận chuyển: </strong>
                {{ shopOrder.transporter }}
              </p>
              <p>
                <strong>Nguồn hàng: </strong>
                {{ shopOrder.source }}
              </p>
            </div>
          </div>
        </article>
      </div>

      <div>
        <h6>Danh sách sản phẩm</h6>
        <article class="card">
          <div class="card-body detail-info-container">
            <el-table
              stripe
              cell-class-name="px-2 py-2 whitespace-nowrap"
              :data="shopOrder.orderItemList"
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
              >
              </el-table-column>
              <el-table-column
                prop="productName"
                label="Tên sản phẩm"
                min-width="200"
              >
              </el-table-column>
              <el-table-column prop="productSize" label="Phân loại" width="80">
              </el-table-column>
              <el-table-column prop="quantity" label="Số lượng" width="80">
              </el-table-column>
              <el-table-column prop="availableQuantity" label="Kho" width="80">
              </el-table-column>
              <el-table-column prop="deliveredQuantity" label="Đã về" width="80">
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
                  <span>{{ Utils.formatPrice(scope.row.price) }} </span>
                </template>
              </el-table-column>
              <el-table-column
                fixed="right"
                prop="totalPrice"
                label="Tổng cộng"
                width="150"
              >
                <template slot-scope="scope">
                  <span class="text-price"
                    >{{ Utils.formatPrice(scope.row.totalPrice) }}
                  </span>
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
            <el-timeline :reverse="false">
              <el-timeline-item
                v-for="(delivery, index) in shopOrderDeliveries"
                :key="index"
                placement="top"
                :timestamp="Utils.getSimpleDateFormat(delivery.deliveryDate)"
              >
                <div>
                  <div v-if="!delivery.isCanceled">
                    <div
                      v-if="
                        shopOrder.status === 'EXECUTE' ||
                        shopOrder.status === 'DELIVERING'
                      "
                    >
                      <el-button
                        v-if="user.authority !== Role.USER"
                        class="m-2"
                        type="warning"
                        @click="buttonUpdateDelivery(delivery)"
                        >Sửa</el-button
                      >
                      <el-button
                        v-if="user.authority !== Role.USER"
                        class="m-2"
                        type="danger"
                        @click="cancelDelivery(delivery.id)"
                        >Hủy</el-button
                      >
                    </div>
                  </div>
                  <el-row v-else>
                    <el-col>
                      <el-button class="m-2" :disabled="true" type="default"
                        >Đã hủy</el-button
                      >
                    </el-col>
                  </el-row>
                  <el-row :gutter="20">
                    <el-col :span="12">
                      <el-table stripe :data="delivery.listProducts" border>
                        <el-table-column
                          prop="productCode"
                          label="Mã sản phẩm"
                          width="100"
                        />
                        <el-table-column
                          prop="productName"
                          label="Tên sản phẩm"
                        />
                        <el-table-column
                          prop="productSize"
                          label="Phân loại"
                          width="100"
                        />
                        <el-table-column
                          prop="quantity"
                          label="SL"
                          width="70"
                        />
                        <el-table-column
                          prop="price"
                          label="Đơn giá"
                          width="70"
                        />
                      </el-table>
                    </el-col>
                    <el-col :span="12">
                      <el-row :gutter="20">
                        <el-col :span="12">
                          <h6>Mã vận đơn:</h6>
                          {{ delivery.trackingOrder }}
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
      :title="isEdit ? 'Sửa đợt giao' : 'Thêm đợt giao'"
      :visible.sync="isDialogAddDeliveryVisible"
      append-to-body
      width="720px"
    >
      <el-form :model="formDelivery" label-position="top">
        <el-form-item label="Mã vận đơn">
          <el-input v-model="formDelivery.trackingOrder" />
        </el-form-item>
        <el-form-item label="Chọn danh sách sản phẩm về hàng">
          <div>
            <el-table stripe :data="orderItemLists" style="width: 100%">
              <el-table-column prop="productCode" label="Mã" width="150" />
              <el-table-column prop="productName" label="Tên" />
              <el-table-column prop="productSize" label="PL" width="100" />
              <el-table-column label="Số lượng" width="120">
                <template slot-scope="scope">
                  <el-input-number
                    v-if="isEdit"
                    class="width-8rem"
                    :controls="false"
                    :min="0"
                    :data-cy="'quantity_' + scope.row.productCode"
                    v-model="scope.row.quantity"
                  />
                  <el-input-number
                    v-else
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
        <el-form-item label="Note">
          <el-input
            type="textarea"
            placeholder="Nhập ghi chú"
            v-model="formDelivery.note"
          />
        </el-form-item>
      </el-form>
      <span v-if="user.authority !== Role.USER" slot="footer" class="dialog-footer">
        <el-button
          :disabled="checkIsNoneItems() || isLoading"
          type="primary"
          @click="handleAddDelivery"
          data-cy="saveShopOrderDelivery"
          >Hoàn thành</el-button
        >
      </span>
    </el-dialog>

    <!-- Edit dialog -->
    <el-dialog
      title="Sửa đơn hàng"
      :visible.sync="isDialogEdit"
      width="1200px"
      append-to-body
    >
      <shop-order-edit v-if="isDialogEdit" :id="shopOrderId" @close="onCloseDialogEdit" />
    </el-dialog>
  </div>
</template>

<script>
import { Utils } from "@/services/core/utils";
import { Role } from "@/services/core/constant";
import {
  getOneShopOrders,
  getShopOrderDeliveries,
  saveShopOrderDelivery,
  cancelShopOrder,
  changeStatusToComplete,
  cancelShopOrderDelivery,
} from "@/services/shop-order.service";

export default {
  name: "ShopOrderDetail",
  layout: "main-layout",
  props: ["id"],
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
      shopOrderId: this.id,
      isDialogEdit: false,
      shopOrder: {},
      shopOrderDeliveries: [],
      isDialogAddDeliveryVisible: false,
      orderItemLists: [],
      formDelivery: {},
      isEdit: false,
      isLoading: false,
    };
  },
  mounted() {
    if (this.shopOrderId) {
      this.getDetailShopOrder();
    } else {
      this.$emit("close");
    }
  },
  methods: {
    checkIsNoneItems() {
      return this.orderItemLists.every((i) => i.quantity === 0 || !i.quantity);
    },
    handleCancelOrder(id) {
      if (confirm(`Bạn chắc chắn muốn hủy đơn hàng #${id}?`)) {
        cancelShopOrder(id).then((res) => {
          if (res.data) {
            this.$notify({
              type: "success",
              message: `Đơn hàng #${id} đã được hủy`,
              position: "bottom-right",
            });
            this.getDetailShopOrder();
          }
        });
      }
    },
    getDetailShopOrder() {
      getOneShopOrders(this.shopOrderId).then((res) => {
        this.shopOrder = res.data;
        const list = JSON.parse(JSON.stringify(this.shopOrder.orderItemList));
        this.orderItemLists = list;
        this.getListShopOrderDeliveries();
        this.shopOrder.isCancel = false;
        switch (res.data.status) {
          case "EXECUTE":
            this.shopOrder.activeStatus = 1;
            break;
          case "DELIVERING":
            this.shopOrder.activeStatus = 2;
            break;
          case "COMPLETE":
            this.shopOrder.activeStatus = 3;
            break;
          case "CANCEL":
            this.shopOrder.activeStatus = 3;
            this.shopOrder.isCancel = true;
            break;
        }
      });
    },
    getListShopOrderDeliveries() {
      getShopOrderDeliveries(this.shopOrderId).then((res) => {
        this.shopOrderDeliveries = res.data;
      });
    },
    getStatus(status) {
      switch (status) {
        case "EXECUTE":
          return "Đã đặt hàng";
        case "DELIVERING":
          return "Đang vận chuyển";
        case "COMPLETE":
          return "Hoàn thành";
        case "CANCEL":
          return "Đã hủy";
      }
    },
    getSummaries(param) {
      const { columns, data } = param;
      const sums = [];
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = "Tổng đơn hàng";
          return;
        } else if (index === 3 || index === 5) {
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
        } else if (index === 7) {
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
          } else {
            sums[index] = "";
          }
        }
      });

      return sums;
    },
    buttonAddDelivery() {
      this.isDialogAddDeliveryVisible = true;
      this.isEdit = false;
      this.formDelivery = {
        note: "",
        trackingOrder: "",
      };
    },
    buttonUpdateDelivery(delivery) {
      this.isEdit = true;
      this.formDelivery = Object.assign({}, delivery);
      this.orderItemLists = this.shopOrder.orderItemList.map((i) => {
        const item = delivery.listProducts.find(
          (p) => p.productId == i.productId
        );
        i.quantity = item ? item.quantity : 0;
        return i;
      });
      this.isDialogAddDeliveryVisible = true;
    },
    handleAddDelivery() {
      if (this.checkIsNoneItems()) {
        this.$notify({
          type: "error",
          message: "Danh sách sản phẩm hàng về bị trống!",
          position: "bottom-right",
        });
        return;
      }
      const listProducts = this.orderItemLists.map((i) => {
        return {
          orderId: this.shopOrderId,
          productId: i.productId,
          quantity: i.quantity,
        };
      });
      const data = {
        id: this.formDelivery.id,
        listProducts: listProducts,
        note: this.formDelivery.note,
        trackingOrder: this.formDelivery.trackingOrder,
        orderId: this.shopOrderId,
        deliveryDate: this.formDelivery.deliveryDate,
      };
      saveShopOrderDelivery(data).then((res) => {
        this.$notify({
          type: "success",
          message: "Tạo đợt giao hàng thành công!",
          position: "bottom-right",
        });
        this.getDetailShopOrder();
        this.isDialogAddDeliveryVisible = false;
      });
    },

    gotoEditOrder() {
      this.isDialogEdit = true;
    },
    onCloseDialogEdit() {
      this.getDetailShopOrder();
      this.isDialogEdit = false;
    },
    cancelDelivery(deliveryId) {
      if (confirm("Bạn có chắc muốn hủy đợt giao này?")) {
        cancelShopOrderDelivery(deliveryId).then((res) => {
          this.$notify({
            type: "success",
            message: "Hủy đợt giao thành công!",
            position: "bottom-right",
          });
          this.getDetailShopOrder();
        });
      }
    },
    completeOrder(order) {
      if (confirm("Bạn xác nhận hoàn thành đơn hàng này?")) {
        this.isLoading = true;
        changeStatusToComplete(order.id)
          .then(() => {
            this.getDetailShopOrder();
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
  color: red;
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
    color: red;
  }
}
.el-checkbox-group {
  max-height: 300px;
}
.text-detail-item {
  font-weight: bold;
}
</style>
