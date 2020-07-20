import request from '@/utils/request'


export default {
    //1 添加课时
    addVideo(video) {
        return request({
            url: `/eduservice/edu-video/addVideo`,
            method: 'post',
            data: video
        })
    },

    // 2 修改课时
    updateVideo(video) {
        return request({
            url: `/eduservice/edu-video/updateVideo`,
            method: 'put',
            data: video
        })
    },

    // 3 根据id删除小节
    removeVideo(videoId) {
        return request({
            url: `/eduservice/edu-video/${videoId}`,
            method: 'delete',
        })
    },

    // 4 根据id获取小节信息
    getVideoBiVdeoId(videoId) {
        return request({
            url: `/eduservice/edu-video/${videoId}`,
            method: 'get',
        })
    }
}