<script setup lang="ts">
import {computed, ref} from "vue";

const props = defineProps({
  nodeId: String,
  fileUri: String,
  title: String
})
const fileType = computed(() => {
  if (!props.fileUri) return 'unknown'
  const ext = props.fileUri.split('.').pop()?.toLowerCase()
  return ext === 'pdf' ? 'pdf' : ['mp4','webm'].includes(ext!) ? 'video' : 'unknown'
})

const assetPath = computed(() => {
  if (!props.fileUri) return ''
  // 用vite的动态路径处理
  return new URL(`/src/assets/${props.fileUri}`, import.meta.url).href
})

</script>

<template>
  <div class="viewer-container">
    <h2>{{ title || '资源预览' }}</h2>

    <template v-if="fileUri">
      <video
          v-if="fileType === 'video'"
          controls
          :src="assetPath"
          class="media-player"
      ></video>

      <iframe
          v-else-if="fileType === 'pdf'"
          :src="assetPath + '#view=fitH'"
          class="pdf-viewer"
      ></iframe>

      <div v-else class="unsupported-type">
        🚫 不支持的文件类型：{{ fileType }}
      </div>
    </template>

    <div v-else class="no-file">
      ⚠️ 该资源没有关联文件
    </div>
  </div>
</template>

<style scoped>
.viewer-container {
  padding: 20px;
  height: 100%;
}

.media-player {
  width: 100%;
  max-height: 80vh;
}

.pdf-viewer {
  width: 100%;
  height: 80vh;
  border: none;
}

.unsupported-type, .no-file {
  padding: 2rem;
  text-align: center;
  color: #f56c6c;
}
</style>