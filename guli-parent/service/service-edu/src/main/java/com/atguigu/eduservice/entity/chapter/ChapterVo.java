package com.atguigu.eduservice.entity.chapter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liukun
 * @description 返回给前端的信息对象
 * @since 2020/6/21
 */
@ApiModel(value = "章节信息")
@Data
public class ChapterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String title;

//    因为是章节和课程小节是一对多的关系，在这个保存小节，方便前端查询
    private List<VideoVo> children = new ArrayList<>();
}
