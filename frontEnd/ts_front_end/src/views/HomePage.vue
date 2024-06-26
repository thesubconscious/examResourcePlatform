<template>
  <div class="home">
    <div class="header">
      <FontAwesomeIcon :icon="['fas', 'user-circle']" @click="toggleMenu" class="icon"/>
      <div v-if="menuVisible" class="menu">
        <p @click="login()">登录</p>
        <p @click="register()">注册</p>
      </div>
    </div>
    <div class="image-container" @click="imageClicked">
      <img src="@/assets/HomeView.jpg" alt="Home View" class="main-image">
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

const menuVisible = ref(false);

const toggleMenu = () => {
  menuVisible.value = !menuVisible.value;
};

const login = () => {
  // 实现登录逻辑
  console.log("Login");
};

const register = () => {
  // 实现注册逻辑
  console.log("Register");
};

const imageClicked = () => {
  console.log('Image Clicked');
};
</script>

<style scoped>
.header, .footer {
  position: fixed;
  left: 0;
  min-height: 3vh;
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
.icon {
  position: absolute;
  right: 10px;
  top: 5px;
  cursor: pointer;
}
.menu {
  background-color: white;
  box-shadow: 0 8px 16px rgba(0,0,0,0.2);
  padding: 8px;
  position: absolute;
  right: 10px;
  top: 40px;
}
.image-container {
  padding-top: 40px;
  text-align: center;
}
.main-image {
  max-width: 100%;
  height: auto;
  display: inline-block; /* To center in the container */
}
</style>