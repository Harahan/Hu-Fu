<template>
  <div class="operation">
    <div class="search-bar">
      <div class="search-bar2">
        <a class="my-pad">{{ $t("operation.context") }}:</a>
        <el-input
            v-model="recordContext"
            :placeholder="$t('operation.pleaseInput')"
            clearable
            style="width:300px;">
        </el-input>
      </div>
      <div class="search-bar2">
        <a class="my-pad">{{ $t("operation.status") }}:</a>
        <el-select
            v-model="recordStatus"
            :placeholder="$t('operation.pleaseSelect')"
            text-align="center"
            clearable
            style="width:150px">
          <el-option
              v-for="item in allStatus"
              :key="$t(item.name)"
              :label="$t(item.name)"
              :value="item.value"

          />
        </el-select>
      </div>
      <div class="search-bar2">
        <el-button
            type="primary"
            @click="search">
          {{ $t('operation.query') }}
        </el-button>
        <el-button
            type="primary"
            @click="clearContextInput">
          {{ $t('operation.reset') }}
        </el-button>
      </div>
    </div>
    <div class="">
      <el-table :data="tableData" stripe
                class="result-table"
                style="overflow-y:auto">
        <template v-slot:empty>
          {{ $t('operation.noData') }}
        </template>
        <el-table-column prop="id" min-width='3%' :label="$t('operation.id')">
        </el-table-column>
        <el-table-column prop="context" min-width='20%' :label="$t('operation.context')">
        </el-table-column>
        <el-table-column prop="userName" min-width='5%' :label="$t('operation.username')">
        </el-table-column>
        <el-table-column prop="status" min-width='5%' :label="$t('operation.status')" :formatter="format2">
        </el-table-column>
        <el-table-column prop="startTime" min-width='10%' :label="$t('operation.subTime')">
        </el-table-column>
        <el-table-column prop="endTime" min-width='10%' :label="$t('operation.execTime')" :formatter="format1">
        </el-table-column>
      </el-table>
    </div>
    <div class="search-bar">
      <el-pagination text-align="center" @size-change="sizeChange" @current-change="currentChange"
                     :current-page="pageNow" :page-size="size" :page-sizes="[5, 10, 15, 20]"
                     layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>

import axios from "axios";

export default {
  name: 'OperationPage',
  components: {},
  mounted() {
    this.refresh();
  },
  data() {
    return {
      allStatus: [
        {
          name: 'operation.submitted',
          value: 'Submitted'
        },
        {
          name: 'operation.inProgress',
          value: 'In Progress'
        },
        {
          name: 'operation.succeed',
          value: 'Succeed'
        },
        {
          name: 'operation.failed',
          value: 'Failed'
        },
        {
          name: 'operation.warning',
          value: 'Warning'
        },
      ],
      tableData: [],
      pageNow: 1,
      size: 10,
      total: 0,
      recordContext: "",
      recordStatus: "",
    }
  },
  methods: {
    format1(row) {
      let date1, date2, execTime;
      date1 = new Date(row.startTime);
      date2 = new Date(row.endTime);
      execTime = date2 - date1;
      let s = 1000;
      let m = s * 60;
      let h = m * 60;
      let d = h * 24;
      if (execTime > d) {
        return parseInt(execTime / d) + "d " + parseInt((execTime % d) / h) + "h";
      } else if (execTime > h) {
        return parseInt(execTime / h) + "h " + parseInt((execTime % h) / m) + "m";
      } else if (execTime > m) {
        return parseInt(execTime / m) + "m " + parseInt((execTime % m) / s) + "s";
      } else if (execTime > s) {
        return parseInt(execTime / s) + "s " + parseInt(execTime % s) + "ms";
      } else {
        return execTime + "ms";
      }
    },
    format2(row) {
      let key = row.status;
      for (let item of this.allStatus) {
        if (key == item.value) {
          return this.$t(item.name);
        }
      }
      return "ERROR!!"
    },
    getQueryRecordInThisPage() {
      this.getQueryRecord();
    },
    refresh() {
      this.recordContext = "";
      this.recordStatus = "";
      this.search();
    },
    search() {
      this.pageNow = 1;
      this.getQueryRecord();
    },
    getQueryRecord() {
      axios
          .post("/sqlRecord/query", {
            context: (this.recordContext.length == 0) ? null : this.recordContext,
            status: (this.recordStatus == 0) ? null : this.recordStatus,
            pageId: this.pageNow,
            pageSize: this.size
          })
          .then((response) => {
            this.tableData = response.data.data;
            this.total = response.data.pagination.total;
          })
          .catch((err) => {
            console.log(err);
          });
    },
    currentChange(val) {
      this.pageNow = val;
      this.getQueryRecordInThisPage();
    },
    sizeChange(val) {
      this.size = val;
      this.pageNow = 1;
      this.getQueryRecordInThisPage();
    },
    clearContextInput() {
      this.recordContext = "";
      this.recordStatus = "";
    },
  }
}
</script>
<style lang="css">
.my-pad {
  padding-right: 20px;
}
.operation {
  margin-left: 20px;
  margin-right: 20px;
}
.search-bar {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 50px;
  width: 95%;
  background-color: white;
  padding: 5px 10px;
}

.search-bar2 {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  height: 50px;
  background-color: white;
  padding: 2px 4px;
}
</style>
