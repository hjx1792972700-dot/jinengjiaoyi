<template>
  <div class="register-page">
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

    <div class="register-content">
      <div class="register-header">
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
          <span class="title-text">用户注册</span>
        </h1>
        <p class="system-subtitle">Create Your Account</p>
      </div>

      <div class="register-card glass-card">
        <h2 class="card-title">
          <el-icon><User /></el-icon>
          填写注册信息
        </h2>

        <el-form ref="registerForm" :model="registerForm" :rules="rules" @submit.prevent>
          <div class="form-group">
            <label class="field-label"><span class="required">*</span> 用户账号</label>
            <div class="cyber-input">
              <el-icon><User /></el-icon>
              <input v-model="registerForm.yonghuzhanghao" placeholder="请输入用户账号" autocomplete="off" />
            </div>
          </div>

          <div class="form-group">
            <label class="field-label"><span class="required">*</span> 密码</label>
            <div class="cyber-input">
              <el-icon><Lock /></el-icon>
              <input v-model="registerForm.mima" :type="showPwd?'text':'password'" placeholder="请输入密码" autocomplete="off" />
              <el-icon class="pwd-toggle" @click="showPwd=!showPwd"><Hide v-if="showPwd" /><View v-else /></el-icon>
            </div>
          </div>

          <div class="form-group">
            <label class="field-label"><span class="required">*</span> 确认密码</label>
            <div class="cyber-input">
              <el-icon><Lock /></el-icon>
              <input v-model="registerForm.mima2" :type="showPwd2?'text':'password'" placeholder="请再次输入密码" autocomplete="off" />
              <el-icon class="pwd-toggle" @click="showPwd2=!showPwd2"><Hide v-if="showPwd2" /><View v-else /></el-icon>
            </div>
          </div>

          <div class="form-group">
            <label class="field-label">用户姓名</label>
            <div class="cyber-input">
              <el-icon><Postcard /></el-icon>
              <input v-model="registerForm.yonghuxingming" placeholder="选填" autocomplete="off" />
            </div>
          </div>

          <div class="form-group">
            <label class="field-label">手机号</label>
            <div class="cyber-input">
              <el-icon><Iphone /></el-icon>
              <input v-model="registerForm.shoujihao" placeholder="选填" autocomplete="off" />
            </div>
          </div>

          <button class="cyber-btn register-btn" @click="submitForm" :disabled="loading">
            <span v-if="!loading">立 即 注 册</span>
            <span v-else><el-icon class="is-loading"><Loading /></el-icon> 注册中...</span>
          </button>
        </el-form>

        <div class="card-footer">
          <span class="footer-hint">已有账号？</span>
          <router-link class="footer-link" to="/index">返回登录</router-link>
        </div>
      </div>
    </div>

    <div class="register-footer">
      <span>&copy; 2026 技能交易系统 &mdash; All Rights Reserved</span>
    </div>
  </div>
</template>

<script>
import { User, Lock, View, Hide, Postcard, Iphone, Loading } from '@element-plus/icons-vue'

export default {
  name: 'Register',
  components: { User, Lock, View, Hide, Postcard, Iphone, Loading },
  data() {
    return {
      tableName: '',
      registerForm: {
        yonghuzhanghao: '',
        yonghuxingming: '',
        mima: '',
        mima2: '',
        shoujihao: '',
      },
      rules: {},
      showPwd: false,
      showPwd2: false,
      loading: false,
      particlePos: Array.from({length: 30}, () => ({
        x: Math.random() * 100,
        y: Math.random() * 100,
        s: 2 + Math.random() * 3
      })),
    }
  },
  created() {
    this.tableName = this.$route.query.role || 'yonghu'
  },
  methods: {
    submitForm() {
      if (!this.registerForm.yonghuzhanghao) {
        this.$message.error('请输入用户账号')
        return
      }
      if (!this.registerForm.mima) {
        this.$message.error('请输入密码')
        return
      }
      if (this.registerForm.mima !== this.registerForm.mima2) {
        this.$message.error('两次密码输入不一致')
        return
      }

      this.loading = true
      var url = this.tableName + '/register'
      var submitData = {
        yonghuzhanghao: this.registerForm.yonghuzhanghao,
        yonghuxingming: this.registerForm.yonghuxingming,
        mima: this.registerForm.mima,
        shoujihao: this.registerForm.shoujihao,
        sfsh: '是',
      }
      this.$http.post(url, submitData).then(res => {
        this.loading = false
        if (res.data.code === 0) {
          this.$message({
            message: '注册成功，即将跳转登录页',
            type: 'success',
            duration: 1500,
            onClose: () => {
              this.$router.push('/index')
            }
          })
        } else {
          this.$message.error(res.data.msg)
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('网络错误，请稍后重试')
      })
    },
  }
}
</script>

<style lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$dark: #0a0e1a;
$card-bg: rgba(15,23,42,0.7);
$border-glow: rgba(0,212,255,0.15);

.register-page {
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

.register-content {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 480px;
  padding: 40px 24px 30px;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;

  .logo-icon {
    width: 64px;
    height: 64px;
    margin: 0 auto 16px;
    animation: logo-spin 20s linear infinite;
  }

  .logo-svg {
    width: 100%;
    height: 100%;
    filter: drop-shadow(0 0 12px rgba(0,212,255,0.3));
  }

  .system-title {
    font-size: 26px;
    font-weight: 700;
    letter-spacing: 4px;
    margin: 0 0 8px;

    .title-text {
      background: linear-gradient(90deg, #fff 0%, $cyan 50%, $purple 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }
  }

  .system-subtitle {
    font-size: 12px;
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

.glass-card {
  background: $card-bg;
  backdrop-filter: blur(20px);
  border: 1px solid $border-glow;
  border-radius: 20px;
  padding: 32px 28px 24px;
  width: 100%;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 24px;
  display: flex;
  align-items: center;
  gap: 10px;

  .el-icon {
    font-size: 20px;
    color: $cyan;
  }
}

.form-group {
  margin-bottom: 16px;
}

.field-label {
  display: block;
  font-size: 13px;
  color: rgba(255,255,255,0.5);
  margin-bottom: 6px;
  padding-left: 2px;

  .required {
    color: #ff4d6a;
    margin-right: 2px;
  }
}

.cyber-input {
  display: flex;
  align-items: center;
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(0,212,255,0.12);
  border-radius: 12px;
  padding: 0 16px;
  height: 48px;
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
    flex-shrink: 0;
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
    flex-shrink: 0;

    &:hover { color: $cyan; }
  }
}

.cyber-btn {
  width: 100%;
  height: 48px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 6px;
  cursor: pointer;
  margin-top: 8px;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
  color: #fff;

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

.register-btn {
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

.card-footer {
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

    &:hover { color: #33e0ff; }
  }
}

.register-footer {
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
</style>
