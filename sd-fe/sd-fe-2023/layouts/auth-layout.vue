<template>
  <div id="kt_body" class="bg-body">
    <!--begin::Authentication -->
    <div class="vh-100 d-flex flex-column flex-lg-row flex-column-fluid">
      <!--begin::Aside-->
      <div
        class="
          d-flex
          flex-column flex-lg-row-auto
          w-xl-600px
          positon-xl-relative
        "
        style="background-color: #f2c98a"
      >
        <!--begin::Wrapper-->
        <div
          class="
            d-flex
            flex-column
            position-xl-fixed
            top-0
            bottom-0
            w-xl-600px
            scroll-y
          "
        >
          <!--begin::Content-->
          <div
            class="d-flex flex-row-fluid flex-column text-center p-10 pt-lg-20"
          >
            <!--begin::Logo-->
            <a href="../../demo14/dist/index.html" class="py-9 mb-5">
              <img
                alt="Logo"
                src="/assets/media/logos/logo-2.svg"
                class="h-60px"
              />
            </a>
            <!--end::Logo-->
            <!--begin::Title-->
            <h1 class="fw-bolder fs-2qx pb-5 pb-md-10" style="color: #986923">
              Keep it simple 2023
            </h1>
            <!--end::Title-->
            <!--begin::Description-->
            <p class="fw-bold fs-2" style="color: #986923">Made by Sd</p>
            <!--end::Description-->
          </div>
          <!--end::Content-->
          <!--begin::Illustration-->
          <div
            class="
              d-flex
              flex-row-auto
              bgi-no-repeat
              bgi-position-x-center
              bgi-size-contain
              bgi-position-y-bottom
              min-h-100px min-h-lg-350px
            "
            style="
              background-image: url('assets/media/illustrations/sketchy-1/13.png');
            "
          ></div>
          <!--end::Illustration-->
        </div>
        <!--end::Wrapper-->
      </div>
      <!--end::Aside-->
      <!--begin::Body-->
      <div class="d-flex flex-column flex-lg-row-fluid py-10">
        <!--begin::Content-->
        <Nuxt />
        <!--end::Content-->
      </div>
      <!--end::Body-->
    </div>
    <!--end::Authentication -->
  </div>
</template>

<script>
export default {
  name: "auth-layout",
  mounted() {
    this.$initTheme();
  },
};
</script>