<script setup>

import router from '../router/index.js'

const props = defineProps({data: {type: Object, default() {return {}}}})

const to = () => {
  router.push(`/book/${props.data.id}`)
}

</script>

<template>
  <div>
    <el-card shadow="hover" :body-style="{ padding: '0px' }">
      <div class="card-content">
        <el-tooltip effect="light" placement="right">
          <template #content>
            <div class="pop">
              <h3>{{ data.bookName }}</h3>
              <p>{{ data.author }} / {{ data.publishDate }} / {{ data.publisher }}</p>
              <p>{{ data.description }}</p>
            </div>
          </template>
          <el-image lazy @click="to()" fit="cover" class="cover no-select" :src="data.cover" />
        </el-tooltip>
        <el-button @click="to()" type="primary" class="book-name" text>{{ data.bookName }}</el-button>
        <p class="author">{{ data.author }}</p>
      </div>

    </el-card>
  </div>
</template>

<style scoped>
.card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.cover {
  width: 100%;
  aspect-ratio: 7 / 10;
  cursor: pointer;
}

.book-name {
  font-size: 16px;
  margin: 5px 0;
}

.author {
  font-size: 12px;
  margin-bottom: 10px;
}

.pop p {
  max-width: 300px;
  word-wrap: break-word;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
}
</style>
