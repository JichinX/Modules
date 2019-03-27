//===================必须的==========================
function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener('WebViewJavascriptBridgeReady',
            function () {
                callback(WebViewJavascriptBridge)
            },
            false);
    }
};
// 第一连接时初始化bridge
connectWebViewJavascriptBridge(function (bridge) {

    // 注册一个"functionInJs",
    //    bridge.registerHandler("functionInJs",
    //        function(data, responseCallback) {
    //            document.getElementById("show").innerHTML = ("data from Java: = " + data);
    //            var responseData = "Javascript Says 我要你的地址!";
    //            // response层
    //            responseCallback(responseData);
    //        });
    bridge.init(function (message, responseCallback) {
        console.log('JS got a message' + message, "message 呢");
        var data = "Javascript Responds 测试中文!";
        console.log('JS responding with ' + data, '什么鬼');
        responseCallback(data);
    });
});
//生成二维码 测试
//type 2表示生成，str为必须
//返回为生成图片的本地路径
function create() {
    var url = "http://183.134.9.47/apk.r1.market.hiapk.com/data/upload/marketClient/HiMarket7.8.1.81_1492485212332.apk";
    window.WebViewJavascriptBridge.callHandler("qr",
        {
            type: 2,
            str: url
        },
        function (responseData) {
            var obj = JSON.parse(responseData);
            document.getElementById("show").innerHTML = responseData;
            document.getElementById("image").src =  obj.data
        });
}
//扫描二维码测试
//type 1 表示扫描 str非必须
//返回为解析出来的字符串
function scann() {
    window.WebViewJavascriptBridge.callHandler("qr", {
        "type": 1
    },
        function (responseData) {
            show(responseData);
        });
};
//获取图片
function requestPicture(){
 window.WebViewJavascriptBridge.callHandler("picture_ext", {
                                  "type": 1
                              },
                                  function (responseData) {
                                  console.log(responseData);
                                      show(responseData);
                                  });
}
function videoRecord(){
 window.WebViewJavascriptBridge.callHandler("video_record", {},
                                  function (responseData) {
                                   var obj = JSON.parse(responseData);
                                   //文件路径
                                   var path = obj.data.filePath;
                                   //缩略图
                                   var poster = obj.data.thumbnail;
                                   //文件名
                                   var name = obj.data.fileName;
                                   show(path+"\n"+poster+"\n"+name);
                                   console.log(responseData);
                                   document.getElementById("video").src = path;
                                   document.getElementById("video").poster = poster;
                                  });
}

function testPrint(){
 window.WebViewJavascriptBridge.callHandler("text_print", {},
                                  function (responseData) {
                                   show(responseData)
                                  });
}
function testCamera(opts){
 window.WebViewJavascriptBridge.callHandler("quick_camera", {
 opt:opts},
                                  function (responseData) {
                                   show(responseData)
                                  });
}
function show(msg) {
    document.getElementById("show").innerHTML = msg;
};
function startPlay(){
  document.getElementById("video").src = "http://10.32.168.149/live/20181218100502350/hls.m3u8";
}