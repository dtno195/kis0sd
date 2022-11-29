<template>
  <div class="d-flex flex-center flex-column flex-column-fluid">
    <!--begin::Wrapper-->
    <div class="w-lg-500px p-10 p-lg-15 mx-auto">
      <!--begin::Form-->
      <ValidationObserver v-slot="{ handleSubmit }">
        <form
          class="form w-100"
          novalidate="novalidate"
          id="kt_sign_in_form"
          action="#"
          @submit.prevent="handleSubmit(onLogin)"
        >
          <!--begin::Heading-->
          <div class="text-center mb-10">
            <!--begin::Title-->
            <h1 class="text-dark mb-3">Đăng nhập hệ thống</h1>
            <!--end::Title-->
            <!--begin::Link-->
            <!-- <div class="text-gray-400 fw-bold fs-4">
              Chưa có tài khoản?
              <a
                @click="goToRegister"
                class="link-primary fw-bolder"
                href="javascript:;"
              >
                Đăng ký
              </a>
            </div> -->
            <!--end::Link-->
          </div>
          <!--begin::Heading-->
          <!--begin::Input group-->
          <ValidationProvider
            name="username"
            rules="required"
            v-slot="{ errors }"
          >
            <div class="fv-row mb-10">
              <!--begin::Label-->
              <label class="form-label fs-6 fw-bolder text-dark"
                >Tên đăng nhập</label
              >
              <!--end::Label-->
              <!--begin::Input-->
              <input
                class="form-control form-control-lg form-control-solid"
                type="text"
                name="username"
                data-cy="username"
                autocomplete="off"
                v-model="loginForm.username"
              />
              <!--end::Input-->
              <div class="mt-1">
                <span class="text-danger">{{ errors[0] }}</span>
              </div>
            </div>
          </ValidationProvider>
          <!--end::Input group-->
          <!--begin::Input group-->

          <ValidationProvider
            name="password"
            rules="required|min:6"
            v-slot="{ errors }"
          >
            <div class="fv-row mb-10">
              <!--begin::Wrapper-->
              <div class="d-flex flex-stack mb-2">
                <!--begin::Label-->
                <label class="form-label fw-bolder text-dark fs-6 mb-0"
                  >Mật khẩu</label
                >
                <!--end::Label-->
                <!--begin::Link-->
                <a
                  href="../../demo14/dist/authentication/layouts/aside/password-reset.html"
                  class="link-primary fs-6 fw-bolder"
                  >Quên mật khẩu?</a
                >
                <!--end::Link-->
              </div>
              <!--end::Wrapper-->
              <!--begin::Input-->
              <input
                class="form-control form-control-lg form-control-solid"
                type="password"
                name="password"
                data-cy="password"
                autocomplete="off"
                v-model="loginForm.password"
              />
              <!--end::Input-->
              <div class="mt-1">
                <span class="text-danger">{{ errors[0] }}</span>
              </div>
            </div>
          </ValidationProvider>
          <!--end::Input group-->
          <!--begin::Actions-->
          <div class="text-center">
            <!--begin::Submit button-->
            <button
              id="kt_sign_in_submit"
              class="btn btn-lg btn-primary w-100 mb-5"
              type="submit"
              data-cy="login"
            >
              <span v-if="!loading" class="indicator-label">Đăng nhập</span>
              <span v-else class="indicator-label">
                Vui lòng chờ...
                <span
                  class="spinner-border spinner-border-sm align-middle ms-2"
                ></span>
              </span>
            </button>
            <!--end::Submit button-->
          </div>
          <!--end::Actions-->
        </form>
      </ValidationObserver>
      <!--end::Form-->
    </div>
    <!--end::Wrapper-->
  </div>
</template>

<style lang="scss" scoped>
</style>

<script>
import { login } from "@/services/auth.service";
export default {
  name: "Login",
  layout: "auth-layout",
  meta: {
    requireAuth: false,
  },
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
      },
      loading: false,
      // Validate
    };
  },
  created() {
    if (process.client) {
      this.$cookiz.set("user", "");
      this.$cookiz.set("token", "");
      this.$store.commit("auth/LOGOUT");
      localStorage.setItem("user", "");
      localStorage.setItem("token", "");
    }
  },
  methods: {
    goToRegister() {
      this.$router.push("/register");
    },
    async onLogin() {
      this.loading = true;
      try {
        const res = await login(this.loginForm);
        this.$cookiz.set("user", res.data.user);
        this.$cookiz.set("token", res.data.jwtToken);
        this.$store.commit("auth/SET_USER", res.data.user);
        this.$store.commit("auth/SET_TOKEN", res.data.jwtToken);
        localStorage.setItem("user", JSON.stringify(res.data.user));
        localStorage.setItem("token", res.data.jwtToken);
        this.$notify({
          type: "success",
          message: "Đăng nhập thành công!",
          position: "bottom-right",
        });
        this.$router.push("/product");
      } catch (err) {
        this.$notify({
          type: "error",
          message: "Đăng nhập thất bại!",
          position: "bottom-right",
        });
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>