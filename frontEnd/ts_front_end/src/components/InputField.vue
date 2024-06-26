<script setup lang="ts">
const props = defineProps({
  label: String,
  modelValue: [String, Number],
  type: {
    type: String,
    default: 'text'
  }
});

const emit = defineEmits(['update:modelValue']);

const id = 'input-field-' + Math.random().toString(36).substr(2, 9); // 生成唯一ID
</script>

<template>
  <div class="input-field">
    <label :for="id">{{ label }}</label>
    <input :id="id" :type="type" :value="modelValue" @input="$emit('update:modelValue', (<HTMLInputElement>$event.target).value)" />
  </div>
</template>

<style scoped>

</style>