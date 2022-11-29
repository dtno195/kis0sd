<template>
  <div class="shop-order-detail-page">
    <app-tab-bar />
    <app-page-title title="Chi tiết đơn hàng nội bộ" />
    <shop-order-detail v-if="id" :id="id" />
  </div>
</template>

<script>
export default {
  // Define name as page cause infinity loading
  layout: "main-layout",
  data() {
    return {
      id: "",
    };
  },
  mounted() {
    if (this.$route.params.id) {
      this.id = this.$route.params.id;
    } else {
      this.$router.go(-1);
    }
  },
};
</script>
