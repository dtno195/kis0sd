<template>
  <div id="kt_body" class="header-fixed header-tablet-and-mobile-fixed">
    <!--begin::Root-->
    <div class="d-flex flex-column flex-root">
      <!--begin::Page-->
      <div class="page d-flex flex-row flex-column-fluid">
        <!--begin::Wrapper-->
        <div class="d-flex flex-column flex-row-fluid">
          <layout-main-layout-header />
          <!--begin::Content wrapper-->
          <div class="d-flex flex-column-fluid">
            <!-- <layout-main-layout-aside /> -->
            <!--begin::Container-->

            <div class="d-flex flex-column flex-column-fluid container-fluid">
              <div class="content flex-column-fluid" id="kt_content">
                <Nuxt />
              </div>
              <layout-main-layout-footer />
            </div>
            <!--end::Container-->
          </div>
          <!--end::Content wrapper-->
        </div>
        <!--end::Wrapper-->
      </div>
      <!--end::Page-->
    </div>
    <!--end::Root-->
  </div>
</template>

<script>
export default {
  name: "main-layout",
  meta: {
    requireAuth: true,
  },
  data() {
    return {
      isShowMenu: true,
      user: null,
    };
  },
  mounted() {
    if (this.$store.state.auth.user) {
      const user = this.$store.state.auth.user;
      if (user === "undefined") {
        this.$router.push("/login");
      } else {
        this.user = user;
      }
    }
    if (!this.user) {
      this.$router.push("/login");
    }
    this.$initTheme();
  },
  methods: {},
};
</script>
