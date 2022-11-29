export default {
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  target: "static",
  ssr: false,

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: 'Kis SD',
    htmlAttrs: {
      lang: 'vi'
    },
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      { hid: "description", name: "description", content: "" },
      { name: "format-detection", content: "telephone=no" },
      {
        rel: "stylesheet",
        href: "https://fonts.googleapis.com/css?family=Open+Sans",
      },
    ],
    link: [{ rel: "icon", type: "image/x-icon", href: "/favicon.ico" }],
    script: [
      // begin::Global Javascript Bundle(used by all pages)
      { src: "/assets/js/scripts.bundle.js" },
      { src: "/assets/plugins/global/plugins.bundle.js" },
      // end::Global Javascript Bundle
      // // begin::Page Vendors Javascript(used by this page)
      // { src: '/assets/plugins/custom/fullcalendar/fullcalendar.bundle.js', body: true },
      // // end::Page Vendors Javascript
      // // begin::Page Custom Javascript(used by this page)
      // { src: '/assets/js/custom/widgets.js', body: true },
      // { src: '/assets/js/custom/apps/chat/chat.js', body: true },
      // { src: '/assets/js/custom/modals/create-app.js', body: true },
      // { src: '/assets/js/custom/modals/upgrade-plan.js', body: true },
      // // end::Page Custom Javascript
    ],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    // Theme: Metronic
    "static/assets/plugins/custom/fullcalendar/fullcalendar.bundle.css",
    "static/assets/plugins/global/plugins.bundle.css",
    "static/assets/css/style.bundle.css",
    // App
    "assets/scss/_element-ui.scss",
    "assets/scss/_font.scss",
    "assets/scss/index.scss",
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    "@/plugins/utils",
    "@/plugins/axios",
    "@/plugins/axios-port",
    "@/plugins/element-ui",
    "@/plugins/vee-validate",
    "@/plugins/v-mask",
    "@/plugins/vue-composition-api",
    { src: "@/plugins/persisted-state.js", ssr: false },
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/typescript
    '@nuxt/typescript-build',
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    "@nuxtjs/axios",
    // Simple usage
    ["cookie-universal-nuxt", { alias: "cookiz" }],
    // Or with plugin options:
    [
      "vue-currency-input/nuxt",
      {
        globalOptions: {
          locale: undefined,
          currency: "VND",
          valueRange: {
            min: 0,
            max: 999999999,
          },
          precision: undefined,
          hideCurrencySymbolOnFocus: true,
          hideGroupingSeparatorOnFocus: true,
          hideNegligibleDecimalDigitsOnFocus: true,
          autoDecimalDigits: false,
          valueScaling: undefined,
          autoSign: true,
          useGrouping: true,
        },
      },
    ],
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    baseURL: '/',
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    transpile: [/^element-ui/, "vee-validate/dist/rules"],
  },
  loading: {
    color: "#009ef7",
    continuous: true,
    height: "4px",
  }, // https://nuxtjs.org/docs/features/loading/
  env: {
    BASE_API: process.env.BASE_API,
    SOCKET_API: process.env.SOCKET_API,
  },
  publicRuntimeConfig: {
    baseURL: process.env.BASE_API,
    socketApi: process.env.SOCKET_API,
    axios: {
      baseURL: process.env.BASE_API,
    },
  },
  axios: {
    // baseURL: "http://128.199.110.147:8088/webapi/v1", // Used as fallback if no runtime config is provided
    baseURL: "http://localhost:16002/webapi/v1", // Used as fallback if no runtime config is provided
    timeout: 5000,
  },
}
