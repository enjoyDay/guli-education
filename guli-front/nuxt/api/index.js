import request from '@/utils/request'

export default {
  //首页中的相关接口
    //查询热门课程和名师
  getIndexData() {
    return request({
      url: '/eduservice/index/index',
      method: 'get'
    })
  }
}
