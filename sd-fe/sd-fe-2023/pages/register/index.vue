<template>
  <div class="limiter">
    <div class="container-login100">
      <div class="wrap-login100">
        <div class="login100-pic js-tilt" data-tilt></div>

        <form class="login100-form validate-form">
          <span class="login100-form-title">Member Register</span>

          <div
            class="wrap-input100 validate-input"
            data-validate="Username is required"
          >
            <input
              class="input100"
              type="text"
              v-model="registerForm.username"
              name="username"
              placeholder="Username"
            />
            <span class="focus-input100"></span>
            <span class="symbol-input100">
              <i class="fa fa-user" />
            </span>
          </div>

          <div
            class="wrap-input100 validate-input"
            data-validate="Password is required"
          >
            <input
              class="input100"
              type="password"
              v-model="registerForm.password"
              name="pass"
              placeholder="Password"
            />
            <span class="focus-input100"></span>
            <span class="symbol-input100">
              <i class="fa fa-lock" aria-hidden="true"></i>
            </span>
          </div>

          <div class="wrap-input100 validate-input">
            <input
              class="input100"
              type="text"
              v-model="registerForm.firstName"
              name="firstName"
              placeholder="First name"
            />
            <span class="focus-input100"></span>
            <span class="symbol-input100">
              <i class="fa fa-user" />
            </span>
          </div>

          <div class="wrap-input100 validate-input">
            <input
              class="input100"
              type="text"
              v-model="registerForm.lastName"
              name="lastName"
              placeholder="Last name"
            />
            <span class="focus-input100"></span>
            <span class="symbol-input100">
              <i class="fa fa-user" />
            </span>
          </div>

          <div class="container-login100-form-btn">
            <button
              class="login100-form-btn"
              @click.prevent="handleRegister(registerForm)"
            >
              Login
            </button>
          </div>

          <div class="text-center p-t-136">
            <span class="txt1">Already have an account?</span>
            <a class="txt2" @click="goToLogin">
              Login
              <i class="fa fa-long-arrow-right m-l-5" aria-hidden="true"></i>
            </a>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
</style>

<script>
import { register } from "@/services/auth.service.ts";

export default {
  name: "Register",
  layout: "auth-layout",
  meta: {
    requireAuth: false,
  },
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error("The password can not be less than 6 digits"));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        username: "",
        password: "",
        firstName: "",
        lastName: "",
      },
      loginRules: {
        username: [{ required: true, trigger: "blur" }],
        password: [
          { required: true, trigger: "blur", validator: validatePassword },
        ],
      },
      passwordType: "password",
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {},
      // Validate
      formData: {
        username: null,
        password: null,
      },
    };
  },
  mounted() {},
  methods: {
    checkCapslock(e) {
      const { key } = e;
      this.capsTooltip = key && key.length === 1 && key >= "A" && key <= "Z";
    },
    showPwd() {
      if (this.passwordType === "password") {
        this.passwordType = "";
      } else {
        this.passwordType = "password";
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      });
    },
    handleRegister(values) {
      this.loading = true;
      register(values).then(() => {
        this.$notify({
          type: "success",
          message: "Đăng ký thành công!",
          position: "bottom-right",
        });
        this.$router.push("/login");
        this.loading = false;
      });
    },
    goToLogin() {
      this.$router.push("/login");
    },
  },
};
</script>