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
.submit-button {
  margin-top: 2vh;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button button {
  width: 100%;
  padding: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  background-color: #007BFF;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #8f8b8b;
}

/* 加载状态样式 */
.submit-button button[disabled] {
  background-color: #aaa;
  cursor: not-allowed;
}
</style>