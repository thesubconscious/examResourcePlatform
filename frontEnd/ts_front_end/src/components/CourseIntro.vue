<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {faPlus} from "@fortawesome/free-solid-svg-icons";
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(faPlus);
import Modal from "@/components/modal/Modal.vue";
import {ref} from "vue";
import {authManager} from "@/services/AuthManager";

const props = defineProps({
  name:String,
  introduction:String,
  path:String,
})
const showModal = ref(false);
const modalType = ref('');

const closeModal = () => {
  showModal.value = false;
}

const openModal = () => {
  showModal.value = true;
  modalType.value = 'uploadFileCourse';
}

const teacherExist = () => {
  return authManager.isLoggedIn && (localStorage.getItem("identity") === 'teacher'||'admin');
}
</script>

<template>
  <div class="course-intro">
    <div class="image-container">
    <img :src="path" alt="课程封面" />
      <font-awesome-icon
          v-if="teacherExist()"
          :icon="['fas', 'plus']"
          class="upload-trigger"
          @click="openModal"
      />
    </div>
    <h1>{{name}}</h1>
    <div class="introduction"><p>介绍：{{ introduction }}</p></div>

    <modal :showModal="showModal" @closeModal="closeModal" :modalType="modalType" />
  </div>
</template>

<style scoped>
.course-intro {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.course-intro img {
  width: 100%;
  height: auto; /* 保持图片比例 */
  max-height: 60vh;
  /*max-width: 50vw;*/
}

.image-container {
  position: relative;
  width: 75%;
}

.upload-trigger {
  position: absolute;
  right: 0px;
  bottom: 0px;
  background: #fff;
  padding: 0.4rem; /*这是大小*/
  border-radius: 50%;
  box-shadow: 0 2px 5px rgba(0,0,0,0.2);
  cursor: pointer;
  transition: transform 0.2s;

  &:hover {
    transform: scale(1.1);
    color: #07a817;
  }
}

.introduction {
  background-color: #f0f0f0; /* 浅灰色背景 */
  min-width: 70%;
  min-height: 10vh;
  position: relative; /* 为伪元素定位做准备 */
}

.introduction p {
  font-family: Arial, sans-serif;
  color: #333;
  /*line-height:1.2; *//* 行高 */
  padding:10px;
  text-align:left;
  font-size:14px;
  font-style: italic; /* 设置斜体 */
  font-weight: bold; /* 设置粗体 */

  /* 确保文本内容不被覆盖 */
  position: relative;
  z-index: 1;
}
.introduction::before,
.introduction::after {
  content:'“';
  font-size: 24px;
  color:#ae1515;
  position:absolute;
  top:-0.1rem; /* 引号高度 */

}
.introduction::before {
  /* 左侧引号 */
  left:-0.9rem;
}
.introduction::after {
  /*右侧引号*/
  right:0rem;
}
</style>