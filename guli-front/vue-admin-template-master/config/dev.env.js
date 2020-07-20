'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  //BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"',// 后端接口地址
  BASE_API: '"http://localhost:8222"',// 后端接口地址
  OSS_PATH: '"http://127.0.0.1:8002"',//文件上传接口
})
