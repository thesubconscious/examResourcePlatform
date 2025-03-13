<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {courseService} from "@/services/courseService";
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
import CourseIntro from "@/components/CourseIntro.vue";
import type ResourceViewer from "@/components/ResourceViewer.vue";
import {resourceService} from "@/services/resourceService";
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
// import {faPlus} from "@fortawesome/free-solid-svg-icons";
// import { library } from '@fortawesome/fontawesome-svg-core';
// library.add(faPlus);


const route = useRoute()

const currentComponent = ref<typeof CourseIntro | typeof ResourceViewer>(CourseIntro); // 初始为课程介绍页面
const currentProps = ref({});

const currentCourse = ref<Course>();
const resources = ref([]);

onMounted(async () => {
  try {
    const res = await courseService.getCoursesDetails(route.params.id);
    currentCourse.value = res;
    console.log(currentCourse.value);
    currentProps.value = {
      name: currentCourse.value.name,
      path: currentCourse.value.img_path,
      introduction: currentCourse.value.introduction,
    }
  } catch (err) {
    console.error(err)
  }

  try {
    const res = await resourceService.getResources(route.params.id);
    resources.value = res;
    console.log(resources.value);
  } catch (err) {
    console.error(err)
  }
})

</script>

<template>
  <Header/>

  <div class="container">
    <div class="content">
      <component :is="currentComponent" v-bind="{ ...currentProps }"></component>
    </div>

    <div class="menu">
      <font-awesome-icon
          :icon="['fas', 'plus']"
          class="upload-trigger"
          @click="createResource"
      />
    </div>
  </div>

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
</style>