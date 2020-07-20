import request from '@/utils/request'

export default {
    // 1 讲师列表（条件查询分页）
    getTeacherListPage(current, limit, teacherQuery) {
        return request({
            url: `/eduservice/edu-teacher/pageTeacherCondition/${current}/${limit}`,
            method: 'post',
            // 传递请求体时，使用data
            // 传递请求参数时，使用params
            data: teacherQuery
        })
    },

    //2 删除讲师（根据id删除）
    deleteTeacher(teacherId) {
        return request({
            url: `/eduservice/edu-teacher/${teacherId}`,
            method: 'delete'
        })
    },

    //3 新增讲师
    saveTeacher(teacher) {
        return request({
            url: `/eduservice/edu-teacher/addTeacher`,
            method: 'post',
            data: teacher
        })
    },

    //4 根据id获取teacher，做回显
    getById(id) {
        return request({
            url: `/eduservice/edu-teacher/getTeacher/${id}`,
            method: 'get'
        })
    },

    //5 更新teacher
    updateById(teacher) {
        return request({
            url: `/eduservice/edu-teacher/${teacher.id}`,
            method: 'put',
            data: teacher
        })
    },

    //6 查询所有讲师
    getListTeacher() {
        return request({
            url: '/eduservice/edu-teacher/findAll',
            method: 'get'
        })
    },
} 