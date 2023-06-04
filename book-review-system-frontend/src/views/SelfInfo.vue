<script setup>
import { useUserStore } from '../stores/user.js'
import { onMounted, reactive, ref } from 'vue'
import { request } from '../api/request.js'
import { ElMessage } from 'element-plus'
import CommentCard from '../components/CommentCard.vue'
import { useMemberStore } from '../stores/member.js'
import JoinMember from '../components/JoinMember.vue'
import md5 from 'md5'

const props = defineProps({})

const userStore = useUserStore()
const memberStore = useMemberStore()

const processing = ref(false)
const formData = ref({
  id: userStore.user.id,
  avatar: userStore.user.avatar,
  username: userStore.user.username,
  email: userStore.user.email,
  phone: userStore.user.phone,
})
const dialogVisible = ref(false)

const joinMemberVisible = ref(false)
const closeJoinMember = () => {
  joinMemberVisible.value = false
}

const save = () => {
  processing.value = true
  request({
    url: `/user`,
    method: 'post',
    data: formData.value,
  }).then(() => {
    ElMessage.success('保存成功')
    dialogVisible.value = false
    userStore.getInfo()
  }).catch(({msg}) => ElMessage.warning(msg))
    .finally(() => processing.value = false)
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
    formData.value.avatar = result
    processing.value = false
  }
}

const processingChangePass = ref(false)
const formEl = ref(null)
const changePassFormValue = ref({
  oldPassword: '',
  newPassword: '',
})
const changePassFormRules = reactive({
  oldPassword: [
    {
      required: true,
      message: '请输入旧密码',
      trigger: 'blur',
    },
  ],
  newPassword: [
    {
      required: true,
      message: '请输入新密码',
      trigger: 'blur',
    },
  ],
})

const changePass = () => {
  formEl.value.validate().then((valid) => {
    if (valid) {
      processingChangePass.value = true
      request({
        url: `/user/change-password`,
        method: 'post',
        data: {
          oldPassword: md5(changePassFormValue.value.oldPassword),
          newPassword: md5(changePassFormValue.value.newPassword),
        },
      })
        .then(() => {
          ElMessage.success('密码修改成功！')
          changePassFormValue.value.oldPassword = ''
          changePassFormValue.value.newPassword = ''
        })
        .catch(({msg}) => {
          ElMessage.error('密码修改失败: ' + msg)
        })
        .finally(() => {
          processingChangePass.value = false
        })
    }
  }).catch(() => {})
}

const commentList = ref([])
const fetchingComment = ref(true)
const getComment = () => {
  fetchingComment.value = true
  request({
    url: `/comment/list/me`,
    method: 'get',
  }).then(({data}) => {
    commentList.value = data || []
  }).catch(({msg}) => ElMessage.error('书评获取失败：' + msg))
    .finally(() => fetchingComment.value = false)
}
onMounted(getComment)
</script>

<template>
  <div>
    <join-member :visible="joinMemberVisible" @close="closeJoinMember" />
    <el-dialog v-model="dialogVisible" :close-on-click-modal="false" :show-close="false">
      <div v-loading="processing">
        <el-form :model="formData" label-width="100px">
          <el-form-item label="修改头像">
            <el-upload action="" accept="image" :show-file-list="false"
                       :auto-upload="false" @change="getFile">
              <div>
                <el-avatar :size="80" :src="formData.avatar" />
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item label="用户名" required>
            <el-input v-model.trim="formData.username" />
          </el-form-item>
          <el-form-item label="电子邮箱" required>
            <el-input v-model.trim="formData.email" />
          </el-form-item>
          <el-form-item label="手机号" required>
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

    <el-row style="animation: BottomToTop 600ms">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex;align-items: center">
              <span>个人信息</span>
            </div>
          </template>
          <div>
            <el-row>
              <el-col :span="8">
                <el-avatar :size="150" :src="userStore.user.avatar" />
              </el-col>
              <el-col :span="16">
                <div class="other-info">
                  <p><span>用户名：</span>{{ userStore.user.username }}
                    <el-tag v-if="memberStore.isMember" type="info">
                      会员到期时间：{{ new Date(memberStore.expireAt).toLocaleString() }}
                    </el-tag>
                    <el-button v-else size="small" @click="joinMemberVisible=true">开通会员</el-button>
                  </p>
                  <p><span>电子邮箱：</span>{{ userStore.user.email }}</p>
                  <p><span>手机号：</span>{{ userStore.user.phone }}</p>
                  <p><span>注册时间：</span>{{ new Date(userStore.user.createdAt).toLocaleString() }}</p>
                </div>
                <el-button style="margin-top: 20px" type="primary" @click="dialogVisible = true">修改信息</el-button>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" style="margin-left: 10px;height: 100%">
          <template #header>
            <div style="display: flex;align-items: center">
              <span>修改密码</span>
            </div>
          </template>
          <div>
            <el-form
              ref="formEl"
              label-width="80px"
              :model="changePassFormValue"
              :rules="changePassFormRules"
            >
              <el-form-item label="旧密码" prop="oldPassword">
                <el-input v-model.trim="changePassFormValue.oldPassword" type="password" />
              </el-form-item>
              <el-form-item label="新密码" prop="newPassword">
                <el-input v-model.trim="changePassFormValue.newPassword" type="password"
                          @keydown.enter="changePass()" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="changePass()">保存</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top: 10px;animation: BottomToTop 800ms;">
      <template #header>
        <div style="display: flex;align-items: center">
          <span>我的书评</span>
        </div>
      </template>
      <comment-card show-book v-loading="fetchingComment" :list="commentList" />
    </el-card>

  </div>
</template>

<style scoped>
.other-info p {
  font-size: 14px;
  line-height: 40px;
}

.other-info p span {
  color: var(--el-color-primary-light-3);
}
</style>
