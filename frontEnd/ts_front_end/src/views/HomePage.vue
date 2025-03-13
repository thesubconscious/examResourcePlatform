<template>
  <div class="home">
    <Header/>

    <h1>欢迎来到学习资源平台</h1>
<!--    <h2 @click="jumpToMainPage()">点此访问主页</h2>-->
<!--    <button @click="jumpToMainPage()" >访问主页</button>-->
    <router-link to="Main" class="router">访问主页</router-link>
    <div class="image-container" @click="imageClicked">
      <button @click="prevImage" class="arrow left-arrow">◀</button>
      <img :src="images[Object.keys(images)[curr_left]].default" alt="Shadowed Home View" class="side-image">
      <img :src="images[Object.keys(images)[curr_main]].default" alt="Home View" class="main-image">
      <img :src="images[Object.keys(images)[curr_right]].default" alt="Shadowed Home View" class="side-image">
      <button @click="nextImage" class="arrow right-arrow">▶</button>
    </div>

    <Footer/>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue';
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(faUserCircle);
import {useRouter} from "vue-router";
import Modal from "@/components/modal/Modal.vue";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

const router = useRouter()

const showModal = ref(false);
const modalType = ref('');

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

const handleModalOpen = (type: 'login' | 'register') => {
  modalType.value = type
  showModal.value = true
}

const imageClicked = () => {
  console.log('Image Clicked');
};
</script>

<style scoped>
h1 {
  text-align: center;
  margin-top: 5vh;
  font-size: 2em;
}

.router {
  display: inline-block;
  text-align: center;
  font-size: 1.1em;
  position: relative;
  left: 50%;
  transform: translateX(-50%);
}
.router:hover {
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
  max-height: 100vh;
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
  z-index: -1; /* 确保侧边图片在箭头下方 */
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
    z-index: -1; /* 确保侧边图片在箭头下方 */
    max-width: 10%;
    /*height: auto;*/
    min-height: 50vh;
    display: inline-block;
    opacity: 0.8;
    filter: blur(8px);
  }
}
</style>