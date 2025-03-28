<template>
  <div class="container">
    <Header/>

    <div class="main">
      <div class="navbar">
        <h2>{{ $t('main.nav.title') }}</h2>
      </div>

      <div class="right-area">
        <div class="header">
          <div class="title">
            <h1>{{ $t('main.title') }}</h1>

            <p @click="scrollToContent">{{ $t('main.more') }}</p>
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

        <div class="content" id="contentTarget">
          <div class="coursesList">
            <div
                v-for="course in courseList"
                :key="course.course_id"
                class="course-item"
                @click="handleCourseClick(course.course_id)"
            >
              <img :src="'/src/assets'+course.img_path" alt="">
              {{ course.name }} - {{ course.teacher.name }}
            </div>

          </div>

        </div>
      </div>
    </div>
    <Footer/>
  </div>
</template>

<script setup lang="ts">
import Header from "@/components/basic/Header.vue";
import Footer from "@/components/basic/Footer.vue";
import {courseService} from "@/services/courseService";
import {useI18n} from "vue-i18n";
import {onMounted, ref} from "vue";
import {useRouter} from "vue-router";

const {t} = useI18n();
const router = useRouter();

const courseList = ref<Course[]>([])
const hotCourseList = ref<Course[]>([
  {
    course_id: "101",
    name: "Introduction to TypeScript",
    introduction: "Learn the basics of TypeScript, a typed superset of JavaScript.",
    img_path: "/course/101.jpg",
    teacher: {
      name: 'T800',
      teacher_id: '800'
    }
  },
  {
    course_id: "102",
    name: "Advanced Vue.js Techniques",
    introduction: "Deep dive into advanced features and patterns in Vue.js.",
    img_path: "/course/102.jpg",
    teacher: {
      name: 'T800',
      teacher_id: '800'
    }
  },
  {
    course_id: "103",
    name: "Fullstack Development with Node.js",
    introduction: "Build scalable applications using Node.js and Express.",
    img_path: "/course/103.jpg",
    teacher: {
      name: 'T800',
      teacher_id: '800'
    }
  }
]) // 暂时写死

onMounted(async () => {
  courseList.value = await courseService.getAllCourses();
  console.log(courseList.value)
});

const handleCourseClick = (courseId: string) => {
  router.push({name: 'CourseDetailPage', params: {id: courseId}});
}

const scrollToContent = () => {
  document.getElementById('contentTarget')?.scrollIntoView({behavior: 'smooth'})
}
</script>

<style scoped>
.container {
  min-height: 100vh;
  background: #f5f7fa;
}

.main {
  display: flex;
  gap: 24px;
  margin: 6vh 2% 2% 1%;
  max-width: 1280px;
  width: 95%;
}

.navbar {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  height: fit-content;
  padding: 24px;
  position: sticky;
  top: 6vh;
  width: 23%;
}

.right-area {
  display: flex;
  flex-direction: column; /* 关键改动 */
  flex: 1;
  gap: 24px;
}

.header {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title h1 {
  font-size: 24px;
  color: #1a1a1a;
}

.title p {
  color: #0066ff;
  cursor: pointer;
  font-weight: 500;
  transition: opacity 0.2s;
}

.title p:hover {
  opacity: 0.8;
}

.hot {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;
}

.content {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.course-item {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 12px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.course-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.course-item img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 12px;
}
</style>