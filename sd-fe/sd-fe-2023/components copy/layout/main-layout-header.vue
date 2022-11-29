<template>
  <!--begin::Header-->
  <div id="kt_header" class="header">
    <!--begin::Container-->
    <div class="container-fluid d-flex flex-stack">
      <!--begin::Brand-->
      <div class="d-flex align-items-center me-5">
        <!--begin::Aside toggle-->
        <div
          class="d-lg-none btn btn-icon btn-active-color-white w-30px h-30px ms-n2 me-3"
          id="kt_aside_toggle"
        >
          <!--begin::Svg Icon | path: icons/duotune/abstract/abs015.svg-->
          <span class="svg-icon svg-icon-2">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
            >
              <path
                d="M21 7H3C2.4 7 2 6.6 2 6V4C2 3.4 2.4 3 3 3H21C21.6 3 22 3.4 22 4V6C22 6.6 21.6 7 21 7Z"
                fill="black"
              />
              <path
                opacity="0.3"
                d="M21 14H3C2.4 14 2 13.6 2 13V11C2 10.4 2.4 10 3 10H21C21.6 10 22 10.4 22 11V13C22 13.6 21.6 14 21 14ZM22 20V18C22 17.4 21.6 17 21 17H3C2.4 17 2 17.4 2 18V20C2 20.6 2.4 21 3 21H21C21.6 21 22 20.6 22 20Z"
                fill="black"
              />
            </svg>
          </span>
          <!--end::Svg Icon-->
        </div>
        <!--end::Aside  toggle-->
        <!--begin::Logo-->
        <NuxtLink to="/">
          <img
            alt="Logo"
            src="/assets/media/logos/logo-2.svg"
            class="h-25px h-lg-30px"
          />
        </NuxtLink>
        <!--end::Logo-->
        <!--begin::Nav-->
        <div class="ms-5 ms-md-10">
          <span class="text-white fs-4 fw-bolder lh-1"
            >Keep it simple 2023</span
          >
          <app-tab-bar />
        </div>
        <!--end::Nav-->
      </div>
      <!--end::Brand-->
      <!--begin::Topbar-->
      <div class="d-flex align-items-center">
        <!--begin::Topbar-->
        <div class="d-flex align-items-center flex-shrink-0">
          <!--begin::Search-->
          <!-- <div class="d-flex align-items-center ms-1">
            <div
              class="btn btn-icon btn-color-white bg-hover-white bg-hover-opacity-10 w-30px h-30px h-40px w-40px position-relative"
            >
              <icon-search />
            </div>
          </div> -->
          <!--end::Search-->
          <!--begin::Chat-->
          <div class="d-flex align-items-center ms-1">
            <div
              class="btn btn-icon btn-color-white bg-hover-white bg-hover-opacity-10 w-30px h-30px h-40px w-40px position-relative"
            >
              <icon-message />
              <span
                class="bullet bullet-dot bg-success h-6px w-6px position-absolute translate-middle top-0 start-50 animation-blink"
              ></span>
            </div>
          </div>
          <!--end::Chat-->
          <!--begin::User-->
          <div
            class="d-flex align-items-center ms-1"
            id="kt_header_user_menu_toggle"
            data-cy="user-menu-toggle"
          >
            <!--begin::User info-->
            <div
              class="btn btn-flex align-items-center bg-hover-white bg-hover-opacity-10 py-2 px-2 px-md-3"
              data-kt-menu-trigger="click"
              data-kt-menu-attach="parent"
              data-kt-menu-placement="bottom-end"
            >
              <!--begin::Name-->
              <div
                class="d-none d-md-flex flex-column align-items-end justify-content-center me-2 me-md-4"
              >
                <span class="text-muted fs-8 fw-bold lh-1 mb-1"
                  >{{ user ? user.firstName : "" }}
                  {{ user ? user.lastName : "" }}</span
                >
                <span class="text-white fs-8 fw-bolder lh-1">{{
                  getRole()
                }}</span>
              </div>
              <!--end::Name-->
              <!--begin::Symbol-->
              <div class="symbol symbol-30px symbol-md-40px">
                <img
                  src="https://i.pinimg.com/736x/0d/c4/dd/0dc4dd7b618b03fc50e90be2dc1ccace.jpg"
                  alt="image"
                />
              </div>
              <!--end::Symbol-->
            </div>
            <!--end::User info-->
            <!--begin::Menu-->
            <div
              class="menu menu-sub menu-sub-dropdown menu-column menu-rounded menu-gray-800 menu-state-bg menu-state-primary fw-bold py-4 fs-6 w-275px"
              data-kt-menu="true"
            >
              <!--begin::Menu item-->
              <div class="menu-item px-3">
                <div class="menu-content d-flex align-items-center px-3">
                  <!--begin::Avatar-->
                  <div class="symbol symbol-50px me-5">
                    <img
                      alt="Logo"
                      src="https://i.pinimg.com/736x/0d/c4/dd/0dc4dd7b618b03fc50e90be2dc1ccace.jpg"
                    />
                  </div>
                  <!--end::Avatar-->
                  <!--begin::Username-->
                  <div class="d-flex flex-column">
                    <div class="fw-bolder d-flex align-items-center fs-5">
                      {{ user ? user.firstName : "" }}
                      {{ user ? user.lastName : "" }}
                      <span
                        class="badge badge-light-success fw-bolder fs-8 px-2 py-1 ms-2"
                        >{{ getRole() }}</span
                      >
                    </div>
                    <a
                      href="#"
                      class="fw-bold text-muted text-hover-primary fs-7"
                      >{{ user ? user.username : "" }}</a
                    >
                  </div>
                  <!--end::Username-->
                </div>
              </div>
              <!--end::Menu item-->
              <!--begin::Menu separator-->
              <div class="separator my-2"></div>
              <!--end::Menu separator-->
              <template v-if="user.authority !== Role.USER">
                <!--begin::Menu item-->
                <div class="menu-item px-5">
                  <NuxtLink to="/admin/user" class="menu-link px-5"
                    >Danh sách nhân viên
                  </NuxtLink>
                </div>
                <!--end::Menu item-->
                <!--begin::Menu item-->
                <div class="menu-item px-5">
                  <NuxtLink to="/admin/log" class="menu-link px-5">
                    <span class="menu-text">Danh sách các file logs</span>
                  </NuxtLink>
                </div>
                <!--end::Menu item-->
                <!--begin::Menu item-->
                <div class="menu-item px-5">
                  <NuxtLink to="/admin/product-log" class="menu-link px-5">
                    <span class="menu-text">Lịch sử kho hàng</span>
                  </NuxtLink>
                </div>
                <!--end::Menu item-->
              </template>
              <!--begin::Menu separator-->
              <div class="separator my-2"></div>
              <!--end::Menu separator-->
              <!--begin::Menu item-->
              <!-- <div
                class="menu-item px-5"
                data-kt-menu-trigger="hover"
                data-kt-menu-placement="left-start"
              >
                <a href="#" class="menu-link px-5">
                  <span class="menu-title position-relative"
                    >Language
                    <span
                      class="fs-8 rounded bg-light px-3 py-2 position-absolute translate-middle-y top-50 end-0"
                      >English
                      <img
                        class="w-15px h-15px rounded-1 ms-2"
                        src="/assets/media/flags/united-states.svg"
                        alt="" /></span
                  ></span>
                </a>
                <div class="menu-sub menu-sub-dropdown w-175px py-4">
                  <div class="menu-item px-3">
                    <NuxtLink to="/" class="menu-link d-flex px-5">
                      <span class="symbol symbol-20px me-4">
                        <img
                          class="rounded-1"
                          src="/assets/media/flags/united-states.svg"
                          alt=""
                        /> </span
                      >English
                    </NuxtLink>
                  </div>
                  <div class="menu-item px-3">
                    <NuxtLink to="/" class="menu-link d-flex px-5">
                      <span class="symbol symbol-20px me-4">
                        <img
                          class="rounded-1"
                          src="/assets/media/flags/japan.svg"
                          alt=""
                        /> </span
                      >Japanese
                    </NuxtLink>
                  </div>
                </div>
              </div> -->
              <!--end::Menu item-->
              <!--begin::Menu item-->
              <!-- <div class="menu-item px-5 my-1">
                <NuxtLink to="/" class="menu-link d-flex px-5">
                  Account Settings
                </NuxtLink>
              </div> -->
              <!--end::Menu item-->
              <!--begin::Menu item-->
              <div class="menu-item px-5">
                <a
                  @click="logout"
                  href="javascript:;"
                  class="menu-link px-5"
                  data-cy="menu-item-logout"
                  >Đăng xuất</a
                >
              </div>
              <!--end::Menu item-->
            </div>
            <!--end::Menu-->
          </div>
          <!--end::User -->
        </div>
        <!--end::Topbar-->
      </div>
      <!--end::Topbar-->
    </div>
    <!--end::Container-->
  </div>
  <!--end::Header-->
</template>

<script>
import { Role } from "@/services/core/constant";

export default {
  name: "main-layout-header",
  data() {
    return {
      Role: Role,
    };
  },
  computed: {
    user() {
      if (this.$store.state.auth.user) {
        const u = this.$store.state.auth.user;
        return u;
      }
      return {
        username: "",
        firstName: "",
        lastName: "",
        authority: "",
      };
    },
  },
  methods: {
    logout() {
      this.$store.commit("auth/LOGOUT");
      localStorage.removeItem("user");
      this.$notify({
        type: "success",
        message: "Đăng xuất thành công!",
        position: "bottom-right",
      });
      this.$router.push("/login");
    },
    getRole() {
      const role = this.user.authority;
      switch (role) {
        case Role.ADMIN:
          return "Admin";
        case Role.MANAGER:
          return "Quản lý";
        case Role.USER:
          return "Nhân viên";
        default:
          return "";
      }
    },
  },
};
</script>
