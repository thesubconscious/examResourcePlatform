<template>
  <div class="home">
    <div class="header">
      <FontAwesomeIcon :icon="['fas', 'user-circle']" @click="toggleMenu" class="icon"/>
      <div v-if="menuVisible" class="menu">
        <p @click="login()">登录</p>
        <p @click="register()">注册</p>
      </div>
    </div>
    <h1>欢迎来到考试学习资源平台</h1>
    <div class="image-container" @click="imageClicked">
      <img src="@/assets/HomeView.jpg" alt="Shadowed Home View" class="side-image">
      <img src="@/assets/HomeView.jpg" alt="Home View" class="main-image">
      <img src="@/assets/HomeView.jpg" alt="Shadowed Home View" class="side-image">
    </div>

    <div v-if="showModal">
      <Modal :showModal="showModal" @closeModal="closeModal" :modalType="modalType" />
    </div>

    <div class="footer">
      github@thesubconscious
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(faUserCircle);
import {useRouter} from "vue-router";
import Modal from "@/components/Modal.vue";

const router = useRouter()

const menuVisible = ref(false);
const showModal = ref(false);
const modalType = ref('');

const toggleMenu = () => {
  menuVisible.value = !menuVisible.value;
};

const login = () => {
  modalType.value = 'login';
  showModal.value = true;
};

const register = () => {
  modalType.value = 'register';
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
}

const imageClicked = () => {
  console.log('Image Clicked');
};
</script>

<style scoped>
.header, .footer {
  position: fixed;
  left: 0;
  height: 3%;
  width: 100%;
  background-color: grey;
  color: white;
  text-align: center;
  padding: 5px;
  font-size: 14px;
}
.header {
  top: 0;
}
.footer {
  bottom: 0;
}

h1 {
  text-align: center;
  margin-top: 5%;
  font-size: 2em;
}

/* icon */
.icon {
  height: 80%;
  position: absolute;
  right: 1%;
  top:10%;
  cursor: pointer;
}
.menu {
  background-color: white;
  box-shadow: 0 8px 16px rgba(0,0,0,0.2);
  padding: 8px;
  position: absolute;
  right: 10px;
  top: 40px;
  color: black;
}
.menu p:hover {
  background-color: lightgray;
  cursor: pointer;
}

/* 图片 */
.image-container {
  text-align: center;
  margin: 3% 0 3% 0;
}
.main-image {
  max-width: 40%;
  min-height: 60%;
  height: auto;
  display: inline-block;
}
.side-image {
  max-width: 30%;
  height: auto;
  display: inline-block;
  opacity: 0.8;
  filter: blur(8px);
}
</style>