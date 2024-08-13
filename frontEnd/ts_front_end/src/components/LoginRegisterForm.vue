<script setup lang="ts">
import { ref } from 'vue';
import InputField from "@/components/InputField.vue";
import SubmitButton from "@/components/SubmitButton.vue";

const props = defineProps({
  isLogin: Boolean
});

const isLoading = ref(false);

const formData = ref({
  email: '',
  name: '',
  password: '',
  confirmPassword: '',
  identity: ''
});

const emit = defineEmits(['submit']);

// const handleSubmit = async () => {
//   emit('submit', { ...formData.value, isLoading });
// };
const handleSubmit = () => {
  emit('submit');
  if (props.isLogin) {
    // 登录逻辑
    console.log('Logging in with', formData.value);
  } else {
    // 注册逻辑
    console.log('Registering with', formData.value);
  }
};
</script>

<template>
  <div>
    <h1 v-if="isLogin">登录</h1>
    <h1 v-else>注册</h1>
    <form @submit.prevent="handleSubmit">
      <input-field label="邮箱" v-model="formData.email" />
      <input-field v-if="!isLogin" type="name" label="名称" v-model="formData.name" />
      <input-field type="password" label="密码" v-model="formData.password" />
      <input-field v-if="!isLogin" type="password" label="确认密码" v-model="formData.confirmPassword" />
      <input-field v-if="!isLogin" type="identity" label="身份" v-model="formData.identity" />

      <submit-button :loading="isLoading" :label="isLogin ? '登录' : '注册'" />
    </form>
  </div>
</template>

<style scoped>

</style>