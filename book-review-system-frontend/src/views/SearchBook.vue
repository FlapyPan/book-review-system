<script setup>
import { onMounted, ref } from 'vue'
import { request } from '../api/request.js'
import BookInfo from '../components/BookInfo.vue'
import { onBeforeRouteUpdate, useRoute } from 'vue-router'

const props = defineProps({})

const route = useRoute()
const bookList = ref([])
const fetchLatest = (r) => {
  bookList.value = []
  const searchKey = r.params['search']
  request({
    url: `/book/list`,
    method: 'get',
    params: {search: searchKey},
  }).then(({data}) => {
    bookList.value = data
  })
}
onMounted(() => fetchLatest(route))

onBeforeRouteUpdate((r) => fetchLatest(r))
</script>

<template>
  <el-card shadow="never" class="card">
    <template #header>
      <div>
        <span>搜索结果</span>
      </div>
    </template>
    <div class="container">
      <BookInfo class="info" v-for="el in bookList" :data="el" :key="el.bookName" />
    </div>
  </el-card>
</template>

<style scoped>
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
  animation: BottomToTop 300ms;
}
</style>
