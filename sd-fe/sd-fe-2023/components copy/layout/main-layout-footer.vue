<template>
  <!--begin::Footer-->
  <div
    class="footer py-4 d-flex flex-column flex-md-row flex-stack"
    id="kt_footer"
  >
    <!--begin::Copyright-->
    <div class="text-dark order-2 order-md-1">
      <span class="text-muted fw-bold me-1">© {{ new Date().getFullYear() }}</span>
      <a
        href="javascript:;"
        target="_blank"
        class="text-gray-800 text-hover-primary"
        >Không Lom Dom</a
      >
    </div>
    <!--end::Copyright-->
  </div>
  <!--end::Footer-->
</template>

<script>
export default {
  name: "main-layout-footer",
  data() {
    return {};
  },
  methods: {},
};
</script>