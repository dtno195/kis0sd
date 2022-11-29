export default ({ app }, inject) => {
  inject('initTheme', (params) => {
    // https://preview.keenthemes.com/metronic8/demo1/documentation/getting-started/customization/javascript.html#dynamic-initialization
    // Boostrap & 3rd-party components initialization
    if (window.KTApp) {
      window.KTApp.init();
    }
    // In-house components initialization
    if (window.KTMenu) {
      window.KTMenu.createInstances();
    }
    if (window.KTDrawer) {
      window.KTDrawer.createInstances();
    }
    if (window.KTScroll) {
      window.KTScroll.createInstances();
    }
    if (window.KTScrolltop) {
      window.KTScrolltop.createInstances();
    }
    if (window.KTSticky) {
      window.KTSticky.createInstances();
    }
    if (window.KTDialer) {
      window.KTDialer.createInstances();
    }
    if (window.KTImageInput) {
      window.KTImageInput.createInstances();
    }
    if (window.KTPasswordMeter) {
      window.KTPasswordMeter.createInstances();
    }
    if (window.KTSwapper) {
      window.KTSwapper.createInstances();
    }
    if (window.KTToggle) {
      window.KTToggle.createInstances();
    }
  })
}