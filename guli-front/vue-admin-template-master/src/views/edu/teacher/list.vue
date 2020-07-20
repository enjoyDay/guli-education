<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="teacherQuery.name" placeholder="讲师名" />
      </el-form-item>

      <el-form-item>
        <el-select v-model="teacherQuery.level" clearable placeholder="讲师头衔">
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>

      <el-form-item label="添加时间">
        <el-date-picker
          v-model="teacherQuery.begin"
          type="datetime"
          placeholder="选择开始时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="teacherQuery.end"
          type="datetime"
          placeholder="选择截止时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          default-time="00:00:00"
        />
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
    >
      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">{{ (page - 1) * limit + scope.$index + 1 }}</template>
      </el-table-column>

      <el-table-column prop="name" label="名称" width="80" />

      <el-table-column label="头衔" width="80">
        <!-- 这里的scope代表整个表格，scope.row代表表格的一行 -->
        <template slot-scope="scope">{{ scope.row.level===1?'高级讲师':'首席讲师' }}</template>
      </el-table-column>

      <el-table-column prop="intro" label="资历" />

      <el-table-column prop="gmtCreate" label="添加时间" width="160" />

      <el-table-column prop="sort" label="排序" width="60" />

      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <router-link :to="'/teacher/edit/'+scope.row.id">
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
import teacher from "@/api/edu/teacher";

// 写核心代码的位置
export default {
  // 定义变量和初始值
  data() {
    return {
      listLoading: true,
      list: null, // 定义查询后的变量
      page: 1, // 定义变量页数
      limit: 10, // 定义每页数量
      total: 0,
      teacherQuery: {} // 定义查询条件
    };
  },

  // 页面渲染之前执行，一般调用的方法放在methods内部
  created() {
    this.getList();
  },

  // 创建具体的方法
  methods: {
    // 每页数量发生变化的时候
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.limit = val;
      this.getList();
    },

    // 页 发生变化的时候
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.page = val;
      this.getList();
    },

    // 讲师列表的方法
    getList() {
      this.listLoading = true;
      teacher
        .getTeacherListPage(this.page, this.limit, this.teacherQuery)
        .then(response => {
          // response返回的数据
          this.list = response.data.rows;
          this.total = response.data.total;

          this.listLoading = false;
        })
        .catch(error => {
          // 请求失败
          console.log(error);
        });
    },

    // 请求搜索框
    resetData() {
      this.teacherQuery = {};
      this.getList();
    },

    // 删除
    removeDataById(teacherId) {
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
        })
        .then(() => {
          return teacher.deleteTeacher(teacherId);
        })
        .then(() => {
          this.getList();
          this.$message({
            type: "success",
            message: "删除成功!"
          });
        })
        .catch(response => {
          // 失败
          if (response === "cancel") {
            this.$message({
              type: "info",
              message: "已取消删除"
            });
          } else {
            this.$message({
              type: "error",
              message: "删除失败"
            });
          }
        });
    }
  }
};
</script>