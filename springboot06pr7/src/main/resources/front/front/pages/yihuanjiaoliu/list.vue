<template>
<mescroll-uni @init="mescrollInit" :up="upOption" :down="downOption" @down="downCallback" @up="upCallback">
	<view class="content">
		<view :style='{"padding":"0","alignItems":"flex-start","flexWrap":"wrap","background":"url() fixed,#fff","display":"flex","width":"100%","position":"relative","justifyContent":"space-between","height":"auto"}'>
			<view class="cu-bar bg-white search" style="width:100%" :style="[{top:CustomBar + 'px'}]">
				<picker v-if="queryList.length>1" mode="selector" :range="queryList" range-key="queryName" :value="queryIndex" @change="queryChange" style="padding-left: 20upx;">
					<view><image style="width: 20upx;height: 33upx;" src="../../static/center/to.png"></image></view>
				</picker>
					<view v-if="queryIndex==0" class="search-form round">
						<text class="cuIcon-search"></text>
						<input v-model="searchForm.biaoti" type="text" placeholder="标题" ></input>
					</view>
				<view class="action">
					<button @tap="search" class="cu-btn shadow-blur round">搜索</button>
				</view>
			</view>
			<view :style='{"width":"100%","alignItems":"flex-start","flexWrap":"wrap","background":"none","display":"flex","height":"auto"}'>

			
			<!-- 样式1 -->
			<view class="uni-product-list" :style='{"padding":"0px 24rpx 24rpx","margin":"24rpx 0 0","flexWrap":"wrap","background":"none","display":"flex","width":"calc(100% - 168rpx)","justifyContent":"space-between","height":"auto"}'>
				<view @tap="onDetailTap(product)" class="uni-product" :style='{"padding":"0px","boxShadow":"0 8rpx 0px #87acf1","margin":"0 0 40rpx","borderColor":"#e9be70","display":"flex","borderRadius":"0px","flexWrap":"wrap","borderWidth":"0px 0px 0px","background":"rgba(255,255,255,1)","width":"46%","position":"relative","borderStyle":"solid","height":"auto"}' v-for="(product,index) in list" :key="index">
					<view class="uni-product-title" :style='{"padding":"4rpx 20rpx","margin":"0 0 8rpx","whiteSpace":"nowrap","overflow":"hidden","color":"#333","borderRadius":"20%","textAlign":"center","background":"none","width":"96%","lineHeight":"48rpx","fontSize":"28rpx","textOverflow":"ellipsis"}'>{{product.biaoti}}</view>
					<view class="uni-product-title" :style='{"padding":"4rpx 20rpx","margin":"0 0 8rpx","whiteSpace":"nowrap","overflow":"hidden","color":"#333","borderRadius":"20%","textAlign":"center","background":"none","width":"96%","lineHeight":"48rpx","fontSize":"28rpx","textOverflow":"ellipsis"}'>{{product.shijian}}</view>
					<view class="uni-product-title" :style='{"padding":"4rpx 20rpx","margin":"0 0 8rpx","whiteSpace":"nowrap","overflow":"hidden","color":"#333","borderRadius":"20%","textAlign":"center","background":"none","width":"96%","lineHeight":"48rpx","fontSize":"28rpx","textOverflow":"ellipsis"}'>{{product.yonghuxingming}}</view>
					<view :style='{"width":"100%","padding":"8rpx 20rpx","justifyContent":"space-between","display":"flex","height":"auto","order":"5"}'>
						<view :style='{"display":"flex"}' v-if="(userid && isAuth('yihuanjiaoliu','修改')) || (!userid && isAuthFront('yihuanjiaoliu','修改'))" @click.stop="onUpdateTap(product.id)">
							<text :style='{"margin":"0 8rpx 0 0","fontSize":"28rpx","lineHeight":"1","color":"#666","display":"inline-block"}' class="cuIcon-edit"></text>
							<text :style='{"fontSize":"28rpx","lineHeight":"1","color":"#666","display":"inline-block"}'>修改</text>
						</view>
						<view :style='{"display":"flex"}' v-if="(userid && isAuth('yihuanjiaoliu','删除')) || (!userid && isAuthFront('yihuanjiaoliu','删除'))" @click.stop="onDeleteTap(product.id)">
							<text :style='{"margin":"0 8rpx 0 0","fontSize":"28rpx","lineHeight":"1","color":"#666","display":"inline-block"}' class="cuIcon-delete"></text>
							<text :style='{"fontSize":"28rpx","lineHeight":"1","color":"#666","display":"inline-block"}'>删除</text>
						</view>
					</view>
				</view>
			</view>
			
			
			
			

			</view>
			
			
			
		</view>

		<button :style='{"border":"2rpx solid #fff","cursor":"pointer","boxShadow":"0 4rpx 8rpx rgba(0,0,0,.3)","color":"#fff","textAlign":"center","bottom":"160rpx","right":"128rpx","outline":"none","borderRadius":"100%","background":"#6195ee","width":"88rpx","lineHeight":"88rpx","fontSize":"28rpx","position":"fixed","height":"88rpx","zIndex":"11111"}' v-if="userid && isAuth('yihuanjiaoliu','新增')" class="add-btn" @click="onAddTap()">新增</button>
		<button :style='{"border":"2rpx solid #fff","cursor":"pointer","boxShadow":"0 4rpx 8rpx rgba(0,0,0,.3)","color":"#fff","textAlign":"center","bottom":"160rpx","right":"128rpx","outline":"none","borderRadius":"100%","background":"#6195ee","width":"88rpx","lineHeight":"88rpx","fontSize":"28rpx","position":"fixed","height":"88rpx","zIndex":"11111"}' v-if="!userid && isAuthFront('yihuanjiaoliu','新增')" class="add-btn" @click="onAddTap()">新增</button>
	</view>
</mescroll-uni>
</template>

<script>
	export default {
		data() {
			return {
				btnColor: ['#409eff','#67c23a','#909399','#e6a23c','#f56c6c','#356c6c','#351c6c','#f093a9','#a7c23a','#104eff','#10441f','#a21233','#503319'],
				queryList:[
					{
						queryName:"标题",
					},
				],
				queryIndex: 0,
				list: [],
				lists: [],
                userid: '',
				mescroll: null, //mescroll实例对象
				downOption: {
					auto: false //是否在初始化后,自动执行下拉回调callback; 默认true
				},
				upOption: {
					noMoreSize: 5, //如果列表已无数据,可设置列表的总数量要大于半页才显示无更多数据;避免列表数据过少(比如只有一条数据),显示无更多数据会不好看; 默认5
					textNoMore: '~ 没有更多了 ~',
				},
				hasNext: true,
				searchForm:{},
				CustomBar: '0'
			};
		},
		computed: {
			baseUrl() {
				return this.$base.url;
			}
		},
		async onShow() {
			this.btnColor = this.btnColor.sort(()=> {
                                return (0.5-Math.random());
                        });
			this.hasNext = true
			// 重新加载数据
			if (this.mescroll) this.mescroll.resetUpScroll()
		},
		onLoad(options) {
            if(options.userid) {
                this.userid=options.userid;
            } else {
                this.userid = "";
            }
			this.hasNext = true
			// 重新加载数据
			if (this.mescroll) this.mescroll.resetUpScroll()
		},
		methods: {
            priceChange(price) {
                return Number(price).toFixed(2);
            },
            preHttp(str) {
                return str && str.substr(0,4)=='http';
            },
			//查询条件切换
			queryChange(e) {
				this.queryIndex=e.detail.value;
				this.searchForm.biaoti="";
			},
			//类别搜索
			// mescroll组件初始化的回调,可获取到mescroll对象
			mescrollInit(mescroll) {
				this.mescroll = mescroll;
			},
			/*下拉刷新的回调 */
			downCallback(mescroll) {
				this.hasNext = true
				// 重置分页参数页数为1
				mescroll.resetUpScroll()
			},
			/*上拉加载的回调: mescroll携带page的参数, 其中num:当前页 从1开始, size:每页数据条数,默认10 */
			async upCallback(mescroll) {
				let params = {
					page: mescroll.num,
					limit: mescroll.size
				}


				if(this.searchForm.biaoti){
					params['biaoti'] = '%' + this.searchForm.biaoti + '%'
				}



                let res = {}
                if(this.userid) {
                    res = await this.$api.page(`yihuanjiaoliu`, params);
                } else {
                    params['sfsh'] = '是';
                    res = await this.$api.list(`yihuanjiaoliu`, params);
                }
				// 如果是第一页数据置空
				if (mescroll.num == 1) this.list = [];
				this.list = this.list.concat(res.data.list);
				let length = Math.ceil(this.list.length/6)
				let arr = [];
				for (let i = 0; i<length; i++){
					arr[i] = this.list.slice(i*6, (i+1)*6)
				}
				this.lists = arr
				if (res.data.list.length == 0) this.hasNext = false;
				mescroll.endSuccess(mescroll.size, this.hasNext);
			},
			// 详情
			onDetailTap(item) {
                uni.setStorageSync("useridTag",this.userid);
				this.$utils.jump(`./detail?id=${item.id}&userid=`+this.userid)
			},
			// 修改
			onUpdateTap(id){
                uni.setStorageSync("useridTag",this.userid);
				this.$utils.jump(`./add-or-update?id=${id}`)
			},
			// 添加
			onAddTap(){
                uni.setStorageSync("useridTag",this.userid);
				this.$utils.jump(`./add-or-update`)
			},
			onDeleteTap(id){
				var _this = this;
				uni.showModal({
					title: '提示',
					content: '是否确认删除',
					success: async function(res) {
						if (res.confirm) {
							await _this.$api.del('yihuanjiaoliu', JSON.stringify([id]));
							_this.hasNext = true
							// 重置分页参数页数为1
							_this.mescroll.resetUpScroll()
						}
					}
				});
			},
			// 搜索
			async search(){
				this.mescroll.num = 1
				let searchForm = {
                    sfsh :'是',
					page: this.mescroll.num,
					limit: this.mescroll.size
				}
				if(this.searchForm.biaoti){
					searchForm['biaoti'] = '%' + this.searchForm.biaoti + '%'
				}
                let res = {};
                if(this.userid) {
                    res = await this.$api.page(`yihuanjiaoliu`, searchForm);
                } else {
                    res = await this.$api.list(`yihuanjiaoliu`, searchForm);
                }
				// 如果是第一页数据置空
				if (this.mescroll.num == 1) this.list = [];
				this.list = this.list.concat(res.data.list);
				let length = Math.ceil(this.list.length/6)
				let arr = [];
				for (let i = 0; i<length; i++){
					arr[i] = this.list.slice(i*6, (i+1)*6)
				}
				this.lists = arr
				if (res.data.list.length == 0) this.hasNext = false;
				this.mescroll.endSuccess(this.mescroll.size, this.hasNext);
			}
		}
	};
</script>

<style lang="scss" scoped>
	.content {
		min-height: calc(100vh - 44px);
		box-sizing: border-box;
	}
	
	.category-one .tab {
		cursor: pointer;
		padding: 0 20rpx 24rpx;
		margin: 0 8rpx;
		color: #333;
		display: inline-block;
		font-size: 28rpx;
		border-color: #069ffa;
		line-height: 160rpx;
		border-radius: 0;
		background: url(http://codegen.caihongy.cn/20221220/f084fc07fcb643b8b92acdba4752de67.png) no-repeat left top,url(http://codegen.caihongy.cn/20221220/1e7abd511d1147b8afeb6858d54a7aef.png) no-repeat right top,url(http://codegen.caihongy.cn/20221220/d9457110e9a34ee49c5994b567d884a9.png) repeat-x;
		width: auto;
		border-width: 0;
		border-style: solid;
		text-align: center;
		min-width: 126rpx;
		height: 122rpx;
	}
	
	.category-one .tab.active {
		cursor: pointer;
		padding: 0 20rpx;
		margin: 0 8rpx;
		color: #333;
		font-weight: 600;
		display: inline-block;
		font-size: 28rpx;
		border-color: #069ffa;
		line-height: 160rpx;
		border-radius: 0;
		background: url(http://codegen.caihongy.cn/20221220/f084fc07fcb643b8b92acdba4752de67.png) no-repeat left top,url(http://codegen.caihongy.cn/20221220/1e7abd511d1147b8afeb6858d54a7aef.png) no-repeat right top,url(http://codegen.caihongy.cn/20221220/d9457110e9a34ee49c5994b567d884a9.png) repeat-x;
		width: auto;
		border-width: 0 0 0px;
		border-style: solid;
		text-align: center;
		min-width: 126rpx;
		height: 122rpx;
	}
	
	.category-two .tab {
		cursor: pointer;
		padding: 0;
		color: #555;
		background: url(http://codegen.caihongy.cn/20221221/c8ff0c9649f9472e926677fcfdbbdd44.png) repeat-x left bottom,#d7eff8;
		display: inline-block;
		width: 100%;
		font-size: 28rpx;
		line-height: 68rpx;
		text-align: center;
		height: 80rpx;
	}
	
	.category-two .tab.active {
		cursor: pointer;
		background-color: #84cef8;
		padding: 0;
		color: #fff;
		background: url(http://codegen.caihongy.cn/20221221/c8ff0c9649f9472e926677fcfdbbdd44.png) repeat-x left bottom,#84cef8;
		font-weight: 600;
		display: inline-block;
		width: 100%;
		font-size: 28rpx;
		line-height: 74rpx;
		text-align: center;
		height: 80rpx;
	}
	
	.category-three .tab {
		cursor: pointer;
		padding: 0;
		color: #fff;
		background: burlywood;
		display: inline-block;
		width: 100%;
		font-size: 28rpx;
		line-height: 80rpx;
		text-align: center;
	}
	
	.category-three .tab.active {
		cursor: pointer;
		padding: 0;
		color: #fff;
		background: #000;
		display: inline-block;
		width: 100%;
		font-size: 28rpx;
		line-height: 80rpx;
		text-align: center;
	}
</style>
