<template>
  <div class="product-create-page">
    <app-tab-bar />
    <app-page-title title="Danh sách các file logs" />

    <div class="card mb-5 mb-xl-10">
      <div class="card-body" v-loading="isTableLoading">
        <el-table
          class="table-header-sticky"
          stripe
          cell-class-name="px-2 py-2 whitespace-nowrap"
          :data="listFileNames"
        >
          <template slot="empty">
            <span
              class="text-xs font-medium text-gray-500 uppercase tracking-wider"
            >
              Không có dữ liệu
            </span>
          </template>
          <el-table-column fixed="left" label="Hành động" width="120">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="info"
                icon="el-icon-download"
                @click="handleDownloadFile(scope.row)"
                >Tải về</el-button
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="fileSizeText"
            label="Kích thước"
            width="120"
          ></el-table-column>
          <el-table-column prop="fileName" label="Tên file"> </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import { saveAs } from "file-saver";
import { getAllListLogFiles, downloadLogFile } from "@/services/log.service";
import { Utils } from "@/services/core/utils";

export default {
  name: "log-list",
  layout: "main-layout",
  middleware: "isAdmin",
  data() {
    return {
      Utils: Utils,
      isTableLoading: false,
      listFileNames: [],
    };
  },
  mounted() {
    this._getListLogFiles();
  },
  methods: {
    async handleDownloadFile(file) {
      this.$notify({
        type: "success",
        message: "Đang thực hiện download file",
        position: "bottom-right",
      });
      const res = await downloadLogFile(file);
      saveAs(res.data, file.fileName);
    },
    async _getListLogFiles() {
      this.isTableLoading = true;
      try {
        const res = await getAllListLogFiles();
        this.listFileNames = res.data.map((item) => {
          item.fileSizeText = Utils.formatFileSize(item.fileSize);
          return item;
        });
      } finally {
        this.isTableLoading = false;
      }
    },
  },
};
</script>
