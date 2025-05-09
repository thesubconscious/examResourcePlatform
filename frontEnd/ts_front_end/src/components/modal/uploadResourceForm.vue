<script setup lang="ts">
import {computed, onMounted, ref} from 'vue'
import {courseService} from "@/services/courseService";
import {useRoute} from "vue-router";

const router = useRoute()
const props = defineProps({
  uploadType: String,
  parentProgress: Number,
  fileReceived: Boolean
})
const emit = defineEmits(['message','file-uploaded'])
const fileInput = ref<HTMLInputElement>()
const isDragging = ref(false)
const progress = ref(0)
const courseId = ref(0)

onMounted( () => {
  courseId.value = Number(router.params.id) || 0
  // console.log(courseId.value)
  // console.log(router.params?.id)
})

const fileAccept = computed(() => {
  if (props.uploadType === 'uploadFileCourse') {
    return 'image/jpeg, image/png'
  }
  return 'video/*, application/pdf' // 视频和PDF类型
})

// 处理文件选择
const handleFilePreCheck = (file: File) => {
  if (props.uploadType === 'uploadFileCourse'){
    if (!file.type.startsWith('image/')) {
      emit('message','只能上传图片文件！')
      return
    }
  }else if(props.uploadType === 'uploadFileResource-post' || props.uploadType === 'uploadFileResource'){

  }

  handleFileUpload(file)
}

// 拖拽处理
const handleDrag = (e: DragEvent) => {
  e.preventDefault()
  console.log("drag")
  isDragging.value = e.type === 'dragover'
}
const handleDrop = (e: DragEvent) => {
  e.preventDefault()
  console.log("drop")
  isDragging.value = false
  const files = e.dataTransfer?.files
  if (files?.[0]) handleFilePreCheck(files[0])
}

// 触发系统选择框
const triggerInput = () => {
  fileInput.value?.click()
}

// 合并进度显示
const mergedProgress = computed(() => {
  return props.uploadType === 'uploadFileResource-post'
      ? (props.parentProgress || 0)
      : progress.value
})

const handleFileUpload = async (file: File) => {
  if(props.uploadType === 'uploadFileCourse'){
    try {
      const response = await courseService.updateCourse(
          {
            "course_id":courseId.value.toString()
          },
          file,
          (p) => progress.value = p
      )

      console.log(response)
      emit('message', "上传成功")
      // progress.value = 0 // 重置进度
    } catch (err) {
      emit('message', err)
      progress.value = -1 // 错误状态
    }
  }
  else if (props.uploadType === 'uploadFileResource-post'){
    emit('file-uploaded', file);
  }

}

</script>

<template>
  <div class="upload-container">
    <!-- 上传区域 -->
    <div
        class="drop-zone"
        :class="{ dragging: isDragging }"
        @dragover.prevent="handleDrag"
        @dragleave.prevent="handleDrag"
        @drop.prevent="handleDrop"
        @click="triggerInput"
    >
      <div class="upload-prompt">
        <template v-if="!fileReceived">
          <svg class="upload-icon" viewBox="0 0 24 24">
            <path d="M19,13H13V19H11V13H5V11H11V5H13V11H19V13Z" />
          </svg>
          <p>拖放文件到这里或点击选择</p>
          <p v-if="props.uploadType === 'uploadFileCourse'" class="hint">仅支持图片格式（JPG/PNG）</p>
          <p v-else class="hint">仅支持视频和pdf格式</p>
        </template>
        <template v-else>
          <svg class="check-icon" viewBox="0 0 24 24">
            <path d="M21,7L9,19L3.5,13.5L4.91,12.09L9,16.17L19.59,5.59L21,7Z"/>
          </svg>
          <p>文件已就绪，点击提交上传</p>
        </template>
      </div>
    </div>

    <!-- 进度条 -->
    <div class="progress-bar">
      <div
          class="progress-fill"
          :style="{ width: `${mergedProgress}%` }"
      ></div>
      <span class="progress-text">{{ mergedProgress }}%</span>
    </div>

    <!-- 隐藏的input -->
    <input
        ref="fileInput"
        type="file"
        :accept="fileAccept"
        class="hidden-input"
        @change="(e) => handleFilePreCheck((e.target as HTMLInputElement).files?.[0]!)"
    />
  </div>
</template>

<style scoped>
/* 根容器样式 */
.upload-container {
  max-width: 85%;
  min-width: 40%;
  margin: 0 auto;
  padding: 8px;
  border-radius: 10px;
  background-color: hsl(152.76deg 100% 94.65%);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /*min-height: 50vh;*/
  display: flex;
  flex-direction: column;
}

.drop-zone {
  flex: 0.8;
  border: 2px dashed #ccc;
  border-radius: 12px;
  margin: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.drop-zone:hover,
.dragging {
  border-color: #4CAF50;
  background-color: rgba(76,175,80,0.1);
}

.upload-prompt {
  text-align: center;
}

.upload-icon {
  width: 64px;
  height: 64px;
  fill: #4CAF50;
}

.hint {
  color: #666;
  font-size: 0.9em;
}

.progress-bar {
  flex: 0.2;
  height: calc(20% - 1rem);
  background: #b2afaf;
  border-radius: 8px;
  margin:10px;
  position: relative;
  overflow:hidden;
  min-height: 5vh;
}

.progress-fill {
  height:100%;
  background:#4CAF50;
  transition:width 0.3s ease;
}

.progress-text{
  position: absolute;
  left:50%;
  top:50%;
  transform: translate(-50%,-50%);
  color: white;
  font-weight: bold;
}

.hidden-input{
  display: none;
}

.check-icon {
  width: 48px;
  height: 48px;
  fill: #4CAF50;
  animation: bounceIn 0.5s;
}

@keyframes bounceIn {
  0% { transform: scale(0.8); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}
</style>