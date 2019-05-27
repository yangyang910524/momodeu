<script src="${ctxStatic}/plugin/oss/aliyun-oss-sdk-4.4.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
    var client =jp.getClient();
    function fileUpload(file,path,succFuc,failFuc) {
        client.multipartUpload(path, file).then(function (result) {
            succFuc("https://webmomofile.oss-cn-beijing.aliyuncs.com/"+path);
        }).catch(function (err) {
            failFuc(err);
        });
    }
    function timestamp(){
        var time = new Date();
        var y = time.getFullYear();
        var m = time.getMonth()+1;
        var d = time.getDate();
        var h = time.getHours();
        var mm = time.getMinutes();
        var s = time.getSeconds();
        var sss=time.getMilliseconds();
        return ""+y+add0(m)+add0(d)+add0(h)+add0(mm)+add0(s)+add00(sss);
    }
    function add0(m){
        return m<10?'0'+m : m;
    }
    function add00(m){
        if(m<10){
            return "00"+m;
        }else if(m<100){
            return "0"+m;
        }else{
            return m;
        }
    }
</script>