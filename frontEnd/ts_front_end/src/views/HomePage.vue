<template>
  <div class="home">
    <div class="header">
      <FontAwesomeIcon :icon="['fas', 'user-circle']" @click="toggleMenu" class="icon"/>
      <div v-if="menuVisible" class="menu">
        <p v-if="userExist()" @click="jumpToDashboardPage()">个人页</p>
        <div v-else class="auth-buttons">
          <p @click="login">登录</p>
          <p @click="register">注册</p>
        </div>
      </div>
    </div>
    <h1>欢迎来到考试学习资源平台</h1>
    <h2 @click="jumpToMainPage()">点此访问主页</h2>
<!--    <button @click="jumpToMainPage()" >访问主页</button>-->
    <div class="image-container" @click="imageClicked">
      <button @click="prevImage" class="arrow left-arrow">◀</button>
      <img :src="images[Object.keys(images)[curr_left]].default" alt="Shadowed Home View" class="side-image">
      <img :src="images[Object.keys(images)[curr_main]].default" alt="Home View" class="main-image">
      <img :src="images[Object.keys(images)[curr_right]].default" alt="Shadowed Home View" class="side-image">
      <button @click="nextImage" class="arrow right-arrow">▶</button>
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
import {computed, ref} from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(faUserCircle);
import {useRouter} from "vue-router";
import Modal from "@/components/Modal.vue";
import {authManager} from "@/services/AuthManager";

const router = useRouter()

const menuVisible = ref(false);
const showModal = ref(false);
const modalType = ref('');

const toggleMenu = () => {
  menuVisible.value = !menuVisible.value;
};

const images = import.meta.glob('@/assets/HomeView*.jpg', { eager: true }) as Record<string, { default: string }>;
const imageCount = Object.keys(images).length;
const currentImageIndex = ref(0);

// 计算当前的左侧和右侧图片索引
const curr_main = ref(currentImageIndex.value);
const curr_left = ref((currentImageIndex.value - 1 + imageCount) % imageCount);
const curr_right = ref((currentImageIndex.value + 1) % imageCount);
// console.log(images[Object.keys(images)[curr_main.value]].default)
// 更新图片索引和图片显示
const updateImages = () => {
  curr_main.value = currentImageIndex.value;
  curr_left.value = (currentImageIndex.value - 1 + imageCount) % imageCount;
  curr_right.value = (currentImageIndex.value + 1) % imageCount;
};

const nextImage = () => {
  currentImageIndex.value = (currentImageIndex.value + 1) % imageCount;
  updateImages();
};

const prevImage = () => {
  currentImageIndex.value = (currentImageIndex.value - 1 + imageCount) % imageCount;
  updateImages();
};

const userExist = () => {
  return authManager.isLoggedIn;
}

const login = () => {
  modalType.value = 'login';
  showModal.value = true;
};

const register = () => {
  modalType.value = 'register';
  showModal.value = true;
};

const jumpToMainPage = () => {
  window.location.replace('#/Main');
}

const jumpToDashboardPage = () => {
  window.location.replace('#/Dashboard');
}

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
  margin-top: 5vh;
  font-size: 2em;
}

h2 {
  text-align: center;
  margin-top: 1vh;
  font-size: 1.3em;
}
h2:hover {
  /*background-color: lightgray;*/
  cursor: pointer;
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

/* 箭头 */
.arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  z-index: 10; /* 确保箭头位于图片之上 */
  pointer-events: auto; /* 确保箭头可以点击 */
}

.left-arrow {
  left: 3%;
}

.right-arrow {
  right: 3%;
}

/* 图片 */
.image-container {
  min-height: 60vh;
  text-align: center;
  margin: 3vh 0 3vh 0;
}
.main-image {
  max-width: 40%;
  /*min-height: 60%;*/
  height: auto;
  display: inline-block;
}
.side-image {
  z-index: 1; /* 确保侧边图片在箭头下方 */
  max-width: 30%;
  height: auto;
  display: inline-block;
  opacity: 0.8;
  filter: blur(8px);
}

@media (max-width: 500px){
  .main-image {
    max-width: 80%;
    min-height: 60vh;
    height: auto;
    display: inline-block;
    /*opacity: 0.8;
    filter: blur(2px);*/
  }
  .side-image {
    z-index: 1; /* 确保侧边图片在箭头下方 */
    max-width: 10%;
    /*height: auto;*/
    min-height: 50vh;
    display: inline-block;
    opacity: 0.8;
    filter: blur(8px);
  }
}
</style>