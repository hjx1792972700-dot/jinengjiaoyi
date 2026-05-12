<template>
  <div class="portal-page">
    <div class="bg-grid"></div>
    <div class="particles">
      <span v-for="i in 30" :key="i" class="particle"
        :style="{
          left: particlePos[i-1].x + '%',
          top: particlePos[i-1].y + '%',
          animationDelay: (i * 0.3) + 's',
          animationDuration: (3 + i % 4) + 's',
          width: particlePos[i-1].s + 'px',
          height: particlePos[i-1].s + 'px',
        }">
      </span>
    </div>

    <div class="portal-content">
      <div class="portal-header">
        <div class="logo-icon">
          <svg viewBox="0 0 100 100" class="logo-svg">
            <defs>
              <linearGradient id="grad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" style="stop-color:#00d4ff;stop-opacity:1" />
                <stop offset="100%" style="stop-color:#7c3aed;stop-opacity:1" />
              </linearGradient>
            </defs>
            <circle cx="50" cy="50" r="45" fill="none" stroke="url(#grad)" stroke-width="2" opacity="0.3"/>
            <circle cx="50" cy="50" r="35" fill="none" stroke="url(#grad)" stroke-width="1.5" opacity="0.5"/>
            <path d="M30 50 L45 35 L55 45 L70 30" fill="none" stroke="url(#grad)" stroke-width="3" stroke-linecap="round"/>
            <path d="M30 60 L45 55 L55 65 L70 50" fill="none" stroke="url(#grad)" stroke-width="2" stroke-linecap="round" opacity="0.6"/>
            <circle cx="30" cy="50" r="3" fill="#00d4ff"/>
            <circle cx="45" cy="35" r="3" fill="#00d4ff"/>
            <circle cx="55" cy="45" r="3" fill="#7c3aed"/>
            <circle cx="70" cy="30" r="3" fill="#7c3aed"/>
          </svg>
        </div>
        <h1 class="system-title">
          <span class="title-text">基于协同过滤的技能交易系统</span>
        </h1>
        <p class="system-subtitle">Collaborative Filtering Skill Trading Platform</p>
      </div>

      <div class="mode-switcher">
        <button :class="['mode-btn', {active: mode==='user'}]" @click="switchMode('user')">
          <i class="el-icon-user"></i>
          <span>用户端</span>
        </button>
        <button :class="['mode-btn', {active: mode==='admin'}]" @click="switchMode('admin')">
          <i class="el-icon-setting"></i>
          <span>管理端</span>
        </button>
        <div class="mode-indicator" :class="mode"></div>
      </div>

      <transition name="panel-slide" mode="out-in">
        <div class="login-panel" v-if="mode==='user'" key="user">
          <div class="panel-card glass-card">
            <h2 class="panel-title">
              <i class="el-icon-user-solid"></i>
              用户登录
            </h2>
            <div class="form-group">
              <div class="cyber-input">
                <i class="el-icon-user"></i>
                <input v-model="userForm.username" placeholder="请输入用户名" autocomplete="off" @keyup.enter="handleUserLogin"/>
              </div>
            </div>
            <div class="form-group">
              <div class="cyber-input">
                <i class="el-icon-lock"></i>
                <input v-model="userForm.password" :type="showUserPwd?'text':'password'" placeholder="请输入密码" autocomplete="off" @keyup.enter="handleUserLogin"/>
                <el-icon class="pwd-toggle" @click="showUserPwd=!showUserPwd"><Hide v-if="showUserPwd" /><View v-else /></el-icon>
              </div>
            </div>
            <button class="cyber-btn user-btn" @click="handleUserLogin" :disabled="userLoading">
              <span v-if="!userLoading">进 入 系 统</span>
              <span v-else><el-icon class="is-loading"><Loading /></el-icon> 登录中...</span>
            </button>
            <div class="panel-footer">
              <span class="footer-hint">没有账号？</span>
              <router-link class="footer-link" to="/register?role=yonghu&pageFlag=register">立即注册</router-link>
            </div>
          </div>
          <div class="panel-features">
            <div class="feature-card" v-for="(f,i) in userFeatures" :key="i">
              <el-icon :size="22"><component :is="f.icon" /></el-icon>
              <span>{{ f.text }}</span>
            </div>
          </div>
        </div>

        <div class="login-panel" v-else key="admin">
          <div class="panel-card glass-card">
            <h2 class="panel-title admin-title">
              <i class="el-icon-monitor"></i>
              管理员登录
            </h2>
            <div class="form-group">
              <div class="cyber-input admin-input">
                <i class="el-icon-user"></i>
                <input v-model="adminForm.username" placeholder="请输入管理员账号" autocomplete="off" @keyup.enter="handleAdminLogin"/>
              </div>
            </div>
            <div class="form-group">
              <div class="cyber-input admin-input">
                <i class="el-icon-lock"></i>
                <input v-model="adminForm.password" :type="showAdminPwd?'text':'password'" placeholder="请输入密码" autocomplete="off" @keyup.enter="handleAdminLogin"/>
                <el-icon class="pwd-toggle" @click="showAdminPwd=!showAdminPwd"><Hide v-if="showAdminPwd" /><View v-else /></el-icon>
              </div>
            </div>
            <button class="cyber-btn admin-btn" @click="handleAdminLogin" :disabled="adminLoading">
              <span v-if="!adminLoading">进 入 后 台</span>
              <span v-else><el-icon class="is-loading"><Loading /></el-icon> 登录中...</span>
            </button>
          </div>
          <div class="panel-features">
            <div class="feature-card admin-feature" v-for="(f,i) in adminFeatures" :key="i">
              <el-icon :size="22"><component :is="f.icon" /></el-icon>
              <span>{{ f.text }}</span>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <div class="portal-footer">
      <span>&copy; 2026 技能交易系统 &mdash; All Rights Reserved</span>
    </div>

    <slider-captcha :visible="captchaVisible" @success="onCaptchaSuccess" @close="captchaVisible=false"></slider-captcha>
  </div>
</template>

<script>
import SliderCaptcha from '@/components/SliderCaptcha.vue'
export default {
  name: 'Portal',
  components: {
    SliderCaptcha,
  },
  data() {
    return {
      mode: 'user',
      userForm: { username: '', password: '' },
      adminForm: { username: '', password: '' },
      showUserPwd: false,
      showAdminPwd: false,
      userLoading: false,
      adminLoading: false,
      captchaVisible: false,
      pendingLogin: null,
      pendingToken: null,
      particlePos: Array.from({length:30}, () => ({
        x: Math.random()*100,
        y: Math.random()*100,
        s: 2 + Math.random()*3
      })),
      userFeatures: [
        { icon: 'Search', text: '智能推荐' },
        { icon: 'Sort', text: '技能交换' },
        { icon: 'Star', text: '收藏评价' },
        { icon: 'ChatDotRound', text: '社区交流' },
      ],
      adminFeatures: [
        { icon: 'DataAnalysis', text: '数据统计' },
        { icon: 'UserFilled', text: '用户管理' },
        { icon: 'CircleCheck', text: '业务管理' },
        { icon: 'Operation', text: '系统配置' },
      ],
    }
  },
  methods: {
    switchMode(m) {
      this.mode = m
    },
    handleUserLogin() {
      if (!this.userForm.username) { this.$message.error('请输入用户名'); return }
      if (!this.userForm.password) { this.$message.error('请输入密码'); return }
      this.userLoading = true
      this.$http.get('yonghu/login', {
        params: { username: this.userForm.username, password: this.userForm.password }
      }).then(res => {
        this.userLoading = false
        if (res.data.code === 0) {
          this.pendingToken = res.data.token
          this.pendingLogin = 'user'
          this.captchaVisible = true
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(() => { this.userLoading = false })
    },
    handleAdminLogin() {
      if (!this.adminForm.username) { this.$message.error('请输入管理员账号'); return }
      if (!this.adminForm.password) { this.$message.error('请输入密码'); return }
      this.adminLoading = true
      this.$http.get('users/login', {
        params: { username: this.adminForm.username, password: this.adminForm.password }
      }).then(res => {
        this.adminLoading = false
        if (res.data.code === 0) {
          this.pendingToken = res.data.token
          this.pendingLogin = 'admin'
          this.captchaVisible = true
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(() => { this.adminLoading = false })
    },
    onCaptchaSuccess() {
      this.captchaVisible = false
      if (this.pendingLogin === 'user') this.doUserLogin()
      else if (this.pendingLogin === 'admin') this.doAdminLogin()
    },
    doUserLogin() {
      localStorage.setItem('frontToken', this.pendingToken)
      localStorage.setItem('UserTableName', 'yonghu')
      localStorage.setItem('username', this.userForm.username)
      localStorage.setItem('frontSessionTable', 'yonghu')
      localStorage.setItem('frontRole', '用户')
      localStorage.setItem('keyPath', 0)
      this.$message({ message: '登录成功，正在进入系统...', type: 'success', duration: 1500 })
      setTimeout(() => { this.$router.push('/main/home') }, 500)
    },
    doAdminLogin() {
      this.$message({ message: '验证成功，正在跳转管理后台...', type: 'success', duration: 1500 })
      setTimeout(() => {
        localStorage.setItem('Token', JSON.stringify(this.pendingToken))
        localStorage.setItem('role', JSON.stringify('管理员'))
        localStorage.setItem('sessionTable', JSON.stringify('users'))
        localStorage.setItem('adminName', JSON.stringify(this.adminForm.username))
        const adminBase = this.$config.baseUrl + 'admin/index.html'
        window.location.href = adminBase + '#/'
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$dark: #0a0e1a;
$card-bg: rgba(15,23,42,0.7);
$border-glow: rgba(0,212,255,0.15);

.portal-page {
  min-height: 100vh;
  background: $dark;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(0,212,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0,212,255,0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  animation: grid-drift 20s linear infinite;
}

@keyframes grid-drift {
  from { transform: translate(0,0); }
  to   { transform: translate(60px,60px); }
}

.particles {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.particle {
  position: absolute;
  background: $cyan;
  border-radius: 50%;
  opacity: 0;
  animation: float-particle linear infinite;
  box-shadow: 0 0 6px $cyan;
}

@keyframes float-particle {
  0%   { opacity: 0; transform: translateY(0) scale(0.5); }
  20%  { opacity: 0.8; }
  80%  { opacity: 0.4; }
  100% { opacity: 0; transform: translateY(-120px) scale(1.2); }
}

.portal-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 580px;
  padding: 50px 24px 30px;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.portal-header {
  text-align: center;
  margin-bottom: 36px;

  .logo-icon {
    width: 72px;
    height: 72px;
    margin: 0 auto 20px;
    animation: logo-spin 20s linear infinite;
  }

  .logo-svg {
    width: 100%;
    height: 100%;
    filter: drop-shadow(0 0 12px rgba(0,212,255,0.3));
  }

  .system-title {
    font-size: 28px;
    font-weight: 700;
    letter-spacing: 4px;
    margin: 0 0 10px;

    .title-text {
      background: linear-gradient(90deg, #fff 0%, $cyan 50%, $purple 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }

  .system-subtitle {
    font-size: 13px;
    color: rgba(255,255,255,0.3);
    letter-spacing: 3px;
    text-transform: uppercase;
    margin: 0;
  }
}

@keyframes logo-spin {
  from { transform: rotate(0deg); }
  to   { transform: rotate(360deg); }
}

.mode-switcher {
  display: flex;
  align-items: center;
  background: rgba(255,255,255,0.04);
  border-radius: 14px;
  padding: 4px;
  margin-bottom: 30px;
  position: relative;
  border: 1px solid rgba(255,255,255,0.06);

  .mode-btn {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 12px 32px;
    border: none;
    background: transparent;
    color: rgba(255,255,255,0.4);
    font-size: 15px;
    font-weight: 500;
    cursor: pointer;
    border-radius: 10px;
    position: relative;
    z-index: 2;
    transition: color 0.3s;

    .el-icon { font-size: 16px; }

    &.active {
      color: #fff;
    }
  }

  .mode-indicator {
    position: absolute;
    top: 4px;
    width: calc(50% - 4px);
    height: calc(100% - 8px);
    border-radius: 10px;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 1;

    &.user {
      left: 4px;
      background: linear-gradient(135deg, rgba(0,212,255,0.2), rgba(0,212,255,0.08));
      box-shadow: 0 0 20px rgba(0,212,255,0.1), inset 0 0 20px rgba(0,212,255,0.05);
      border: 1px solid rgba(0,212,255,0.2);
    }

    &.admin {
      left: calc(50%);
      background: linear-gradient(135deg, rgba(124,58,237,0.2), rgba(124,58,237,0.08));
      box-shadow: 0 0 20px rgba(124,58,237,0.1), inset 0 0 20px rgba(124,58,237,0.05);
      border: 1px solid rgba(124,58,237,0.2);
    }
  }
}

.login-panel {
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.glass-card {
  background: $card-bg;
  backdrop-filter: blur(20px);
  border: 1px solid $border-glow;
  border-radius: 20px;
  padding: 36px 32px 28px;
}

.panel-title {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 28px;
  display: flex;
  align-items: center;
  gap: 10px;

  .el-icon {
    font-size: 22px;
    color: $cyan;
  }

  &.admin-title .el-icon {
    color: $purple;
  }
}

.form-group {
  margin-bottom: 18px;
}

.cyber-input {
  display: flex;
  align-items: center;
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(0,212,255,0.12);
  border-radius: 12px;
  padding: 0 16px;
  height: 50px;
  transition: all 0.3s;

  &:focus-within {
    border-color: rgba(0,212,255,0.4);
    background: rgba(0,212,255,0.04);
    box-shadow: 0 0 20px rgba(0,212,255,0.08);
  }

  > .el-icon:first-child {
    font-size: 17px;
    color: rgba(0,212,255,0.5);
    margin-right: 12px;
  }

  input {
    flex: 1;
    border: none;
    outline: none;
    background: transparent;
    font-size: 14px;
    color: #e2e8f0;
    height: 100%;

    &::placeholder { color: rgba(255,255,255,0.25); }
  }

  .pwd-toggle {
    font-size: 17px;
    color: rgba(255,255,255,0.3);
    cursor: pointer;
    transition: color 0.2s;

    &:hover { color: $cyan; }
  }

  &.admin-input {
    border-color: rgba(124,58,237,0.12);

    &:focus-within {
      border-color: rgba(124,58,237,0.4);
      background: rgba(124,58,237,0.04);
      box-shadow: 0 0 20px rgba(124,58,237,0.08);
    }

    > .el-icon:first-child { color: rgba(124,58,237,0.5); }
    .pwd-toggle:hover { color: $purple; }
  }
}

.cyber-btn {
  width: 100%;
  height: 50px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 6px;
  cursor: pointer;
  margin-top: 6px;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
  color: #fff;

  &::before {
    content: '';
    position: absolute;
    inset: 0;
    border-radius: 12px;
    opacity: 0;
    transition: opacity 0.3s;
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.user-btn {
  background: linear-gradient(135deg, rgba(0,212,255,0.8), rgba(0,150,200,0.9));
  box-shadow: 0 4px 24px rgba(0,212,255,0.2);

  &:hover:not(:disabled) {
    box-shadow: 0 6px 32px rgba(0,212,255,0.35);
    transform: translateY(-1px);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }
}

.admin-btn {
  background: linear-gradient(135deg, rgba(124,58,237,0.8), rgba(100,40,200,0.9));
  box-shadow: 0 4px 24px rgba(124,58,237,0.2);

  &:hover:not(:disabled) {
    box-shadow: 0 6px 32px rgba(124,58,237,0.35);
    transform: translateY(-1px);
  }

  &:active:not(:disabled) {
    transform: translateY(0);
  }
}

.panel-footer {
  text-align: center;
  margin-top: 20px;

  .footer-hint {
    font-size: 13px;
    color: rgba(255,255,255,0.3);
  }

  .footer-link {
    font-size: 13px;
    color: $cyan;
    text-decoration: none;
    font-weight: 500;
    margin-left: 4px;
    transition: color 0.2s;

    &:hover { color: lighten($cyan, 10%); }
  }
}

.panel-features {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.feature-card {
  background: $card-bg;
  backdrop-filter: blur(12px);
  border: 1px solid $border-glow;
  border-radius: 14px;
  padding: 18px 12px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  transition: all 0.3s;

  .el-icon {
    font-size: 22px;
    color: $cyan;
  }

  span {
    font-size: 12px;
    color: rgba(255,255,255,0.6);
    letter-spacing: 1px;
  }

  &:hover {
    border-color: rgba(0,212,255,0.3);
    transform: translateY(-3px);
    box-shadow: 0 8px 24px rgba(0,212,255,0.08);
  }

  &.admin-feature {
    border-color: rgba(124,58,237,0.1);

    .el-icon { color: $purple; }

    &:hover {
      border-color: rgba(124,58,237,0.3);
      box-shadow: 0 8px 24px rgba(124,58,237,0.08);
    }
  }
}

.portal-footer {
  position: relative;
  z-index: 1;
  padding: 20px;
  text-align: center;

  span {
    font-size: 12px;
    color: rgba(255,255,255,0.15);
    letter-spacing: 1px;
  }
}

.panel-slide-enter-active,
.panel-slide-leave-active {
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

.panel-slide-enter {
  opacity: 0;
  transform: translateX(30px);
}

.panel-slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}
</style>
