<script setup lang="ts">
import {ref, onMounted, computed} from 'vue'
import { useRoute } from 'vue-router'
import {courseService} from "@/services/courseService";
import Footer from "@/components/basic/Footer.vue";
import Header from "@/components/basic/Header.vue";
import CourseIntro from "@/components/courseDetail/CourseIntro.vue";
import ResourceViewer from "@/components/courseDetail/ResourceViewer.vue";
import {resourceService} from "@/services/resourceService";
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import Modal from "@/components/modal/Modal.vue";
import {authManager} from "@/services/AuthManager";
// import {faPlus} from "@fortawesome/free-solid-svg-icons";
// import { library } from '@fortawesome/fontawesome-svg-core';
// library.add(faPlus);


const route = useRoute()

// 定义组件props联合类型
type ComponentProps =
    | { name: string; introduction: string; path: string } // CourseIntro
    | { nodeId: number; fileUri: string | null; title: string } // ResourceViewer
const currentComponent = ref<typeof CourseIntro | typeof ResourceViewer>(CourseIntro)
const currentProps = ref<ComponentProps>({} as ComponentProps)

const currentCourse = ref<Course>();
const resources = ref([]);
interface ResourceNode {
  node_id: number
  parent_node_id: number | null
  type: 'CHAPTER' | 'LEAF'
  title: string
  display_order: number
  folder: boolean
  file_uri: string | null
}

const showModal = ref(false);
const modalType = ref('');

onMounted(async () => {
  try {
    currentCourse.value = await courseService.getCoursesDetails(route.params.id);
    // console.log(currentCourse.value);
    currentProps.value = {
      name: currentCourse.value!.name,
      path: currentCourse.value!.img_path,
      introduction: currentCourse.value!.introduction,
    } as ComponentProps;
  } catch (err) {
    console.error(err)
  }

  try {
    resources.value = await resourceService.getResources(route.params.id);
    console.log(resources.value);
  } catch (err) {
    console.error(err)
  }

})

const closeModal = () => {
  showModal.value = false;
}

const createResource = () => {
  showModal.value = true;
  modalType.value = 'createResource';
}

const teacherExist = () => {
  return authManager.isLoggedIn && (localStorage.getItem("identity") === 'teacher'||'admin');
}

// 计算章节列表
const chapters = computed(() => {
  return resources.value
      .filter((n: ResourceNode) => n.parent_node_id === null)
      .sort((a, b) => a.display_order - b.display_order)
})

// 获取子节点
const getChildren = (chapterId: number) => {
  return resources.value
      .filter((n: ResourceNode) => n.parent_node_id === chapterId)
      .sort((a, b) => a.display_order - b.display_order)
}

const openResourceViewer = (node: ResourceNode) => {
  currentComponent.value = ResourceViewer;
  currentProps.value = {
    nodeId: node.node_id,
    fileUri: node.file_uri,
    title: node.title
  } as ComponentProps
}
</script>

<template>
  <Header/>

  <div class="container">
    <div class="content">
      <component :is="currentComponent" v-bind="{ ...currentProps }"></component>
    </div>

    <div class="menu">
<!--      <font-awesome-icon-->
<!--          :icon="['fas', 'plus']"-->
<!--          class="upload-trigger"-->
<!--          @click="createResource"-->
<!--      />-->
      <button v-if="teacherExist()" @click="createResource" >创建资源</button>
      <!-- 层级结构 -->
      <div class="tree-view">
        <!-- 修改章节循环 -->
        <div v-for="(chapter, chapterIndex) in chapters" :key="chapter.node_id">
          <div class="chapter-item">
            📁章节{{ chapterIndex + 1 }}.  {{ chapter.title }}
          </div>

          <!-- 修改子节点循环 -->
          <div
              v-for="(child, childIndex) in getChildren(chapter.node_id)"
              :key="child.node_id"
              class="child-item"
              @click="openResourceViewer(child)"
          >
            📄子节点{{ chapterIndex + 1 }}.{{ childIndex + 1 }}  {{ child.title }}
          </div>
        </div>
      </div>
    </div>
  </div>

  <modal :showModal="showModal" @closeModal="closeModal" :modalType="modalType" />
  <Footer/>
</template>

<style scoped>
.container {
  display: flex;
  margin-top: 2%;
  width: 100%;
  height: 95vh;
}

.content {
  flex-basis: 70%; /* 左侧内容区占70%宽度 */
  padding: 20px;
  box-sizing: border-box;
}

.menu {
  flex-basis: 30%; /* 右侧目录区占30%宽度 */
  background-color: #f4f4f4; /* 给目录区一个背景色以便区分 */
  padding: 20px;
}

ul {
  list-style-type:none;
  padding-left:0;
}
li{
  cursor:pointer;
  margin-bottom:.5em;
}

.tree-view {
  /*margin-top: 0.7rem;*/
}

.chapter-item {
  padding: 8px 12px;
  font-weight: 600;
  border-radius: 4px;
  margin: 0.5rem 0px;
  background: rgba(64,158,255,.1);
}

.child-item {
  padding: 6px 12px 6px 2rem; /* 最后一位调整缩进 */
  margin-left: 12px;
  border-left: 2px solid #e4e7ed;
  color: #606266;
  transition: all 0.3s;
}

.child-item:hover {
  background: rgba(64,158,255,.05);
  /*border-left-color: #409eff;*/
}

</style>