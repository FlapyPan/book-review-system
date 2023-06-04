import { createRouter, createWebHashHistory } from 'vue-router'
import { useUserStore } from '../stores/user.js'

export const routerMap = [
  {
    path: '/',
    name: 'index',
    component: () => import('../views/IndexView.vue'),
    children: [
      {
        path: '',
        name: 'index',
        component: () => import('../views/IndexLatest.vue'),
      },
      {
        path: 'search/:search',
        name: 'search',
        component: () => import('../views/SearchBook.vue'),
      },
      {
        path: 'me',
        name: 'self-info',
        component: () => import('../views/SelfInfo.vue'),
      },
      {
        path: 'book/:id',
        name: 'book',
        component: () => import('../views/BookPage.vue'),
      },
    ],
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue'),
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/RegisterView.vue'),
  },
  {
    path: '/forget',
    name: 'forget',
    component: () => import('../views/ForgetView.vue'),
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/admin/AdminIndex.vue'),
    children: [
      {
        path: 'book',
        name: 'admin-book',
        component: () => import('../views/admin/BookManager.vue'),
      },
      {
        path: 'user',
        name: 'admin-user',
        component: () => import('../views/admin/UserManager.vue'),
      },
      {
        path: 'comment',
        name: 'admin-comment',
        component: () => import('../views/admin/CommentManager.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    ...routerMap,
    {path: '/admin/:catchAll(.*)', redirect: '/admin'},
    {path: '/:catchAll(.*)', redirect: '/'},
  ],
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.path.startsWith('/admin') && !userStore.isAdmin) {
    next('/')
    return
  }
  if (to.path.startsWith('/me') && !userStore.isLogin) {
    next('/login')
    return
  }
  next()
})

export default router
