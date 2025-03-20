<script setup lang="ts">
import { ref } from 'vue';
import InputField from "@/components/basic/InputField.vue";
import SubmitButton from "@/components/basic/SubmitButton.vue";
import {courseService} from "@/services/courseService";
import {useI18n} from "vue-i18n";
import {useRouter} from "vue-router";


const { t } = useI18n();
const router = useRouter()
const formData = ref({
  course_id: '',
  parent_node_id: '',
  type: '',
  title: '',
  teacher_id: ''
});
const emit = defineEmits<{
  (event: 'message', message: string): void;
}>();

const handleSubmit = async () => {
  try {
    const id= localStorage.getItem('userId');
    if(id !== null){
      const res = await courseService.createCourse(formData.value.name, formData.value.introduction, id);
      console.log('Create course successful:', res);
      emit('message', res.message);
    }
    else{
      emit('message', "id不存在，请重新登录")
    }
  }catch (e) {
    console.log(e);
    emit('message', e);
  }
};
</script>

<template>
  <div>
    <h1>{{$t('createCourseForm.title')}}</h1>
    <form @submit.prevent="handleSubmit">
      <input-field :label="$t('createCourseForm.form.name')" v-model="formData.name" />
      <input-field :label="$t('createCourseForm.form.introduction')" v-model="formData.introduction" :is-large="true"/>
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