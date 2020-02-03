<template>
  <div class="components-input-demo-presuffix">
    <!---->
    <a-input @click="openModal" placeholder="请点击选择部门" v-model="orgNames" readOnly :disabled="disabled">
      <a-icon slot="prefix" type="cluster" title="部门选择控件"/>
      <a-icon v-if="orgIds" slot="suffix" type="close-circle" @click="handleEmpty" title="清空"/>
    </a-input>

    <j-select-org-modal
      ref="innerOrgSelectModal"
      :modal-width="modalWidth"
      :multi="multi"
      :rootOpened="rootOpened"
      :org-id="orgIds"
      @ok="handleOK"
      @initComp="initComp"/>
  </div>
</template>

<script>
  import JSelectOrgModal from './modal/JSelectOrgModal'
  export default {
    name: 'JSelectOrg',
    components:{
      JSelectOrgModal
    },
    props:{
      modalWidth:{
        type:Number,
        default:500,
        required:false
      },
      multi:{
        type:Boolean,
        default:false,
        required:false
      },
      rootOpened:{
        type:Boolean,
        default:true,
        required:false
      },
      value:{
        type:String,
        required:false
      },
      disabled:{
        type: Boolean,
        required: false,
        default: false
      },
      // 自定义返回字段，默认返回 id
      customReturnField: {
        type: String,
        default: 'id'
      }
    },
    data(){
      return {
        visible:false,
        confirmLoading:false,
        orgNames:"",
        orgIds:''
      }
    },
    mounted(){
      this.orgIds = this.value
    },
    watch:{
      value(val){
        if (this.customReturnField === 'id') {
          this.orgIds = val
        }
      }
    },
    methods:{
      initComp(orgNames){
        this.orgNames = orgNames
      },
      openModal(){
        this.$refs.innerOrgSelectModal.show()
      },
      handleOK(rows, idstr) {
        let value = ''
        if (!rows && rows.length <= 0) {
          this.orgNames = ''
          this.orgIds = ''
        } else {
          value = rows.map(row => row[this.customReturnField]).join(',')
          this.orgNames = rows.map(row => row['orgName']).join(',')
          this.orgIds = idstr
        }
        this.$emit("change", value)
      },
      getOrgNames(){
        return this.orgNames
      },
      handleEmpty(){
        this.handleOK('')
      }
    },
    model: {
      prop: 'value',
      event: 'change'
    }
  }
</script>

<style scoped>
  .components-input-demo-presuffix .anticon-close-circle {
    cursor: pointer;
    color: #ccc;
    transition: color 0.3s;
    font-size: 12px;
  }
  .components-input-demo-presuffix .anticon-close-circle:hover {
    color: #f5222d;
  }
  .components-input-demo-presuffix .anticon-close-circle:active {
    color: #666;
  }
</style>