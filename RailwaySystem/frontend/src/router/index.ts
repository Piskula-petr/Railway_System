import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import SearchForm from "@/components/search-form/SearchForm.vue";
import LoginForm from "@/components/login-form/LoginForm.vue";
import RoutesInfo from "@/components/route-info/RoutesInfo.vue";
import TrainMap from "@/components/train-map/TrainMap.vue";

const routes: Array<RouteRecordRaw> = [

    {
        path: '/',
        name: 'SearchForm',
        component: SearchForm,
    }, 

    {
        path: "/prihlaseni",
        name: "LoginForm",
        component: LoginForm,
    },

    {
        path: '/vysledky',
        name: 'RoutesInfo',
        component: RoutesInfo,
        props: true,
    },

    {
        path: "/mapa",
        name: "TrainMap",
        component: TrainMap,
    },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router