<script setup lang="ts">
import {ref, computed, onMounted} from 'vue';

const props = defineProps({
  label: String,
  modelValue: [String, Number],
  type: { type: String, default: 'text' }
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
  <div class="input-field">
    <label :for="id">{{ label }}</label>
    <input :id="id" :type="type" v-model="computedValue" />
  </div>
</template>