<script setup>
import { request } from '../../api/request.js'
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import EbookManager from '../../components/admin/EbookManager.vue'

const categoryList = ref([])
const fetchCategory = () => request({
  url: `/category`,
  method: 'get',
}).then(({data}) => {
  categoryList.value = [...data]
})
const tmpCategory = ref('')
const categoryAdd = () => request({
  url: `/category`,
  method: 'post',
  data: {name: tmpCategory.value},
}).then(() => {
  ElMessage.success('添加成功')
  tmpCategory.value = ''
})
  .then(fetchCategory).catch(({msg}) => ElMessage.error(msg))

const tableData = ref([])
const fetching = ref(true)
const fetchData = (search) => {
  fetching.value = true
  request({
    url: `/book/list`,
    method: 'get',
    params: {search},
  }).then(({data}) => {
    tableData.value = data
  }).then(fetchCategory)
    .catch(({msg}) => ElMessage.error(msg))
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
  cover: '',
  bookName: '',
  author: '',
  publisher: '',
  publishDate: '',
  translator: '',
  pages: 0,
  price: 0.0,
  binding: '',
  isbn: '',
  description: '',
  categoryId: null,
})
const dialogVisible = ref(false)

const add = () => {
  formData.value = {
    id: 0,
    cover: '',
    bookName: '',
    author: '',
    publisher: '',
    publishDate: '',
    translator: '',
    pages: 1,
    price: 0.0,
    binding: '',
    isbn: '',
    description: '',
    categoryId: null,
  }
  dialogVisible.value = true
}

const save = () => {
  processing.value = true
  request({
    url: `/book`,
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
    url: `/book/${row.id}`,
    method: 'delete',
  }).then(() => {
    fetchData()
  }).catch(({msg}) => ElMessage.error(msg))
}

const getFile = (file) => {
  if (file.raw.size / 1024 / 1024 > 1) {
    ElMessage.error('图片大小限制为1MB!')
    return
  }
  processing.value = true
  const reader = new FileReader()
  let result = ''
  reader.readAsDataURL(file.raw)
  reader.onload = () => result = reader.result
  reader.onerror = () => processing.value = false
  reader.onloadend = () => {
    formData.value.cover = result
    processing.value = false
  }
}

const showEbookManager = ref(false)
const ebookData = ref({
  data: [], bookId: 0, bookName: '',
})
const fetchingEbookData = ref(false)
const manageEbook = (index, row) => {
  const bookId = row.id
  showEbookManager.value = true
  fetchingEbookData.value = true
  loadEbookData(bookId)
  ebookData.value.bookName = row.bookName
}
const loadEbookData = (bookId) => {
  request({
    url: `/ebook/book/${bookId}`,
    method: 'get',
  }).then(({data}) => {
    ebookData.value.data = data || []
    ebookData.value.bookId = bookId
  }).catch(({msg}) => ElMessage.error('电子书列表加载失败：' + msg))
    .finally(() => fetchingEbookData.value = false)
}

</script>

<template>
  <div>
    <el-dialog v-model="showEbookManager" :close-on-click-modal="false">
      <template #header>
        {{ ebookData.bookName }}
      </template>
      <div v-loading="fetchingEbookData">
        <ebook-manager :data="ebookData.data" :book-id="ebookData.bookId" @refresh="loadEbookData" />
      </div>
    </el-dialog>
    <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :show-close="false">
      <div v-loading="processing">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="书籍封面" required>
            <el-upload action="" accept="image" :show-file-list="false"
                       :auto-upload="false" @change="getFile">
              <div>
                <img style="height: 100px" v-if="formData.cover" :src="formData.cover" alt="" />
                <el-icon style="font-size: 40px" v-else>
                  <Plus />
                </el-icon>
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item label="书名" required>
            <el-input v-model.trim="formData.bookName" />
          </el-form-item>
          <el-form-item label="分类">
            <el-row>
              <el-col :span="12">
                <el-select
                  v-model="formData.categoryId"
                  filterable
                  placeholder="请选择分类">
                  <el-option
                    v-for="item in categoryList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-col>
              <el-col :span="12">
                <el-input placeholder="添加新分类" v-model.trim="tmpCategory" @keydown.enter="categoryAdd()" />
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item label="作者" required>
            <el-input v-model.trim="formData.author" />
          </el-form-item>
          <el-form-item label="译者">
            <el-input v-model.trim="formData.translator" />
          </el-form-item>
          <el-form-item label="出版社" required>
            <el-input v-model.trim="formData.publisher" />
          </el-form-item>
          <el-form-item label="出版日期" required>
            <el-date-picker v-model="formData.publishDate" />
          </el-form-item>
          <el-form-item label="页数" required>
            <el-input v-model.number="formData.pages" type="number" />
          </el-form-item>
          <el-form-item label="定价" required>
            <el-input v-model.number="formData.price" type="number" />
          </el-form-item>
          <el-form-item label="装帧">
            <el-input v-model.trim="formData.binding" />
          </el-form-item>
          <el-form-item label="ISBN" required>
            <el-input v-model.trim="formData.isbn" />
          </el-form-item>
          <el-form-item label="简介">
            <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 6 }" v-model.trim="formData.description" />
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
                  placeholder="书名、作者、ISBN" />
        <el-button @click="submitSearch()">搜索</el-button>
        <div style="flex: 1 1"></div>
        <el-button type="primary" @click="add()">添加书籍</el-button>
      </div>
      <el-table :data="tableData" style="width: 100%" max-height="70vh">
        <el-table-column label="封面">
          <template #default="scope">
            <el-image style="height: 60px;width: 40px" fit="contain" :src="scope.row.cover" />
          </template>
        </el-table-column>
        <el-table-column label="书名" prop="bookName" />
        <el-table-column label="作者" prop="author" />
        <el-table-column label="出版社" prop="publisher" />
        <el-table-column label="译者" prop="translator" />
        <el-table-column label="出版日期" prop="publishDate" />
        <el-table-column label="页数" prop="pages" />
        <el-table-column label="定价" prop="price" />
        <el-table-column label="装帧" prop="binding" />
        <el-table-column label="ISBN" prop="isbn" />
        <el-table-column label="操作" align="center" fixed="right" :width="220">
          <template #default="scope">
            <div style="display: flex">
              <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
                编辑
              </el-button>
              <el-button type="primary" size="small" @click="manageEbook(scope.$index, scope.row)">
                管理电子书
              </el-button>
              <el-popconfirm title="确认删除？" @confirm="handleDelete(scope.$index, scope.row)">
                <template #reference>
                  <el-button size="small" type="danger">
                    删除
                  </el-button>
                </template>
              </el-popconfirm>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
</style>
