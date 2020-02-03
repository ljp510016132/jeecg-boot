<template>
  <a-modal
    :width="modalWidth"
    :visible="visible"
    title="部门搜索"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
    cancelText="关闭"
    wrapClassName="ant-modal-cust-warp"
    >
    <!--部门树-->
    <template>
      <a-form :form="form">
      <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="上级部门">
      <a-tree
        multiple
        treeCheckable="tree"
        checkable
        :checkedKeys="checkedKeys"
        allowClear="true"
        :checkStrictly="true"
        @check="onCheck"
        :dropdownStyle="{maxHeight:'200px',overflow:'auto'}"
        :treeData="organTree"
        placeholder="请选择上级部门"
        >
      </a-tree>
      </a-form-item>
      </a-form>
    </template>
  </a-modal>
</template>

<script>
  import pick from 'lodash.pick'
  import { getAction } from '@/api/manage'
  import { queryIdTree } from '@/api/api'
  import userModal from './UserModal'
  export default {
    name: "OrganWindow",
    components: {
      userModal,
    },
    data () {
      return {
        checkedKeys:[], // 存储选中的部门id
        userId:"", // 存储用户id
        model:{}, // 存储SysUserOrgansVO表
        userOrganModel:{userId:'',organIdList:[]}, // 存储用户id一对多部门信息的对象
        organList:[], // 存储部门信息
        modalWidth:400,
        organTree:[],
        title:"操作",
        visible: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        headers:{},
        form:this.$form.createForm(this),
        url: {
          userId:"/sys/user/generateUserId", // 引入生成添加用户情况下的url
        },
      }
    },
    methods: {
      add (checkedOrganKeys,userId) {
        this.checkedKeys = checkedOrganKeys;
        this.userId = userId;
        this.edit({});
      },
      edit (record) {
        this.organList = [];
        this.queryOrganTree();
        this.form.resetFields();
        this.visible = true;
        this.model = Object.assign({}, record);
        let filedsVal = pick(this.model,'id','userId','organIdList');
        this.$nextTick(() => {
          this.form.setFieldsValue(filedsVal);
        });
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.organList = [];
        this.checkedKeys = [];
      },
      handleSubmit () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err) => {
          if (!err) {
            that.confirmLoading = true;
            if(this.userId == null){
              getAction(this.url.userId).then((res)=>{
                if(res.success){
                  let formData = {userId:res.result,
                  organIdList:this.organList}
                  console.log(formData)
                  that.$emit('ok', formData);
                }
              }).finally(() => {
                that.organList = [];
                that.confirmLoading = false;
                that.close();
              })
            }else {
              let formData = {userId:this.userId,
                organIdList:this.organList}
              console.log(formData)
              that.organList = [];
              that.$emit('ok', formData);
              that.confirmLoading = false;
              that.close();
            }
          }
        })
      },
      handleCancel () {
        this.close()
      },

      // 选择部门时作用的API
      onCheck(checkedKeys, info){
        this.organList = [];
        this.checkedKeys = checkedKeys.checked;
        let checkedNodes = info.checkedNodes;
        for (let i = 0; i < checkedNodes.length; i++) {
          let de = checkedNodes[i].data.props;
          let organ = {key:"",value:"",title:""};
          organ.key = de.value;
          organ.value = de.value;
          organ.title = de.title;
          this.organList.push(organ);
        }
        console.log('onCheck', checkedKeys, info);
      },
      queryOrganTree(){
        queryIdTree().then((res)=>{
          if(res.success){
            this.organTree = res.result;
          }
        })
      },
      modalFormOk(){

      }
      },
  }
</script>

<style scoped>
  .ant-table-tbody .ant-table-row td{
    padding-top:10px;
    padding-bottom:10px;
  }
</style>