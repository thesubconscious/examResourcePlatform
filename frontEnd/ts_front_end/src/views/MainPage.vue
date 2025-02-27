<template>
  <div class="container">
  <Header/>

    <div class="main">
      <div class="navbar">
        <h2>{{$t('main.nav.title')}}</h2>
      </div>

      <div class="content">
        <div class="title">
        <h1>{{$t('main.content.title')}}</h1>

        <p>{{$t('main.content.more')}}</p>
        </div>

        <div class="coursesList">
          <div
              v-for="course in courseList"
              :key="course.course_id"
              class="course-item"
          >
            {{ course.name }} - 教师ID: {{ course.teacher_id }}
          </div>

        </div>
      </div>

    </div>
  <Footer/>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import {courseService} from "@/services/courseService";
import {useI18n} from "vue-i18n";
import {onMounted, ref} from "vue";

const { t } = useI18n();

const courseList = ref<Course[]>([]) // 初始化为空数组更合理

onMounted(async () => {
  courseList.value = await courseService.getAllCourses();
  console.log(courseList.value)
});
</script>

<style scoped>
.container{
  display: flex;
  flex-direction: column;
}

.main {
  display: flex;
  margin-top: 2%;
}

.navbar{
  position:sticky;
  height: 100%;
  width: 23%;
  padding: 2vh;
}

.content{
  flex:1;
}
</style>