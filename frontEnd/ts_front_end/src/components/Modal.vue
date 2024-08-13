<script setup lang="ts">
import {ref} from "vue";
import LoginRegisterForm from "@/components/LoginRegisterForm.vue";

import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
library.add(faTimes);


const props = defineProps({
  showModal:Boolean,
  modalType:String
})
const form_data = ref({
  name: '',
  email: '',
  password:'',

  title: '',
  author: '',
  article_text: '',
});
const showMessage = ref(false);
const messageText = ref('');

const emit = defineEmits<{
  (e: 'closeModal'): void;
}>();

const closeModal = () => {
  emit('closeModal'); //通知父组件modal被关闭

  //reset
  form_data.value = {
    name: '',
    email: '',
    password:'',

    title: '',
    author: '',
    article_text: '',
  };
  showMessage.value = false;
  messageText.value = '';
}
</script>

<template>
  <div v-if="showModal" class="modal-overlay">
    <div class="modal-container">
      <div class="modal-header">
<!--        <h2 v-if="modalType === 'createArticle' || modalType === 'register'">{{ modalType === 'createArticle' ? 'Create Article' : 'Register New User' }}</h2>-->
<!--        <span @click="closeModal" class="close-button">X</span>-->
        <font-awesome-icon @click="closeModal" :icon="['fas', 'times']" class="close-button" />
      </div>

      <div class="modal-body">
        <div v-if="modalType === 'login' || modalType === 'register'">
          <login-register-form
              :isLogin = "modalType === 'login'"
          />
        </div>
      </div>

      <div v-if="showMessage" class="message">
        {{ messageText }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(5px);
}

.modal-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  width: 40%;
  min-height: 30%;
}

.modal-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 1%;
}

.close-button {
  right: 5%;
  cursor: pointer;
  font-size: 18px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, textarea {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}

.submit-button {
  background-color: #4caf50;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.message {
  margin-top: 10px;
  color: #fff;
  background-color: #4caf50;
  padding: 10px;
  border-radius: 4px;
}
</style>