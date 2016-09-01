$(document).ready(function () {
    
    window.app = app || {};
    app.imageHandler = {};
    
    app.imageHandler.uploadImage = function(event, callback){
        app.main.showSpinner();
        var data = new FormData();
        data.append("file", event.target.files[0]);
        $.ajax({
            accepts: "text/plain",
            dataType: "text",
            data: data,
            type: 'POST',
            enctype: 'multipart/form-data',
            url: '/images/upload',
            cache: false,
            contentType: false,
            processData: false,
            success: function(url) {
                callback(url);
            },
            error: function(e){
                alert("Failed to upload image: Error "+e.status+" "+e.statusText);
            }, 
            complete: function(){
                app.main.hideSpinner();
            }
        });
    };
    
    tinymce.init({
        selector:'.text-editor', 
        min_height: 300,
        theme: 'modern',
        plugins: [
          'advlist autolink lists link image charmap print preview hr anchor pagebreak',
          'searchreplace wordcount visualblocks visualchars code fullscreen',
          'insertdatetime media nonbreaking save table contextmenu directionality',
          'template paste textcolor colorpicker textpattern imagetools'
        ],
        menu: {
            edit: {title: 'Edit', items: 'undo redo | cut copy paste pastetext | selectall'},
            insert: {title: 'Insert', items: 'link image media | template hr'},
            view: {title: 'View', items: 'visualaid'},
            format: {title: 'Format', items: 'bold italic underline strikethrough superscript subscript | formats | removeformat'},
            table: {title: 'Table', items: 'inserttable tableprops deletetable | cell row column'},
            tools: {title: 'Tools', items: 'code'}
        },
        toolbar1: 'insertfile undo redo | template | styleselect fontsizeselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media',
        toolbar2: 'print preview | forecolor backcolor emoticons',
        fontsize_formats: '8px 10px 12px 14px 16px 18px 24px 36px',
        image_advtab: true,
        content_css: $($('link[rel=stylesheet]').get(0)).attr('href'),
        templates: [
          { title: 'Alert Danger', url: '/templates/AlertDanger.html' },
          { title: 'Alert Info', url: '/templates/AlertInfo.html' },
          { title: 'Alert Warning', url: '/templates/AlertWarning.html' },
          { title: 'Button Default', url: '/templates/ButtonDefault.html' },
          { title: 'Button Primary', url: '/templates/ButtonPrimary.html' },
          { title: 'Content Only', url: '/templates/ContentOnly.html' },
          { title: 'Content Left Image Right', url: '/templates/ContentLeftImageRight.html' },
          { title: 'Content Richt Image Left', url: '/templates/ContentRightImageLeft.html' },
          { title: 'Jumbotron', url: '/templates/Jumbotron.html' },
          { title: 'Panel', url: '/templates/Panel.html' }
        ],
        file_picker_callback: function(callback, value, meta) {
            
            if (meta.filetype === 'file' || meta.filetype === 'media') {
                alert('Zur Zeit werden nur Bilder Uploads unterstützt.');
            }

            if (meta.filetype === 'image') {
                $('body').append($('<input id="file-input" name="image" type="file">'));
                $('#file-input').on('change', function(){app.imageHandler.uploadImage(event, callback);});
                $('#file-input').click();
            }
        },
        file_picker_types: 'image',
        relative_urls: false,
        remove_script_host: true,
        document_base_url: location.hostname,
        extended_valid_elements: 'script,style,svg',
        inline_styles: true
    });
});