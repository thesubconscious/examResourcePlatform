import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from "@/views/page/LoginPage.vue";
import RegisterPage from "@/views/page/RegisterPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // {
    //   path: '/',
    //   name: 'login',
    //   component: LoginPage
    // },
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
  ]
})

export default router
