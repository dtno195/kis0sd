<template>
  <div class="user-create-page">
    <app-tab-bar />
    <app-page-title title="Chỉnh sửa tài khoản" />
    <div class="card mb-5 mb-xl-10" v-loading="isLoading">
      <div class="card-body">
        <div class="container">
          <el-form
            :model="userForm"
            :rules="rules"
            ref="userForm"
            label-width="150px"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="Tên tài khoản">
                  <el-input v-model="userForm.username" readonly />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Quyền" prop="authority">
                  <el-select class="w-100" v-model="userForm.authority">
                    <el-option
                      v-for="p in listRoles"
                      :key="p.value"
                      :label="p.label"
                      :value="p.value"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="Họ và tên đệm" prop="firstName">
                  <el-input v-model="userForm.firstName" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Tên" prop="lastName">
                  <el-input v-model="userForm.lastName" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="Số điện thoại" prop="phone">
                  <el-input v-model="userForm.phone" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="Email" prop="mail">
                  <el-input v-model="userForm.mail" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-button @click="isEditPassword = !isEditPassword">
              Cập nhật mật khẩu
            </el-button>
            <el-row v-if="isEditPassword" :gutter="20">
              <el-col :span="12">
                <el-form-item label="Mật khẩu" prop="password">
                  <el-input type="password" v-model="userForm.password" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item
                  label="Nhập lại mật khẩu"
                  prop="passwordConfirmed"
                >
                  <el-input
                    type="password"
                    v-model="userForm.passwordConfirmed"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </div>
      <div class="card-footer d-flex justify-content-end py-4 px-8">
        <el-button @click="goBack">Hủy</el-button>
        <el-button type="primary" @click="submitForm('userForm')"
          >Lưu</el-button
        >
      </div>
    </div>
  </div>
</template>
<script>
import { Role } from "@/services/core/constant";
import { updateUser, getOneUser } from "@/services/user.service";

export default {
  name: "user-edit",
  layout: "main-layout",
  middleware: "isAdmin",
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Vui lòng nhập mật khẩu!"));
      } else {
        if (this.userForm.passwordConfirmed !== "") {
          this.$refs.userForm.validateField("passwordConfirmed");
        }
        callback();
      }
    };
    const validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Vui lòng nhập lại mật khẩu!"));
      } else if (value !== this.userForm.password) {
        callback(new Error("Mật khẩu không khớp nhau!"));
      } else {
        callback();
      }
    };
    const validatePhone = (rule, value, callback) => {
      const regex = new RegExp("\\+[0-9]{2}-[0-9]{4}-[0-9]{4}");
      if (regex.test(value)) {
        callback();
      } else {
        callback(new Error("Vui lòng nhập đúng định dạng số điện thoại!"));
      }
    };
    return {
      isLoading: false,
      isEditPassword: false,
      userForm: {
        id: this.$route.params["id"],
        firstName: "",
        lastName: "",
        phone: "",
        mail: "",
        authority: "",
        password: "",
        passwordConfirmed: "",
      },
      listRoles: [
        { value: Role.ADMIN, label: "Admin" },
        { value: Role.MANAGER, label: "Quản lý" },
        { value: Role.USER, label: "Nhân viên" },
      ],
      rules: {
        firstName: [
          {
            required: true,
            message: "Vui lòng nhập họ và tên đệm!",
            trigger: "blur",
          },
          {
            min: 1,
            max: 15,
            message: "Độ dài không quá 15 ký tự!",
            trigger: "blur",
          },
        ],
        lastName: [
          {
            required: true,
            message: "Vui lòng nhập tên!",
            trigger: "blur",
          },
          {
            min: 1,
            max: 15,
            message: "Độ dài không quá 15 ký tự!",
            trigger: "blur",
          },
        ],
        phone: [
          {
            required: true,
            message: "Vui lòng nhập số điện thoại!",
            trigger: "blur",
          },
          // { validator: validatePhone, trigger: ["blur", "change"] },
        ],
        password: [
          {
            required: true,
            message: "Vui lòng nhập mật khẩu!",
            trigger: "blur",
          },
          { validator: validatePass, trigger: "blur" },
          {
            min: 1,
            max: 100,
            message: "Độ dài không quá 100 ký tự!",
            trigger: "blur",
          },
        ],
        passwordConfirmed: [
          {
            required: true,
            message: "Vui lòng nhập lại mật khẩu!",
            trigger: "blur",
          },
          { validator: validatePass2, trigger: "blur" },
          {
            min: 1,
            max: 100,
            message: "Độ dài không quá 100 ký tự!",
            trigger: "blur",
          },
        ],
        mail: [
          {
            type: "email",
            message: "Vui lòng nhập đúng định dạng email!",
            trigger: ["blur", "change"],
          },
        ],
      },
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    async loadData() {
      try {
        this.isLoading = true;
        const res = await getOneUser(this.userForm.id);
        Object.assign(this.userForm, res.data);
      } finally {
        this.isLoading = false;
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          try {
            this.isLoading = true;
            const data = {
              firstName: this.userForm.firstName,
              lastName: this.userForm.lastName,
              phone: this.userForm.phone,
              mail: this.userForm.mail,
              authority: this.userForm.authority,
            };
            if (this.isEditPassword) {
              data.password = this.userForm.password;
              data.passwordConfirmed = this.userForm.passwordConfirmed;
            }
            const res = await updateUser(this.userForm.id, data);
            this.$notify({
              type: "success",
              message: "Cập nhật tài khoản thành công!",
              position: "bottom-right",
            });
            this.goBack();
          } catch {
            this.$notify({
              type: "error",
              message: "Cập nhật tài khoản thất bại!",
              position: "bottom-right",
            });
          } finally {
            this.isLoading = false;
          }
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    goBack() {
      this.$router.push("/admin/user");
    },
  },
};
</script>
<style lang="scss" scoped></style>
