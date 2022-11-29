<template>
  <div class="user-create-page">
    <keyboard-events :keyCode="13" @keyup="getUsers" />
    <app-tab-bar />
    <app-page-title title="Danh sách nhân viên" />

    <div class="card mb-5 mb-xl-10">
      <div class="card-body">
        <el-form :model="formSearch" class="form" label-position="left">
          <div class="action-horizontal">
            <div class="flex-grow-1">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="Username / SĐT" class="fw-bold">
                    <el-input
                      maxlength="100"
                      class="width-max-25rem"
                      v-model="formSearch.keySearch"
                      placeholder="Nhập tên tài khoản/số điện thoại"
                    />
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label="Quyền" class="fw-bold">
                    <el-select v-model="formSearch.authority" placeholder="">
                      <el-option
                        v-for="s in listRoles"
                        :key="s.value"
                        :label="s.label"
                        :value="s.value"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
            <div class="action-horizontal__action-container">
              <el-button icon="el-icon-search" @click="getUsers" type="primary">
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
          <span class="card-label fw-bolder fs-3">Danh sách nhân viên</span>
        </div>
        <div class="d-flex align-items-center app-actions">
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
          :data="listUsers"
        >
          <template slot="empty">
            <span
              class="text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Không có dữ liệu
            </span>
          </template>
          <el-table-column fixed="left" label="Hành động" width="140">
            <template slot-scope="scope">
              <el-tooltip content="Cập nhật">
                <el-button
                  size="mini"
                  type="info"
                  icon="el-icon-edit"
                  @click="handleEditUser(scope.row)"
                />
              </el-tooltip>
              <el-tooltip content="Xóa">
                <el-button
                  size="mini"
                  type="danger"
                  icon="el-icon-delete"
                  @click="handleRemoveUser(scope.row)"
                />
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="id" label="#ID" width="60" />
          <el-table-column
            prop="fullName"
            label="Tên nhân viên"
            min-width="150"
          />
          <el-table-column prop="phone" label="SĐT" width="120" />
          <el-table-column prop="username" label="Tên đăng nhập" width="150" />
          <el-table-column prop="mail" label="Email" width="200" />
          <el-table-column prop="authority" label="Quyền" width="150">
            <template slot-scope="scope">
              {{ getAuthorityFormat(scope.row.authority) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="lastLoginTime"
            label="Đăng nhập gần nhất"
            width="150"
          >
            <template slot-scope="scope">
              <el-tooltip
                effect="dark"
                :content="Utils.formatDate(scope.row.lastLoginTime)"
              >
                <span>{{
                  Utils.getSimpleDateFormat(scope.row.lastLoginTime)
                }}</span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column prop="createdTime" label="Ngày tạo" width="150">
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
import { Role } from "@/services/core/constant";
import { getAllListUsers, deleteUser } from "@/services/user.service";

export default {
  name: "user-list",
  layout: "main-layout",
  middleware: "isAdmin",
  data() {
    return {
      Utils: Utils,
      isTableLoading: false,
      // Form
      formSearch: {
        keySearch: "",
        authority: null,
      },
      listRoles: [
        { value: null, label: "Tất cả" },
        { value: Role.ADMIN, label: "Admin" },
        { value: Role.MANAGER, label: "Quản lý" },
        { value: Role.USER, label: "Nhân viên" },
      ],
      // Data
      listUsers: [],
      // Pagination
      totalElements: 0,
      page: 1,
      size: 30,
    };
  },
  mounted() {
    this.getUsers();
  },
  methods: {
    goToCreatePage() {
      this.$router.push({ path: "/admin/user/create" });
    },
    handleEditUser(item) {
      this.$router.push({ path: `/admin/user/edit/${item.id}` });
    },
    handleRemoveUser(item) {
      if (confirm(`Bạn chắc chắn muốn xóa tài khoản ${item.username}?`)) {
        deleteUser(item.id).then(() => {
          this.$notify({
            type: "success",
            message: "Xóa thành công!",
            position: "bottom-right",
          });
          this.getUsers();
        });
      }
    },
    changePage(p) {
      this.page = p;
      this.getUsers();
    },
    handleSizeChange(val) {
      this.size = val;
      this.getUsers();
    },
    async getUsers() {
      try {
        this.isTableLoading = true;
        const param = this._getQueryParam();
        const res = await getAllListUsers(param);
        if (res.data) {
          this.listUsers = res.data.content || [];
          this.totalElements = res.data.totalElements;
        }
      } finally {
        this.isTableLoading = false;
      }
    },
    // Private functions
    _getQueryParam() {
      const data = {
        page: this.page - 1,
        size: this.size,
        sortBy: "createdTime",
        sortDesc: true,
      };
      if (this.formSearch.authority) {
        data.authority = this.formSearch.authority;
      }
      if (this.formSearch.keySearch) {
        data.keySearch = this.formSearch.keySearch;
      }
      return new URLSearchParams(data);
    },
    getAuthorityFormat(authority) {
      switch (authority) {
        case Role.ADMIN:
          return "Quản trị viên";
        case Role.MANAGER:
          return "Quản lý";
        case Role.USER:
          return "Nhân viên";
      }
    },
  },
};
</script>
