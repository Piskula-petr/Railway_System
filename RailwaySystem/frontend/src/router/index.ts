import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import SearchForm from "../components/search-form/SearchForm.vue";
import RoutesInfo from "../components/route-info/RoutesInfo.vue";

const routes: Array<RouteRecordRaw> = [

  {
    path: '/',
    name: 'SearchForm',
    component: SearchForm,
  }, 
  {
    path: '/vysledky',
    name: 'RoutesInfo',
    component: RoutesInfo,
    props: true,
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router