<script setup lang="ts">
import {UserService} from '@/services/userService';
import Modal from '@/components/modal/Modal.vue';
import CourseList from '@/components/Dashboard/CourseList.vue';
import {computed, nextTick, onMounted, reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {
  faUserCircle,
  faArrowLeft,
  faLanguage,
  faRightFromBracket,
  faFolderPlus,
  faBook
} from '@fortawesome/free-solid-svg-icons';
import {library} from '@fortawesome/fontawesome-svg-core';

library.add(faUserCircle, faArrowLeft, faLanguage, faRightFromBracket, faFolderPlus, faBook);
import {useI18n} from "vue-i18n";
import i18n from "@/services/i18n";


const router = useRouter()
const userService = new UserService();
const {t} = useI18n()

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
const showTitle = ref(false)
const showContent = ref(false)
const activeTab = ref('profile')
const baseTabs = computed(() => [
  {id: 'profile', label: t('dashboard.tabs.profile')},
  {id: 'actions', label: t('dashboard.tabs.actions')},
  {id: 'settings', label: t('dashboard.tabs.settings')}
])

onMounted(async () => {
  const id = localStorage.getItem("userId");
  if (id) {
    userDetails.value = await userService.getUserDetails(id);
  } else {
    console.error('用户未登录或缺少userId');
  }

  window.addEventListener('resize', capturePosition);
});

const isTeacher = computed(() => ['teacher', 'admin'].includes(userDetails.value.identity))

const openModal = (type: string) => {
  modalType.value = type;
  showModal.value = true;
}

const closeModal = () => {
  showModal.value = false;

  if (modalType.value == 'displayMessage') {
    router.push({name: 'Home'});
  }
}

const logout = async () => {
  try {
    let result = await userService.logout()
    message.value = result;
    openModal('displayMessage');
  } catch (err) {
    message.value = '登出失败：网络异常'
  }
}

const changeLanguage = () => {
  const newLang = i18n.global.locale.value === 'zh-CN' ? 'en' : 'zh-CN' //TODO: 之后用Modal选择语言
  localStorage.setItem('userLang', newLang) // 存储
  i18n.global.locale.value = newLang
}

const jumpToPage = (event: MouseEvent, routeName: string) => {
  const path = router.resolve({name: routeName}).href;

  if (event.ctrlKey || event.metaKey || event.shiftKey) {
    window.open(path, '_blank')
  } else {
    router.push({name: routeName})
  }
}

// 新增返回状态控制
const interfaceState = reactive({
  isMasterViewInactive: false,
  listVisible: false,
  animationStart: false,
  originPos: {x: 0, y: 0},
  floatingPos: {x: 0, y: 0},
})
const originalPos = ref<HTMLElement>()
const floatingPos = ref<HTMLElement>()
const currentPos = ref({x: 0, y: 0})
const capturePosition = async () => {
  await nextTick()
  if (!floatingPos.value || !originalPos.value) return

  // 双位置同步捕获
  const originRect = originalPos.value.getBoundingClientRect()
  const floatRect = floatingPos.value.getBoundingClientRect()

  interfaceState.originPos = {
    x: originRect.left + window.scrollX,
    y: originRect.top + window.scrollY
  }

  interfaceState.floatingPos = {
    x: floatRect.left + window.scrollX,
    y: floatRect.top + window.scrollY
  }

  console.log(interfaceState)
}

const toggleInterface = () => {
  if (interfaceState.isMasterViewInactive) {
    showContent.value = false
    showTitle.value = false
    setTimeout(() => {
      interfaceState.listVisible = false
      interfaceState.animationStart = false
      setTimeout(() => {
        interfaceState.isMasterViewInactive = false
      }, 600)  // 等动画完成
    }, 500)

  } else {
    interfaceState.isMasterViewInactive = true
    interfaceState.animationStart = true
    setTimeout(() => {
      interfaceState.listVisible = true

      showTitle.value = true
      setTimeout(() => {
        showContent.value = true
      }, 500);
    }, 600) // 等动画完成
  }
}

const clickActiveTab = async (name: string) => {
  activeTab.value = name;

  if (name == "actions") {
    await capturePosition();
    currentPos.value = interfaceState.originPos;
  }
}
</script>

<template>
  <div class="dashboard-background">
    <div class="dashboard-container">
      <div class="left-arrow" @click="jumpToPage($event, 'Main')">
        <font-awesome-icon :icon="['fas', 'arrow-left']" class="icon-container-small"/>
        <span class="button-caption-large">{{ $t('dashboard.back') }}</span>
      </div>

      <!--      <div class="{ 'collapsed-mode': interfaceState.isCollapsed }">-->
      <!-- 独立出来的列表按钮，用绝对定位 -->
      <div
          ref="floatingPos"
          class="icon-wrapper non-display top-m1"
          @click="toggleInterface"
      >
        <font-awesome-icon class="icon-container" :icon="['fas', 'book']"/>
        <span class="button-caption">{{ $t('dashboard.courseAction.list') }}</span>
      </div>

      <div
          v-if="activeTab === 'actions'"
          class="icon-wrapper quantum-button"
          :style="{
              left: `${currentPos.x}px`,
              top: `${currentPos.y}px`,
              transform: `translate(
                ${interfaceState.animationStart
                  ? interfaceState.floatingPos.x - interfaceState.originPos.x
                  : 0}px,
                ${interfaceState.animationStart
                  ? interfaceState.floatingPos.y - interfaceState.originPos.y
                  : 0}px
              )`,
            }"
          @click="toggleInterface"
      >
        <font-awesome-icon :icon="['fas', 'book']" class="icon-container"/>
        <span class="button-caption">{{ $t('dashboard.courseAction.list') }}</span>
      </div>

      <transition-group name="master-view" tag="div">
        <div v-if="!interfaceState.isMasterViewInactive" key="original">
          <!--          <div key="original">-->

          <!-- 头部信息区 -->
          <div class="dashboard-header">
            <h2>{{ $t('dashboard.title') }}</h2>
            <FontAwesomeIcon :icon="['fas', 'user-circle']" class="user-avatar"/>
          </div>

          <!-- 导航选项卡 -->
          <nav class="tab-bar">
            <button
                v-for="tab in baseTabs"
                :key="tab.id"
                @click="clickActiveTab(tab.id)"
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
                  <h3>{{ $t('dashboard.infos.title') }}</h3>
                  <p><span class="detail-label">{{ $t('dashboard.infos.name') }}:</span> {{ userDetails.name }}</p>
                  <p><span class="detail-label">{{ $t('dashboard.infos.email') }}:</span> {{ userDetails.email }}
                  </p>
                  <button @click="openModal('editProfile')" class="edit-button">
                    {{ $t('dashboard.infos.edit') }}
                  </button>
                </div>
              </div>

              <!-- 标签栏2：用户功能区 -->
              <!-- 根据身份判断显示不同的专属功能+通用功能 -->
              <div v-show="activeTab === 'actions'" id="course-section" class="action">
                <div v-if="isTeacher" class="icon-wrapper " @click="openModal('createCourse')">
                  <font-awesome-icon :icon="['fas', 'folder-plus']" class="icon-container"/>
                  <span class="button-caption">{{ $t('dashboard.courseAction.create') }}</span>
                </div>
                <!--              <transition name="flying-button" mode="out-in">-->
                <div class="icon-wrapper non-display" id="list-button" ref="originalPos" @click="toggleInterface">
                  <font-awesome-icon :icon="['fas', 'book']" class="icon-container"/>
                  <span class="button-caption">{{ $t('dashboard.courseAction.list') }}</span>
                </div>
                <!--              </transition>-->
              </div>

              <!-- 标签栏3：系统功能区 -->
              <div v-show="activeTab === 'settings'" id="system-settings" class="action">
                <div class="icon-wrapper" @click="changeLanguage">
                  <font-awesome-icon :icon="['fas', 'language']" class="icon-container"/>
                  <span class="button-caption">{{ $t('dashboard.switchLanguageButton') }}</span>
                </div>
                <div class="icon-wrapper" @click="logout">
                  <font-awesome-icon :icon="['fas', 'right-from-bracket']" class="icon-container"/>
                  <span class="button-caption">{{ $t('dashboard.logout') }}</span>
                </div>
              </div>
            </div>
          </transition>
        </div>
      </transition-group>
      <!--      </div>-->

      <!-- 课程列表组件 -->
      <transition name="course-list">
        <CourseList v-if="interfaceState.listVisible" :show-title="showTitle" :show-content="showContent"/>
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

<style scoped>
/* 基础布局 */
html, body {
  height: 100%; /* 修复浏览器默认高度 */
  margin: 0; /* 清除默认边距 */
}

.dashboard-background {
  background: #97c3ed;
  min-height: 100vh; /* 关键属性 */
  display: flex;
}

.dashboard-container {
  margin: 0 auto;
  max-width: 45%;
  background: #fff;
  padding: 2rem;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  flex-grow: 1; /* 允许容器扩展 */
}

.dashboard-header {
  text-align: center;
  position: relative;
  padding: 2rem 0;
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

.left-arrow {
  /*max-width: 15%;*/
  display: flex;
  /*flex-direction: column;*/
  align-items: center;
  gap: 10px; /* 图标和文字间距 */

  color: #007bff;
}

.left-arrow:hover {
  cursor: pointer;
  color: #0056b3; /* 加深颜色 */
  text-decoration: none;
}

.quantum-button {
  position: absolute;
  z-index: 100; /* 确保能点到 */
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
  margin: 0 auto;
  margin-top: 5vh;
  min-height: 30vh;
  max-width: 70%;
  padding: 0.7rem 2rem;
}

.action {
  display: grid;
  gap: 1px; /* 隐形边框关键！ */
  grid-template-columns: repeat(3, 1fr); /* 强制三列 */
  background: transparent; /* 隐藏容器边框 */
  place-items: center;
}

.non-display {
  opacity: 0;
  visibility: hidden;
  position: absolute;
}

.top-m1 {
  margin-top: 1rem;
}


/*.collapse-mode #list-button {
  left: 20px !important;
  top: 20px !important;
  transform: translate(0, 0) !important;
}*/

/* 动画部分 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity .3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 主体内容动画 */
/*.master-view-leave-active {
  transition: all 0.6s cubic-bezier(0.68, -0.6, 0.32, 1.6);
  position: absolute;
}
.master-view-leave-to {
  opacity: 0.5;
  transform: translate(35vw,20vh);
}

.master-view-enter-active {
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  !*position: absolute;*!
  right:30vw;
  bottom:30vh;
}
.master-view-enter-from {
  opacity: 0.5;
  transform: translate(35vw,20vh);
}*/

</style>