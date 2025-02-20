<template>
  <div class="dashboard-background">
  <div class="dashboard-container">
    <div >
      <button><- 返回主页</button>
    </div>

    <!-- 头部信息区 -->
    <div class="dashboard-header">
        <h2>个人主页</h2>
        <FontAwesomeIcon :icon="['fas', 'user-circle']" class="user-avatar"/>
    </div>

    <!-- 导航选项卡 -->
    <nav class="tab-bar">
      <button
          v-for="tab in baseTabs"
          :key="tab.id"
          @click="activeTab = tab.id"
          :class="{ 'active-tab': activeTab === tab.id }"
          class="tab-button"
      >
        {{ tab.label }}
      </button>
    </nav>

    <!-- 下面设置标签栏 -->
    <transition name="fade" mode="out-in">
      <div class="tab-content">
    <!-- 标签栏1：个人信息 -->
        <div v-show="activeTab === 'profile'" class="profile-section">
          <div class="user-details">
            <h3>个人信息</h3>
            <p><span class="detail-label">姓名:</span> {{ userDetails.name }}</p>
            <p><span class="detail-label">邮箱:</span> {{ userDetails.email }}</p>
            <button @click="openModal('editProfile')" class="edit-button">
              修改资料
            </button>
          </div>
        </div>

    <!-- 标签栏2：用户功能区 -->
    <!-- 根据身份判断显示不同的专属功能+通用功能 -->
        <div v-show="activeTab === 'actions'" class="course-section">
          <div v-if="isTeacher" class="teacher-actions">
            <button class="create-course-btn" @click="openModal('createCourse')">
              + 新建课程
            </button>
          </div>
          <button class="create-course-btn" @click="openModal('createCourse')">
            课程列表
          </button>
        </div>

    <!-- 标签栏3：系统功能区 -->
        <div v-show="activeTab === 'settings'" class="system-settings">
          <!-- TODO：切换语言 -->
          <button class="logout-button">切换语言</button>
          <button @click="logout" class="logout-button">Logout</button>
        </div>
      </div>
    </transition>


    <div v-if="showModal">
      <Modal
          :showModal="showModal"
          @closeModal="closeModal"
          :modalType="modalType"
          :message="message"
      />
    </div>
  </div>
  </div>
</template>

<script setup lang="ts">
import { UserService } from '@/services/userService';
import Modal from '@/components/Modal.vue';
import {computed, onMounted, ref} from "vue";
import {useRouter} from "vue-router";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import { faUserCircle } from '@fortawesome/free-solid-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';
import {authManager} from "@/services/AuthManager";
library.add(faUserCircle);

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
const activeTab = ref('profile')
const baseTabs = ref([
  { id: 'profile', label: '个人中心', visible: true },
  { id: 'actions', label: '功能管理', visible: true },
  { id: 'settings', label: '系统设置', visible: true }
])

onMounted(async () => {
  const id = localStorage.getItem("userId");
  if (id) {
    userDetails.value = await userService.getUserDetails(id);
  } else {
    console.error('用户未登录或缺少userId');
  }
});

const isTeacher = computed(() => ['teacher', 'admin'].includes(userDetails.value.identity))

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

const logout = async() => {
  try {
    let result = await userService.logout()
    message.value = result;
    openModal('displayMessage');
  } catch (err) {
    message.value = '登出失败：网络异常'
  }
}

// 根据身份过滤选项卡
// const filteredTabs = computed(() => {
//   return baseTabs.value.filter(tab => {
//     if (tab.id === 'actions' && !userDetails.value.identity) return false
//     return tab.visible
//   })
// })
</script>

<style scoped>
/* 基础布局 */
html, body {
  height: 100%; /* 修复浏览器默认高度 */
  margin:0; /* 清除默认边距 */
}

.dashboard-background {
  background: #97c3ed;
  min-height: 100vh; /* 关键属性 */
  display: flex;
}

.dashboard-container {
  position: relative;
  left: 22.5%;
  max-width:50%;
  /*margin: 0 2rem 0 2rem;*/
  background:#fff;
  padding:2rem;
  box-shadow:0 0 15px rgba(0,0,0,0.1);
  flex-grow:1; /* 允许容器扩展 */
}

.dashboard-header {
  text-align: center;
  position: relative;
  padding-bottom: 1.5rem; /* 给分割线留空间 */
}

.dashboard-header::after {
  content: '';
  position: absolute;
  left: 50%;
  bottom: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg,
  transparent 0%,
  #ff006a 40%,
  #007bff 60%,
  transparent 100%
  ); /* 渐变式分割线 */
  transform: translateX(-50%);
}

.user-avatar {
  margin: 4% 0 3% 0;
  font-size: 4rem !important; /* 用字体大小控制SVG尺寸 */
}

.tab-bar {
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  margin: 2rem 0;
}

.tab-button {
  padding: 0.8rem 2rem;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.active-tab {
  background: #007bff;
  color: white;
}

.tab-content {
  min-height: 400px;
  padding: 2rem;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity .3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity:0;
}
</style>