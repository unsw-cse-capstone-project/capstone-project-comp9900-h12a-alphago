<template>
  <div class="home">
    <Header>
      <template v-if="this.hasLogin">
        <el-dropdown
          trigger="hover"
          @command="handleCommand"
          style="align-items: center"
          placement="bottom"
        >
          <div class="user">
            <el-badge
              v-if="parseInt(this.unread) !== 0"
              :value="this.unread"
              :max="99"
              class="item"
            >
              <el-avatar :size="70" :src="avatar"></el-avatar>
            </el-badge>
            <el-avatar v-else :size="70" :src="avatar"></el-avatar>
          </div>

<!--            <div class="drop">-->
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile" icon="el-icon-user-solid">
              My Profile</el-dropdown-item
            >
            <el-dropdown-item command="property" icon="el-icon-house">
              My Properties</el-dropdown-item
            >
            <el-dropdown-item command="auction" icon="el-icon-s-home">
              My Auctions</el-dropdown-item
            >

            <el-dropdown-item command="notification" icon="el-icon-bell">
              Notifications
              <el-badge
                v-show="parseInt(this.unread) !== 0"
                class="mark"
                :value="this.unread"
                style="padding: 0; background-color: white"
              />
            </el-dropdown-item>

            <el-dropdown-item command="logout" icon="el-icon-turn-off">
              Log out</el-dropdown-item
            >
          </el-dropdown-menu>
<!--            </div>-->
        </el-dropdown>
      </template>
      <template v-else>
        <div class="back-btn">
          <span
            id="back-btn"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('login')"
            >Sign In <i class="el-icon-check"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
        <div class="back-btn">
          <span
            id="back-btn2"
            style="padding: 2px 5px; font-size: 20px"
            @click="goto('register')"
            >Sign Up <i class="el-icon-user"></i
          ></span>
          <div class="bottom-line"></div>
        </div>
      </template>
    </Header>

    <div class="main" id="main">
       <el-row type="flex" justify="center" class="search">
        <el-col :span="15">
          <div class="input">
            <el-col :span="24">
<!--              <div style="background-color: white;border-radius:50px 0 0 50px  ">-->
              <el-row type="flex" justify="center" >
              <el-col :span="1" style="z-index: 5;">
                <div style="z-index: 5; height:49px; border-radius:50px 0 0 50px;">

                </div>
              </el-col>
              <el-col :span="21">
              <vue-google-autocomplete
                class="auto-input"
                ref="address"
                id="address"
                classname="form-control"
                placeholder="Please search suburb name or postcode here"
                v-on:placechanged="getAddressData"
                @keyup.enter="toSearch"
                country="au"
                style="border:0; height:49px;z-index: 2; border-radius: 0"
                types="(cities)"
              >
              </vue-google-autocomplete>
              </el-col>
                <el-col  :span="2" id="search-btn">
                  <el-button
                          style="
                  border-radius:0 50px 50px 0;
                  height:49px;
                  color: white;
                  border:0;
                  background-color: rgb(20, 60, 127);
                "
                          icon="el-icon-search"
                          @click="toSearch"
                  >
                    Search
                  </el-button>
                </el-col>
              </el-row>
<!--              </div>-->
            </el-col>
          </div>
        </el-col>
      </el-row>
      <el-row type="flex" justify="center">
        <el-col :span="6"> </el-col>
        <el-col :span="6"> </el-col>
        <el-col :span="6"> </el-col>
      </el-row>
    </div>
    <Footer></Footer>
  </div>
</template>

<script>
// @ is an alias to /src
import Header from "@/components/Header.vue";
import { mapActions } from "vuex";
import $ from "jquery";
import Canvas from "@/utils/canvas-nest.min.js";
import VueGoogleAutocomplete from "vue-google-autocomplete";


export default {
  name: "Home",
  title: "AlphaGo Home",
  components: {
    Header,
    VueGoogleAutocomplete,
    // Canvas
  },
  data() {
    return {
      hasLogin: false,
      bathNum: 1,
      bedroomNum: 1,
      carNum: 1,
      address:"",
      unread: "",

      // 浏览器宽度
      screenWidth: 0,

    };
  },
  created() {
    this.username = localStorage.getItem("username");
    // this.username = this.$store.state.username;
    //   this.username='123'
    if (this.username !== null) {
      this.hasLogin = true;
      this.avatar = localStorage.getItem("avatar");
      this.$axios
        .get("/notification/unread")
        .then((response) => {
          if (response.data.code === 200) {
            this.unread = response.data.result;
          }else if(response.data.code === 400){
              this.$message.error(response.data.msg);
          }else{
              console.log(response.data.msg);
          }
        })
        .catch(function (error) {
          this.$message.error(error);
        });
    }
    this.firstname = localStorage.getItem("firstname");
  },

  methods: {
    ...mapActions(["logout"]),

    openValue() {
      this.show = !this.show;
    },
    getvalue(index, item) {
      this.value = item.name;
      this.show = false;
    },

    getAddressData: function (addressData, placeResultData, id) {
      this.address = addressData;
    },

    handleCommand(command) {
      switch (command) {
        case "profile":
          this.$router.push("/profile");
          break;
        case "property":
          this.$router.push("/propmag");
          break;
        case "auction":
          this.$router.push("/auctionmag");
          break;
        case "notification":
          this.$router.push("/notice");
          break;
        case "logout":
          this.$axios.post("/user/logout").then((response) => {
            if (response.status >= 200 && response.status < 300) {
              if (response.data.code === 200) {
                this.logout();
                location.reload();
              } else if(response.data.code === 400){
                  this.$message.error(response.data.msg);
              }else{
                  console.log(response.data.msg);
              }
            } else {
              console.log(response.data.msg);
            }
          });
          break;
        default:
          break;
      }
    },
    toSearch() {
      var addr = document.getElementById("address").value;
      if (addr===""){
        this.$router.push({
          path:"/search",
          query:{
            currPage:1,
          },
        });
        return ;
      }
      if (isNaN(addr)) {
        if (this.address.locality === undefined){
          this.$message.error("Please validate the suburb name by Google Map first!");
          return;
        }
        let state = this.address.administrative_area_level_1;
        let latitude = this.address.latitude;
        let longitude = this.address.longitude;
        addr = this.address.locality;
        this.$router.push({
          path: "/search",
          query: {
            suburb: addr,
            state: state,
            lat : latitude,
            lng: longitude,
            currPage:1,
          },
        });
      } else {
        if (addr.toString().length != 4) {
          this.$message.error("Please input a valid postcode!");
          return;
        }
        this.$router.push({
          path: "/search",
          query: {
            postcode: addr,
            currPage: 1,
          },
        });
      }
    },
    goto(name) {
      this.$router.push({ name: name });
    },
  },

  mounted() {
    $("#back-btn").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });

    $("#back-btn2").hover(function (event) {
      $(this).stop().animate({ "margin-left": "10px" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "100px" }, 300);
    });
    $("#back-btn2").mouseleave(function (event) {
      $(this).stop().animate({ "margin-left": "0" }, 300);
      $(this).next(".bottom-line").stop().animate({ width: "0" }, 300);
    });

    $("#main").hover(function (event) {
      $(this).stop().animate({ opacity: "0.9" }, 300);
    });
    $("#main").mouseleave(function (event) {
      $(this).stop().animate({ opacity: "0.8" }, 300);
    });
  },
};
</script>

<style lang="scss">
.user {
  display: flex;
  align-items: center;
}
.main {
  height: calc(100vh - 165px);
  opacity: 0.8;
  z-index: -3;
  /*box-shadow: inset 0 2px 10px 4px #1a1b1d;*/
  background-image: url("../assets/bg.png");
  background-size:100% 100%;
  min-height:700px;
}

.home{
  min-height:762px;
  max-height:100%;
  display: block;
}

@media only screen and (max-width: 1300px) and (min-width: 1100px) {
  .main {
    height: calc(100vh - 165px);
    opacity: 0.8;
    z-index: -3;
    /*box-shadow: inset 0 2px 10px 4px #1a1b1d;*/
    background-image: url("../assets/bg.png");
  }
}

@media only screen and (max-width: 1100px) {
  .main {
    background-image: url("../assets/main-bg_copy.png");
  }
}


.search {
  .input {
    margin: 20% auto;
  }
}

.search-criteria {
  .el-col {
    display: flex;
    align-items: center;
    text-align: center;
  }
  img {
    width: 35px;
    height: 35px;
    margin-right: 10px;
  }
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.back-btn {
  cursor: pointer;
  position: relative;
  width: 150px;
  /*border:1px solid #123123;*/
}
.back-btn .bottom-line {
  position: absolute;
  /*margin-left:40px;*/
  width: 0;
  height: 2px;
  left: 0;
  bottom: -1px;
  background-color: #3b4c73;
}
.el-dropdown-menu {
  padding: 25px 20px 25px 10px !important;
  border-radius: 0 !important;
}

 .el-dropdown-menu__item {
  &:hover {
    transform: translateX(10px);
    transition-duration: 0.5s;
    border-bottom: 2px solid rgba(4, 10, 30, 0.96);
    background-color: white !important;
    color: #143469 !important;
  }
}
  .auto-input{
    outline:none !important;
  }



</style>
