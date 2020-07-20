package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.chapter.ChapterVo;
import com.atguigu.eduservice.entity.chapter.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author liukun
 * @since 2020-06-20
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService eduVideoService;

    @Override
    public List<ChapterVo> getChapterAndVideoInfo(String courseId) {
//        1 获取章节信息
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper();
        wrapperChapter.eq("course_id", courseId);
        wrapperChapter.orderByAsc("sort", "id");
        List<EduChapter> chapters = baseMapper.selectList(wrapperChapter);

//        2 获取课程信息
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("course_id", courseId);
        wrapperVideo.orderByAsc("sort", "id");
        List<EduVideo> videos = eduVideoService.list(wrapperVideo);

//        组装最终返回给前端的队象
        List<ChapterVo> finalChapterVo = new ArrayList<>();
        for (int i = 0; i < chapters.size(); i++) {
            EduChapter eduChapter = chapters.get(i);
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter, chapterVo);
            finalChapterVo.add(chapterVo);

            List<VideoVo> videoVoArrayList = new ArrayList<>();
            for (int i1 = 0; i1 < videos.size(); i1++) {
                EduVideo eduVideo = videos.get(i1);
                if (eduVideo.getChapterId().equals(eduChapter.getId())) {
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo, videoVo);
                    videoVoArrayList.add(videoVo);
                }
            }

            chapterVo.setChildren(videoVoArrayList);
        }


        return finalChapterVo;
    }

    /**
     * 删除章节，会把所有的小节都删除
     *
     * @param chapterId 章节id
     * @return 返回是否成功
     */
    @Override
    public boolean deleteChapter(String chapterId) {
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper();
        wrapperVideo.eq("chapter_id", chapterId);
        Map<String, Object> map = eduVideoService.getMap(wrapperVideo);
        if (map != null && map.size() > 0) {
            throw new GuliException(20001, "该章节内有课，不能删除");
        }

        int i = baseMapper.deleteById(chapterId);
        if (i == 0) {
            throw new GuliException(20001, "删除章节失败");
        }

        QueryWrapper<EduVideo> wrapper = new QueryWrapper();
        wrapper.eq("chapter_id", chapterId);
        boolean remove = eduVideoService.remove(wrapper);

        return remove;
    }

    //课程大纲列表,根据课程id进行查询
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        //1 根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);

        //2 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);

        //创建list集合，用于最终封装数据
        List<ChapterVo> finalList = new ArrayList<>();

        //3 遍历查询章节list集合进行封装
        //遍历查询章节list集合
        for (int i = 0; i < eduChapterList.size(); i++) {
            //每个章节
            EduChapter eduChapter = eduChapterList.get(i);
            //eduChapter对象值复制到ChapterVo里面
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            //把chapterVo放到最终list集合
            finalList.add(chapterVo);

            //创建集合，用于封装章节的小节
            List<VideoVo> videoList = new ArrayList<>();

            //4 遍历查询小节list集合，进行封装
            for (int m = 0; m < eduVideoList.size(); m++) {
                //得到每个小节
                EduVideo eduVideo = eduVideoList.get(m);
                //判断：小节里面chapterid和章节里面id是否一样
                if(eduVideo.getChapterId().equals(eduChapter.getId())) {
                    //进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    //放到小节封装集合
                    videoList.add(videoVo);
                }
            }
            //把封装之后小节list集合，放到章节对象里面
            chapterVo.setChildren(videoList);
        }
        return finalList;
    }


}
