$(function () {
    var articleId =  getQueryString("articleId");
    var isEdit = articleId ? true : false;
    // 根据isEdit确定为添加页面还是更新页面
    var updateUrl = "/3/update";
    var addUrl = "/3/add";


    $('#submit').click(function(){
        var article = {};
        if (isEdit){
            article.id = articleId;
        }

        article.articleCategory = {
            articleCategoryId:$('#article_category_id').val()
        };
        article.tag = $('#tagIds').val();
        article.articleTitle = $('#article_title').val();
        article.description = $('#description').val();
        article.articleContent = $('#article_content').val();
        var articleImg = $('#upload')[0].files[0];
        var formData = new FormData();//创建表单
        formData.append('articleImg',articleImg);
        formData.append('articleStr',JSON.stringify(article));//将article转化为JSON字符串并传入

        $.ajax({
            url:(isEdit?updateUrl:addUrl),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if(data.success){

                    alert('提交成功');

                    if (data.user.userType==3){
                        window.location.href = '/admin/article';
                    }else {
                        window.location.href = '/people/'+data.user.userId+'/3';
                    }

                }else{
                    alert('提交失败'+data.errMsg);
                }

            }

        });

    });


});