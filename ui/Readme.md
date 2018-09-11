## UI模块使用说明

### 更新日志

- 2018-09-07 

> 添加水印（使用说明见*水印*）

### 使用说明

**水印**

> 现有水印的实现是在DecorView下的Content 添加一子View，然后通过给View 设置背景来实现

1. 启用

   ```java
   //启用水印 mark 水印字符   
   enableWaterMark("");
   ```

2. 禁用

   ```java
   disableWaterMark();
   ```

3. 自定义

   ```java
   //实现自定义水印效果，返回View
   protected View onCreateWaterMarkView(String waterMark, int width, int height){
     //...
   }
   ```
