<template>
  <div class="app-container">
    <template>
      <div class="app-container">
        <el-form label-width="120px">
          <el-form-item label="讲师名称">
            <el-input v-model="teacher.name" />
          </el-form-item>
          <el-form-item label="讲师排序">
            <el-input-number v-model="teacher.sort" controls-position="right" min="0" />
          </el-form-item>
          <el-form-item label="讲师头衔">
            <el-select v-model="teacher.level" clearable placeholder="请选择">
              <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
              -->
              <el-option :value="1" label="高级讲师" />
              <el-option :value="2" label="首席讲师" />
            </el-select>
          </el-form-item>
          <el-form-item label="讲师资历">
            <el-input v-model="teacher.career" />
          </el-form-item>
          <el-form-item label="讲师简介">
            <el-input v-model="teacher.intro" :rows="10" type="textarea" />
          </el-form-item>

          <!-- 讲师头像：TODO -->
          <!-- 讲师头像 -->
          <el-form-item label="讲师头像">
            <!-- 头衔缩略图 -->
            <pan-thumb :image="teacher.avatar" />
            <!-- 文件上传按钮 -->
            <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像</el-button>

            <!--
              v-show：是否显示上传组件
              :key：类似于id，如果一个页面多个图片上传控件，可以做区分
              :url：后台上传的url地址
              @close：关闭上传组件
              @crop-upload-success：上传成功后的回调 
              <input type="file" name="file"/>
            -->
            <image-cropper
              v-show="imagecropperShow"
              :width="300"
              :height="300"
              :key="imagecropperKey"
              :url="BASE_API+'/ossservice/file/upload'"
              field="file"
              @close="close"
              @crop-upload-success="cropSuccess"
            />
          </el-form-item>

          <el-form-item>
            <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
    </template>
  </div>
</template>

<script>
import teacher from "@/api/edu/teacher";
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";

export default {
  // 引入的上传图片组件
  components: { ImageCropper, PanThumb },

  data() {
    return {
      teacher: {
        name: "",
        sort: 0,
        level: 1,
        career: "",
        intro: "",
        avatar: "" // 头像
      },

      //上传弹框组件是否显示
      imagecropperShow: false,
      imagecropperKey: 0, //上传组件key值
      BASE_API: process.env.BASE_API, //获取dev.env.js里面地址
      saveBtnDisabled: false // 保存按钮是否禁用,
    };
  },

  created() {
    this.init();
  },

  methods: {
    // 关闭上传弹窗后
    close() {
      this.imagecropperShow = false;
      //上传组件初始化，做这个的原因是当上传一次图片成功后，再此点击会直接出现上传成功，加上后会初始化
      this.imagecropperKey = this.imagecropperKey + 1;
    },

    // 上传成功后的回调方法
    cropSuccess(data) {
      this.imagecropperShow = false;
      //上传之后接口返回图片地址
      this.teacher.avatar = data.url;
      this.imagecropperKey = this.imagecropperKey + 1;
      this.$message({
            type: "success",
            message: "上传成功"
          });
    },

    init() {
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id;
        this.fetchDataById(id);
      } else {
        this.teacher = {};
      }
    },

    saveOrUpdate() {
      this.saveBtnDisabled = true;
      if (!this.teacher.id) {
        this.saveData();
      } else {
        this.updateTeacher();
      }
    },

    // 根据id查询记录
    fetchDataById(id) {
      teacher
        .getById(id)
        .then(response => {
          this.teacher = response.data.items;
        })
        .catch(response => {
          this.$message({
            type: "error",
            message: "获取数据失败"
          });
        });
    },

    // 更新
    updateTeacher() {
      teacher
        .updateById(this.teacher)
        .then(response => {
          return this.$message({
            type: "success",
            message: "修改成功!"
          });
        })
        .then(response => {
          this.$router.push({ path: "/teacher/table" });
        })
        .catch(error => {
          return this.$message({
            type: "error",
            message: "修改失败!"
          });
        });
    },

    // 保存
    saveData() {
      teacher
        .saveTeacher(this.teacher)
        .then(response => {
          // 显示提示成功信息
          return this.$message({
            type: "success",
            message: "保存成功"
          });
        })
        .then(response => {
          // 回到讲师列表页面
          this.$router.push({ path: "/teacher/table" });
        })
        .catch(error => {
          return this.$message({
            type: "error",
            message: "保存失败"
          });
        });
    }
  },

  watch: {
    $route(to, from) {
      this.init();
    }
  }
};
</script>
