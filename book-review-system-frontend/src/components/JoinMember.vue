<script setup>
import { useMemberStore } from '../stores/member.js'
import { ElMessage } from 'element-plus'
import { computed } from 'vue'

const props = defineProps({
  visible: {type: Boolean, default: false},
})
const emits = defineEmits(['close'])

const _v = computed({
  get: () => props.visible,
  set: (_) => {},
})

const memberStore = useMemberStore()

const join = () => {
  memberStore.join().then(() => {
    ElMessage.success('开通成功！')
    emits('close')
  }).catch(() => ElMessage.error('开通会员失败'))
}

</script>

<template>
  <el-dialog v-model="_v" :close-on-click-modal="false" :show-close="false">
    <template #header>
      <div style="display: flex;align-items: center">
        <span>会员开通</span>
      </div>
    </template>
    <div>
    </div>
    <template #footer>
      <div>
        <el-button @click="emits('close')">取消</el-button>
        <el-button @click="join()" type="primary">开通一个月会员</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<style scoped>
</style>
