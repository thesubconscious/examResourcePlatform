<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {courseService} from "@/services/courseService";


const route = useRoute()
const currentCourse = ref<Course>()

onMounted(async () => {
  try {
    const res = await courseService.getCoursesDetails(route.params.id);
    currentCourse.value = res.data;
  } catch (err) {
    console.error('抓取课程详情失败:', err)
  }
})

</script>

<template>

</template>

<style scoped>

</style>