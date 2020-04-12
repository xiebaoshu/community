$(function () {
    var articleId =  getQueryString("articleId");
    var updateUrl = "/1/update";
    $('#submit').click(function(){
        var lostArticle = {};
        lostArticle.id = articleId;
        lostArticle.articleCategory = {
            articleCategoryId:$('#article_category_id').val()
        };
        lostArticle.area = {
            areaId:$('#area_id').val()
        };
        lostArticle.itemCategory = {
            itemCategoryId:$('#item_category_id').val()
        };
        lostArticle.articleTitle = $('#article_title').val();
        lostArticle.phone = $('#phone').val();
        lostArticle.description = $('#description').val();
        lostArticle.articleContent = $('#article_content').val();
        lostArticle.tag = $('#tagIds').val();
        lostArticle.createTime = $('#create_time').val();
        lostArticle.finishTime = $('#finish_time').val();
        lostArticle.finishUser = {
            userId:$('#finisher_id').val()
        };

        var lostArticleImg = $('#upload')[0].files[0];
        var formData = new FormData();//创建表单
        formData.append('lostArticleImg',lostArticleImg);
        formData.append('lostArticleStr',JSON.stringify(lostArticle));//将lostArticle转化为JSON字符串并传入

        $.ajax({
            url:updateUrl,
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if(data.success){
                    alert('更新成功');
                    if (data.user.userType==3){
                        window.location.href = '/admin/article';
                    }else {
                        window.location.href = '/people/'+data.user.userId+'/1';
                    }

                }else{
                    alert('更新失败'+data.errMsg);
                }

            }

        });

    });
});