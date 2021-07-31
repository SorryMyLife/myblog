<template>

  <div>

    <el-tabs type="card">
      <el-tab-pane label="听听音乐" name="first">
        <el-container>
          <el-header>
            <el-row :gutter="10">
              <el-col :span="20">
                <el-input placeholder="输入歌曲名称或者选择本地歌曲文件夹" v-model="input_music_name" clearable>
                </el-input>
              </el-col>
              <el-col :span="2">
                <el-button icon="el-icon-search" @click="music_search" circle></el-button>
              </el-col>
              <el-col :span="2">
                <el-button icon="el-icon-folder-add" @click="slocaldir" circle></el-button>
              </el-col>
            </el-row>
          </el-header>
          <el-main>
            <el-row :gutter="10">
              <el-col :span="8">
                <div>
                  <dl>
                    <dt v-for="(play_name,index) in play_list_show" :key="index">
                      <el-card>
                        <el-row>
                          <el-col :span="12">
                            {{play_name.name}}
                          </el-col>
                          <el-col :span="12">
                            <el-button icon="el-icon-video-play" @click="play_audio(play_name,index,$event)" circle></el-button>

                          </el-col>
                        </el-row>
                      </el-card>
                    </dt>
                  </dl>
                </div>
                <div>
                  <el-pagination v-if="play_list.length > 5" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" :current-page="default_current_change"
                    :page-sizes="[5,10, 20, 30, 40]" :page-size="default_size_change"
                    layout="total, sizes, prev, pager, next, jumper" :total="play_list.length">
                  </el-pagination>
                </div>
              </el-col>
              <el-col :span="8">
                <div>
                  <h1 style="display: none;" id="musicname"></h1>
                  <video style="display: none;" id="play" width="320" height="240" controls></video>

                </div>
                <div>
                  <el-button  id="back_btn" icon="el-icon-back" @click="back_m" circle></el-button>
                  <el-button  id="next_btn"  icon="el-icon-right" @click="next_m" circle></el-button>
                </div>
              </el-col>
              <el-col :span="8"></el-col>

            </el-row>
            <input type="file" id="filepicker" style="display: none;" name="fileList" @click="aa" webkitdirectory multiple />
          </el-main>

        </el-container>


      </el-tab-pane>
      <el-tab-pane label="看看视频" name="second">
        <div>
          <el-row :gutter="10">
            <el-col :span="20">
              <el-input placeholder="输入视频链接" v-model="input_video_link" clearable>
              </el-input>
            </el-col>
            <el-col :span="2">
              <el-button icon="el-icon-video-play" @click="to_video" circle></el-button>
            </el-col>

          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>


  </div>

</template>

<script>
  export default {
    data() {
      return {
        input_video_link: '',
        play_list: [],
        play_list_show: [],
        current_play_m: '',
        tmp_m: '',
        input_music_name: '',
        default_size_change: 5,
        default_current_change: 1,
        array_start: 0,
        array_end: 5
      };
    },
    methods: {
      to_video(){
        window.location.href="http://okjx.cc/?url="+this.input_video_link
      },
      showMb(){

        document.getElementById('musicname').style.display="block"
        document.getElementById('play').style.display="block"

      },
      back_m(){
        if(this.current_play_m == 0){
          this.current_play_m=5
          this.handleCurrentChange(this.default_current_change-1)
        }
        this.play_audio(this.play_list[this.getNowPlayIndexOnPlayList(this.current_play_m)-1],this.current_play_m-1)
      },
      next_m(){
        if(this.current_play_m == 4){
          this.current_play_m=-1
          this.handleCurrentChange(this.default_current_change+1)
        }
        this.play_audio(this.play_list[this.getNowPlayIndexOnPlayList(this.current_play_m)+1],this.current_play_m+1)
      },
      changePlayList() {
        this.play_list_show = []
        let plistLen=this.play_list.length
        let n = plistLen - this.array_end;
        if(n<this.default_size_change){
          this.array_end = this.array_end + n
        }
        // console.log("play end ::: ",this.array_end,n)
        for (; this.array_start < this.array_end; this.array_start++) {
          this.play_list_show.push(this.play_list[this.array_start])
        }
      },
      handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.default_current_change = 1
        this.default_size_change = val;
        this.array_start = 0
        this.array_end = this.default_size_change
        this.changePlayList()
      },
      handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.default_current_change = val;
        this.array_end = this.default_current_change * this.default_size_change
        this.array_start = this.array_end - this.default_size_change
        // console.log(this.array_start,this.array_end)
        this.changePlayList()
      },
      slocaldir() {
        let that = this
        document.getElementById("filepicker").click()
      },
      music_search() {
        this.$message({
          message: '暂未实现',
          type: 'warning'
        });
        console.log("music_search :: ", this.input_music_name)
      },

      aa(e) {
        let that = this
        document.getElementById("filepicker").addEventListener("change", function(event) {
          that.play_list = event.target.files
          for (; that.array_start < that.array_end; that.array_start++) {
            that.play_list_show.push(that.play_list[that.array_start])
          }
        }, false);

      },
      getNowPlayIndexOnPlayList(index) {
        let NowIndex = this.default_current_change * this.default_size_change - (this.default_size_change - index);
        return NowIndex;
      },
      play_audio(mp3file, index,btnevent) {
        // console.log(btnevent.currentTarget.firstElementChild.className)
        let that=this
        let name = mp3file.name
        let NowIndex = this.getNowPlayIndexOnPlayList(index)
        let audio_paly = document.getElementById('play')
        that.showMb()

        document.getElementById("musicname").innerText = name
        console.log("play::: ",NowIndex,this.play_list[NowIndex].name,that.current_play_m,index)

        mp3file = this.getUrl(mp3file)
        // console.log(mp3file)
        audio_paly.src = mp3file
        audio_paly.onload = function(e) {
          window.URL.revokeObjectURL(audio_paly.src);
        }

          if(audio_paly.paused){
            audio_paly.play();
          }else{
            audio_paly.pause()
          }

        that.current_play_m = index
        audio_paly.onended = function() {
          if (NowIndex < (that.play_list.length -1)) {
            if(index < that.default_size_change-1){
              that.play_audio(that.play_list[NowIndex+1], index+1)
            }else{
              if(NowIndex>= ((that.default_current_change*that.default_size_change ) -1)){
                that.handleCurrentChange(that.default_current_change+1)
              }
              that.play_audio(that.play_list[NowIndex+1], 0)
            }
          } else {
            that.handleCurrentChange(1)
            that.play_audio(that.play_list[0], 0)
          }
        }
      },
      getUrl(flie) {
        let url = ''
        if (window.createObjectURL != undefined) { // basic
          url = window.createObjectURL(flie);
        } else if (window.webkitURL != undefined) { // webkit or chrome
          url = window.webkitURL.createObjectURL(flie);
        } else if (window.URL != undefined) { // mozilla(firefox)
          url = window.URL.createObjectURL(flie);
        }
        return url
      }
    }
  }
</script>

<style>
</style>
