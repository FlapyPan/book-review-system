<script setup>
import { reactive, ref } from 'vue'
import { useUserStore } from '../stores/user'
import router from '../router/index.js'
import { ElMessage } from 'element-plus'
import { useMemberStore } from '../stores/member.js'

const props = defineProps({})

const processing = ref(false)
const formEl = ref(null)
const loginForm = reactive({
  username: '',
  password: '',
})
const loginFormRules = reactive({
  username: [
    {
      required: true,
      message: '请输入账号',
      trigger: 'blur',
    },
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur',
    },
  ],
})

const userStore = useUserStore()
const memberStore = useMemberStore()
const onSubmit = () => {
  formEl.value.validate().then((valid) => {
    if (valid) {
      processing.value = true
      userStore
        .login(loginForm.username, loginForm.password)
        .then(memberStore.load)
        .then(() => {
          router.push('/')
        })
        .catch(({msg}) => {
          ElMessage.error('登录失败: ' + msg)
        })
        .finally(() => {
          processing.value = false
        })
    }
  }).catch(() => {})
}

</script>

<template>
  <div v-loading="processing"
       style="height:100%;display: flex;align-items: center;justify-content: center">
    <el-card style="min-width:50%;margin-bottom: 10%">
      <template #header>
        <div>
          <el-text size="large">登录</el-text>
        </div>
      </template>
      <el-form
        ref="formEl"
        label-width="100px"
        :model="loginForm"
        :rules="loginFormRules"
        style="max-width: 500px"
      >
        <el-form-item label="账号" prop="username">
          <el-input v-model.trim="loginForm.username" autofocus />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model.trim="loginForm.password" type="password" @keydown.enter="onSubmit()" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit()">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
          <el-button text @click="router.push('/forget')">忘记密码？</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
</style>
