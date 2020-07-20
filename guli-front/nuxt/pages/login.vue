<template>
  <div class="main">
    <div class="title">
      <a class="active" href="/login">登录</a>
      <span>·</span>
      <a href="/register">注册</a>
    </div>

    <div class="sign-up-container">
      <el-form ref="userForm" :model="user">
        <el-form-item
          class="input-prepend restyle"
          prop="email"
          :rules="[{ required: true, message: '请输入邮箱号码', trigger: 'blur' },{validator: checkPhone, trigger: 'blur'}]"
        >
          <div>
            <el-input type="text" placeholder="邮箱号" v-model="user.email" />
            <i class="iconfont icon-phone" />
          </div>
        </el-form-item>

        <el-form-item
          class="input-prepend"
          prop="password"
          :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]"
        >
          <div>
            <el-input type="password" placeholder="密码" v-model="user.password" />
            <i class="iconfont icon-password" />
          </div>
        </el-form-item>

        <div class="btn">
          <input type="button" class="sign-in-button" value="登录" @click="submitLogin()" />
        </div>
      </el-form>
      <!-- 更多登录方式 -->
      <div class="more-sign">
        <h6>社交帐号登录</h6>
        <ul>
          <li>
            <a
              id="weixin"
              class="weixin"
              target="_blank"
              href="http://localhost:8150/api/ucenter/wx/login"
            >
              <i class="iconfont icon-weixin" />
            </a>
          </li>
          <li>
            <a id="qq" class="qq" target="_blank" href="#">
              <i class="iconfont icon-qq" />
            </a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import "~/assets/css/sign.css";
import "~/assets/css/iconfont.css";
import cookie from "js-cookie";

import login from "@/api/login";

export default {
  layout: "sign",

  data() {
    return {
      user: {
        email: "",
        password: ""
      },
      loginInfo: {}
    };
  },

  methods: {
    checkPhone(rule, value, callback) {
      //debugger
      // if (!(/^1[34578]\d{9}$/.test(value))) {
      //   return callback(new Error('手机号码格式不正确'))
      // }
      return callback();
    },

    submitLogin() {
      login.submitLogin(this.user).then(response => {
        if (response.data.success) {
          //把token存在cookie中、也可以放在localStorage中
          cookie.set("guli_token", response.data.data.token);
           var a = cookie.get("guli_token");

          //登录成功根据token获取用户信息
          login.getLoginInfo().then(response => {
            console.log("用户信息：", response);
            this.loginInfo = response.data.data.item;
            //将用户信息记录cookie
            cookie.set("guli_ucenter", this.loginInfo);
          });

          //提示注册成功
          this.$message({
            type: "success",
            message: "登录成功"
          });

          //跳转首页页面index
          this.$router.push({ path: "/" });
        }
      });
    }
  }
};
</script>
<style>
.el-form-item__error {
  z-index: 9999999;
}
</style>