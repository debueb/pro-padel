const LOCAL_STORAGE_LAST_PAGE = 'lastPage';

var project = {
    showShadow: function () {
        $('#shadow').show();
    },

    hideShadow: function () {
        $('#shadow').hide();
    },

    showSpinner: function () {
        this.showShadow();
        $('#ballWrapper').css('display', 'block');
    },

    hideSpinner: function () {
        this.hideShadow();
        $('#ballWrapper').css('display', 'none');
    },

    isNative(){
        return "standalone" in window.navigator && window.navigator.standalone || window.navigator.userAgent.indexOf('ProPadel') >= 0;
    },
    
    enableBackButton: function () {
        $('.btn-back').livequery(function(){
            //show btn-back in header
            if (project.isNative()){
                $(this).show();
            }
            //btn-back is used not only in header
            $(this).on('click tap', function(){
                window.history.back();
            });
        });
    },

    enableForms: function () {
        var that = this;
        //prevent duplicate form submission
        $('form:not(".ajaxify")').livequery(function(){
            $(this).on('submit', function () {
                
                window.addEventListener("pagehide", function(){
                   that.hideSpinner();
                   $('form :submit').removeAttr("disabled");
                });
                that.showSpinner();
                $('form :submit').attr("disabled", "disabled");
            });
        });

        //enable target switching
        $('.btn-form-submit').livequery(function(){
            $(this).on('click tap', function () {
                if (!!$(this).attr('data-href')) {
                    $(this).parents('form').attr('action', $(this).attr('data-href'));
                }
            });
        });
    },
    
    enableFormAutoSubmit: function(){
        $('.auto-submit').livequery(function(){
            $(this).on('change', function(){
                $(this).closest('form').submit(); 
            });
        });
    },
    
    enableDatePicker: function () {

        var addLeadingZero = function(str){
           return ("0" + str).slice(-2);
        };

        var getSimpleDate = function (date) {
            return date.getFullYear() + "-" + addLeadingZero(date.getMonth()+1) + "-" + addLeadingZero(date.getDate());
        };
        
        // parse a date in yyyy-mm-dd format
        var parseDate = function(input) {
            var parts = input.split('-');
            // new Date(year, month [, day [, hours[, minutes[, seconds[, ms]]]]])
            return new Date(parts[0], parts[1]-1, parts[2]); // Note: months are 0-based
        };

        $('.datepicker-container').livequery(function () {
            var datepicker      = $(this).find('.datepicker'),
                altField        = $(this).find('.datepicker-input'),
                datePickerIcon  = $(this).find('.datepicker-icon'),
                textField       = $(this).find('.datepicker-text'),
                textContainer   = $(this).find('.datepicker-text-container'),
                maxDate         = $(datepicker).attr('data-max-date'),
                allowPast       = $(datepicker).attr('data-allow-past'),
                dayConfigs      = $(datepicker).attr('data-day-config');
                
            if (!maxDate) {
                //no maximum date by default
                maxDate = null;
            }
            
            if (!allowPast){
                allowPast = false;
            }

            if (!!dayConfigs) {
                dayConfigs = JSON.parse(dayConfigs);
            }
            
            var defaultDate = new Date();
            var dateValue = altField.val();
            if (!!dateValue) {
                defaultDate = parseDate(dateValue);
            }
            datepicker.datepicker({
                dateFormat: "DD, d. MM yy",
                defaultDate: defaultDate,
                altField: altField,
                altFormat: "yy-mm-dd",
                nextText: "",
                prevText: "",
                minDate: (allowPast) ? null : new Date(),
                maxDate: maxDate,
                onSelect: function (dateText) {
                    textField.text(dateText);
                    datepicker.slideUp();
                    /* changes on hidden input fields do not fire automatically, 
                     * MutationObserver does not work on old Androids */
                    if (altField){
                        $(altField).trigger('change');
                    }
                },
                beforeShowDay: function (date) {
                    if (!!dayConfigs) {
                        var dayConfig = dayConfigs[getSimpleDate(date)];
                        if (!!dayConfig){
                            return [dayConfig.selectable, ""];
                        }
                    } else {
                        return [true, ""];
                    }
                    return [false, ""];
                }
            });

            $(".datepicker").datepicker("option", "showAnim", 'slideDown');

            var toggleDatePicker = function () {
                if ($(datepicker.datepicker()).css('display') === 'none') {
                    datepicker.slideDown();
                    $('body,html').stop(true,true).animate({scrollTop: textContainer.offset().top - $('.navbar-fixed-top').height()});
                } else {
                    datepicker.slideUp();
                }
            };

            textField.on('click tap', function () {
                toggleDatePicker();
            });

            datePickerIcon.on('click tap', function () {
                toggleDatePicker();
            });

            textField.text($.datepicker.formatDate("DD, d. MM yy", datepicker.datepicker("getDate")));

            if (datepicker.attr('data-show-on-init') !== 'true') {
                datepicker.hide();
            }
        });
    },

    enablePrivateDataLinks: function () {
        var that = this;
        $('.private-data').livequery(function(){
            $(this).on('click tap', function () {
                var prefix = $(this).attr('data-prefix');
                var fake = $(this).attr('data-fake');
                document.location.href = prefix + that.rot47(fake);
            });
        });
    },

    rot47: function (s) {
        var x = [];
        for (var i = 0; i < s.length; i++) {
            var j = s.charCodeAt(i);
            if ((j >= 33) && (j <= 126)) {
                x[i] = String.fromCharCode(33 + ((j + 14) % 94));
            } else {
                x[i] = String.fromCharCode(j);
            }
        }
        return x.join('');
    },

    enableSideMenu: function () {
        var isOpen = function () {
            return $('#site-canvas').hasClass('show-nav');
        };

        var toggleMenu = function () {
            if (isOpen()) {
                $('#site-shadow').fadeOut(300);
                // close side menu
                $('#site-canvas, #site-shadow, #site-menu, .footer').removeClass('show-nav');
                // reenable scrolling of content
                $('#site-shadow').off('touchmove');
            } else {
                $('#site-shadow').fadeIn(300);
                // open side menu
                $('#site-canvas, #site-shadow, #site-menu, .footer').addClass('show-nav');
                // prevent scrolling of content when interacting with site menu
                $('#site-shadow').on('touchmove', function(e) {e.preventDefault();});
            }
        };

        var toggleNavbarToggle = function(){
            var btn = $('.navbar-toggle');
                if (btn.hasClass('active')){
                    btn.removeClass('active');
                } else {
                    btn.addClass('active');
                }
                toggleMenu();
                return true;
        };

        $('.navbar-toggle, #site-shadow, #site-menu a').livequery(function(){
            $(this).on('tap click', function () {
                if (!$(this).hasClass('hasSubmodules')){
                    return toggleNavbarToggle();
                }
            });
        });
        
        var onResizeEnd = function(){
            if ($('.navbar-collapse').is(':visible') && isOpen()){
                toggleNavbarToggle();
            }
        };

        var doit;
        window.onresize = function(){
          clearTimeout(doit);
          doit = setTimeout(onResizeEnd, 100);
        };
    },

    enableAccordion: function () {
        $('.accordion').livequery(function(){
            $(this).accordion({"heightStyle": "content"});
        });
    },

    enableBookingLoginSelection: function () {
        $('.btn-booking-submit').livequery(function(){
            $(this).on('tap click', function () {
                $('#bookingType').val($(this).attr('data-booking-type'));
            });
        });
    },

    enableSelectPicker: function () {
        var options = {
            iconBase: 'fa',
            tickIcon: 'fa-check',
            dropupAuto: false,
            countSelectedText: '{0}'
        };
        $('.select-simple, .select-multiple').livequery(function(){
            if (!!$(this).attr('data-selected-text-format')){
                options.selectedTextFormat = 'count > 2';
            }
            $(this).selectpicker(options);
        });
        
        $('.select-ajax-search').livequery(function(){
            var liveOptions = options;
            liveOptions.liveSearch = true;
            var typeSource = $(this).attr('data-abs-param-type-source');
            $(this).selectpicker(liveOptions);
            if($(this).data('selectpicker')){
                $(this).ajaxSelectPicker({
                    ajax: {
                        method: 'GET',
                        data: function () {
                            var typeValue;
                            if (typeSource){
                                typeValue = $(typeSource).val();
                            }
                            return {
                                q: '{{{q}}}',
                                type: typeValue
                            };
                        }
                    },
                    cache: false,
                    clearOnEmpty: true,
                    preserveSelected: true
                });
            }
        });
    },

    enablePlusMinusInputFields: function () {
        $('.btn-plus-minus').livequery(function(){
            $(this).on('tap click', function (e) {
                e.preventDefault();

                fieldName = $(this).attr('data-field');
                type = $(this).attr('data-type');
                var input = $("input[name='" + fieldName + "']");
                var currentVal = parseInt(input.val());
                if (!isNaN(currentVal)) {
                    if (type === 'minus') {
                        if (currentVal > input.attr('min')) {
                            input.val(currentVal - 1).change();
                        }
                        if (parseInt(input.val()) === input.attr('min')) {
                            $(this).attr('disabled', true);
                        }
                    } else if (type === 'plus') {

                        if (currentVal < input.attr('max')) {
                            input.val(currentVal + 1).change();
                        }
                        if (parseInt(input.val()) === input.attr('max')) {
                            $(this).attr('disabled', true);
                        }
                    }
                } else {
                    input.val(0);
                }
            });
        });
        $('.input-plus-minus').livequery(function(){
            $(this).focusin(function () {
                $(this).data('oldValue', $(this).val());
            });
            $(this).change(function () {

                var minValue = parseInt($(this).attr('min'));
                var maxValue = parseInt($(this).attr('max'));
                var valueCurrent = parseInt($(this).val());

                var name = $(this).attr('name');
                if (valueCurrent >= minValue) {
                    $(".btn-plus-minus[data-type='minus'][data-field='" + name + "']").removeAttr('disabled');
                } else {
                    $(this).val($(this).data('oldValue'));
                }
                if (valueCurrent <= maxValue) {
                    $(".btn-plus-minus[data-type='plus'][data-field='" + name + "']").removeAttr('disabled');
                } else {
                    $(this).val($(this).data('oldValue'));
                }
            });
        });
    },

    enableUpdateBooking: function () {
        var updatePrice = function(){
            var target = $('#booking-price'),
                price = $(this).find('option:selected').attr('data-price');
            target.html(price);
        };
        
        $('select.booking-duration').livequery(function(){
            updatePrice.apply(this);
            $(this).on('change', function () {
                updatePrice.apply(this);
            });
        });
    },

    enablePayMill: function () {
        var that = this;
        var formIdentifier = '.paymill-form';

        function PaymillResponseHandler(error, result) {
            that.hideSpinner();
            that.hideShadow();
            $(formIdentifier).removeAttr("disabled");
            $(formIdentifier).find('button').removeAttr("disabled");
            if (error) {
                console.log(error);
                // Shows the error above the form
                $.ajax({
                    url: "translate",
                    dataType: 'text',
                    data: "key="+error.apierror
                    }).done(function (data) {
                        $("#error").text(data);
                    }).fail(function () {
                        $("#error").text(error.apierror);
                });
            } else {
                $('#token').val(result.token);
                $('#token-form').submit();
            }
        }

        $(formIdentifier).submit(function (event) {
            // Deactivate submit button to avoid further clicks
            $(this).attr("disabled", "disabled");
            var paymentType = $(this).attr('data-payment-type');
            switch(paymentType){
                
                case 'creditcard':
                    paymill.createToken({
                        number:         $('.card-number').val().replace(/\s+/g, "").replace(/-/g, ""), // required, ohne Leerzeichen und Bindestriche
                        exp_month:      $('.card-expiry-month').val(), // required
                        exp_year:       $('.card-expiry-year').val(), // required, vierstellig z.B. "2016"
                        cvc:            $('.card-cvc').val(), // required
                        amount_int:     $('.card-amount-int').val(), // required, integer, z.B. "15" für 0,15 Euro 
                        currency:       $('.card-currency').val(), // required, ISO 4217 z.B. "EUR" od. "GBP"
                        cardholder:     $('.card-holdername').val() // optional
                    }, PaymillResponseHandler);    
                    break;
                    
                case 'directdebit':
                    paymill.createToken({
                        iban:           $('.iban').val().replace(/\s+/g, ""),
                        bic:            $('.bic').val(),
                        accountholder:  $('.holdername').val()
                    }, PaymillResponseHandler);
                    break;
            }

            return false;
        });
    },
    
    enableRegexChecksOnInputs: function(){
        $('input[type="text"]').livequery(function(){
            var regex = new RegExp($(this).attr('data-valid-chars'));
            if (!!regex) {
                $(this).on('keypress', function (e) {
                    var charCode = !e.charCode ? e.which : e.charCode;

                    if (!regex.test(String.fromCharCode(charCode))) {
                        e.preventDefault();
                    }
                });
            }
        });
    },
    
    enableAdvancedProfile: function(){
        $('.picture').livequery(function(){
            var $picture = $(this);
            $picture.on('click tap', function(){
                $(this).next().first('.picture-input').click();
                return false;
            });
            $('.picture-input').on('change', function(){
                var value = $(this).val();
                var lastIndex = (value.lastIndexOf("\\") === -1) ? 0 : value.lastIndexOf("\\")+1;
                $picture.find('.picture-subtext').text(value.slice(lastIndex)); 
            });
        });
    },
    
    enableSelectToggle: function () {
        
        var showSelect = function(){
            //hide all select-toggle- elements
            $(document).find("[class^='select-toggle-']").hide();
            
            var selectedValues = [];
            $(document).find('.select-toggle').each(function(){
                selectedValues.push($(this).val());
            });
            
            for (var i=0; i<selectedValues.length; i++){
                $(document).find('[class~="select-toggle-' + selectedValues[i] + '"]').fadeIn();
            }
        };
        
        $('select.select-toggle').livequery(function(){
            showSelect.apply(this);
            
            $('select.select-toggle').on('change', function () {
                showSelect.apply(this);
            });
        });
    },
    
    enableAutoSearch: function(){
        $('form.search').livequery(function(){
            var timeout,
                $form = $(this),
                $inuptElement = $(this).find('input[name="search"]'),
                $clearSearchBtn = $form.find('.clear-search');
            
            var toggleSearchBtnVisibility = function(){
                if ($inuptElement.val() === ''){
                    $clearSearchBtn.hide();
                } else {
                    $clearSearchBtn.show();
                }
            };
            toggleSearchBtnVisibility();
            
            $inuptElement.on('change', toggleSearchBtnVisibility);
            $inuptElement.on('keyup', function(){
                toggleSearchBtnVisibility();
                if (!!timeout){
                    clearTimeout(timeout);
                }
                timeout = setTimeout(function(){
                    $form.submit();
                }, 1000);
                toggleSearchBtnVisibility;
            });
           
            $form.on('submit', function(){
                if (!!timeout){
                    clearTimeout(timeout);
                } 
            });
            
           
           $clearSearchBtn.on('click tap', function(){
               $form.find('input[name="search"]').val('');
               $form.submit();
           });
        });
    },
    
    enableTooltips: function(){
        $('[data-toggle="tooltip"]').livequery(function(){
            $(this).webuiPopover({
                animation: 'pop',
                maxWidth: '200px'
            });
        });
        $(window).on('statechange', function(){
           $('[data-toggle="tooltip"]').webuiPopover('destroy'); 
        });
    },
    
    enableSubmodules: function(){
        $('a.menu-item.hasSubmodules').livequery(function(){
            $(this).on('click tap', function () {
                var $a = $(this);
                var id = $a.attr('id');
                $('a.menu-item.hasSubmodules.active').each(function(){
                    var $b = $(this);
                    var otherId = $b.attr('id');
                    if (otherId !== id){
                         $b.parent().find($('.' + otherId + '-subModules')).hide();
                         $('.wrapper').removeClass('expanded');
                         $b.removeClass('active');
                    }
                });
                $a.parent().find($('.' + id + '-subModules')).slideToggle(200);
                $a.toggleClass('active');
                if (!$a.closest('#site-menu').length){
                    $('.wrapper').toggleClass('expanded');
                }
                return false;
            });
        });
        $(window).on('statechangecomplete', function(){
            $('.wrapper').removeClass('expanded');
        });
    },
    
    enableSlick: function(){
        $('.gallery-autoplay').livequery(function(){
            var $elem = $(this);
            $elem.on('init', function(event, slick){
                window.setTimeout(function(){$(window).trigger('slickinitialized');}, 300);
            });

            var autoplaySpeed = $elem.attr('data-autoplay-speed') || 5000;

            $elem.slick({
                "dots": true,
                "accessibility": true,
                "autoplay": true,
                "autoplaySpeed": autoplaySpeed,
                "arrows": true,
                "adaptiveHeight": false,
                "lazyLoad": "progressive"
            });
        });
    },
    
    saveLastPage(){
        if (project.isNative()){
            window.localStorage.setItem(LOCAL_STORAGE_LAST_PAGE, window.location.href);
            $(window).on('statechangecomplete', function(){
                window.localStorage.setItem(LOCAL_STORAGE_LAST_PAGE, window.location.href);
            });
        }
    }
};

/* localStorage in-memory polyfill */
if (!('localStorage' in window)) {
  window.localStorage = {
    _data       : {},
    setItem     : function(id, val) { return this._data[id] = String(val); },
    getItem     : function(id) { return this._data.hasOwnProperty(id) ? this._data[id] : undefined; },
    removeItem  : function(id) { return delete this._data[id]; },
    clear       : function() { return this._data = {}; }
  };
}

var lastPage = window.localStorage.getItem(LOCAL_STORAGE_LAST_PAGE);
//if we are running standalone mode, restore the page that the user last viewed
if (project.isNative() && lastPage && lastPage !== window.location.href){
    //make sure to remove last page to prevent redirect loops
    window.localStorage.removeItem(LOCAL_STORAGE_LAST_PAGE);
    window.location.href = lastPage;
} else {        
    $(document).ready(function () {
        project.enableBackButton();
        project.enableForms();
        project.enablePrivateDataLinks();
        project.enableDatePicker();
        project.enableFormAutoSubmit();
        project.enableSideMenu();
        project.enableAccordion();
        project.enableBookingLoginSelection();
        project.enableSelectPicker();
        project.enablePlusMinusInputFields();
        project.enableUpdateBooking();
        project.enableRegexChecksOnInputs();
        project.enablePayMill();
        project.enableAdvancedProfile();
        project.enableSelectToggle();
        project.enableAutoSearch();
        project.enableTooltips();
        project.enableSubmodules();
        project.enableSlick();
        project.saveLastPage();
    });
}

module.exports = project;
