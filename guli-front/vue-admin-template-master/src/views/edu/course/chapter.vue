<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="active" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-button type="text" @click="openChapterDialog()">添加章节</el-button>
    <!-- 章节 -->
    <ul class="chanpterList">
      <li v-for="chapter in chapterNestedList" :key="chapter.id">
        <p>
          {{ chapter.title }}
          <span class="acts">
            <el-button type="text" @click="openVideoDialog(chapter.id)">添加课时</el-button>
            <el-button style type="text" @click="openEditChatper(chapter.id)">编辑</el-button>
            <el-button type="text" @click="deleteChapter(chapter.id)">删除</el-button>
          </span>
        </p>

        <!-- 小节课程信息 -->
        <ul class="chanpterList videoList">
          <li v-for="video in chapter.children" :key="video.id">
            <p>
              {{ video.title }}
              <span class="acts">
                <el-button type="text" @click="openEditVideo(video.id)">编辑</el-button>
                <el-button type="text" @click="deleteVideo(video.id)">删除</el-button>
              </span>
            </p>
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="previous">上一步</el-button>
      <el-button :disabled="saveBtnDisabled" type="primary" @click="next">下一步</el-button>
    </div>

    <!-- 添加和修改章节表单 -->
    <el-dialog :visible.sync="dialogChapterFormVisible" title="添加章节">
      <el-form :model="chapter" label-width="120px">
        <el-form-item label="章节标题">
          <el-input v-model="chapter.title" />
        </el-form-item>
        <el-form-item label="章节排序">
          <el-input-number v-model="chapter.sort" :min="0" controls-position="right" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogChapterFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加和修改课时表单 -->
    <el-dialog :visible.sync="dialogVideoFormVisible" title="添加课时">
      <el-form :model="video" label-width="120px">
        <el-form-item label="课时标题">
          <el-input v-model="video.title" />
        </el-form-item>
        <el-form-item label="课时排序">
          <el-input-number v-model="video.sort" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="是否免费">
          <el-radio-group v-model="video.isFree">
            <el-radio :label="true">免费</el-radio>
            <el-radio :label="false">默认</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传视频">
          <el-upload
            :on-success="handleVodUploadSuccess"
            :on-remove="handleVodRemove"
            :before-remove="beforeVodRemove"
            :on-exceed="handleUploadExceed"
            :file-list="fileList"
            :action="BASE_API+'/vodservice/video/upload'"
            :limit="1"
            class="upload-demo"
          >
            <el-button size="small" type="primary">上传视频</el-button>
            <el-tooltip placement="right-end">
              <div slot="content">
                最大支持1G，
                <br />支持3GP、ASF、AVI、DAT、DV、FLV、F4V、
                <br />GIF、M2T、M4V、MJ2、MJPEG、MKV、MOV、MP4、
                <br />MPE、MPG、MPEG、MTS、OGG、QT、RM、RMVB、
                <br />SWF、TS、VOB、WMV、WEBM 等视频格式上传
              </div>
              <i class="el-icon-question" />
            </el-tooltip>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVideoFormVisible = false">取 消</el-button>
        <el-button :disabled="saveVideoBtnDisabled" type="primary" @click="saveOrUpdateVideo">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import chapter from "@/api/edu/chapter";
import video from "@/api/edu/video";
import vod from "@/api/edu/vod";

export default {
  data() {
    return {
      // 进度条
      active: 2,
      saveBtnDisabled: false,
      courseId: "", //课程id
      chapterNestedList: [], // 章节嵌套课时列表
      chapter: {
        //封装章节数据
        title: "",
        sort: 0
      },
      video: {
        title: "",
        sort: 0,
        isFree: 0,
        videoSourceId: "", // 视频id
        videoOriginalName: "", // 视频真实名
        videoSourcePath: "", // 视频http路径
        videoOriginalPath: "" // 视频文件夹的路径
      },
      dialogChapterFormVisible: false, //章节弹框
      dialogVideoFormVisible: false, //小节弹框
      saveVideoBtnDisabled: false,
      fileList: [], //上传文件列表
      BASE_API: process.env.BASE_API
    };
  },
  created() {
    //获取路由的id值
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id;
      //根据课程id查询章节和小节
      this.getChapterVideo();
    }
  },
  methods: {
    previous() {
      this.$router.push({ path: "/course/info/" + this.courseId });
    },
    next() {
      //跳转到第下步
      this.$router.push({ path: "/course/publish/" + this.courseId });
    },
    // 获取章节小节全部信息
    getChapterVideo() {
      chapter.getChaperVideoInfo(this.courseId).then(reponse => {
        this.chapterNestedList = reponse.data.items;
      });
    },

    //==============================小节操作====================================
    saveOrUpdateVideo() {
      if (!this.video.id) {
        this.addVideo();
      } else {
        this.updateVideo();
      }
    },

    // 添加课时信息
    addVideo() {
      this.video.courseId = this.courseId;
      console.log("video数据：", video);
      video.addVideo(this.video).then(reponse => {
        // 关闭章节弹窗
        this.dialogVideoFormVisible = false;
        //提示
        this.$message({
          type: "success",
          message: "添加课时成功!"
        });
        // 刷新页面
        this.getChapterVideo();
      });
    },

    // 更新课时信息
    updateVideo() {
      video.updateVideo(this.video).then(response => {
        // 关闭章节弹窗
        this.dialogVideoFormVisible = false;
        //提示
        this.$message({
          type: "success",
          message: "修改课时成功!"
        });
        // 刷新页面
        this.getChapterVideo();
      });
    },

    // 弹出课时弹窗
    openVideoDialog(chapterId) {
      //弹框
      this.dialogVideoFormVisible = true;
      this.video = {};
      this.fileList = [];
      //设置章节id
      this.video.chapterId = chapterId;
    },

    // 打开编辑课时框
    openEditVideo(videoId) {
      console.log("小节id:", videoId);
      this.dialogVideoFormVisible = true;
      this.fileList=[];
      video.getVideoBiVdeoId(videoId).then(response => {
        this.video = response.data.item;
        console.log("获取小节信息：", this.video.videoOriginalName);
        if(this.video.videoOriginalName) {
          this.fileList = [{'name': this.video.videoOriginalName,'url':this.video.videoSourcePath}]
        }
      });
    },

    // 删除小节
    deleteVideo(videoId) {
      this.$confirm("此操作会删除课程，是否继续？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        video.removeVideo(videoId).then(response => {
          //提示
          this.$message({
            type: "success",
            message: "删除课时成功!"
          });
          // 刷新页面
          this.getChapterVideo();
        });
      });
    },

    //上传视频文件成功回调
    handleVodUploadSuccess(response, file, fileList) {
      this.video.videoSourceId = response.data.videoSourceId;
      this.video.videoOriginalName = response.data.videoOriginalName;
      this.video.videoSourcePath = response.data.videoSourcePath;
      this.video.videoOriginalPath = response.data.videoOriginalPath;
    },

    //视图上传多于一个视频
    handleUploadExceed(files, fileList) {
      this.$message.warning("想要重新上传视频，请先删除已上传的视频");
    },

    // 删除视频前
    beforeVodRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },

    // 删除视频
    handleVodRemove(file, fileList) {
      console.log("删除视频文件:", file);
      console.log("删除视频列表:", fileList);
      vod.removeById(this.video.videoOriginalPath).then(response => {
        this.video.videoSourceId = "";
        this.video.videoOriginalName = "";
        this.video.videoSourcePath = "";
        this.video.videoOriginalPath = "";
        this.fileList = [];
        this.$message({
          type: "success",
          message: response.message
        });
      });
    },
    //==============================章节操作====================================
    saveOrUpdate() {
      if (!this.chapter.id) {
        this.addChapter();
      } else {
        this.updateChapter();
      }
    },
    // 修改章节
    updateChapter() {
      chapter.updateChapter(this.chapter).then(response => {
        // 关闭章节弹窗
        this.dialogChapterFormVisible = false;
        //提示
        this.$message({
          type: "success",
          message: "修改章节成功!"
        });
        // 刷新页面
        this.getChapterVideo();
      });
    },

    // 保存章节信息
    addChapter() {
      this.chapter.courseId = this.courseId;
      chapter.addChapter(this.chapter).then(reponse => {
        // 关闭章节弹窗
        this.dialogChapterFormVisible = false;
        //提示
        this.$message({
          type: "success",
          message: "添加章节成功!"
        });
        // 刷新页面
        this.getChapterVideo();
      });
    },

    // 弹出添加章节的对话框
    openChapterDialog() {
      //弹框
      this.dialogChapterFormVisible = true;
      // 章节中的数据清空
      this.chapter.title = "";
      this.chapter.sort = 0;
    },

    // 编辑章节对话框
    openEditChatper(chapterId) {
      this.dialogChapterFormVisible = true;
      //  调用接口回显数据
      chapter.getChapter(chapterId).then(response => {
        this.chapter = response.data.chapter;
      });
    },

    // 删除章节
    deleteChapter(chapterId) {
      this.$confirm("此操作会删除章节，是否继续？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        chapter.deleteChapter(chapterId).then(response => {
          this.$message({
            type: "success",
            message: "删除章节成功!"
          });

          this.getChapterVideo();
        });
      });
    }
  }
};
</script>










<!--  将样式的定义放在页面的最后
 scope表示这里定义的样式只在当前页面范围内生效，不会污染到其他的页面-->
<style scoped>
.chanpterList {
  position: relative;
  list-style: none;
  margin: 0;
  padding: 0;
}

.chanpterList li {
  position: relative;
}

.chanpterList p {
  float: left;
  font-size: 20px;
  margin: 10px 0;
  padding: 10px;
  height: 70px;
  line-height: 50px;
  width: 100%;
  border: 1px solid #ddd;
}

.chanpterList .acts {
  float: right;
  font-size: 14px;
}

.videoList {
  padding-left: 50px;
}

.videoList p {
  float: left;
  font-size: 14px;
  margin: 10px 0;
  padding: 10px;
  height: 50px;
  line-height: 30px;
  width: 100%;
  border: 1px dotted #ddd;
}
</style>