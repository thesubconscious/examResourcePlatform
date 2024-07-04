<script setup lang="ts">
import { useRouter } from 'vue-router';
import LoginRegisterForm from '@/components/LoginRegisterForm.vue';
import { UserService } from "@/services/userService";
import type {RegisterForm} from "@/type/Form";

const userService = new UserService();
const router = useRouter();

const handleRegister = async ({ email, name, password, confirmPassword, identity, isLoading }:RegisterForm) => {
  if (!email || !name || !password || !confirmPassword || !identity) {
    alert('请填写所有必填项！');
    return;
  }

  if (password !== confirmPassword) {
    alert('密码和确认密码不匹配！');
    return;
  }

  isLoading.value = true;
  try {
    const userData = await userService.register(email, name, password, identity);
    alert("register successfully")
    await router.push({name: 'login'});
  } catch (error) {
    console.error('API请求失败', error);
    alert('出现错误，请稍后再试！');
  } finally {
    isLoading.value = false;
  }
};
</script>

<template>
  <div class="register-page-container">
    <login-register-form
        :isLogin="false"
        @submit="handleRegister"
    />
  </div>
</template>

<style scoped>

</style>