<script setup lang="ts">
const props = defineProps({
  loading: Boolean,
  label: String
});
</script>

<template>
  <button :disabled="loading" class="submit-button">
    <span v-if="!loading">{{ label }}</span>
    <span v-else>加载中...</span>
  </button>
</template>

<style scoped>

</style>