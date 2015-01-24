$(document).ready(function () {

    $('#summernote').livequery(function(){
        $(this).summernote({
            onImageUpload: function (files, editor, welEditable) {
                alert("Image upload not supported. Use image URL instead.");
            }
        });
    });
    $('#news-form').livequery(function(){
        $(this).on('submit', function () {
            $("#message").val($('#summernote').code());
        });
    });
});