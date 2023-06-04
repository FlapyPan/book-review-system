import { defineStore } from 'pinia'
import { request } from '../api/request.js'
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import md5 from 'md5'

export const TOKEN_STORAGE_KEY = 'token'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem(TOKEN_STORAGE_KEY))
  const setToken = (v) => {
    localStorage.setItem(TOKEN_STORAGE_KEY, v)
    token.value = v
  }
  const user = ref(null)

  const getInfo = () => {
    return request({
      url: `/user/me`,
      method: 'get',
    }).then(({data}) => {
      user.value = data
    })
  }
  if (token.value) {
    getInfo().catch(() => {
      localStorage.removeItem(TOKEN_STORAGE_KEY)
      token.value = null
      user.value = null
      ElMessage.warning('登录失效，请登录')
    })
  }
  const login = (username, password) => {
    return request({
      url: `/user/login`,
      method: 'post',
      data: {username, password: md5(password)},
    }).then(({data}) => {
      setToken(data)
      return getInfo()
    })
  }
  const logout = () => {
    localStorage.removeItem(TOKEN_STORAGE_KEY)
    location.reload()
  }

  return {
    user, login, getInfo, logout,
    isLogin: computed(() => token.value !== null),
    isAdmin: computed(() => user.value?.roleList?.includes('admin')),
    token, setToken,
  }
})
