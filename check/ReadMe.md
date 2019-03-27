## 检查模块使用说明

### 一、声明

> 1. 此模块是在web模块基础上进行接口的扩展，故涉及到web模块内的修改，此模块不做改动。
> 
> 2. 此模块是通用于检查性质的业务
> 
> 3. 具体的业务，还需继承扩展，以适应不通的业务需求

### 二、具体接口说明：

> 前端调用格式均为如下格式，也有简略的方式，具体可以参考 注入的js源码
> 
> 文档内参数分为 **name**、**data**、**callBackJson** 进行说明
> 
> ```javascript
>  window.WebViewJavascriptBridge
>    .callHandler("name",
>                 data,
>                 function (responseData) {
>                   show(responseData);
>                   });
> ```
> 
> 返回的数据格式均以以下方式包裹，以下文档只显示，data部分
> 
> ```json
> {
> "code": 200,
> "data":"",
> "msg": "XXXX",
> "serverCode": 0
> }
> ```

#### 1. 接口

1. 获取位置信息

   |      | 值               | 参考  | 说明                                         |
   |:----:|:---------------:|:---:|:------------------------------------------:|
   | name | location        | --  |                                            |
   | data | true/false/null | --  | true表示在未获取到地址的情况下吗，等待获取到位置信息再返回结果，默认为false |
   | json | --              |     | --                                         |

2. 打开相机

   打开相机现在扩展为两个，一个是只获取照片路径，另一个获取照片携带的Exif信息

   |      | 值      | 参考                                                                                 |
   | ----:|:------:|:----------------------------------------------------------------------------------:|
   | name | camera | --                                                                                 |
   | data | --     | --                                                                                 |
   | json | --     | /data/user/0/com.codvision.check/files/DLD_20180903_163444_2586436440059892000.jpg |

   |      | 值          | 参考                                                                                                                       |
   |:----:|:----------:|:------------------------------------------------------------------------------------------------------------------------:|
   | name | camera_ext | --                                                                                                                       |
   | data | --         | --                                                                                                                       |
   | json | --         | /data/user/0/com.codvision.check/files/DLD_20180903_163444_2586436440059892000.jpg;30.400189972222222;120.27969797222222 |

3. 录音

   |      | 值      | 参考                                                                                 |
   |:----:|:------:|:----------------------------------------------------------------------------------:|
   | name | record | --                                                                                 |
   | data | --     | --                                                                                 |
   | json | --     | /data/user/0/com.codvision.check/files/DLD_20180903_163444_2586436440059892000.aac |

4. 获取图片

   |      | 值       | 参考                                                                                 |
   |:----:|:-------:|:----------------------------------------------------------------------------------:|
   | name | picture | --                                                                                 |
   | data | --      | --                                                                                 |
   | json | --      | /data/user/0/com.codvision.check/files/DLD_20180903_163444_2586436440059892000.jpg |

   |      | 值           | 参考                                                                                                                       |
   |:----:|:-----------:|:------------------------------------------------------------------------------------------------------------------------:|
   | name | picture_ext | --                                                                                                                       |
   | data | --          | --                                                                                                                       |
   | json | --          | /data/user/0/com.codvision.check/files/DLD_20180903_163444_2586436440059892000.jpg;30.400189972222222;120.27969797222222 |

5. 文件上传

   |      | 值        | 参考  |
   |:----:|:--------:|:---:|
   | name | upload   | --  |
   | data | String数组 |     |
   | json | --       |     |

   |      | 值          | 参考  |
   |:----:|:----------:|:---:|
   | name | upload_new | --  |
   | data | String数组   |     |
   | json | --         |     |

6. 跳转

   |      | 值          | 参考  |
   |:----:|:----------:|:---:|
   | name | "openLink" | --  |
   | data | String     | --  |
   | json | --         |     |

7. 二维码扫描

   |      | 值      | 参考  |
   |:----:|:------:|:---:|
   | name | qrcode | --  |
   | data | --     | --  |
   | json | --     |     |

   |      | 值      | 参考  | 说明  |
   |:----:|:------:|:---:| --- |
   | name | qr     | --  | --  |
   | data | QrData |     | 见下图 |
   | json | --     |     | --  |

   QrData实体类属性如下：

   ```java
   public static final class QrData {
           /**
            * 二维码操作类别 1 识别 2 生成
            */
           private int type;
           /**
            * 二维码生成时提供的Str
            */
           private String str;
   ...
       }
   ```

8. 地图导航

   |      | 值          | 参考                |
   |:----:|:----------:|:-----------------:|
   | name | nav        | --                |
   | data | JsonObject | {lat:0.0,lng:0.0} |
   | json | --         | --                |

9. 返回上一页

   |      | 值    | 参考  |
   |:----:|:----:|:---:|
   | name | back | --  |
   | data | --   | --  |
   | json | --   | --  |

10. 退出容器

    |      | 值    | 参考  |
    |:----:|:----:|:---:|
    | name | exit | --  |
    | data | --   | --  |
    | json | --   | --  |

#### 2.对URL的响应

> 此内容在Web模块内，在此简单介绍

**1.图片、视频资源**

```tex
//图片
NativeImage://[路径]
//音频
NativeAudio:// [路径]
```

**2.短信、电话**

> 对短信、电话的响应是解析通用的格式，并未单独做协议定义。可参考 [https://www.cnblogs.com/liuhongfeng/p/4976599.html](https://www.cnblogs.com/liuhongfeng/p/4976599.html)
> 
> 如对现有实现不满意，可在继承的WebViewActivity内重写对应的处理方法
> 
> ```java
>        /**
>      * 打电话
>      *
>      * @param url 格式 tel:10086
>      * @return
>      */
>     protected boolean onTel(final String url) {
>         return true;
>     }
> 
>     /**
>      * 发短信
>      *
>      * @param url 格式 sms:10086,10010,10000?body=sadadsad阿打算打的实打实大的宣传
>      * @return
>      */
>     protected boolean onSms(String url) {
>         return true;
>     }
> 
>     /**
>      * 发邮件
>      *
>      * @param url
>      * @return
>      */
>     protected boolean onMailTo(String url) {
>         return false;
>     }
> ```

```html
<a href="tel:10086">拨打 10086</a>
<a href="sms:10086,10010?body=ceshi">短信10086</a>
```

### 三、版本更新

- V1.1.7.4

  > 添加视频录制时参数控制，存在与Data中；参数类如下 
  > 
  > ```java
  > class VideoConfig {
  > 
  >         /**
  >          * 最大录制时间 单位 s
  >          */
  >         private int timeMax;
  >         /**
  >          * 最小录制时间 单位 s
  >          */
  >         private int timeMin;
  >   //setter getter 略
  > }
  > ```
