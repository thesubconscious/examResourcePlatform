<script setup lang="ts">
import { UserService } from '@/services/userService';
import Modal from '@/components/Modal.vue';
import {onMounted, ref} from "vue";

const userService = new UserService();

const userDetails = ref({
  userId: '',
  email: '',
  name: '',
  password: '',
  identity: ''
});
const modalType = ref('')
const showModal = ref(false)

onMounted(async () => {
  const userId = userService.getCookie('USER_ID');
  if (userId) {
    userDetails.value = await userService.getUserDetails(userId);
  } else {
    console.error('用户未登录或缺少 userId');
  }
});

const is_teacher = () => {
  if(userDetails.value.identity == 'teacher'){
    return true;
  }
  return false;
}

const openModal = (type: string) => {
  modalType.value = type;
  showModal.value = true;
}

const closeModal = (type: string) => {
  showModal.value = false;
}
</script>

<template>
  <div class="dashboard-container">
    <h2>Welcome to Your Dashboard!</h2>

    <div v-if="userDetails" class="user-details">
      <p>User ID: {{ userDetails.userId }}</p>
      <p>Name: {{ userDetails.name }} </p>
      <p>Email: {{ userDetails.email }}</p>
      <p>Identity: {{ userDetails.identity }}</p>
    </div>

    <div v-if="is_teacher" class="admin-actions">
      <button @click="openModal('createArticle')" class="action-button">Create Article</button>
      <button @click="openModal('register')" class="action-button">Register New User</button>
    </div>

    <!--    <div v-else class="admin-actions">-->
    <!--      <button @click="openModal('createArticle')" class="action-button">Create Article</button>-->
    <!--    </div>-->

    <button @click="logout" class="logout-button">Logout</button>

    <div v-if="showModal">
      <Modal :showModal="showModal" @closeModal="closeModal" :modalType="modalType" />
    </div>
  </div>
</template>

<style scoped>

</style>