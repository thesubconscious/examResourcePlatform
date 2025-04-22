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
          class="title-container"
      >
        <!-- 左侧装饰线 -->
        <div class="decor-line left-line"></div>

        <div class="title-content">
        <span
            class="title-item"
            @click="handleStudyClick"
        >学习</span>

          <!-- 中间装饰线 -->
          <div class="divider-vertical"></div>

          <span
              class="title-item"
              @click="handleTeachClick"
          >教学</span>
        </div>

        <!-- 右侧装饰线 -->
        <div class="decor-line right-line"></div>
      </div>
    </transition>

    <transition name="fade">
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
    </transition>

  </div>

</template>

<style scoped>
.container {
  margin-top: 5vh;
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

/* 进入/离开 过渡 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s ease;
}

/* 进入开始/离开结束 状态 */
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}


/* 标题 */
.title-container {
  padding: 1.5rem 0;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 2rem;
}

/*.title-wrapper {
  position: relative;
  display: flex;

}*/

.title-content {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.decor-line {
  width: 2px;
  height: 40px;
  background: linear-gradient(
      180deg,
      transparent 0%,
      #ff6b6b 30%,
      #4ecdc4 70%,
      transparent 100%
  );
}

.left-line {
  transform: rotate(180deg);
}

.right-line {
  transform: rotate(0deg);
}

.divider-vertical {
  width: 2px;
  height: 32px;
  background: linear-gradient(
      180deg,
      transparent 0%,
      #4ecdc4 50%,
      transparent 100%
  );
}

.title-item {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0 8px;
  position: relative;
}

.title-item:hover {
  /*color: #2c3e50;*/
  transform: translateY(-2px);
}

.title-item:hover::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 100%;
  height: 2px;
/*  background: #4ecdc4;*/
}


/* 课程内容 */
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