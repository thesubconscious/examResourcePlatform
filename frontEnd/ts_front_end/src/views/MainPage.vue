<template>
  <div class="container">
  <Header/>

    <div class="main">
      <div class="navbar">
        <h2>{{$t('main.nav.title')}}</h2>
      </div>

      <div class="header">
        <div class="title">
          <h1>{{$t('main.content.title')}}</h1>

          <p>{{$t('main.content.more')}}</p>
        </div>

        <div class="hot">
          <div
              v-for="course in hotCourseList"
              :key="course.course_id"
              class="course-item"
              @click="handleCourseClick(course.course_id)"
          >
            <img :src="course.img_path">
            {{ course.name }} - {{ course.teacher.name }}
          </div>
        </div>
      </div>

      <div class="content">
        <div class="coursesList">
          <div
              v-for="course in courseList"
              :key="course.course_id"
              class="course-item"
              @click="handleCourseClick(course.course_id)"
          >
            {{ course.name }} - {{ course.teacher.name }}
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
import {useRouter} from "vue-router";

const { t } = useI18n();
const router = useRouter();

const courseList = ref<Course[]>([])
const hotCourseList = ref<Course[]>([
  {
    course_id: "101",
    name: "Introduction to TypeScript",
    introduction: "Learn the basics of TypeScript, a typed superset of JavaScript.",
    img_path: "/assets/courseImages/typescript.jpg",
    teacher: {
      name:'T800',
      teacher_id:'800'
    }
  },
  {
    course_id: "102",
    name: "Advanced Vue.js Techniques",
    introduction: "Deep dive into advanced features and patterns in Vue.js.",
    img_path: "/assets/courseImages/vuejs.jpg",
    teacher: {
      name:'T800',
      teacher_id:'800'
    }
  },
  {
    course_id: "103",
    name: "Fullstack Development with Node.js",
    introduction: "Build scalable applications using Node.js and Express.",
    img_path: "/assets/courseImages/nodejs.jpg",
    teacher: {
      name:'T800',
      teacher_id:'800'
    }
  }
]) // 暂时写死

onMounted(async () => {
  courseList.value = await courseService.getAllCourses();
  console.log(courseList.value)
});

const handleCourseClick = (courseId:string) => {
  router.push({ name: 'CourseDetailPage', params: { id: courseId } });
}
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

.header{
  flex:1;
}

.content{
  flex: 1;
}
</style>