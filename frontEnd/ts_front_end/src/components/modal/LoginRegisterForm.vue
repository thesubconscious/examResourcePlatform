<script setup lang="ts">
import {onMounted, ref} from 'vue';
import InputField from "@/components/basic/InputField.vue";
import SubmitButton from "@/components/basic/SubmitButton.vue";
import { UserService } from '@/services/userService';
import {useRouter} from "vue-router";

const router = useRouter()
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
const identityOptions = ref<Array<{ name: string }>>([])

onMounted( () =>{
  identityOptions.value = [
      {
        name:"teacher"
      },
    {
      name:"student"
    }
  ]
})
const emit = defineEmits<{
  (e: 'message', errorMessage: string): void;
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
      router.push({name:'Dashboard'})
      // window.location.replace('#/dashboard');
    } catch (err) {
      console.error('Login failed:', err);
      emit('message', err);
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
<!--      <input-field v-if="!isLogin" type="identity" label="身份：" v-model="formData.identity" />-->
      <div v-if="!isLogin" class="form-group">
      <label >身份：</label>
      <select
          v-model="formData.identity"
          class="form-select"
      >
        <option
            v-for="identity in identityOptions"
            :key="identity.name"
            :value="identity.name"
        >
          {{ identity.name }}
        </option>
      </select>
      </div>

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
  background-color: hsl(152.76deg 100% 94.65%);
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

.form-group {
  min-width: 52%;
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中对齐 */
}

.form-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background-color: white;
  transition: border-color 0.2s;

  &:focus {
    outline: none;
    border-color: #4fd1c5;
    box-shadow: 0 0 0 2px rgba(79, 209, 197, 0.2);
  }
}

label {
  display: inline-block;
  font-size: 1.0rem;
  margin-right: .5rem;
  white-space: nowrap; /*禁止换行*/
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