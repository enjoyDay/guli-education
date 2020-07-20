<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:30px;" />

    <el-tree
      ref="tree2"
      :data="data2"
      :props="defaultProps"
      :filter-node-method="filterNode"
      class="filter-tree"
      default-expand-all
    />
  </div>
</template>

<script>
import subject from "@/api/edu/subject";
export default {
  data() {
    return {
      filterText: "",
      data2: [],
      // 树形展示数据结构要和后端一致
      defaultProps: {
        children: "children",
        label: "title"
      }
    };
  },

  created() {
    // 页面渲染后查询所有课程科目
    this.getAllSubject();
  },

  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    }
  },

  methods: {
    getAllSubject() {
      subject
        .getAllSubject()
        .then(response => {
          this.data2 = response.data.list
          console.log('获取到的数据：', this.data2);
        })
        .catch(error => {
          console.log(error);
        });
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.title.toLowerCase().indexOf(value) !== -1;
    }
  }
};
</script>

