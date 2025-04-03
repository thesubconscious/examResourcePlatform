<script setup lang="ts">
import {ref} from "vue";

const isExpanded = ref(false);
</script>

<template>
  <div
      class="moving-box"
      :class="{ 'expanded': isExpanded }"
      @click="isExpanded = !isExpanded"
  >
    {{ isExpanded ? '点击返回' : '点我出发' }}
  </div>
</template>

<style scoped>

.moving-box {
  /* 核心动力引擎 */
  transition: transform 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55), background 0.3s ease;
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

.expanded {
  /* 魔法变形指令 */
  transform: translate(30vw, 30vh);
  background: #4ecdc4;
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
}
</style>