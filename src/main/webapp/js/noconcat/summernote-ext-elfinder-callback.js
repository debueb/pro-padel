function elfinderDialog() {
  var fm = $('<div/>').dialogelfinder({
    url : '/admin/elfinder/connector', // change with the url of your connector
    lang : 'en',
    //width : '80%;//',
    height: 450,
    destroyOnClose : true,
    getFileCallback : function(files, fm) {
      console.log(files);
      $('#summernote').summernote('editor.insertImage', files.url);
    },
    commandsOptions : {
      getfile : {
        oncomplete : 'close',
        folders : false
      }
    }

  }).dialogelfinder('instance');
}
