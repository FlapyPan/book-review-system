import axios from 'axios'
import { useUserStore } from '../stores/user.js'
import router from '../router/index.js'

export const baseURL = import.meta.env.VITE_BACKEND_BASE_URL
export const tokenHeaderKey = import.meta.env.VITE_TOKEN_HEADER_KEY

const instance = axios.create({
  baseURL,
  responseType: 'json',
  withCredentials: false,
  headers: {
    'Content-Type': 'application/json',
  },
})
instance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    config.headers[tokenHeaderKey] = userStore.token
    return config
  },
)
instance.interceptors.response.use(
  (response) => {
    const code = response.data.code
    if (code !== 200) {
      return Promise.reject(response.data)
    }
    if (code === 400) {
      router.push('/login')
      return Promise.reject(response.data)
    }
    return Promise.resolve(response.data)
  },
  (error) => {
    return Promise.reject({
      code: error?.response?.status || 520,
      msg: error?.response?.statusText || '未知错误',
      data: null,
    })
  },
)

/**
 * @param {Object} options 参数
 * @param {string} options.url 请求地址
 * @param {("get"|"post"|"put"|"delete")} options.method 请求方法
 * @param {Object} [options.params] 查询参数
 * @param {Object} [options.data] 请求数据
 * @return {Promise<?>}
 */
export const request = ({
  url,
  method,
  params,
  data,
}) => instance({url, method, params, data})

export const formRequest = ({url, data}) =>
  instance.postForm(url, data, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })

export const downloadRequest = (url) => {
  const userStore = useUserStore()
  const headers = {}
  headers[tokenHeaderKey] = userStore.token
  return axios.get(url, {
    baseURL,
    withCredentials: false,
    headers,
  })
}
