<template>
  <div class="mb-5 mb-xl-10">
    <div class="px-10 py-0">
      <!--begin::Navs-->
      <ul
        class="
          nav nav-stretch nav-line-tabs nav-line-tabs-2x
          border-transparent
          fs-5
          fw-bolder
        "
      >
        <!--begin::Nav item-->
        <!-- <li class="nav-item mt-2">
          <NuxtLink
            to="/"
            class="nav-link text-active-primary ms-0 me-10 py-5"
            :class="{ active: isRouteActive('/') }"
            exact-active-class="active"
            >Dashboard
          </NuxtLink>
        </li> -->
        <!--end::Nav item-->
        <!--begin::Nav item-->
        <li class="nav-item mt-2">
          <NuxtLink
            to="/product"
            class="nav-link text-active-primary ms-0 me-10 py-5"
            :class="{ active: isRouteActive('/product') }"
            exact-active-class="active"
            >Kho hàng
          </NuxtLink>
        </li>
        <!--end::Nav item-->
        <!--begin::Nav item-->
        <!-- <li class="nav-item mt-2">
          <NuxtLink
            to="/warehouse"
            class="nav-link text-active-primary ms-0 me-10 py-5"
            :class="{ active: isRouteActive('/warehouse') }"
            exact-active-class="active"
            >Kho hàng
          </NuxtLink>
        </li> -->
        <!--end::Nav item-->
        <!--begin::Nav item-->
        <li class="nav-item mt-2">
          <NuxtLink
            to="/customer-order"
            class="nav-link text-active-primary ms-0 me-10 py-5"
            :class="{ active: isRouteActive('/customer-order') }"
            exact-active-class="active"
            >Đơn hàng
          </NuxtLink>
        </li>
        <!--end::Nav item-->
        <!--begin::Nav item-->
        <li class="nav-item mt-2">
          <NuxtLink
            to="/shop-order"
            class="nav-link text-active-primary ms-0 me-10 py-5"
            :class="{ active: isRouteActive('/shop-order') }"
            exact-active-class="active"
            >Đơn hàng nội bộ
          </NuxtLink>
        </li>
        <!--end::Nav item-->
      </ul>
      <!--begin::Navs-->
    </div>
  </div>
</template>

<script>
export default {
  name: "app-tab-bar",
  data() {
    return {};
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