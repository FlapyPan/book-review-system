import { defineStore } from 'pinia'
import { request } from '../api/request.js'
import { ref } from 'vue'

export const useMemberStore = defineStore('member', () => {
  const isMember = ref(false)
  const expireAt = ref('')
  const check = () => request({
    url: `/member/check`,
    method: 'get',
  })
  const load = () => check().then(({data}) => {
    isMember.value = true
    expireAt.value = data
  }).catch(() => isMember.value = false)

  load()

  const join = () => request({
    url: `/member/join`, method: 'get',
  }).then(check).then(({data}) => {
    isMember.value = true
    expireAt.value = data
  })

  return {
    isMember, check, join, expireAt, load,
  }
})
