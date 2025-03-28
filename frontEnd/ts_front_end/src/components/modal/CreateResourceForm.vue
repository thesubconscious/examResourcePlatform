<script setup lang="ts">
import {onMounted, ref, watch} from 'vue';
import InputField from "@/components/basic/InputField.vue";
import SubmitButton from "@/components/basic/SubmitButton.vue";
import {courseService} from "@/services/courseService";
import {useI18n} from "vue-i18n";
import {useRoute, useRouter} from "vue-router";
import {resourceService} from "@/services/resourceService";
import UploadResourceForm from "@/components/modal/uploadResourceForm.vue";


const {t} = useI18n();
const router = useRouter();
const route = useRoute();
const receivedFile = ref<File | null>(null);
const fileExisted = ref<boolean>(false);
const pendingUpload = ref<Promise<any> | null>(null)
const progress = ref(0);
const formData = ref({
  course_id: '',
  parent_node_id: '',
  type: '',
  title: ''
});
const modalType = ref("uploadFileResource-post");
const emit = defineEmits<{
  (event: 'message', message: string): void;
}>();

onMounted(() => {
  formData.value.course_id = <string>route.params.id;
})

const handleFileReceived = (file: File) => {
  receivedFile.value = file
  fileExisted.value = true;
}

const handleSubmit = async () => {
  if (!receivedFile.value && formData.value.type === 'LEAF') {
    emit('message', '请先选择文件')
    return
  }

  progress.value = 0;
  try {
    const res = await resourceService.createResource(
        route.params.id,
        formData.value,
        receivedFile.value!, // 这里用暂存的文件
        (p) => progress.value = p
    );
    receivedFile.value = null // 清空暂存
    // console.log('Create resource successful:', res);
    emit('message', 'Create resource successful:' + res);
  } catch (e) {
    console.log(e);
    emit('message', e);
  }finally {
    progress.value = 100; // 不知道为什么到不了100
    // formData.value.course_id = <string>route.params.id;
  }
};

const chapterOptions = ref<Array<{ node_id: number; title: string }>>([])
watch(() => formData.value.type, async (newVal) => {
  if (newVal === 'LEAF' && formData.value.course_id) {
    try {
      const data = await resourceService.getResourcesChapters(formData.value.course_id)
      chapterOptions.value = data.map(item => ({node_id: item.node_id, title: item.title}))
    } catch (err) {
      console.error('加载章节失败:', err)
    }
  } else {
    formData.value.parent_node_id = ''
  }
})

const handleMessage = (message: string) => {
  emit("message", message);
}
</script>

<template>
  <div>
    <h1>{{ $t('createResourceForm.title') }}</h1>
    <form @submit.prevent="handleSubmit()">
      <input-field :label="$t('createResourceForm.form.title')" v-model="formData.title"/>

      <div class="form-group">
        <label>{{ $t('createResourceForm.form.typeLabel') }}</label>
        <select
            v-model="formData.type"
            class="form-select"
        >
          <option value="CHAPTER">
            {{ $t('createResourceForm.form.chapterType') }}
          </option>
          <option value="LEAF">
            {{ $t('createResourceForm.form.leafType') }}
          </option>
        </select>
      </div>

      <div
          v-if="formData.type === 'LEAF'"
          class="form-group"
      >
        <label>{{ $t('createResourceForm.form.parentChapter') }}</label>
        <select
            v-model="formData.parent_node_id"
            class="form-select"
            :disabled="!chapterOptions.length"
        >
          <option
              v-for="chapter in chapterOptions"
              :key="chapter.node_id"
              :value="chapter.node_id"
          >
            {{ chapter.title }}
          </option>
        </select>

      </div>

      <div
          v-if="formData.type === 'LEAF' && formData.parent_node_id "
          class="form-group"
      >
        <upload-resource-form
            :parent-progress="progress"
            :uploadType="modalType"
            :file-received="fileExisted"
            @message="handleMessage"
            @file-uploaded="handleFileReceived"
        />
      </div>

      <submit-button :label="$t('createCourseForm.form.submit')"/>
    </form>
  </div>
</template>

<style scoped>
/* 根容器样式 */
div {
  max-width: 85%;
  min-width: 50%;
  margin: 0 auto;
  padding: 8px;
  border-radius: 10px;
  background-color: hsl(152.76deg 100% 94.65%);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
h1 {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

/* 表单样式 */
form {
  display: flex;
  flex-direction: column;
  gap: 1.5vh;
}

.form-group {
  min-width: 65%;
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中对齐 */
}

.form-select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background-color: white;
  transition: border-color 0.2s;

  &:focus {
    outline: none;
    border-color: #4fd1c5;
    box-shadow: 0 0 0 2px rgba(79, 209, 197, 0.2);
  }
}

label {
  display: inline-block;
  font-size: 1.0rem;
  margin-right: .5rem;
  white-space: nowrap; /*禁止换行*/
}


/* 响应式布局 */
@media (max-width: 500px) {
  div {
    padding: 15px;
  }

  h1 {
    font-size: 20px;
  }

}
</style>