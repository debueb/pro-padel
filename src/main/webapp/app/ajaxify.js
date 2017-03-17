// Ajaxify
// v1.0.1 - 30 September, 2012
// https://github.com/browserstate/ajaxify
var project = require('./project');

(function (window) {
    
    if (!window.history){
        return;
    }

    // Prepare our Variables
    var $ = window.jQuery,
        document = window.document,
        payload = undefined;

    // Wait for Document
    $(function () {
        // Prepare Variables
        var defaultContentSelector = '.wrapper',
            contentSelector, 
            $content,
            replaceElement,
            $body = $(document.body),
            completedEventName = 'statechangecomplete',
            /* Application Generic Variables */
            $window = $(window);
            
        
        // HTML Helper
        var documentHtml = function (html) {
            // Prepare
            var result = String(html)
                    .replace(/<\!DOCTYPE[^>]*>/i, '')
                    .replace(/<(html|head|body|title|meta|script)([\s\>])/gi,'<div class="document-$1"$2')
                    .replace(/<\/(html|head|body|title|meta|script)\>/gi,'</div>');
            
            return $.trim(result);
        };
        
        var isInternalURL = function(url){
            var localDomain = new RegExp('^http[s]?:\/\/'+document.location.hostname, 'g');
            if (url && url.match(localDomain)){
                return true;
            }
            return false;
        };

        // Ajaxify Helper
        $.fn.ajaxify = function () {
            // Prepare
            var getContent = function(){
                var content;
                contentSelector = $(this).attr('data-content') || defaultContentSelector;
                if ($(this).attr('data-replace')){
                    content = $($(this).attr('data-replace')).filter(':first');
                    replaceElement = true;
                } else {
                    content = $(contentSelector).filter(':first');
                    replaceElement = false;
                }
                // Ensure Content
                if (content.length === 0) {
                    content = $body;
                }
                return content;
            };

            $('#offline-msg-btn').on('click', function(){
                $('#shadow').hide();
                $('#offline-msg').hide();
                return false;
            });
            
            
            // Ajaxify
            var $this = $(this);
            $this.find('a').click(function (event) {
                if (!window.navigator.onLine){
                    $('#shadow').show();
                    $('#offline-msg').show();
                    return false;
                }
                
                // Prepare
                var $this       = $(this),
                    url         = this.href,
                    title       = $this.attr('title') || null,
                    anchorId    = $(this).attr('data-anchor');
                    
                //only ajaxify internal links
                if (!isInternalURL(url) || $this.hasClass('no-ajaxify')){
                    return true;
                }
                
                $content = getContent.apply(this);
                    
                // Continue as normal for cmd clicks etc
                if (event.which === 2 || event.metaKey) {
                    return true;
                }

                // Ajaxify this link
                history.pushState({method: 'GET', url: url, anchorId: anchorId}, title, url);
                event.preventDefault();
                $window.trigger('statechange');
                return false;
            });
            
            $this.find('form').off().on('submit', function (event) {
                if (!window.navigator.onLine){
                    $('#shadow').show();
                    $('#offline-msg').show();
                    return false;
                }
                
                if (window.tinymce){
                    window.tinymce.triggerSave();
                }
                
                var $this       = $(this),
                    //using location.origin + location.pathname instead of location.href, as location.href may GET contains parameters
                    //which interfere with the GET parameters that we set below
                    url         = $(this).prop('action') || document.location.origin + document.location.pathname,
                    method      = $this.attr('method'),
                    contentType = $this.attr('enctype') || undefined,
                    anchorId    = $this.attr('data-anchor');
                
                //storing payload in closure variable as FormData cannot be passed to pushState
                payload     = (contentType === 'multipart/form-data') ? new FormData($this[0]): $this.serialize();
                    
                //only ajaxify internal links
                if (!isInternalURL(url) || $this.hasClass('no-ajaxify')){
                    return true;
                }
                
                $content = getContent.apply(this);
                
                //in order to update the URL in the browser, we construct the URL here
                //to avoid the ajax call from duplicating all parameters we set the data to null
                if (method === 'GET'){
                    url = url + '?' + decodeURIComponent(payload);
                    payload = null;
                }
                // Ajaxify this link
                history.pushState({method: method, url: url, contentType: contentType, anchorId: anchorId}, null, url);
                event.preventDefault();
                $window.trigger('statechange');
                return false;
            });

            // Chain
            return $this;
        };

        // Ajaxify our Internal Links
        $body.ajaxify();

        // Hook into State Changes
        $window.bind('statechange popstate', function () {
            // Prepare Variables
            var stateData = history.state;
            if (stateData.replaceState){
                return;
            }
              
            //close any open select pickers that are absolutely attatched to the body
            $('.select-simple, .select-multiple, .select-ajax-search').selectpicker().each(function(){
                if ($(this).hasClass('open')){
                   $(this).removeClass('open');
                }
            });
                    
            project.showSpinner();
           
            // Ajax Request the Traditional Page
            $.ajax({
                url:            stateData.url,
                type:           stateData.method,
                contentType:    (stateData.contentType === 'multipart/form-data') ? false : undefined,
                processData:    (stateData.contentType === 'multipart/form-data') ? false : undefined,
                data:           payload,
                success: function (responseData, status, xhr) {
                    // Prepare
                    var $data = $(documentHtml(responseData)),
                        $dataBody = $data.find('.document-body:first'),
                        $dataContent = $dataBody.find(contentSelector).filter(':first'),
                        contentHtml, $scripts;

                    //remove Google Analytics script
                    $dataContent.find('.document-script[data-ajaxify="false"]').detach();

                    // Fetch the scripts
                    $scripts = $dataContent.find('.document-script');
                    if ($scripts.length) {
                        $scripts.detach();
                        $scripts = $scripts.filter(function(){
                            var type = $(this).attr('type');
                            return !type || type === 'text/javascript';
                        });
                    }
                    
                    // Fetch the content
                    contentHtml = $dataContent.html() || $data.html();
                    if (!contentHtml) {
                        document.location.href = stateData.url;
                        return false;
                    }

                    if (!$content){
                        $content = $(defaultContentSelector);
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
                    if (!!stateData.anchorId){
                        var topPosition = 0,
                            offset = $(stateData.anchorId).offset();
                        if (offset){
                            topPosition = offset.top - $('.navbar').height();
                        }
                        $(window).scrollTop(topPosition);
                    } else if (!replaceElement){
                        $(window).scrollTop(0);
                    }

                    // Inform Google Analytics of the change
                    if (typeof window._gaq !== 'undefined') {
                        window._gaq.push(['_trackPageview', relativeUrl]);
                    }
                    
                    //Update the URL if it was a redirect
                    var redirectURL = xhr.getResponseHeader("RedirectURL");
                    if (!!redirectURL){
                        history.replaceState({replaceState: true}, document.title, redirectURL);
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
                    payload = undefined;
                    project.hideSpinner();
                }
            }); // end ajax

        }); // end onStateChange

    }); // end onDomLoad

})(window); // end closure