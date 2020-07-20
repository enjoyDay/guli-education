import request from '@/utils/request'
export default {
    // 分页获取评论
    getPageList(page, limit, courseId) {
        return request({
            url: `/eduservice/edu-comment/${page}/${limit}`,
            method: 'get',
            params: { courseId }
        })
    },

    // 添加评论
    addComment(comment) {
        return request({
            url: `/eduservice/edu-comment/auth/save`,
            method: 'post',
            data: comment
        })
    }
}