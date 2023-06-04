<script setup>
import { reactive, ref } from 'vue'
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

const onSubmit = () => {
  formEl.value.validate().then((valid) => {
    if (valid) {
      processing.value = true
      request({
        url: `/user/register`,
        method: 'post',
        data: {
          username: formValue.username,
          email: formValue.email,
          phone: formValue.phone,
          password: md5(formValue.password),
        },
      }).then(() => {
        ElMessage.success('注册成功！正在跳转登录页面...')
        router.push('/login')
      })
        .catch(({msg}) => {
          ElMessage.error('注册失败')
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
          <el-text size="large">注册账号</el-text>
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
        <el-form-item label="手机号" prop="phone">
          <el-input v-model.number="formValue.phone" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model.trim="formValue.password" type="password"/>
        </el-form-item>
        <el-form-item label="重复密码" prop="rePassword">
          <el-input v-model.trim="formValue.rePassword" type="password" @keydown.enter="onSubmit()" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit()">注册</el-button>
          <el-button text @click="router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
</style>
