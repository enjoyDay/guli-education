<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="courseQuery.title" placeholder="课程名称" />
      </el-form-item>

      <el-form-item label="课程讲师">
        <el-select v-model="courseQuery.teacherId" placeholder="请选择讲师">
          <el-option
            v-for="teacher in teacherList"
            :key="teacher.id"
            :label="teacher.name"
            :value="teacher.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="课程分类">
        <el-select
          v-model="courseQuery.subjectParentId"
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
        <el-select v-model="courseQuery.subjectId" placeholder="二级分类">
          <el-option
            v-for="subject in subjectTwoList"
            :key="subject.id"
            :label="subject.title"
            :value="subject.id"
          />
        </el-select>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="getList()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <!-- 数据列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="数据加载中"
      border
      fit
      highlight-current-row
      row-class-name="myClassList"
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">{{ (page - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>

      <el-table-column label="课程信息" width="470" align="center">
        <template slot-scope="scope">
          <div class="info">
            <div class="pic">
              <img :src="scope.row.cover" alt="scope.row.title" width="150px" />
            </div>
            <div class="title">
              <router-link :to="'/course/publish/'+scope.row.id">
                <a href>{{ scope.row.title }}</a>
              </router-link>
              <p>{{ scope.row.lessonNum }}课时</p>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column label="创建时间" align="center">
        <template slot-scope="scope">{{ scope.row.gmtCreate.substr(0, 10) }}</template>
      </el-table-column>

      <el-table-column label="发布时间" align="center">
        <template slot-scope="scope">{{ scope.row.gmtModified.substr(0, 10) }}</template>
      </el-table-column>

      <el-table-column label="价格" width="100" align="center">
        <template slot-scope="scope">
          {{ Number(scope.row.price) === 0 ? '免费' :
          '¥' + scope.row.price.toFixed(2) }}
        </template>
      </el-table-column>

      <el-table-column prop="buyCount" label="付费学员" width="100" align="center">
        <template slot-scope="scope">{{ scope.row.buyCount }}人</template>
      </el-table-column>

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/course/info/'+scope.row.id">
            <el-button type="primary" size="mini" icon="el-icon-edit">修改</el-button>
          </router-link>

          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeDataById(scope.row.id)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="page"
      layout="sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[1, 2, 5, 10]"
      :page-size="limit"
      style="padding: 30px 0; text-align: right;"
    ></el-pagination>
  </div>
</template>

<script>
import course from "@/api/edu/course";
import teacher from "@/api/edu/teacher";
import subject from "@/api/edu/subject";
export default {
  data() {
    return {
      listLoading: true,
      list: null, // 定义查询后的变量
      page: 1, // 定义变量页数
      limit: 10, // 定义每页数量
      total: 0,
      courseQuery: {},
      subjectOneList: [],
      subjectTwoList: [],
      teacherList: []
    };
  },
  created() {
    this.init();
  },
  methods: {
    // 初始化，开始时就获取讲师列表和一级列表
    init() {
      this.getList();
      // 获取讲师列表
      this.getTeacherList();
      // 获取一级列表
      this.getOneSubject();
    },

    getTeacherList() {
      teacher.getListTeacher().then(reponse => {
        this.teacherList = reponse.data.items;
      });
    },

    // 一级分类
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

    // 获取课程列表
    getList() {
      this.listLoading = true;
      course
        .getCourseListPage(this.page, this.limit, this.courseQuery)
        .then(response => {
          // reponse返回的数据
          this.list = response.data.rows;
          this.total = response.data.total;

          this.listLoading = false;
        })
        .catch(error => {
          console.log("请求失败", error);
        });
    },

    // 重置搜索框
    resetData() {
      this.courseQuery = {};
      this.getList();
    },

    // 当前页发生改变
    handleCurrentChange(val) {
      this.page = val;
      this.getList();
    },

    // 页数发生变化
    handleSizeChange(val) {
      this.limit = val;
      this.getList();
    },

    removeDataById(courseId) {
      this.$confirm(
        "此操作将永久删除该课程，以及该课程下的章节和视频，是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          return course.removeById(courseId);
        })
        .then(() => {
          this.getList();
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        })
        .catch(response => {
          if (response == "cancel") {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          }
        });
    }
  }
};
</script>
<style scoped>
.myClassList .info {
  width: 450px;
  overflow: hidden;
}
.myClassList .info .pic {
  width: 150px;
  height: 90px;
  overflow: hidden;
  float: left;
}
.myClassList .info .pic a {
  display: block;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}
.myClassList .info .pic img {
  display: block;
  width: 100%;
}
.myClassList td .info .title {
  width: 280px;
  float: right;
  height: 90px;
}
.myClassList td .info .title a {
  display: block;
  height: 48px;
  line-height: 24px;
  overflow: hidden;
  color: #00baf2;
  margin-bottom: 12px;
}
.myClassList td .info .title p {
  line-height: 20px;
  margin-top: 5px;
  color: #818181;
}
</style>