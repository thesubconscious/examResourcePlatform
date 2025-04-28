<script setup lang="ts">
import {computed, ref} from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {faArrowLeft, faUserCircle} from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';
library.add(faUserCircle,faArrowLeft);
import {authManager} from "@/services/AuthManager";
import {useRoute, useRouter} from 'vue-router'
import Modal from "@/components/modal/Modal.vue";
const router = useRouter()
const route = useRoute()

const menuVisible = ref(false);
const showModal = ref(false);
const modalType = ref('');

const isCourseDetail = computed(() => route.path.startsWith('/courses/'))

const toggleMenu = () => {
  menuVisible.value = !menuVisible.value;
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

const jumpToDashboardPage = () => {
  // window.location.replace('#/Dashboard');
  router.push({ name: 'Dashboard' })
}

const closeModal = () => {
  showModal.value = false;
}
</script>

<template>
  <div class="header">
    <FontAwesomeIcon :icon="faArrowLeft" @click="$router.go(-1)" class="icon-left" v-if="isCourseDetail"/>
    <FontAwesomeIcon :icon="['fas', 'user-circle']" @click="toggleMenu" class="icon-right"/>
    <div v-if="menuVisible" class="menu">
      <p v-if="userExist()" @click="jumpToDashboardPage()">个人页</p>
      <div v-else class="auth-buttons">
        <p @click="login">登录</p>
        <p @click="register">注册</p>
      </div>
    </div>
  </div>

  <div v-if="showModal">
    <Modal :showModal="showModal" @closeModal="closeModal" :modalType="modalType" />
  </div>
</template>

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

/* icon */
.icon-left {
  height: 80%;
  position: absolute;
  left: 1%;
  top:10%;
  cursor: pointer;
}
.icon-right {
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
</style>