<template>
  <transition name="captcha-fade">
    <div class="captcha-overlay" v-if="visible" @click.self="close">
      <div class="captcha-modal">
        <div class="captcha-header">
          <div class="header-left">
            <el-icon class="captcha-shield"><Lock /></el-icon>
            <span class="captcha-title">安全验证</span>
          </div>
          <el-icon class="captcha-close-btn" @click="close"><Close /></el-icon>
        </div>
        <div class="captcha-body">
          <p class="captcha-desc">拖动下方滑块，将拼图移至正确位置</p>
          <div class="canvas-container">
            <canvas ref="canvasBg" :width="CW" :height="CH"></canvas>
            <canvas ref="canvasBlock" class="canvas-block"
              :width="blockW" :height="CH"
              :style="{left: blockX + 'px'}">
            </canvas>
            <transition name="captcha-fade">
              <div v-if="statusTip" class="status-tip" :class="statusClass">
                <el-icon><component :is="statusClass === 'success' ? 'CircleCheck' : 'CircleClose'" /></el-icon>
                {{ statusTip }}
              </div>
            </transition>
          </div>
          <div class="slider-box">
            <div class="slider-track">
              <div class="slider-fill" :style="{width: sliderX + 24 + 'px'}" :class="statusClass"></div>
              <div class="slider-thumb" :class="[statusClass, {dragging: dragging}]"
                :style="{left: sliderX + 'px'}"
                @mousedown.prevent="onDragStart"
                @touchstart.prevent="onDragStart">
                <el-icon><component :is="thumbIconComp" /></el-icon>
              </div>
            </div>
            <span class="slider-hint" v-show="sliderX < 3 && !statusTip">
              <el-icon class="hint-arrow"><DArrowRight /></el-icon>
              向右拖动滑块完成验证
            </span>
          </div>
          <div class="captcha-footer">
            <span class="refresh-link" @click="reset">
              <el-icon><RefreshRight /></el-icon> 换一张
            </span>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import {
  Lock,
  Close,
  CircleCheck,
  CircleClose,
  DArrowRight,
  RefreshRight,
  Check,
} from '@element-plus/icons-vue'

const PI = Math.PI
const CW = 340
const CH = 190
const PIECE = 46
const RADIUS = 10

function createPuzzlePath(ctx, x, y) {
  ctx.beginPath()
  ctx.moveTo(x, y)
  ctx.arc(x + PIECE / 2, y - RADIUS + 2, RADIUS, 0.72 * PI, 2.26 * PI)
  ctx.lineTo(x + PIECE, y)
  ctx.arc(x + PIECE + RADIUS - 2, y + PIECE / 2, RADIUS, 1.21 * PI, 2.78 * PI)
  ctx.lineTo(x + PIECE, y + PIECE)
  ctx.lineTo(x, y + PIECE)
  ctx.arc(x + RADIUS - 2, y + PIECE / 2, RADIUS + 0.4, 2.76 * PI, 1.24 * PI, true)
  ctx.lineTo(x, y)
}

function randInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min
}

import captcha1 from '@/assets/captcha/captcha1.jpg'
import captcha2 from '@/assets/captcha/captcha2.jpg'
import captcha3 from '@/assets/captcha/captcha3.jpg'
import captcha4 from '@/assets/captcha/captcha4.jpg'
import captcha5 from '@/assets/captcha/captcha5.jpg'

const captchaImages = [captcha1, captcha2, captcha3, captcha4, captcha5]

export default {
  name: 'SliderCaptcha',
  components: {
    Lock,
    Close,
    CircleCheck,
    CircleClose,
    DArrowRight,
    RefreshRight,
    Check,
  },
  props: {
    visible: { type: Boolean, default: false }
  },
  data() {
    return {
      CW, CH,
      blockW: PIECE + RADIUS * 2 + 8,
      blockX: 0,
      sliderX: 0,
      targetX: 0,
      statusTip: '',
      statusClass: '',
      dragging: false,
      startX: 0,
      startSliderX: 0,
      imgIdx: -1
    }
  },
  computed: {
    thumbIconComp() {
      if (this.statusClass === 'success') return 'Check'
      if (this.statusClass === 'fail') return 'Close'
      return 'DArrowRight'
    }
  },
  watch: {
    visible(v) {
      if (v) this.$nextTick(() => this.reset())
    }
  },
  methods: {
    reset() {
      this.sliderX = 0
      this.blockX = 0
      this.statusTip = ''
      this.statusClass = ''
      this.draw()
    },
    pickNextImage() {
      let idx
      do {
        idx = randInt(0, captchaImages.length - 1)
      } while (idx === this.imgIdx && captchaImages.length > 1)
      this.imgIdx = idx
      return captchaImages[idx]
    },
    draw() {
      const bgCanvas = this.$refs.canvasBg
      const blkCanvas = this.$refs.canvasBlock
      if (!bgCanvas || !blkCanvas) return

      bgCanvas.width = CW
      blkCanvas.width = this.blockW

      const bgCtx = bgCanvas.getContext('2d')
      const blkCtx = blkCanvas.getContext('2d')

      const x = randInt(PIECE + RADIUS + 40, CW - PIECE - RADIUS - 20)
      const y = randInt(RADIUS + 20, CH - PIECE - RADIUS - 20)
      this.targetX = x

      const img = new Image()
      img.src = this.pickNextImage()
      img.onload = () => {
        bgCtx.drawImage(img, 0, 0, CW, CH)

        createPuzzlePath(bgCtx, x, y)
        bgCtx.fillStyle = 'rgba(0,0,0,0.45)'
        bgCtx.fill()
        bgCtx.lineWidth = 1.5
        bgCtx.strokeStyle = 'rgba(255,255,255,0.5)'
        bgCtx.stroke()

        blkCtx.save()
        blkCtx.shadowOffsetX = 0
        blkCtx.shadowOffsetY = 0
        blkCtx.shadowBlur = 10
        blkCtx.shadowColor = 'rgba(0,0,0,0.7)'
        createPuzzlePath(blkCtx, 4, y)
        blkCtx.lineWidth = 2
        blkCtx.strokeStyle = 'rgba(255,255,255,0.9)'
        blkCtx.stroke()
        blkCtx.clip()
        blkCtx.shadowBlur = 0
        blkCtx.drawImage(img, -(x - 4), 0, CW, CH)
        blkCtx.restore()

        blkCtx.save()
        createPuzzlePath(blkCtx, 4, y)
        blkCtx.lineWidth = 1.5
        blkCtx.strokeStyle = 'rgba(255,255,255,0.6)'
        blkCtx.stroke()
        blkCtx.restore()
      }
    },
    onDragStart(e) {
      if (this.statusClass === 'success') return
      this.dragging = true
      this.startX = e.clientX || e.touches[0].clientX
      this.startSliderX = this.sliderX
      document.addEventListener('mousemove', this.onDragMove)
      document.addEventListener('mouseup', this.onDragEnd)
      document.addEventListener('touchmove', this.onDragMove, { passive: false })
      document.addEventListener('touchend', this.onDragEnd)
    },
    onDragMove(e) {
      if (!this.dragging) return
      e.preventDefault && e.preventDefault()
      const cx = e.clientX || (e.touches && e.touches[0].clientX) || 0
      let dx = cx - this.startX + this.startSliderX
      dx = Math.max(0, Math.min(dx, CW - this.blockW))
      this.sliderX = dx
      this.blockX = dx
    },
    onDragEnd() {
      if (!this.dragging) return
      this.dragging = false
      document.removeEventListener('mousemove', this.onDragMove)
      document.removeEventListener('mouseup', this.onDragEnd)
      document.removeEventListener('touchmove', this.onDragMove)
      document.removeEventListener('touchend', this.onDragEnd)

      if (Math.abs(this.sliderX - this.targetX) < 7) {
        this.statusTip = '验证成功!'
        this.statusClass = 'success'
        setTimeout(() => this.$emit('success'), 600)
      } else {
        this.statusTip = '验证失败，请重试'
        this.statusClass = 'fail'
        setTimeout(() => this.reset(), 1000)
      }
    },
    close() {
      this.$emit('close')
    }
  }
}
</script>

<style lang="scss" scoped>
$gold: #0ea5e9;

.captcha-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  backdrop-filter: blur(4px);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
}

.captcha-modal {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.2);
  width: 400px;
  overflow: hidden;
  animation: captcha-pop 0.3s cubic-bezier(0.34,1.56,0.64,1);
}

@keyframes captcha-pop {
  from { transform: scale(0.85) translateY(20px); opacity: 0; }
  to   { transform: scale(1) translateY(0); opacity: 1; }
}

.captcha-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid #f5f0e8;

  .header-left {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .captcha-shield {
    font-size: 18px;
    color: $gold;
  }

  .captcha-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }

  .captcha-close-btn {
    font-size: 18px;
    color: #bbb;
    cursor: pointer;
    padding: 4px;
    border-radius: 6px;
    transition: all 0.2s;

    &:hover { color: #666; background: #f5f5f5; }
  }
}

.captcha-body {
  padding: 16px 20px 14px;
}

.captcha-desc {
  font-size: 13px;
  color: #999;
  margin: 0 0 12px;
  text-align: center;
}

.canvas-container {
  position: relative;
  width: 340px;
  height: 190px;
  border-radius: 12px;
  overflow: hidden;
  margin: 0 auto;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  border: 1px solid #f0ede6;

  canvas { display: block; }

  .canvas-block {
    position: absolute;
    top: 0;
    left: 0;
  }

  .status-tip {
    position: absolute;
    bottom: 0; left: 0; right: 0;
    padding: 8px 0;
    text-align: center;
    font-size: 13px;
    font-weight: 600;
    letter-spacing: 1px;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 6px;

    .el-icon { font-size: 16px; }

    &.success {
      background: rgba(103,194,58,0.9);
      color: #fff;
    }

    &.fail {
      background: rgba(245,108,108,0.9);
      color: #fff;
    }
  }
}

.slider-box {
  position: relative;
  margin-top: 16px;
  height: 48px;

  .slider-track {
    position: relative;
    width: 340px;
    height: 48px;
    border-radius: 24px;
    background: #f9f5ed;
    border: 1.5px solid #e8e0cf;
    overflow: hidden;
    margin: 0 auto;
  }

  .slider-fill {
    position: absolute;
    top: 0; left: 0; bottom: 0;
    border-radius: 24px;
    background: linear-gradient(90deg, rgba(149,119,65,0.15), rgba(149,119,65,0.25));
    transition: background 0.2s;

    &.success { background: linear-gradient(90deg, rgba(103,194,58,0.2), rgba(103,194,58,0.35)); }
    &.fail    { background: linear-gradient(90deg, rgba(245,108,108,0.2), rgba(245,108,108,0.35)); }
  }

  .slider-thumb {
    position: absolute;
    top: 0;
    width: 48px;
    height: 48px;
    border-radius: 50%;
    background: #fff;
    box-shadow: 0 2px 8px rgba(0,0,0,0.12);
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: grab;
    z-index: 2;
    border: 2px solid $gold;
    transition: border-color 0.2s, background 0.2s, box-shadow 0.2s, transform 0.15s;
    user-select: none;

    .el-icon {
      font-size: 20px;
      color: $gold;
      font-weight: bold;
    }

    &:hover {
      box-shadow: 0 4px 16px rgba(149,119,65,0.25);
      transform: scale(1.06);
    }

    &.dragging {
      cursor: grabbing;
      box-shadow: 0 4px 20px rgba(149,119,65,0.3);
      transform: scale(1.08);
    }

    &.success {
      border-color: #67c23a;
      background: #67c23a;
      .el-icon { color: #fff; }
    }

    &.fail {
      border-color: #f56c6c;
      background: #f56c6c;
      .el-icon { color: #fff; }
    }
  }

  .slider-hint {
    position: absolute;
    top: 0; left: 0; right: 0;
    line-height: 48px;
    text-align: center;
    color: #b5a47a;
    font-size: 13px;
    pointer-events: none;
    user-select: none;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 4px;

    .hint-arrow {
      animation: hint-pulse 1.5s infinite ease-in-out;
      font-size: 14px;
    }
  }
}

@keyframes hint-pulse {
  0%, 100% { transform: translateX(0); opacity: 0.6; }
  50%      { transform: translateX(6px); opacity: 1; }
}

.captcha-footer {
  margin-top: 12px;
  text-align: right;

  .refresh-link {
    font-size: 13px;
    color: $gold;
    cursor: pointer;
    transition: color 0.2s;
    display: inline-flex;
    align-items: center;
    gap: 4px;

    .el-icon { transition: transform 0.3s; }

    &:hover {
      color: darken($gold, 10%);
      .el-icon { transform: rotate(180deg); }
    }
  }
}

.captcha-fade-enter-active,
.captcha-fade-leave-active {
  transition: opacity 0.25s;
}
.captcha-fade-enter,
.captcha-fade-leave-to {
  opacity: 0;
}
</style>
