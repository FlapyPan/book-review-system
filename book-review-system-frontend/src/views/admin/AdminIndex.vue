<script setup>
import { useUserStore } from '../../stores/user.js'
import router from '../../router/index.js'

const props = defineProps({})

const userStore = useUserStore()

</script>

<template>
  <el-container class="container">
    <el-header class="header">
      <el-button text type="primary">管理后台</el-button>
      <div style="flex: 1"></div>
      <el-button type="danger" @click="router.push('/')">返回主页</el-button>
    </el-header>
    <el-container>
      <el-aside width="200px" id="admin-aside">
        <el-menu router>
          <el-menu-item index="/admin">
            <span>主页</span>
          </el-menu-item>
          <el-menu-item index="/admin/user">
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/book">
            <span>书籍管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/comment">
            <span>书评审核</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.container {
  min-height: 100vh;
}

.header {
  --el-header-padding: 0;
  position: sticky;
  top: 0;
  background-color: #545c64;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.main {
  position: relative;
  background-color: #f1f0ed;
}
</style>

<style>
#admin-aside .el-menu {
  border: none;
}
</style>
