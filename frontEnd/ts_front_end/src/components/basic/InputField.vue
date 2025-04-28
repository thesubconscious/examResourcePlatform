<script setup lang="ts">
import {ref, computed, onMounted} from 'vue';

const props = defineProps({
  label: String,
  modelValue: [String, Number],
  type: { type: String, default: 'text' },
  isLarge: { type: Boolean, default: false }
});

const emit = defineEmits(['update:modelValue']);

const id = 'input-field-' + Math.random().toString(36).substr(2, 9);
const inputValue = ref(props.modelValue);

const computedValue = computed({
  get: () => inputValue.value,
  set: (value) => {
    inputValue.value = value;
    emit('update:modelValue', value);
  }
});

//针对自动填充
onMounted(() => {
  inputValue.value = props.modelValue;
});
</script>

<template>
    <div class="input-field" :class="{ 'input-field--large': isLarge }">
      <label>{{ label }}</label>
      <!-- 根据 isLarge 动态选择 input 或 textarea -->
      <component
          :is="isLarge ? 'textarea' : 'input'"
          :type="!isLarge && type"
          :value="modelValue"
          @input="$emit('update:modelValue', $event.target.value)"
          class="form-control"
      />
    </div>
</template>

<style scoped>
.input-field {
  display: flex; /* 使用 Flexbox 布局 */
  align-items: center; /* 垂直居中对齐 */
}

.input-field label {
  margin-right: .5rem; /* 添加右边距，使其与输入框分开 */
}

.input-field input,
.input-field textarea {
  padding: 0.5rem;
  transition: border-color 0.4s ease;
}

/* 当 isLarge 为 true 时，应用更大的样式 */
.input-field--large textarea {
  min-height: 8vh;
}

.input-field input:focus,
.input-field textarea:focus {
  border-color: #007BFF;
  outline: none;
}
</style>