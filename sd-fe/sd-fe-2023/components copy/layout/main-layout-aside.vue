<template>
  <!--begin::Aside-->
  <div
    id="kt_aside"
    class="aside card"
    data-kt-drawer="true"
    data-kt-drawer-name="aside"
    data-kt-drawer-activate="{default: true, lg: false}"
    data-kt-drawer-overlay="true"
    data-kt-drawer-width="{default:'200px', '300px': '250px'}"
    data-kt-drawer-direction="start"
    data-kt-drawer-toggle="#kt_aside_toggle"
  >
    <!--begin::Aside menu-->
    <div class="aside-menu flex-column-fluid px-5">
      <!--begin::Aside Menu-->
      <div
        class="hover-scroll-overlay-y my-5 pe-4 me-n4"
        id="kt_aside_menu_wrapper"
        data-kt-scroll="true"
        data-kt-scroll-activate="{default: false, lg: true}"
        data-kt-scroll-height="auto"
        data-kt-scroll-dependencies="#kt_header, #kt_aside_footer"
        data-kt-scroll-wrappers="#kt_aside, #kt_aside_menu"
        data-kt-scroll-offset="{lg: '75px'}"
      >
        <!--begin::Menu-->
        <div
          class="menu menu-column menu-rounded fw-bold fs-6"
          id="#kt_aside_menu"
          data-kt-menu="true"
        >
          <div class="menu-item">
            <div class="menu-content pb-2">
              <span class="menu-section text-muted text-uppercase fs-8 ls-1"
                >Dashboard</span
              >
            </div>
          </div>
          <div class="menu-item">
            <NuxtLink
              to="/"
              class="menu-link"
              :class="{ active: isRouteActive('/') }"
              exact-active-class="active"
            >
              <span class="menu-icon">
                <icon-graph-circle />
              </span>
              <span class="menu-title">Dashboard</span>
            </NuxtLink>
          </div>
          <div class="menu-item">
            <div class="menu-content pt-8 pb-2">
              <span class="menu-section text-muted text-uppercase fs-8 ls-1"
                >Entities</span
              >
            </div>
          </div>

          <div
            v-if="user.authority !== Role.USER"
            data-kt-menu-trigger="click"
            class="menu-item menu-accordion"
          >
            <span class="menu-link">
              <span class="menu-icon">
                <icon-user />
              </span>
              <span class="menu-title">Admin</span>
              <span class="menu-arrow"></span>
            </span>
            <div class="menu-sub menu-sub-accordion menu-active-bg">
              <div class="menu-item">
                <NuxtLink
                  to="/admin/user"
                  class="menu-link"
                  :class="{ active: isRouteActive('/admin/user') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/admin/user/create"
                  class="menu-link"
                  :class="{ active: isRouteActive('/admin/user/create') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Thêm mới</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/admin/log"
                  class="menu-link"
                  :class="{ active: isRouteActive('/admin/log') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách các file logs</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/admin/product-log"
                  class="menu-link"
                  :class="{ active: isRouteActive('/admin/product-log') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Lịch sử kho hàng</span>
                </NuxtLink>
              </div>
            </div>
          </div>

          <div data-kt-menu-trigger="click" class="menu-item menu-accordion">
            <span class="menu-link">
              <span class="menu-icon">
                <icon-grid />
              </span>
              <span class="menu-title">Kho hàng</span>
              <span class="menu-arrow"></span>
            </span>
            <div class="menu-sub menu-sub-accordion menu-active-bg">
              <div class="menu-item">
                <NuxtLink
                  to="/product"
                  class="menu-link"
                  :class="{ active: isRouteActive('/product') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/product/create"
                  class="menu-link"
                  :class="{ active: isRouteActive('/product/create') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Thêm mới</span>
                </NuxtLink>
              </div>
              <div v-if="user.authority !== Role.USER" class="menu-item">
                <NuxtLink
                  to="/admin/product-log"
                  class="menu-link"
                  :class="{ active: isRouteActive('/admin/product-log') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Lịch sử cập nhật</span>
                </NuxtLink>
              </div>
            </div>
          </div>

          <!-- <div data-kt-menu-trigger="click" class="menu-item menu-accordion">
            <span class="menu-link">
              <span class="menu-icon">
                <icon-warehouse />
              </span>
              <span class="menu-title">Kho hàng</span>
              <span class="menu-arrow"></span>
            </span>
            <div class="menu-sub menu-sub-accordion menu-active-bg">
              <div class="menu-item">
                <NuxtLink
                  to="/warehouse"
                  class="menu-link"
                  :class="{ active: isRouteActive('/warehouse') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách</span>
                </NuxtLink>
              </div>
              <div v-if="user.authority !== Role.USER" class="menu-item">
                <NuxtLink
                  to="/warehouse/create"
                  class="menu-link"
                  :class="{ active: isRouteActive('/warehouse/create') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Thêm mới</span>
                </NuxtLink>
              </div>
              <div v-if="user.authority !== Role.USER" class="menu-item">
                <NuxtLink
                  to="/warehouse/log"
                  class="menu-link"
                  :class="{ active: isRouteActive('/warehouse/log') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Lịch sử cập nhật</span>
                </NuxtLink>
              </div>
            </div>
          </div> -->

          <div data-kt-menu-trigger="click" class="menu-item menu-accordion">
            <span class="menu-link">
              <span class="menu-icon">
                <icon-cart />
              </span>
              <span class="menu-title">Đơn hàng</span>
              <span class="menu-arrow"></span>
            </span>
            <div class="menu-sub menu-sub-accordion menu-active-bg">
              <div class="menu-item">
                <NuxtLink
                  to="/customer-order"
                  class="menu-link"
                  :class="{ active: isRouteActive('/customer-order') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/customer-order/create"
                  class="menu-link"
                  :class="{ active: isRouteActive('/customer-order/create') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Thêm mới</span>
                </NuxtLink>
              </div>
            </div>
          </div>

          <div data-kt-menu-trigger="click" class="menu-item menu-accordion">
            <span class="menu-link">
              <span class="menu-icon">
                <icon-truck />
              </span>
              <span class="menu-title">Đơn hàng nội bộ</span>
              <span class="menu-arrow"></span>
            </span>
            <div class="menu-sub menu-sub-accordion menu-active-bg">
              <div class="menu-item">
                <NuxtLink
                  to="/shop-order"
                  class="menu-link"
                  :class="{ active: isRouteActive('/shop-order') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Danh sách</span>
                </NuxtLink>
              </div>
              <div class="menu-item">
                <NuxtLink
                  to="/shop-order/create"
                  class="menu-link"
                  :class="{ active: isRouteActive('/shop-order/create') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Thêm mới</span>
                </NuxtLink>
              </div>
              <!-- <div class="menu-item">
                <NuxtLink
                  to="/shop-order/detail/"
                  class="menu-link"
                  :class="{ active: isRouteActive('/shop-order/detail') }"
                  exact-active-class="active"
                >
                  <span class="menu-bullet">
                    <span class="bullet bullet-dot"></span>
                  </span>
                  <span class="menu-title">Chi tiết</span>
                </NuxtLink>
              </div> -->
            </div>
          </div>
        </div>
        <!--end::Menu-->
      </div>
    </div>
    <!--end::Aside menu-->
  </div>
  <!--end::Aside-->
</template>

<script>
import { Role } from "@/services/core/constant";

export default {
  name: "main-layout-aside",
  data() {
    return {
      Role: Role,
    };
  },
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
  methods: {
    isRouteActive(route) {
      if (this.$nuxt.$route.path === route) {
        return true;
      } else {
        return false;
      }
    },
  },
};
</script>
