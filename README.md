&emsp;&emsp;由于目前市面上的app大部分是由原生和H5独立或混合编写，本文尝试对原生项目进行改造，实现原生(Android)和uni-app以及html5项目混编，使其能在iOS、Android、H5、小程序等多个平台运行，从而实现跨平台开发。

### [第一种实现方式：在uni-app宿主项目中添加原生以及H5插件模块](https://ask.dcloud.net.cn/article/354160)

#### 第一步：新建uni-app项目

&emsp;&emsp;参照：[第一篇：快速上手uni-app项目](https://www.jianshu.com/p/b298c55a095c)

#### 第二步：调用H5插件模块

&emsp;&emsp;将编写好的html使用uniapp中提供的[web-view](https://uniapp.dcloud.io/component/web-view)组件装载到app中运行，web-view是一个 web 浏览器组件，它可以用来承载网页的容器，会自动铺满整个页面。

&emsp;&emsp;uniapp可以用来加载本地和网络网页，小程序仅支持加载网络网页[(个人类型与海外类型的小程序不支持使用 web-view 组件)](https://developers.weixin.qq.com/miniprogram/dev/component/web-view.html)，App 平台同时支持网络网页和本地网页，但本地网页及相关资源（js、css等文件）必须放在 uni-app 项目根目录->hybrid->html 文件夹下，如下为一个加载本地网页的uni-app项目文件目录示例：
```
┌─components         //目录：组件文件存放目录
├─hybrid             //目录：本地网页及相关资源
│  └─html
│     ├─css
│     │  └─test.css
│     ├─img
│     │  └─icon.png
│     ├─js
│     │  └─test.js
│     └─local.html
├─pages              //目录：业务页面文件存放目录
│  └─index
│     └─index.vue
├─static             //目录：存放应用引用静态资源（如图片、视频等）的地方，注意：静态资源只能存放于此
├─main.js            //Vue初始化入口文件
├─App.vue            //应用配置，用来配置App全局样式以及监听应用的生命周期
├─manifest.json      //配置应用名称、appid、logo、版本等打包信息
└─pages.json         //配置页面路由、导航条、选项卡等页面类信息
```
**&emsp;&emsp;1.uniapp加载本地网页示例代码：**
```
<template>
    <view>
        <web-view src="/hybrid/html/local.html"></web-view>
    </view>
</template>
```
**&emsp;&emsp;2.uniapp加载网络网页示例代码：**
```
<template>
    <view>
        <web-view  src="https://uniapp.dcloud.io/static/web-view.html"></web-view>
    </view>
</template>
```

更多详见：
&emsp;&emsp;[1.web-view组件DCloud官方介绍](https://uniapp.dcloud.io/component/web-view)
&emsp;&emsp;[2.区别于传统 web 开发的注意](https://uniapp.dcloud.io/matter?id=%e5%8c%ba%e5%88%ab%e4%ba%8e%e4%bc%a0%e7%bb%9f-web-%e5%bc%80%e5%8f%91%e7%9a%84%e6%b3%a8%e6%84%8f)

#### 第三步：调用原生(Android)插件模块
&emsp;&emsp;[官方参考文档：Android uni-app原生插件开发文档 ](https://ask.dcloud.net.cn/article/35416)
&emsp;&emsp;[官方示例代码：最新Andorid平台SDK下载（UniPlugin-Hello-AS）](http://download.dcloud.net.cn/Android-SDK@1.9.9.56749_20181226-1.zip)

&emsp;&emsp;该部分内容实践，我们将按照以下步骤进行：

**&emsp;&emsp;第1步：新建一个Android项目**

&emsp;&emsp;使用Android Studio IDE点击左上角File->New->New Peoject，填写Application name等信息，一路Next，最后Finish就创建好了一个Android工程。
![新建一个Android项目](https://upload-images.jianshu.io/upload_images/2783386-9e00f9d714b4beaf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;Android应用开发这里不详细叙述，如需学习出门问谷歌。

**&emsp;&emsp;第2步：改造已有Android项目，使其支持uniapp运行**

&emsp;&emsp;&emsp;&emsp;[参考：使用新版本5+SDK创建最简Android原生工程](http://ask.dcloud.net.cn/article/13232)

&emsp;&emsp;&emsp;&emsp;1.复制[SDK](http://download.dcloud.net.cn/Android-SDK@1.9.9.56749_20181226-1.zip)->libs->lib.5plus.base-release.aar文件到原生工程工程的app->libs目录下

![引入SDK](https://upload-images.jianshu.io/upload_images/2783386-f4ddd1ede2de068b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;&emsp;&emsp;2.修改工程的build.gradle文件
&emsp;&emsp;&emsp;&emsp;2.1 添加aar文件引用到dependencies,代码如下:
```
implementation(name: 'lib.5plus.base-release', ext: 'aar')
```

![添加aar文件引用](https://upload-images.jianshu.io/upload_images/2783386-d4386f5de8c083ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;&emsp;&emsp;2.2 添加aar文件搜索路径添到gradle文件，与dependencies同级, 代码如下:
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
```

![添加aar文件搜索路径](https://upload-images.jianshu.io/upload_images/2783386-2ba0c109f4e3d5b6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;&emsp;&emsp;3.修改工程的Androidmanifest.xml文件
&emsp;&emsp;&emsp;&emsp;3.1 修改application代码如下：
```
android:name="io.dcloud.application.DCloudApplication"
```

![增加applition name](https://upload-images.jianshu.io/upload_images/2783386-45935400c3835eaa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;&emsp;&emsp;3.2 修改代码如下：
```
<activity
            android:name="io.dcloud.PandoraEntry"
            android:configChanges="orientation|keyboardHidden|keyboard|navigation"
            android:label="@string/app_name"
            android:launchMode="singleTask"
            android:hardwareAccelerated="true"
            android:theme="@style/TranslucentTheme"
            android:screenOrientation="user"
            android:windowSoftInputMode="adjustResize" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```

![增加uniapp启动入口页面](https://upload-images.jianshu.io/upload_images/2783386-40a08afa8c4db4b0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**&emsp;&emsp;第3步：开发Android原生插件**
点击Android Studio 菜单项 File->New->New Module,选择Android Library, 点击Next.填写与插件相关信息点击finish。
![创建Android插件Module](https://upload-images.jianshu.io/upload_images/2783386-f989aa8c6f631c51.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

**第4步：uniapp代码中调用原生插件**
```
               // require插件名称
				const dcRichAlert = uni.requireNativePlugin('DCloud-RichAlert');
			   // 使用插件
			   dcRichAlert.show({
			       position: 'bottom',
			       title: "提示信息",
			       titleColor: '#FF0000',
			       content: "<a href='https://uniapp.dcloud.io/' value='Hello uni-app'>uni-app</a> 是一个使用 Vue.js 开发跨平台应用的前端框架!\n免费的\n免费的\n免费的\n重要的事情说三遍",
			       contentAlign: 'left',
			       checkBox: {
			           title: '不再提示',
			           isSelected: true
			       },
			       buttons: [{
			           title: '退下'
			       },
			       {
			           title: '明白',
			           titleColor: '#3F51B5'
			       }
			       ]
			   }, result => {
			       switch (result.type) {
			           case 'button':
			               console.log("callback---button--" + result.index);
			               break;
			           case 'checkBox':
			               console.log("callback---checkBox--" + result.isSelected);
			               break;
			           case 'a':
			               console.log("callback---a--" + JSON.stringify(result));
			               break;
			           case 'backCancel':
			               console.log("callback---backCancel--");
			               break;
			      }
			   });
```

**&emsp;&emsp;第5步：将uni-app添加到Android项目中运行**

&emsp;&emsp;在HbuilderX中选择将要添加到原生中uni-app项目点击发行->原生App-本地打包，将打包后的文件如下：

![](https://upload-images.jianshu.io/upload_images/2783386-eb85206208974103.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;将打包后的文件放入Android assets目录下：
![](https://upload-images.jianshu.io/upload_images/2783386-681cf56e37425b55.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

&emsp;&emsp;将Android assets目录下添加以下文件，具体代码[github FeedBackStar-Android](https://github.com/XieXiePro/FeedBackStar-Android)下载地址：
![](https://upload-images.jianshu.io/upload_images/2783386-df1717e6ce8e4234.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


&emsp;&emsp;程序运行截图：
![](https://upload-images.jianshu.io/upload_images/2783386-8eaca6931bb5fe2e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



&emsp;&emsp;至此，在uni-app宿主项目中添加原生以及H5插件模块实践基本完成。

&emsp;&emsp;**总结：**该方式的优点是项目能直接发布为小程序（在微信小程序中直接调用自己编写的原生插件模块还未实践）；缺点是对已开发好的原生项目改动较大，不能使用cookie等，封闭代码库和兼容性问题等。

### [第二种实现方式：在原生宿主项目中运行uni-app以及H5插件模块](http://ask.dcloud.net.cn/article/66)

#### 第一步：新建Android项目
#### 第二步：引入aar包
#### 第三步：调用H5插件模块
#### 第四步：调用uniapp插件模块

&emsp;&emsp;该方式的优点是对于已经开发好的原生项目改动较小；缺点是只能将插件模块单独发布为小程序而不能将宿主和插件一并发布为小程序，封闭代码库和兼容性问题。

&emsp;&emsp;完整项目代码见：https://github.com/XieXiePro/FeedBackStar-Android