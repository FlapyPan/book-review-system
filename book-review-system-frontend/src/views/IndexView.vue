<script setup>
import { RouterView } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import { ref } from 'vue'
import router from '../router/index.js'
import { useUserStore } from '../stores/user.js'

const userStore = useUserStore()

const searchWords = ref('')
const submitSearch = () => {
  router.push(`/search/${searchWords.value}`)
}

</script>

<template>
  <main>
    <header class="no-select">
      <div class="logo" @click="router.push('/')">
        <h1>书评</h1>
      </div>
      <div class="header-search">
        <el-input class="header-search-input" v-model="searchWords" placeholder="书名、作者、ISBN"
                  :prefix-icon="Search" @keydown.enter="submitSearch()" />
        <el-button class="header-search-btn" @click="submitSearch()">搜索</el-button>
      </div>
      <el-dropdown v-if="userStore.isLogin" trigger="click">
        <div class="header-avatar">
          <el-avatar :src="userStore.user.avatar"></el-avatar>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="router.push('/me')">个人中心</el-dropdown-item>
            <el-dropdown-item v-if="userStore.isAdmin" @click="router.push('/admin')">管理后台</el-dropdown-item>
            <el-dropdown-item @click="userStore.logout()" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button v-else type="primary" text @click="router.push('/login')">登录/注册</el-button>
    </header>
    <div class="content">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </main>

</template>

<style scoped>
header {
  position: sticky;
  overflow: hidden;
  width: 100vw;
  height: 70px;
  background-color: rgba(241, 240, 237, 0.95);
  padding: 0 10%;
  z-index: 2000;
  top: 0;
  display: flex;
  align-items: center;
  border-bottom: 1px solid var(--el-color-primary-light-9);
}

header .logo {
  cursor: pointer;
}

header .logo h1 {
  font-size: 36px;
  color: var(--color-primary);
  margin: 0 40px 0 0;
}

header .header-search {
  flex: 1;
  display: flex;
  align-items: center;
}

header .header-search-input {
  max-width: 300px;
  margin-right: 6px;
}

header .header-avatar {
  height: 40px;
  width: 40px;
  cursor: pointer;
}

.content {
  padding: 20px 10%;
  min-width: 760px;
  overflow-x: hidden;
}

@media screen and (max-width: 750px) {
  header .header-search-btn {
    display: none;
  }
}
</style>
