package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
public interface EduChapterService extends IService<EduChapter> {
    /**
     * 根据课程ID获取章节和课时信息
     *
     * @param courseId 课程ID
     * @return 章节和课时信息
     */
    List<ChapterVo> getChapterAndVideoInfo(String courseId);

    boolean deleteChapter(String chapterId);

    /**
     * 根据课程id查询章节和小节
     * @param courseId 课程id
     * @return
     */
    List<ChapterVo> getChapterVideoByCourseId(String courseId);
}
