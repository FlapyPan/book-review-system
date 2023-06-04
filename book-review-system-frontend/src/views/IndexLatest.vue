<script setup>
import { onMounted, ref } from 'vue'
import { request } from '../api/request.js'
import BookInfo from '../components/BookInfo.vue'

const props = defineProps({})

const title = ref('新书速递')

const categoryList = ref([])
const fetchCategory = () => {
  request({
    url: `/category`,
    method: 'get',
  }).then(({data}) => {
    categoryList.value = data
  })
}
onMounted(fetchCategory)

const bookList = ref([])
const fetchLatest = () => {
  title.value = '新书速递'
  request({
    url: `/book/latest`,
    method: 'get',
  }).then(({data}) => {
    bookList.value = data
  }).catch(() => bookList.value = [])
}
onMounted(fetchLatest)

const fetchCategoryList = ({name, id}) => {
  title.value = name
  request({
    url: `/book/category/${id}`,
    method: 'get',
  }).then(({data}) => {
    bookList.value = data
  }).catch(() => bookList.value = [])
}

</script>

<template>
  <div>
    <el-card shadow="never" class="card">
      <template #header>
        <div>
          <span>热门分类</span>
        </div>
      </template>
      <div style="display: flex;flex-wrap: wrap">
        <el-button style="margin: 10px" @click="fetchLatest()">新书速递</el-button>
        <el-button style="margin: 10px" v-for="el in categoryList" @click="fetchCategoryList(el)">
          {{el.name }}
        </el-button>
      </div>
    </el-card>
    <el-card shadow="never" class="card">
      <template #header>
        <div>
          <span>{{ title }}</span>
        </div>
      </template>
      <div class="container">
        <BookInfo class="info" v-for="el in bookList" :data="el" :key="el.bookName" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.card {
  animation: BottomToTop 800ms;
  margin-bottom: 10px;
}

.container {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.info {
  margin: 10px;
  flex: 1;
  min-width: 200px;
  max-width: 260px;
}
</style>
