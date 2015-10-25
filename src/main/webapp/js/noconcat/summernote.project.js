$(document).ready(function () {
    
    function resetProgressBar(){
        $('#shadow').hide();
        $('progress').attr('value','0.0');
        $('progress').hide();
    }
    
    function progressHandlingFunction(e){
        console.log(e);
        console.log(e.lengthComputable);
        if(e.lengthComputable){
            $('#shadow').show();
            $('progress').show();
            $('progress').attr({value:e.loaded, max:e.total});
            // reset progress on complete
            if (e.loaded === e.total) {
                resetProgressBar();
            }
        }
    }

    $('#summernote').livequery(function(){
        $(this).summernote({
            toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'clear']],
//            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
//            ['height', ['height']],
            ['insert', ['link', 'picture', 'video', 'hr']],
            ['table', ['table']],
            ['view', ['fullscreen', 'codeview']]
//            ['help', ['help']]
          ],
            onImageUpload: function (files, editor, welEditable) {
                data = new FormData();
                data.append("file", files[0]);
                $.ajax({
                    accepts: "text/plain",
                    dataType: "text",
                    data: data,
                    type: 'POST',
                    enctype: 'multipart/form-data',
                    xhr: function() {
                        var myXhr = $.ajaxSettings.xhr();
                        if (myXhr.upload) myXhr.upload.addEventListener('progress', progressHandlingFunction, false);
                        return myXhr;
                    },
                    url: '/images/upload',
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function(url) {
                        editor.insertImage(welEditable, url);
                    },
                    error: function(e){
                        resetProgressBar();
                        alert("Failed to upload image: Error "+e.status+" "+e.statusText);
                    }
                });
            }
        });
    });
    $('#news-form').livequery(function(){
        $(this).on('submit', function () {
            $("#message").val($('#summernote').code());
        });
    });
});