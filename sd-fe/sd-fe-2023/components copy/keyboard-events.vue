<template>
  <div>
    <slot default></slot>
  </div>
</template>

<script>
export default {
  name: "keyboard-events",
  props: {
    keyCode: { type: Number },
  },
  created() {
    const component = this;
    this.handler = function (e) {
      if (e.keyCode === component.keyCode) {
        component.$emit("keyup", e);
      }
    };
    window.addEventListener("keyup", this.handler);
  },
  beforeDestroy() {
    window.removeEventListener("keyup", this.handler);
  },
};
</script>
