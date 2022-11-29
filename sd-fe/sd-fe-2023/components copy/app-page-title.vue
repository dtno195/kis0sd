<template>
  <div class="toolbar mb-2">
    <div class="page-title d-flex flex-column me-3">
      <!--begin::Title-->
      <h1 class="d-flex text-dark fw-bolder my-1 fs-3">{{ title }}</h1>
      <!--end::Title-->
      <!--begin::Breadcrumb-->
      <ul class="breadcrumb breadcrumb-dot fw-bold text-gray-600 fs-7 my-1">
        <!--begin::Item-->
        <li
          class="breadcrumb-item text-gray-600"
          v-for="(item, i) in crumbs"
          :key="i"
          :class="item.classes"
        >
          <nuxt-link :to="item.path" class="text-gray-600 text-hover-primary">
            {{ item.name }}
          </nuxt-link>
        </li>
        <!--end::Item-->
      </ul>
      <!--end::Breadcrumb-->
    </div>
  </div>
</template>

<script>
export default {
  name: "app-page-title",
  props: {
    title: { type: String },
  },
  data() {
    return {};
  },
  computed: {
    crumbs() {
      const crumbs = [];
      this.$route.matched.forEach((item, i, { length }) => {
        const crumb = {};
        crumb.path = item.path;
        crumb.name = item.meta && item.meta.name ? item.meta.name : item.name;
        // is last item?
        if (i === length - 1) {
          // is param route? .../.../:id
          if (item.regex.keys.length > 0) {
            crumbs.push({
              path: item.path.replace(/\/:[^/:]*$/, ""),
              name:
                item.meta && item.meta.name
                  ? item.meta.name.replace(/-[^-]*$/, "")
                  : item.name.replace(/-[^-]*$/, ""),
            });
            crumb.path = this.$route.path;
            crumb.name =
              this.$route.meta && this.$route.meta.name
                ? this.$route.meta.name
                : this.$route.name;
          }
          crumb.classes = "is-active";
        }
        crumbs.push(crumb);
      });
      return crumbs;
    },
  },
  methods: {},
};
</script>