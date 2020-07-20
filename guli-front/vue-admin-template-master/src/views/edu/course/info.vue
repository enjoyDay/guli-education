<template>
  <div class="app-container">
    <h2 style="text-align: center;">发布新课程</h2>

    <el-steps :active="active" process-status="wait" align-center style="margin-bottom: 40px;">
      <el-step title="填写课程基本信息" />
      <el-step title="创建课程大纲" />
      <el-step title="最终发布" />
    </el-steps>

    <el-form label-width="120px">
      <el-form-item label="课程标题">
        <el-input v-model="courseInfo.title" placeholder=" 示例：机器学习项目课：从基础到搭建项目视频课程。专业名称注意大小写" />
      </el-form-item>

      <!-- 所属分类 TODO -->
      <el-form-item label="课程分类">
        <el-select
          v-model="courseInfo.subjectParentId"
          placeholder="一级分类"
          @change="subjectLevelOneChanged"
        >
          <el-option
            v-for="subject in subjectOneList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>

        <!-- 二级分类 -->
        <el-select v-model="courseInfo.subjectId" placeholder="二级分类">
          <el-option
            v-for="subject in subjectTwoList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>
      </el-form-item>

      <!-- 课程讲师 TODO -->
      <!-- 课程讲师 -->
      <el-form-item label="课程讲师">
        <el-select v-model="courseInfo.teacherId" placeholder="请选择">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="总课时">
        <el-input-number
          :min="0"
          v-model="courseInfo.lessonNum"
          controls-position="right"
          placeholder="请填写课程的总课时数"
        />
      </el-form-item>

      <!-- 课程简介 TODO -->
      <!-- 课程简介-->
      <el-form-item label="课程简介">
        <tinymce :height="300" v-model="courseInfo.description" />
      </el-form-item>

      <!-- 课程封面 TODO -->
      <!-- 课程封面-->
      <el-form-item label="课程封面">
        <el-upload
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          :action="BASE_API+'/ossservice/file/upload'"
          class="avatar-uploader"
        >
          <img :src="courseInfo.cover" />
        </el-upload>
      </el-form-item>

      <el-form-item label="课程价格">
        <el-input-number
          :min="0"
          v-model="courseInfo.price"
          controls-position="right"
          placeholder="免费课程请设置为0元"
        />元
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存并下一步</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import course from "@/api/edu/course";
import teacher from "@/api/edu/teacher";
import subject from "@/api/edu/subject";
import Tinymce from "@/components/Tinymce"; //引入富文本组件
export default {
  //声明组件
  components: { Tinymce },

  data() {
    return {
      // 进度条
      active: 1,
      saveBtnDisabled: false,
      courseInfo: {
        title: "",
        subjectParentId: "",
        subjectId: "",
        teacherId: "",
        lessonNum: 0,
        description: "",
        cover: "/static/upload.jpg", //添加一个默认图片
        price: 0.0
      },
      default_courseInfo: {
        title: "",
        subjectParentId: "",
        subjectId: "",
        teacherId: "",
        lessonNum: 0,
        description: "",
        cover: "/static/upload.jpg", //添加一个默认图片
        price: 0.0
      },
      BASE_API: process.env.BASE_API, // 接口API地址
      subjectOneList: [],
      subjectTwoList: [],
      teacherList: [],
      courseId: ""
    };
  },
  created() {
    this.init();
  },

  watch: {
    // 路由发生变化
    $route(to, from) {
      this.init();
    }
  },

  methods: {
    //   初始化
    init() {
      // 上一步回显信息
      //获取路由id值
      if (this.$route.params && this.$route.params.id) {
        this.courseId = this.$route.params.id;
        //调用根据id查询课程的方法
        this.getInfo();
      } else {
        this.courseInfo={...this.default_courseInfo}
        this.getOneSubject();
        this.getListTeacher();
      }
    },

    // 根据课程ID查询
    getInfo() {
      course
        .getCourseInfoId(this.courseId)
        .then(response => {
          //在courseInfo课程基本信息，包含 一级分类id 和 二级分类id,但是还没有名字
          this.courseInfo = response.data.courseInfoVo;
          //1 查询所有的分类，包含一级和二级
          subject.getAllSubject().then(response => {
            //2 获取所有一级分类
            this.subjectOneList = response.data.list;
            //3 把所有的一级分类数组进行遍历，
            for (var i = 0; i < this.subjectOneList.length; i++) {
              //获取每个一级分类
              var oneSubject = this.subjectOneList[i];
              //比较当前courseInfo里面一级分类id和所有的一级分类id
              if (this.courseInfo.subjectParentId == oneSubject.id) {
                //获取一级分类所有的二级分类
                this.subjectTwoList = oneSubject.children;
              }
            }
          });

          //初始化所有讲师
          this.getListTeacher();
        })
        .catch(error => {
          console.log(error);
        });
    },

    // 保存并下一步的方法
    saveOrUpdate() {
      // 这里不用this.courseId是因为
      if (!this.courseInfo.id) {
        this.saveData();
      } else {
        this.updateData();
      }
    },

    saveData() {
      course.addCourseInfo(this.courseInfo).then(response => {
        //提示
        this.$message({
          type: "success",
          message: "添加课程信息成功!"
        });
        //跳转到第二步,带参数courseId，添加之后使用response.data.courseId可以使用
        this.$router.push({
          path: "/course/chapter/" + response.data.courseId
        });
      });
    },

    updateData() {
      course.updateData(this.courseInfo).then(response => {
        //提示
        this.$message({
          type: "success",
          message: "修改课程信息成功!"
        });
        //跳转到第二步，修改之后response是不返回值的,因此只能使用原来的courseId
        this.$router.push({ path: "/course/chapter/" + this.courseId });
      });
    },

    // 查询课程一级分类
    getOneSubject() {
      subject.getAllSubject().then(response => {
        this.subjectOneList = response.data.list;
      });
    },

    // 查询课程二级分类，这个在每次一级目录改变时调用
    subjectLevelOneChanged(value) {
      //value就是一级分类id值
      //遍历所有的分类，包含一级和二级
      for (var i = 0; i < this.subjectOneList.length; i++) {
        //每个一级分类
        var oneSubject = this.subjectOneList[i];
        //判断：所有一级分类id 和 点击一级分类id是否一样
        if (value === oneSubject.id) {
          //从一级分类获取里面所有的二级分类
          this.subjectTwoList = oneSubject.children;
          //把二级分类id值清空(这是为了解决切换一级分类时，二级分类没有及时清空)
          this.courseInfo.subjectId = "";
        }
      }
    },

    // 查询课程教师
    getListTeacher() {
      teacher.getListTeacher().then(reponse => {
        this.teacherList = reponse.data.items;
      });
    },

    //上传封面成功调用的方法
    handleAvatarSuccess(res, file) {
      this.courseInfo.cover = res.data.url;
    },

    //上传之前调用的方法，判断图片格式和大小
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    }
  }
};
</script>
<style scoped>
.tinymce-container {
  line-height: 29px;
}
</style>