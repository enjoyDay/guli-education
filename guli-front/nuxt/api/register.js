import request from '@/utils/request'

export default {
  //根据邮箱发送短信
  sendCode(email) {
    return request({
      url: `/msmservice/msm/send-simple-mail/`,
      method: 'post',
      data: email
    })
  },

  //用户注册
  registerMember(formItem) {
    return request({
      url: `/ucenterservice/ucenter-member/register`,
      method: 'post',
      data: formItem
    })
  }

}