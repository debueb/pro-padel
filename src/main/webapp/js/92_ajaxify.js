// Ajaxify
// v1.0.1 - 30 September, 2012
// https://github.com/browserstate/ajaxify
(function (window, undefined) {

    // Prepare our Variables
    var
        History = window.History,
        $ = window.jQuery,
        document = window.document;

    // Check to see if History.js is enabled for our Browser
    if (!History.enabled) {
        return false;
    }

    // Wait for Document
    $(function () {
        // Prepare Variables
        var contentSelector,
            $content,
            replaceElement,
            $body = $(document.body),
            completedEventName = 'statechangecomplete',
            /* Application Generic Variables */
            $window = $(window),
            rootUrl = History.getRootUrl();
            
        
        // HTML Helper
        var documentHtml = function (html) {
            // Prepare
            var result = String(html)
                    .replace(/<\!DOCTYPE[^>]*>/i, '')
                    .replace(/<(html|head|body|title|meta|script)([\s\>])/gi,'<div class="document-$1"$2')
                    .replace(/<\/(html|head|body|title|meta|script)\>/gi,'</div>');
            
            return $.trim(result);
        };

        // Ajaxify Helper
        $.fn.ajaxify = function () {
            // Prepare
            var $this = $(this);

            $('#offline-msg-btn').on('click', function(){
                $('#shadow').hide();
                $('#offline-msg').hide();
                return false;
            });
            
            // Ajaxify
            //$this.find('a.ajaxify').off().on('click', function (event) { //this does not trigger correctly
            $this.find('a.ajaxify').click(function (event) {
                if (!window.navigator.onLine){
                    $('#shadow').show();
                    $('#offline-msg').show();
                    return false;
                }
                
                // Prepare
                var $this = $(this),
                    url = $this.attr('href'),
                    title = $this.attr('title') || null;
                    
                    contentSelector = $this.attr('data-content') || '.wrapper';
                    if ($this.attr('data-replace')){
                        $content = $($this.attr('data-replace')).filter(':first');
                        replaceElement = true;
                    } else {
                        $content = $(contentSelector).filter(':first');
                        replaceElement = false;
                    }
                    // Ensure Content
                    if ($content.length === 0) {
                        $content = $body;
                    }

                    
                    
                // Continue as normal for cmd clicks etc
                if (event.which === 2 || event.metaKey) {
                    return true;
                }

                // Ajaxify this link
                History.pushState({data: null, method: 'GET'}, title, url);
                event.preventDefault();
                return false;
            });
            
            $this.find('form.ajaxify').off().on('submit', function (event) {
                if (!window.navigator.onLine){
                    $('#shadow').show();
                    $('#offline-msg').show();
                    return false;
                }

                var data = $(this).serialize(),
                    method = $(this).attr('method'),
                    url = $(this).attr('action') || "";
                
                //in order to update the URL in the browser, we construct the URL here
                //to avoid the ajax call from duplicating all parameters we set the data to null
                if (method === 'GET'){
                    url = url + '?' + decodeURIComponent(data);
                    data = null;
                }
                // Ajaxify this link
                History.pushState({data: data, method: method}, null, url);
                event.preventDefault();
                return false;
            });

            // Chain
            return $this;
        };

        // Ajaxify our Internal Links
        $body.ajaxify();

        // Hook into State Changes
        $window.bind('statechange', function () {
            // Prepare Variables
            var State = History.getState(),
                url = State.url,
                relativeUrl = url.replace(rootUrl, ''),
                data = State.data;
              
            //close any open select pickers that are absolutely attatched to the body
            $('.select-simple, .select-multiple, .select-ajax-search').selectpicker().each(function(){
                if ($(this).hasClass('open')){
                   $(this).removeClass('open');
                }
            });
                    
            app.main.showSpinner();
           
            // Ajax Request the Traditional Page
            $.ajax({
                url: url,
                type: data.method,
                data: data.data,
                success: function (data, textStatus, jqXHR) {
                    // Prepare
                    var
                            $data = $(documentHtml(data)),
                            $dataBody = $data.find('.document-body:first'),
                            $dataContent = $dataBody.find(contentSelector).filter(':first'),
                            contentHtml, $scripts;

                    //remove Google Analytics script
                    $dataContent.find('.document-script[data-id="ga"]').detach();

                    // Fetch the scripts
                    $scripts = $dataContent.find('.document-script');
                    if ($scripts.length) {
                        $scripts.detach();
                    }
                    
                    // Fetch the content
                    contentHtml = $dataContent.html() || $data.html();
                    if (!contentHtml) {
                        document.location.href = url;
                        return false;
                    }

                    // Update the content
                    $content.stop(true, true);
                    if (replaceElement){
                        $content.replaceWith(contentHtml);
                    } else {
                        $content.html(contentHtml);
                    }
                    $(contentSelector).ajaxify().show();

                    // Update the title and meta tags
                    document.title = $data.find('.document-title:first').text();
                    try {
                        $('title').html(document.title.replace('<', '&lt;').replace('>', '&gt;').replace(' & ', ' &amp; '));
                        
                        $data.find('.document-meta').each(function(){
                            var name    = $(this).attr('name'),
                                content = $(this).attr('content');
                            if (name && content){
                                var $metaTag = $('head').find('meta[name="'+name+'"]');
                                if (!$metaTag.length){
                                    $metaTag = $('<meta name="'+name+'">');
                                    $('head').append($metaTag);
                                }
                                $metaTag.attr('content', content);
                            }
                        });                  
                    } catch (Exception) {
                        //empty
                    }

                    // Add the scripts
                    $scripts.each(function () {
                        var $script = $(this), scriptText = $script.text(), scriptNode = document.createElement('script');
                        if ($script.attr('src')) {
                            if (!$script[0].async) {
                                scriptNode.async = false;
                            }
                            scriptNode.src = $script.attr('src');
                        }
                        scriptNode.appendChild(document.createTextNode(scriptText));
                        $content.get(0).appendChild(scriptNode);
                    });

                    $window.trigger(completedEventName);

                    // Complete the change
                    if (!replaceElement){
                        $(window).scrollTop(0);
                    }

                    // Inform Google Analytics of the change
                    if (typeof window._gaq !== 'undefined') {
                        window._gaq.push(['_trackPageview', relativeUrl]);
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);
                    console.log(textStatus);
                    console.log(errorThrown);
                    document.location.href = url;
                    return false;
                },
                complete: function(){
                    app.main.hideSpinner();
                }
            }); // end ajax

        }); // end onStateChange

    }); // end onDomLoad

})(window); // end closure