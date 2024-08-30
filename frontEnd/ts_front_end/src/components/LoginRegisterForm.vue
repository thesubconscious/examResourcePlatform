<script setup lang="ts">
import { ref } from 'vue';
import InputField from "@/components/InputField.vue";
import SubmitButton from "@/components/SubmitButton.vue";
import { UserService } from '@/services/userService';

const userService = new UserService();

const props = defineProps({
  isLogin: Boolean,
});

const isLoading = ref(false);

const formData = ref({
  email: '',
  name: '',
  password: '',
  confirmPassword: '',
  identity: ''
});

const emit = defineEmits<{
  (e: 'LRFail', errorMessage: string): void;
}>();

// const handleSubmit = async () => {
//   emit('submit', { ...formData.value, isLoading });
// };
const handleSubmit = async () => {
  // emit('submit', formData.value);
  if (props.isLogin) {
    // 登录逻辑
    // console.log('Logging in with', formData.value);
    try {
      const response = await userService.login(formData.value.email, formData.value.password);
      console.log('Login successful:', response);
      window.location.replace('#/dashboard');
    } catch (err) {
      console.error('Login failed:', err);
      emit('LRFail', err.response.data);
    }
  } else {
    // 注册逻辑
    // console.log('Registering with', formData.value);
    try {
      const response = await userService.register(formData.value.email, formData.value.name, formData.value.password, formData.value.identity);
      console.log('Register successful:', response);
    } catch (err) {
      console.error('Register failed:', err);
    }
  }
};
</script>

<template>
  <div>
    <h1 v-if="isLogin">登录</h1>
    <h1 v-else>注册</h1>
    <form @submit.prevent="handleSubmit">
      <input-field label="邮箱：" v-model="formData.email" />
      <input-field v-if="!isLogin" type="name" label="名称：" v-model="formData.name" />
      <input-field type="password" label="密码：" v-model="formData.password" />
      <input-field v-if="!isLogin" type="password" label="确认密码：" v-model="formData.confirmPassword" />
      <input-field v-if="!isLogin" type="identity" label="身份：" v-model="formData.identity" />

      <submit-button :loading="isLoading" :label="isLogin ? '登录' : '注册'" />
    </form>
  </div>
</template>

<style scoped>
/* 根容器样式 */
div {
  max-width: 85%;
  min-width: 50%;
  margin: 0 auto;
  padding: 8px;
  border-radius: 10px;
  background-color: #f9f9f9;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
h1 {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

/* 表单样式 */
form {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
}


/* 响应式布局 */
@media (max-width: 500px) {
  div {
    padding: 15px;
  }

  h1 {
    font-size: 20px;
  }

}
</style>