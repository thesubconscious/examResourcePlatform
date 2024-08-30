<script setup lang="ts">
import { UserService } from '@/services/userService';
import Modal from '@/components/Modal.vue';
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const userService = new UserService();
const router = useRouter();

const userDetails = ref({
  userId: '',
  email: '',
  name: '',
  password: '',
  identity: ''
});
const modalType = ref('')
const message = ref()
const showModal = ref(false)

onMounted(async () => {
  const userId = localStorage.getItem("userId");
  if (userId) {
    userDetails.value = await userService.getUserDetails(userId);
  } else {
    console.error('用户未登录或缺少userId');
  }
});

const is_teacher = () => {
  return userDetails.value.identity == 'teacher';
}

const openModal = (type: string) => {
  modalType.value = type;
  showModal.value = true;
}

const closeModal = () => {
  showModal.value = false;

  if(modalType.value == 'displayMessage') {
    // router.push("/");
    window.location.replace('#/');
  }
}

const logout = () => {
  let result = userService.logout()
  modalType.value = 'displayMessage';
  message.value = result;
  //TODO： 用Modal做个消息框
}
</script>

<template>
  <div class="dashboard-container">
    //TODO: 头像
    <h2>Welcome to Your Dashboard!</h2>

    <div v-if="userDetails" class="user-details">
<!--      <p>User ID: {{ userDetails.userId }}</p>-->
      <p>Name: {{ userDetails.name }} </p>
      <p>Email: {{ userDetails.email }}</p>
      <p>Identity: {{ userDetails.identity }}</p>
    </div>

    <div v-if="is_teacher" class="admin-actions">
      <button @click="openModal('createCourse')" class="action-button">Create Course</button>
    </div>

    <!--    <div v-else class="admin-actions">-->
    <!--      <button @click="openModal('createArticle')" class="action-button">Create Article</button>-->
    <!--    </div>-->

    <button @click="logout" class="logout-button">Logout</button>

    <div v-if="showModal">
      <Modal
          :showModal="showModal"
          @closeModal="closeModal"
          :modalType="modalType"
          :message="message"
      />
    </div>
  </div>
</template>

<style scoped>

</style>