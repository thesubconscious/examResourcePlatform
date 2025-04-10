<script setup lang="ts">
import {onMounted, ref} from "vue";
import {courseService} from "@/services/courseService";
import {useRouter} from "vue-router";

const router = useRouter();

const showTitle = ref(false)
const showContent = ref(false)
const courseList = ref<Course[]>([])

onMounted(async () => {
  showTitle.value = true;

  setTimeout(() => {
    showContent.value = true
  }, 500);

  const student_id = localStorage.getItem('userId');
  if (student_id) {
    courseList.value = await courseService.getStudentCourses(student_id);
    console.log(courseList);
  }

})

const jumpToPage = (event: MouseEvent, routeName: string, courseId: number) => {
  const path = router.resolve({name: routeName, params: {id: courseId}}).href;

  if (event.ctrlKey || event.metaKey || event.shiftKey) {
    window.open(path, '_blank')
  } else {
    router.push({name: routeName, params: {id: courseId}});
  }
}

</script>

<template>
  <transition name="course-list" mode="out-in">
    <div
        v-if="showTitle"
        class="moving-box"
    >
      {{ '测试标题' }}
    </div>
  </transition>

  <div v-if="showContent" class="coursesList">
    <div
        v-for="course in courseList"
        :key="course.course_id"
        class="course-item"
        @click="jumpToPage($event, 'CourseDetailPage', course.course_id)"
    >
      <img :src="'/src/assets'+course.img_path" alt="">
      {{ course.name }} - {{ course.teacher.name }}
    </div>

  </div>

</template>

<style scoped>

.moving-box {
  /*transition: transform 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55), background 0.3s ease;*/
  margin: 1rem auto;
  width: 120px;
  height: 60px;
  background: #ff6b6b;
  border-radius: 8px;
  display: grid;
  place-items: center;
  color: white;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.course-list-enter-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.course-list-enter-from {
  opacity: 0;
  transform: translateY(-100%);
}

.course-list-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.course-list-leave-to {
  opacity: 0;
  transform: translateY(-100%);
}
</style>