<script setup>
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import router from '../router/index.js'
import { ElMessage } from 'element-plus'
import { request } from '../api/request.js'
import md5 from 'md5'

const props = defineProps({})

const processing = ref(false)
const formEl = ref(null)
const formValue = reactive({
  username: '',
  email: '',
  captcha: '',
  phone: '',
  password: '',
  rePassword: '',
})
const formRules = reactive({
  username: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur',
    },
  ],
  email: [
    {
      required: true,
      message: '请输入电子邮箱',
      trigger: 'blur',
    },
  ],
  captcha: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur',
    },
  ],
  phone: [
    {
      required: true,
      message: '请输入手机号',
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
  rePassword: [
    {
      required: true,
      message: '两次输入的密码不一致',
      trigger: 'blur',
      validator(rule, value, callback) {
        if (value === '' || value !== formValue.password) {
          callback(new Error())
        } else {
          callback()
        }
      },
    },
  ],
})

const locked = computed(() => formValue.email.trim() === '')
const countDown = ref(0)
const sendCaptcha = () => {
  if (locked.value) return
  if (countDown.value > 0) return
  countDown.value = 60
  request({
    url: `/user/forget/captcha`,
    method: 'get',
    params: {email: formValue.email.trim()},
  }).then(() => {
    ElMessage.success('发送成功，请注意检查邮件')
  }).catch(({msg}) => {
    ElMessage.error(msg)
  })
}
let intervalId = 0
onMounted(() => {
  intervalId = setInterval(() => {
    if (countDown.value > 0) countDown.value -= 1
  }, 1000)
})
onUnmounted(() => clearInterval(intervalId))

const onSubmit = () => {
  formEl.value.validate().then((valid) => {
    if (valid) {
      processing.value = true
      request({
        url: `/user/forget`,
        method: 'post',
        data: {
          username: formValue.username,
          email: formValue.email,
          captcha: formValue.captcha,
          phone: formValue.phone,
          password: md5(formValue.password),
        },
      }).then(() => {
        ElMessage.success('密码修改成功！正在跳转登录页面...')
        router.push('/login')
      })
        .catch(({msg}) => {
          ElMessage.error(msg)
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
          <el-text size="large">重置密码</el-text>
        </div>
      </template>
      <el-form
        ref="formEl"
        label-width="100px"
        :model="formValue"
        :rules="formRules"
        style="max-width: 500px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model.trim="formValue.username" autofocus />
        </el-form-item>
        <el-form-item label="电子邮箱" prop="email">
          <el-input v-model.trim="formValue.email" type="email" />

        </el-form-item>
        <el-form-item>
          <el-button :disabled="locked||countDown>0" @click="sendCaptcha()">
            {{ countDown === 0 ? '获取验证码' : `冷却时间：${countDown}s` }}
          </el-button>
        </el-form-item>
        <el-form-item label="邮箱验证码" prop="captcha">
          <el-input v-model.trim="formValue.captcha" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model.number="formValue.phone" />
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input v-model.trim="formValue.password" type="password" />
        </el-form-item>
        <el-form-item label="重复新密码" prop="rePassword">
          <el-input v-model.trim="formValue.rePassword" type="password" @keydown.enter="onSubmit()" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit()">重置</el-button>
          <el-button text @click="router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
</style>
