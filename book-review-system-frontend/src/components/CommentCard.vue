<script setup>
import { Memo } from '@element-plus/icons-vue'
import router from '../router/index.js'

const props = defineProps({
  showBook: {type: Boolean, default: false},
  list: {type: Array, default: () => []},
})
</script>

<template>
  <div>
    <el-card class="comment-card" shadow="never" v-for="el in list">
      <template #header>
        <div style="display: flex;align-items: center">
          <el-avatar shape="circle" size="small" :src="el.userAvatar" />
          <div class="comment-username" v-text="el.username"></div>
          <div v-if="showBook" style="display: flex;align-items: center">
            <el-button class="comment-book-name" text type="primary" @click="router.push(`/book/${el.bookId}`)">
              <el-icon>
                <Memo />
              </el-icon>
              &nbsp;{{ el.bookName }}
            </el-button>
            <el-tag disable-transitions size="small" v-if="el.status === 0">待审核</el-tag>
            <el-tag disable-transitions size="small" v-else-if="el.status === 1" type="danger">未通过</el-tag>
            <el-tag disable-transitions size="small" v-else-if="el.status === 2" type="success">通过</el-tag>
            <el-tag disable-transitions size="small" v-else type="info">未知</el-tag>
            <span style="color: var(--el-color-danger)" v-if="el.status===1">&nbsp;&nbsp;原因： {{ el.statusReason }}</span>
          </div>
        </div>
      </template>
      <div class="comment-title" v-text="el.title"></div>
      <div class="comment-content" v-text="el.content"></div>
    </el-card>
  </div>
</template>

<style scoped>
.comment-card {
  margin-bottom: 30px;
}

.comment-book-name {
  font-size: 13px;
  padding: 0;
  margin: 0 1em;
}

.comment-username {
  margin-left: 8px;
  font-size: 14px;
  color: var(--el-color-primary-light-5);
}

.comment-title {
  color: var(--el-color-primary-light-3);
  margin-bottom: 20px;
}

.comment-content {
  font-size: 14px;
  line-height: 30px;
  word-wrap: break-word;
}
</style>
