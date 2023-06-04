<script setup>
import { request } from '../../api/request.js'
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const fetching = ref(true)
const fetchData = (search) => {
  fetching.value = true
  request({
    url: `/user/list`,
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
  id: 0,
  username: '',
  password: '',
  email: '',
  phone: '',
})
const dialogVisible = ref(false)

const save = () => {
  processing.value = true
  request({
    url: `/user`,
    method: 'post',
    data: {...formData.value},
  }).then(() => {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchData()
  }).catch(({msg}) => ElMessage.warning(msg))
    .finally(() => processing.value = false)
}

const handleEdit = (index, row) => {
  formData.value = {...row}
  dialogVisible.value = true
}
const handleDelete = (index, row) => {
  request({
    url: `/user/${row.id}`,
    method: 'delete',
  }).then(() => {
    fetchData()
  }).catch(({msg}) => ElMessage.error(msg))
}

</script>

<template>
  <div>
    <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :show-close="false">
      <div v-loading="processing">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="用户名" required>
            <el-input v-model.trim="formData.username" />
          </el-form-item>
          <el-form-item label="电子邮箱" required>
            <el-input v-model.trim="formData.email" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model.trim="formData.phone" />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save()">
          保存
        </el-button>
      </span>
      </template>
    </el-dialog>
    <el-card v-loading="fetching">
      <div style="display: flex;margin-bottom: 10px;align-items: center">
        <el-input v-model="searchValue" @keydown.enter="submitSearch()" style="max-width: 200px;margin-right: 10px"
                  placeholder="用户名、电子邮箱、手机号" />
        <el-button @click="submitSearch()">搜索</el-button>
        <div style="flex: 1 1"></div>
      </div>
      <el-table :data="tableData" style="width: 100%" max-height="70vh">
        <el-table-column label="头像">
          <template #default="scope">
            <el-image style="height: 60px;width: 40px" fit="contain" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column label="用户名" prop="username" />
        <el-table-column label="电子邮箱" prop="email" />
        <el-table-column label="手机号" prop="phone" />
        <el-table-column label="注册日期" prop="createdAt" />
        <el-table-column label="操作" align="center" fixed="right" :width="140">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
              编辑
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
