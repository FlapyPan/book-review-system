<script setup>
import { useRoute } from 'vue-router'
import { downloadRequest, request } from '../api/request.js'
import { computed, onMounted, reactive, ref } from 'vue'
import router from '../router/index.js'
import { ElMessage, ElMessageBox } from 'element-plus'
import CommentCard from '../components/CommentCard.vue'
import { useUserStore } from '../stores/user.js'
import { useMemberStore } from '../stores/member.js'

const route = useRoute()

const userStore = useUserStore()

const memberStore = useMemberStore()

const id = parseInt(route.params['id'])

const book = ref({})

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
const cateName = (id) => {
  for (const category of categoryList.value) {
    if (category.id === id) return category.name
  }
  return '暂无'
}

const rank = ref({
  score: 0,
  count: 0,
  yourScore: null,
})
const fetchingRank = ref(true)
const fetchRank = () => {
  fetchingRank.value = true
  request({
    url: `/rank/book/${id}`,
    method: 'get',
  }).then(({data}) => {
    rank.value = data
  }).catch(({msg}) => ElMessage.error(msg))
    .finally(() => fetchingRank.value = false)
}
onMounted(fetchRank)
const saveRank = (score) => {
  fetchingRank.value = true
  request({
    url: `/rank`,
    method: 'post',
    data: {bookId: id, score},
  }).then(() => {
    ElMessage.success('评分成功')
  }).catch(({msg}) => {
    ElMessage.error(msg)
  }).finally(fetchRank)
}

const fetching = ref(true)
const fetchData = () => {
  fetching.value = true
  request({
    url: `/book/${id}`,
    method: 'get',
  }).then(({data}) => {
    book.value = data
  }).catch(() => {})
    .finally(() => fetching.value = false)
}
onMounted(fetchData)

const toSearch = (key) => {
  router.push(`/search/${key}`)
}

const fetchingEbookData = ref(false)
const ebookData = ref([])
const loadEbookData = () => {
  request({
    url: `/ebook/book/${id}`,
    method: 'get',
  }).then(({data}) => {
    ebookData.value = data || []
  }).catch(({msg}) => ElMessage.error('电子书列表加载失败：' + msg))
    .finally(() => fetchingEbookData.value = false)
}
onMounted(() => {
  if (userStore.isLogin) loadEbookData()
})
const downloadEbook = (row) => {
  if (!memberStore.isMember) {
    ElMessageBox.confirm('下载功能需要vip会员，是否跳转至个人中心开通会员？', {
      confirmButtonText: '跳转',
      cancelButtonText: '取消',
    }).then(() => {
      router.push('/me')
    }).catch(() => {})
    return
  }
  const url = `/ebook/download/${row.id}`
  downloadRequest(url).then(({data}) => {
    const a = document.createElement('a')
    a.setAttribute('download', row.filename)
    const obj = window.URL.createObjectURL(new Blob([data]))
    a.href = obj
    a.click()
    window.URL.revokeObjectURL(obj)
  })
}

const commentList = ref([])
const fetchingComment = ref(true)
const getComment = () => {
  fetchingComment.value = true
  request({
    url: `/comment/list/book/${id}`,
    method: 'get',
  }).then(({data}) => {
    commentList.value = data || []
  }).catch(({msg}) => ElMessage.error('书评获取失败：' + msg))
    .finally(() => fetchingComment.value = false)
}
onMounted(getComment)

const processing = ref(false)
const formEl = ref(null)
const commentForm = reactive({
  title: '',
  content: '',
})
const commentFormRules = reactive({
  title: [
    {
      required: true,
      message: '请输入书评标题',
      trigger: 'blur',
    },
  ],
  content: [
    {
      required: true,
      message: '请输入书评内容',
      trigger: 'blur',
    },
  ],
})
const canSubmit = computed(() => commentForm.content.length < 200)
const onSubmit = () => {
  formEl.value.validate().then((valid) => {
    if (valid) {
      processing.value = true
      request({
        url: `/comment`,
        method: 'post',
        data: {
          bookId: id,
          title: commentForm.title,
          content: commentForm.content,
        },
      }).then(() => {
        ElMessage.success('提交成功！审核成功后可见')
        commentForm.title = ''
        commentForm.content = ''
      }).catch(({msg}) => {
        ElMessage.error('提交失败: ' + msg)
      }).finally(() => {
        processing.value = false
      })
    }
  }).catch(() => {})
}

</script>

<template>
  <div>
    <el-card class="info-card">
      <div v-loading="fetching">
        <h1>{{ book.bookName }}</h1>
        <el-row>
          <el-col :span="6">
            <div class="cover">
              <el-image :src="book.cover" />
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info">
              <p>作者：
                <span style="font-size: 18px;color: var(--color-primary);cursor: pointer"
                      @click="toSearch(book.author)"
                >
              {{ book.author }}
            </span>
              </p>
              <p>出版社：{{ book.publisher || '暂无' }}</p>
              <p>分类：{{ cateName(book.categoryId) }}</p>
              <p>出版日期：{{ book.publishDate || '暂无' }}</p>
              <p>译者：{{ book.translator || '暂无' }}</p>
              <p>页数：{{ book.pages || '暂无' }}</p>
              <p>价格：{{ book.price || '暂无' }}</p>
              <p>ISBN：{{ book.isbn || '暂无' }}</p>
            </div>
          </el-col>
          <el-col :span="8">
            <div style="display: flex;flex-direction: column;align-items: center">
              <div v-loading="fetchingRank" style="display: flex;flex-direction: column;align-items: center">
                <span style="margin: 5px 0;font-size: 14px">总评分：</span>
                <el-rate v-model="rank.score" size="large" show-score disabled />
                <span v-if="!userStore.isLogin" style="margin: 5px 0;font-size: 12px;color: gray">请登录后评分</span>
                <template v-else>
                  <span style="margin: 5px 0;font-size: 14px">你的评分：</span>
                  <div style="display: flex;align-items: center">
                    <el-rate @change="saveRank" v-model="rank.yourScore" size="large"
                             :show-score="rank.yourScore!==null" />
                    <span v-if="rank.yourScore===null" style="margin: 5px 0;font-size: 12px;color: gray">未评分</span>
                  </div>

                </template>
              </div>
              <div class="description">
                <div>内容简介</div>
                <p>
                  {{ book.description }}
                </p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-card shadow="never" class="download-card" v-if="!userStore.isLogin">
      发表书评或下载电子书请
      <el-button text type="primary" @click="router.push('/login')">登录</el-button>
    </el-card>
    <el-card shadow="never" class="download-card" v-if="userStore.isLogin">
      <template #header>
        <div style="font-size: 18px;color: var(--el-color-primary-light-3)">电子书下载</div>
      </template>
      <div v-loading="fetchingEbookData">
        <el-table :data="ebookData">
          <el-table-column label="文件名" prop="filename" />
          <el-table-column label="上传时间" prop="createdAt" />
          <el-table-column label="操作" align="center" fixed="right" :width="140">
            <template #default="scope">
              <el-button @click="downloadEbook(scope.row)" size="small" type="primary">
                下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <el-card class="review-card" shadow="never">
      <template #header>
        <div style="font-size: 18px;color: var(--el-color-primary-light-3)">书评</div>
      </template>
      <div class="comment">
        <el-card class="add-comment" shadow="never" v-if="userStore.isLogin">
          <template #header>
            <div style="font-size: 18px;color: var(--el-color-primary-light-3)">编写书评</div>
          </template>
          <div v-loading="processing">
            <el-form
              ref="formEl"
              label-width="100px"
              :model="commentForm"
              :rules="commentFormRules"
            >
              <el-form-item label="标题" maxlength="50" prop="title">
                <el-input v-model.trim="commentForm.title" />
              </el-form-item>
              <el-form-item label="内容" prop="content">
                <el-input placeholder="书评内容至少200字" :min="200" type="textarea"
                          :autosize="{ minRows: 6, maxRows: 50 }"
                          v-model="commentForm.content" />
              </el-form-item>
              <el-form-item>
                <el-button :disabled="canSubmit" type="primary" @click="onSubmit()">提交</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
        <comment-card v-loading="fetchingComment" :list="commentList" />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
h1 {
  margin-top: 0;
  color: var(--el-color-primary-light-3);
}

.info-card {
  margin-bottom: 10px;
  animation: BottomToTop 400ms;
}

.cover {
}

.info {
  padding: 0 2em;
}

.info p {
  font-size: 16px;
  line-height: 30px;
}

.description {
  margin-top: 30px;
  width: 100%;
  max-height: 150px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.description div {
  color: var(--el-color-primary-light-3);
  text-align: center;
  font-size: 16px;
  margin-bottom: 10px;
}

.description p {
  font-size: 14px;
  text-align: left;
  width: 100%;
  word-wrap: break-word;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
}

.download-card {
  margin: 10px 0;
  animation: BottomToTop 600ms;
}

.review-card {
  animation: BottomToTop 800ms;
}

.add-comment {
  margin-bottom: 30px;
}


</style>
