import request from '@/utils/request'

export default {
    //1 添加课程信息
    addCourseInfo(courseInfo) {
        return request({
            url: '/eduservice/edu-course/addCourseInfo',
            method: 'post',
            data: courseInfo
        })
    },

    // 2 根据课程ID查询课程信息
    getCourseInfoId(id) {
        return request({
            url: `/eduservice/edu-course/course-info/${id}`,
            method: 'get'
        })
    },

    // 3 更新数据
    updateData(courseInfo) {
        return request({
            url: '/eduservice/edu-course/updateCourseInfo',
            method: 'put',
            data: courseInfo
        })
    },

    // 4 发布数据
    publishCourse(courseId) {
        return request({
            url: `/eduservice/edu-course/publish-course/${courseId}`,
            method: 'put'
        })
    },

    // 5 获取发布的信息
    getPublishCourseInfo(courseId) {
        return request({
            url: '/eduservice/edu-course/getPublishCourseInfo/' + courseId,
            method: 'get'
        })
    },

    // 6 根据条件分页查询课程信息列表
    getCourseListPage(current, limit, courseQuery) {
        return request({
            url: `/eduservice/edu-course/pageCourseCondition/${current}/${limit}`,
            method: 'post',
            // 传递请求体时，使用data
            // 传递请求参数时，使用params
            data: courseQuery
        })
    },

    // 7 根据id删除课程
    removeById(courseId) {
        return request({
            url: `/eduservice/edu-course/${courseId}`,
            method: 'delete'
        })
    }
}