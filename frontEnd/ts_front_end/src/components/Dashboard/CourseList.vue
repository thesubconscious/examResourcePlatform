<script setup lang="ts">
import {onMounted, ref} from "vue";
import {courseService} from "@/services/courseService";
import {useRouter} from "vue-router";

const router = useRouter();

const props = defineProps({
  showTitle: Boolean,
  showContent: Boolean,
})

// const showTitle = ref(false)
// const showContent = ref(false)
const courseList = ref<Course[]>([])

onMounted(async () => {
  // showTitle.value = true;
  //
  // setTimeout(() => {
  //   showContent.value = true
  // }, 500);

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
  <div class="container">
    <transition name="course-title" mode="out-in">
      <div
          v-if="showTitle"
      >
        {{ '学习' }}
        {{ '教学' }}
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

.course-title-enter-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.course-title-enter-from {
  opacity: 0;
  transform: translateY(-100%);
}

.course-title-leave-active {
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.course-title-leave-to {
  opacity: 0;
  transform: translateY(-100%);
}


.coursesList {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 16px;

  background: white;
  /*border-radius: 12px;*/
  padding: 0.5rem;
  /*box-shadow: 0 2px 8px rgba(0,0,0,0.1);*/
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.course-item img {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  margin-bottom: 12px;
}
</style>