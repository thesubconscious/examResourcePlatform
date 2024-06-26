<script setup lang="ts">
import { useRouter } from 'vue-router';
import LoginRegisterForm from '@/components/LoginRegisterForm.vue';
import { UserService } from "@/services/userService";
import type {LoginForm} from "@/type/Form";
const userService = new UserService();
const router = useRouter();

const handleLogin = async ({ email, password, isLoading }:LoginForm) => {
  if (!email || !password) {
    alert('请填写所有必填项！');
    return;
  }

  isLoading.value = true;
  try {
    const userData = await userService.login(email, password);
    document.cookie = "userID="+userData.token+";";
    document.cookie = "path=/;HttpOnly;SameSite=Strict";
    alert("login successfully")
    // router.push({ name: 'dashboard' });
  } catch (error) {
    console.error('API请求失败', error);
    alert('出现错误，请稍后再试！');
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div class="login-page-container">
    <div class="login-form-wrapper">
        <login-register-form
          isLogin
          @submit="handleLogin"
      />
<!--      <div class="additional-links">-->
<!--        <router-link to="/forgot-password">忘记密码？</router-link>-->
<!--      </div>-->
    </div>
    <div class="register-redirect">
      <p>没有账户？<router-link to="/register">立即注册！</router-link></p>
    </div>
  </div>
</template>

<style scoped>

</style>