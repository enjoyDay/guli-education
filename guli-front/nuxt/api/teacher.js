import request from '@/utils/request'

export default {
  //分页讲师查询的方法
  getTeacherList(page, limit) {
    return request({
      url: `/eduservice/edu-teacher/pageTeacher/${page}/${limit}`,
      method: 'get'
    })
  },
  //讲师详情的方法
  getTeacherInfo(id) {
    return request({
      url: `/eduservice/edu-teacher/getTeacher/${id}`,
      method: 'get'
    })
  },

  // 根据讲师ID获取该讲师下的课程
  getById(teacherId) {
    return request({
      url: `/eduservice/edu-course/front/teacher-id/${teacherId}`,
      method: 'get'
    })
  }

}