<script setup>
import { ref } from 'vue'
import { formRequest, request } from '../../api/request.js'
import { ElMessage } from 'element-plus'

const props = defineProps({
  bookId: {type: Number, default: 0},
  data: {type: Array, default: () => []},
})
const emits = defineEmits(['refresh'])

const handleDelete = (index, row) => {
  request({
    url: `/ebook/${row.id}`,
    method: 'delete',
  }).then(() => {
    emits('refresh', props.bookId)
  }).catch(({msg}) => ElMessage.error(msg))
}

const uploader = ref(null)
const upload = () => {
  const file = uploader.value?.files[0]
  const form = new FormData()
  form.set('bookId', props.bookId + '')
  form.set('file', file)
  formRequest({
    url: `/ebook`,
    data: form,
  }).then(() => {
    emits('refresh', props.bookId)
  }).catch(({msg}) => ElMessage.error('上传失败：' + msg))
}
</script>

<template>
  <div>
    <div>
      <input ref="uploader" style="display: none" type="file" @change="upload">
      <el-button type="primary" @click="uploader?.click()">上传</el-button>
    </div>
    <el-table :data="data">
      <el-table-column label="文件名" prop="filename" />
      <el-table-column label="上传时间" prop="createdAt" />
      <el-table-column label="操作" align="center" fixed="right" :width="140">
        <template #default="scope">
          <el-popconfirm title="确认删除？" @confirm="handleDelete(scope.$index, scope.row)">
            <template #reference>
              <el-button size="small" type="danger">
                删除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<style scoped>
</style>
