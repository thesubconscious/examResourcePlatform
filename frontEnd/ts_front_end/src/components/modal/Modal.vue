<script setup lang="ts">
import {ref} from "vue";
import LoginRegisterForm from "@/components/modal/LoginRegisterForm.vue";
import CreateCourseForm from "@/components/modal/CreateCourseForm.vue";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { library } from '@fortawesome/fontawesome-svg-core';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import UploadResourceForm from "@/components/modal/uploadResourceForm.vue";

library.add(faTimes);


const props = defineProps({
  showModal:Boolean,
  modalType:String,
  message:String,
})

const showMessage = ref(false);
const messageText = ref('');

const emit = defineEmits<{
  (e: 'closeModal'): void;
}>();

const closeModal = () => {
  emit('closeModal'); //通知父组件modal被关闭

  showMessage.value = false;
  messageText.value = '';
}

const handleMessage = (message: string) => {
  messageText.value = '';

  messageText.value = message;
  showMessage.value = true;
}
</script>

<template>
  <div v-if="showModal" class="modal-overlay">
    <div class="modal-container">
      <div class="modal-header">
        <font-awesome-icon @click="closeModal" :icon="['fas', 'times']" class="close-button" />
      </div>

      <div class="modal-body">
        <div v-if="modalType === 'login' || modalType === 'register'">
          <login-register-form
              :isLogin = "modalType === 'login'"
              @message = "handleMessage"
          />
        </div>

        <div v-if="modalType === 'createCourse'">
          <create-course-form
            @message = "handleMessage"
          />
        </div>

        <div v-if="modalType === 'uploadFileCourse' ">
          <upload-resource-form
            :uploadType = "modalType"
            @message = "handleMessage"
          />
        </div>

        <div v-if="modalType === 'displayMessage'" class="displayMessage">
          {{ props.message }}
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
  z-index: 100;
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
  min-height: 10%;
}
@media (max-width: 500px){
  .modal-container {
    width: 80%;
  }
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

.message {
  margin-top: 10px;
  color: #fff;
  background-color: #4caf50;
  padding: 10px;
  border-radius: 4px;
}

.displayMessage {
  position: fixed;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
/*  background: rgba(255,255,255,0.9);*/
  padding: 1.5rem 3rem;
/*  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  border: 1px solid #eee;*/
/*  z-index: 9999;*/
  text-align: center;
  font-size: 1.1em;
/*  max-width: 80vw;
  min-width: 300px;*/
}
</style>