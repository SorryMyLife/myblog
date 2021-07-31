// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import MyblogHead from '@/myblog/blog_head.vue'
import axios from 'axios'

Vue.config.productionTip = false
Vue.prototype.$axios=axios

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
new Vue({
  el: '#bloghead',
  router,
  components: { MyblogHead },
  template: '<MyblogHead/>'
})
