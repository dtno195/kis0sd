<template >
  <input
    class="width-10rem el-input__inner"
    type="text"
    :value="modelValue"
    @input="setFormat"
  />
</template>
<script>
export default {
  prop: [
    {
      value: {
        require: true,
        default: 0,
      },
    },
  ],
  data() {
    return {
      modelValue: this.value,
    };
  },
  methods: {
    setFormat(e) {
      if (this.modelValue !== "") {
        this.modelValue = Intl.NumberFormat("en-US").format(
          e.target.value.replaceAll(/[\.,]/g, "")
        );
      }
      this.$emit("input", this.modelValue.replaceAll(/[\.,]/g, ""));
      this.$emit("change", this.modelValue.replaceAll(/[\.,]/g, ""));
    },
  },
};
</script>