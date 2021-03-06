var projectEditor = window.projectEditor || {};

projectEditor.destroyTinyMce = function(){
    for (var i = tinymce.editors.length - 1 ; i > -1 ; i--) {
        var ed_id = tinymce.editors[i].id;
        console.log(ed_id);
        tinymce.execCommand("mceRemoveEditor", true, ed_id);
    }
};

projectEditor.initTinyMce = function () {
    
    var cssLinks =  $('link[title="project_css"]').map(function(i, link) {
                        return $(link).attr('href');
                    }).toArray().join(',');
    
    tinymce.init({
        entity_encoding: 'raw',
        content_css: cssLinks,
        body_class: 'text-editor-iframe',
        selector:'.text-editor', 
        min_height: 300,
        theme: 'modern',
        plugins: [
          'advlist autolink lists link image charmap print preview hr anchor pagebreak',
          'searchreplace wordcount visualblocks visualchars code fullscreen',
          'insertdatetime media nonbreaking save table contextmenu directionality',
          'template paste textcolor colorpicker textpattern imagetools codemirror'
        ],
        imagetools_toolbar: "rotateleft rotateright | flipv fliph | imageoptions",
        image_dimensions: false,
        image_class_list: [
          {title: 'Full Width', value: 'full-width'},
          {title: 'None', value: ''}
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
        templates: [
          { title: 'Alert Danger', url: '/static/templates/AlertDanger.html' },
          { title: 'Alert Info', url: '/static/templates/AlertInfo.html' },
          { title: 'Alert Warning', url: '/static/templates/AlertWarning.html' },
          { title: 'Button Default', url: '/static/templates/ButtonDefault.html' },
          { title: 'Button Primary', url: '/static/templates/ButtonPrimary.html' },
          { title: 'Content Only', url: '/static/templates/ContentOnly.html' },
          { title: 'Content Left Image Right', url: '/static/templates/ContentLeftImageRight.html' },
          { title: 'Content Right Image Left', url: '/static/templates/ContentRightImageLeft.html' },
          { title: 'Blog Post Image Right', url: '/static/templates/BlogPostImageRight.html' },
          { title: 'Blog Post Image Left', url: '/static/templates/BlogPostImageLeft.html' },
          { title: 'Jumbotron', url: '/static/templates/Jumbotron.html' },
          { title: 'Panel', url: '/static/templates/Panel.html' }
        ],
        file_picker_callback: function(callback, value, meta) {
            
            if (meta.filetype === 'file' || meta.filetype === 'media') {
                alert('Currently only image uploads are supported.');
            }

            if (meta.filetype === 'image') {
                $('#file-manager').data({
                    'basePath': '/admin/files',
                    'onFileSelect': function(url){
                        callback(url);
                    }
                });
                
                $('head').append('<script src="/static/filemanager/vendor.js"></script>');
                $('head').append('<script src="/static/filemanager/file-manager.js"></script>');
                $('head').append('<link href="/static/filemanager/vendor.css" rel="stylesheet"/>');
                $('head').append('<link href="/static/filemanager/file-manager.css" rel="stylesheet"/>');
            }
        },
        file_picker_types: 'image',
        //make sure image urls are always absolute to the hostname, e.g. start with /images/image
        relative_urls: false,
        remove_script_host: true,
        document_base_url: location.hostname,
        //allow custom html elements
        custom_elements: 'svg,clippath,polygon,link,script',
        //allow script and style tag attributes and svg attributes
        extended_valid_elements: 'script[src|async|defer|type],style,link[rel|type|href],svg[width|height],clippath[id],polygon[points]',
        //allow style and script elements as body children
        valid_children : '+body[style],+body[script],+body[link]',
        //void removal of empty elements
        verify_html: false,
        //avoid CDATA comments, as Whitespacefilter will remove all line breaks, causing everything after the opening CDATA tag to be a comment
        init_instance_callback : function(editor) {
            editor.serializer.addNodeFilter('script,style', function(nodes, name) {
                var i = nodes.length, node, value, type;

                function trim(value) {
                    /*jshint maxlen:255 */
                    /*eslint max-len:0 */
                    return value.replace(/(<!--\[CDATA\[|\]\]-->)/g, '\n')
                            .replace(/^[\r\n]*|[\r\n]*$/g, '')
                            .replace(/^\s*((<!--)?(\s*\/\/)?\s*<!\[CDATA\[|(<!--\s*)?\/\*\s*<!\[CDATA\[\s*\*\/|(\/\/)?\s*<!--|\/\*\s*<!--\s*\*\/)\s*[\r\n]*/gi, '')
                            .replace(/\s*(\/\*\s*\]\]>\s*\*\/(-->)?|\s*\/\/\s*\]\]>(-->)?|\/\/\s*(-->)?|\]\]>|\/\*\s*-->\s*\*\/|\s*-->\s*)\s*$/g, '');
                }
                while (i--) {
                    node = nodes[i];
                    value = node.firstChild ? node.firstChild.value : '';

                    if (value.length > 0) {
                        node.firstChild.value = trim(value);
                    }
                }
            });
        },
        //configure syntax highlighting for source code view
        codemirror: {
            fullscreen: true,
            path: 'codemirror-5.23.0/',
            indentOnInit: true
        }
    });
};

$(window).on('statechange', projectEditor.destroyTinyMce);
$(window).off('statechangecomplete', 'projectEditorInitTinyMce');
$(window).on('statechangecomplete', 'projectEditorInitTinyMce', projectEditor.initTinyMce);
$(document).ready(projectEditor.initTinyMce());
