<script setup lang="ts">

const props = defineProps({
  type:String,
  path:String,
})

</script>

<template>
  <div>
    <h2>资源查看器</h2>
    <!-- 假设这里会根据传入的类型展示不同内容 -->
    <video v-if="type === 'video'" controls :src="path"></video>
    <embed v-else-if="type === 'pdf'" type="application/pdf" :src="path" width="100%" height="600px" />
  </div>
</template>

<style scoped>

</style>