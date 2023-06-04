<script setup>
import { request } from '../../api/request.js'
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const fetching = ref(true)
const fetchData = (search) => {
  fetching.value = true
  request({
    url: `/comment/list`,
    method: 'get',
    params: {search},
  }).then(({data}) => {
    tableData.value = data
  }).catch(({msg}) => ElMessage.error(msg))
    .finally(() => fetching.value = false)
}
onMounted(() => fetchData())

const searchValue = ref('')
const submitSearch = () => {
  fetchData(searchValue.value)
}

const processing = ref(false)
const formData = ref({
  userName: '',
  bookName: '',
  id: 0,
  title: '',
  content: '',
  status: 0,
})
const dialogVisible = ref(false)

const save = (status) => {
  ElMessageBox.prompt('拒绝原因', '审核')
    .then(({value}) => {
      processing.value = true
      request({
        url: `/comment`,
        method: 'post',
        data: {
          id: formData.value.id,
          status: status,
          statusReason: status === 2 ? '通过' : value,
        },
      }).then(() => {
        ElMessage.success('保存成功')
        dialogVisible.value = false
        fetchData()
      }).catch(({msg}) => ElMessage.warning(msg))
        .finally(() => processing.value = false)
    }).catch(() => {})
}

const handleEdit = (index, row) => {
  formData.value = {...row}
  dialogVisible.value = true
}
const handleDelete = (index, row) => {
  request({
    url: `/comment/${row.id}`,
    method: 'delete',
  }).then(() => {
    fetchData()
  }).catch(({msg}) => ElMessage.error(msg))
}

const statusName = (status) => {
  if (status === 0) return '待审核'
  if (status === 1) return '审核未通过'
  if (status === 2) return '审核通过'
  return '未知'
}
</script>

<template>
  <div>
    <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :show-close="false">
      <div v-loading="processing">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="书名">
            <el-input v-model.trim="formData.bookName" disabled />
          </el-form-item>
          <el-form-item label="用户名">
            <el-input v-model.trim="formData.username" disabled />
          </el-form-item>
          <el-form-item label="标题">
            <el-input v-model.trim="formData.title" disabled />
          </el-form-item>
          <el-form-item label="状态">
            <span v-text="statusName(formData.status)"></span>
            <span v-if="formData.status===1">&nbsp;: {{ formData.statusReason }}</span>
          </el-form-item>
          <el-form-item label="内容">
            <el-input type="textarea" :autosize="{ minRows: 6, maxRows: 30 }" v-model.trim="formData.content"
                      disabled />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="danger" @click="save(1)">
          未通过
        </el-button>
        <el-button type="success" @click="save(2)">
         通过
        </el-button>
      </span>
      </template>
    </el-dialog>
    <el-card v-loading="fetching">
      <div style="display: flex;margin-bottom: 10px;align-items: center">
        <div style="flex: 1 1"></div>
      </div>
      <el-table :data="tableData" style="width: 100%" max-height="70vh">
        <el-table-column label="书籍名" prop="bookName" />
        <el-table-column label="用户名" prop="username" />
        <el-table-column label="标题" prop="title" />
        <el-table-column label="创建日期" prop="createdAt" />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-tag disable-transitions v-if="scope.row.status === 0">待审核</el-tag>
            <el-tag disable-transitions v-else-if="scope.row.status === 1" type="danger">未通过</el-tag>
            <el-tag disable-transitions v-else-if="scope.row.status === 2" type="success">通过</el-tag>
            <el-tag disable-transitions v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" :width="140">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
              审核
            </el-button>
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
    </el-card>
  </div>
</template>

<style scoped>
</style>
