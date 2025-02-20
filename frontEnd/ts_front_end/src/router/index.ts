import { createRouter, createWebHistory,createWebHashHistory } from 'vue-router'
import HomePage from "@/views/HomePage.vue";
import DashboardPage from "@/views/DashboardPage.vue";
import MainPage from "@/views/MainPage.vue";

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomePage
    },
    {
      path: '/Main',
      name: 'Main',
      component: MainPage
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardPage
    },
  ]
})

export default router
