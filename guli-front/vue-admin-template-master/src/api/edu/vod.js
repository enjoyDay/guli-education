import request from '@/utils/request'
export default {
    // 1 删除视频
    removeById(videoOriginalPath) {
        console.log("路径", videoOriginalPath)
        return request({
            url: `/eduservice/edu-video/video-source/`,
            method: 'post',
            data: `${videoOriginalPath}`
        })
    }
}