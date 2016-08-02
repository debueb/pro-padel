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
        var $self = $(this);
        $self.summernote({
            airmode: false,
            popover: false,
            minHeight: 200,
            toolbar: [
                ['style', ['style']],
                ['font', ['bold', 'italic', 'underline', 'strikethrough', 'superscript', 'subscript', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['insert', ['template', 'map', 'link', 'picture', 'video', 'hr']],
                ['view', ['fullscreen', 'codeview']]
            ],
            template: {
                path: '/templates', // path to your template folder
                list: [ // list of your template (without the .html extension)
                    'Table',
                    'GallerySlider',
                    'Panel',
                    'ButtonPrimary',
                    'ButtonDefault',
                    'Jumbotron',
                    'AlertInfo',
                    'AlertWarning',
                    'AlertDanger'
                ]
            },
            map: {
                apiKey: 'AIzaSyBu3x1GrcboxPru4-Rq9wvdYeyXIyVKS-s',
                // This will be used when map is initialized in the dialog.
                center: {
                  lat: 50.9573771,
                  lng: 6.8268944
                },
                zoom: 13
            },
            callbacks: {
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
                            $self.summernote('editor.insertImage', url);
                            //editor.insertImage(welEditable, url);
                        },
                        error: function(e){
                            resetProgressBar();
                            alert("Failed to upload image: Error "+e.status+" "+e.statusText);
                        }
                    });
                }
            }
        });
    });
    $('.summernote-form').livequery(function(){
        $(this).on('submit', function () {
            $("#summernote-input").val($('#summernote').summernote('code'));
        });
    });
});