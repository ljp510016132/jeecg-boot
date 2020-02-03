<template>
  <a-modal
    :title="currTitle"
    :width="450"
    :visible="visible"
    :closable="false"
    :maskClosable="closable">
    <template slot="footer">
      <a-button v-if="closable" @click="close">关闭</a-button>
      <a-button type="primary" @click="organOk">确认</a-button>
    </template>

    <a-form>
      <a-form-item
        :labelCol="{span:4}"
        :wrapperCol="{span:20}"
        style="margin-bottom:10px"
        :validate-status="validate_status">
        <a-tooltip placement="topLeft" >
          <template slot="title">
            <span>您隶属于多部门，请选择当前所在部门</span>
          </template>
          <a-avatar style="backgroundColor:#87d068" icon="gold" />
        </a-tooltip>
        <a-select v-model="organSelected" :class="{'valid-error':validate_status=='error'}" placeholder="请选择登录部门" style="margin-left:10px;width: 80%">
          <a-icon slot="suffixIcon" type="gold" />
          <a-select-option
            v-for="d in organList"
            :key="d.id"
            :value="d.orgCode">
            {{ d.organName }}
          </a-select-option>
        </a-select>
      </a-form-item>
    </a-form>


  </a-modal>
    
</template>

<script>
  import { getAction,putAction } from '@/api/manage'
  import Vue from 'vue'
  import store from '@/store/'
  import { USER_INFO } from "@/store/mutation-types"

  export default {
    name: 'OrganSelect',
    props:{
      title:{
        type:String,
        default:"部门选择",
        required:false
      },
      closable:{
        type:Boolean,
        default:false,
        required:false
      },
      username:{
        type:String,
        default:"",
        required:false
      }
    },
    watch:{
      username(val){
        if(val){
          this.loadOrganList()
        }
      }
    },
    data(){
      return {
        currTitle:this.title,
        visible:false,
        organList:[],
        organSelected:"",
        validate_status:"",
        currOrganName:"",
      }
    },
    created(){
      //this.loadOrganList()
    },
    methods:{
      loadOrganList(){
        return new Promise(resolve => {
          let url = "/sys/user/getCurrentUserOrgans"
          this.currOrganName=''
          getAction(url).then(res=>{
            if(res.success){
              let organs = res.result.list
              let orgCode = res.result.orgCode
              if(organs && organs.length>0){
                for(let i of organs){
                  if(i.orgCode == orgCode){
                    this.currOrganName = i.organName
                    break
                  }
                }
              }
              this.organSelected = orgCode
              this.organList  = organs
              if(this.currOrganName){
                this.currTitle ="部门切换（当前部门 : "+this.currOrganName+"）"
              }

            }
            resolve()
          })
        })
      },
      close(){
        this.organClear()
      },
      organOk(){
        if(!this.organSelected){
          this.validate_status='error'
          return false
        }
        let obj = {
          orgCode:this.organSelected,
          username:this.username
        }
        putAction("/sys/selectOrgan",obj).then(res=>{
          if(res.success){
            const userInfo = res.result.userInfo;
            Vue.ls.set(USER_INFO, userInfo, 7 * 24 * 60 * 60 * 1000);
            store.commit('SET_INFO', userInfo);
            //console.log("---切换组织机构---userInfo-------",store.getters.userInfo.orgCode);
            this.organClear()
          }
        })
      },
      show(){
        //如果组件传值username此处就不用loadOrganList了
        this.loadOrganList().then(()=>{
          this.visible=true
          if(!this.organList || this.organList.length<=0){
            this.$message.warning("您尚未设置部门信息!")
            this.organClear()
          }
        })
      },
      organClear(){
        this.organList=[]
        this.organSelected=""
        this.visible=false
        this.validate_status=''
        this.currOrganName=""
      },
    }

  }
</script>
<style scoped>
  .valid-error .ant-select-selection__placeholder{
    color: #f5222d;
  }
</style>