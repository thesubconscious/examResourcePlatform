import { createRouter, createWebHistory,createWebHashHistory } from 'vue-router'
import LoginPage from "@/views/LoginPage.vue";
import RegisterPage from "@/views/RegisterPage.vue";
import HomePage from "@/views/HomePage.vue";
import DashboardPage from "@/views/DashboardPage.vue";

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: HomePage
    },
    {
      path: '/login',
      name: 'login',
      component: LoginPage
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterPage
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardPage
    },
  ]
})

export default router
