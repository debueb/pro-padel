/*
 * Main.js
 * project specific functionality
 */
var app = app || {};
app.main = {};

(function () {

    var self = this;

    self.showShadow = function () {
        $('#shadow').show();
    };

    self.hideShadow = function () {
        $('#shadow').hide();
    };

    self.showSpinner = function () {
        self.showShadow();
        $('#ballWrapper').css('display', 'block');
    };

    self.hideSpinner = function () {
        self.hideShadow();
        $('#ballWrapper').css('display', 'none');
    };

    self.enableBackButton = function () {
        $('.btn-back').livequery(function(){
            $(this).on('click tap', function () {
                window.History.back();
            });
        });
    };

    self.enableForms = function () {
        //prevent duplicate form submission
        $('form').not('.ajaxify').livequery(function(){
            $(this).on('submit', function () {
                window.addEventListener("pagehide", function(){
                   self.hideSpinner();
                   $('form :submit').removeAttr("disabled");
                });
                self.showSpinner();
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
    };
    
    self.enableDatePicker = function () {

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
            var datepicker = $(this).find('.datepicker'),
                    altField = $(this).find('.datepicker-input'),
                    datePickerIcon = $(this).find('.datepicker-icon'),
                    textField = $(this).find('.datepicker-text'),
                    textContainer = $(this).find('.datepicker-text-container'),
                    maxDate = $(datepicker).attr('data-max-date'),
                    allowPast = $(datepicker).attr('data-allow-past'),
                    dayConfigs = $(datepicker).attr('data-day-config');

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
                    $('body', 'html').animate({scrollTop: textContainer.offset().top - $('.navbar-fixed-top').height()});
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
    };

    self.enablePrivateDataLinks = function () {
        $('.private-data').livequery(function(){
            $(this).on('click tap', function () {
                var prefix = $(this).attr('data-prefix');
                var fake = $(this).attr('data-fake');
                document.location.href = prefix + app.main.rot47(fake);
            });
        });
    };

    self.rot47 = function (s) {
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
    };

    self.enableSideMenu = function () {
        var isOpen = function () {
            return $('#site-canvas').hasClass('show-nav');
        };

        var toggleMenu = function () {
            if (isOpen()) {
                $('#site-shadow').fadeOut(300);
                // close side menu
                if (navigator.userAgent.match(/iemobile/i)) {
                    $('#site-canvas, #site-shadow, .footer').removeClass('show-nav');
                    $('#site-menu').css('right', '');
                } else {
                    $('#site-canvas, #site-shadow, #site-menu, .footer').removeClass('show-nav');
                }
                // reenable scrolling of content
                $('#site-shadow').off('touchmove');
            } else {
                $('#site-shadow').fadeIn(300);
                // open side menu
                if (navigator.userAgent.match(/iemobile/i)) {
                    $('#site-canvas, #site-shadow, .footer').addClass('show-nav');
                    $('#site-menu').css('right', '0px');
                } else {
                    $('#site-canvas, #site-shadow, #site-menu, .footer').addClass('show-nav');
                }
                // prevent scrolling of content when interacting with site menu
                $('#site-shadow').on('touchmove', function(e) {e.preventDefault();});
            }
        };

        $('.navbar-toggle, #site-shadow, #site-menu a').livequery(function(){
            $(this).on('tap click', function () {
                var btn = $('.navbar-toggle');
                if (btn.hasClass('active')){
                    btn.removeClass('active');
                } else {
                    btn.addClass('active');
                }
                toggleMenu();
                return true;
            });
        });
    };

    self.enableAccordion = function () {
        $('.accordion').livequery(function(){
            $(this).accordion({"heightStyle": "content"});
        });
    };

    self.enableBookingLoginSelection = function () {
        $('.btn-booking-submit').livequery(function(){
            $(this).on('tap click', function () {
                $('#bookingType').val($(this).attr('data-booking-type'));
            });
        });
    };

    self.enableSelectPicker = function () {
        $('.select-simple, .select-multiple').livequery(function(){
            var options = {
                iconBase: 'fa',
                tickIcon: 'fa-check',
                dropupAuto: false,
                countSelectedText: '{0}'
            };
            if (!!$(this).attr('data-selected-text-format')){
                options.selectedTextFormat = 'count > 2';
            }
            $(this).selectpicker(options);
        });
        
        $('.select-ajax-search').livequery(function(){
            $(this).selectpicker({
                liveSearch: true
            });
            if($(this).data('selectpicker')){
                $(this).ajaxSelectPicker({
                    ajax: {
                        method: 'GET',
                        data: function () {
                            var params = {
                                q: '{{{q}}}'
                            };
                            return params;
                        }
                    },
                    cache: false,
                    clearOnEmpty: true,
                    preserveSelected: true
                });
            }
        });
    };

    self.enablePlusMinusInputFields = function () {
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

                minValue = parseInt($(this).attr('min'));
                maxValue = parseInt($(this).attr('max'));
                valueCurrent = parseInt($(this).val());

                name = $(this).attr('name');
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
    };

    self.enableUpdateBooking = function () {
        var updateAll = function(){
            var target = $(this).find('option:selected').attr('data-target');
                $('.booking-duration-container').hide();
                $('.booking-duration-container').find('select').prop('disabled', 'disabled');
                $(target).show();
                $(target).find('select').prop('disabled', false);
                updatePrice.apply($(target).find('select[name="duration"]'));
        };
        
        var updatePrice = function(){
            var target = $('#booking-price'),
                price = $(this).find('option:selected').attr('data-price');
            target.html(price);
        };
        
        $('select[id="booking-court"]').livequery(function(){
            //first run
            updateAll.apply(this);
        
            $(this).on('change', function () {
                updateAll.apply(this);
            });
        });
        
        $('select.booking-duration').livequery(function(){
            $(this).on('change', function () {
                updatePrice.apply(this);
            });
        });
    };

    self.enablePayMill = function () {

        var formIdentifier = '.paymill-form';

        function PaymillResponseHandler(error, result) {
            self.hideSpinner();
            self.hideShadow();
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
    };
    
    self.enableRegexChecksOnInputs = function(){
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
    };
    
    self.enableAdvancedProfile = function(){
        $('.picture').livequery(function(){
            var $picture = $(this);
            $picture.on('click tap', function(){
                $(this).next().first('.picture-input').click();
                return false;
            });
            $('.picture-input').on('change', function(){
                var value = $(this).val();
                var lastIndex = (value.lastIndexOf("\\") === -1) ? 0 : value.lastIndexOf("\\")+1;
                $(this).prev().first('.picture').find('.picture-subtext').text(value.slice(lastIndex)); 
            });
        });
        
        $('div.product-chooser').not('.disabled').find('div.product-chooser-item').livequery(function(){
            $(this).on('click tap', function(){
                $(this).parent().parent().find('div.product-chooser-item').removeClass('selected');
                $(this).addClass('selected');
                $(this).find('input[type="radio"]').prop("checked", true);
            });
	});
    };
    
    self.enableGalleryAutoPlay = function(){
        $('.gallery-autoplay').livequery(function(){
            if (!($(this).parents('.summernote-form').length)){ //do not initialize when inside editor
                $(this).slick({
                    "autoplaySpeed": 3000,
                    "arrows": true,
                    "adaptiveHeight": false
                });
            }
        });
    };
    
    self.enableSelectToggle = function () {
        
        var showSelect = function(){
            //hide all select-toggle- elements
            $(document).find("[class^='select-toggle-']").hide();
            
            var selectedValues = [];
            $(document).find('select.select-toggle').each(function(){
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
    };
    
    self.enableAutoSearch = function(){
        $('form.search').livequery(function(){
            
            var timeout;
            
            var $form = $(this);
            $form.find('input[name="search"]').on('keyup', function(){
                if (!!timeout){
                    clearTimeout(timeout);
                }
                timeout = setTimeout(function(){
                    $form.submit();
                }, 1000);
               
           });
        });
    };  
    
    return app;
}).apply(app.main);


$(document).ready(function () {

    app.main.enableBackButton();
    app.main.enableForms();
    app.main.enablePrivateDataLinks();
    app.main.enableDatePicker();
    app.main.enableSideMenu();
    app.main.enableAccordion();
    app.main.enableBookingLoginSelection();
    app.main.enableSelectPicker();
    app.main.enablePlusMinusInputFields();
    app.main.enableUpdateBooking();
    app.main.enableRegexChecksOnInputs();
    app.main.enablePayMill();
    app.main.enableAdvancedProfile();
    app.main.enableGalleryAutoPlay();
    app.main.enableSelectToggle();
    app.main.enableAutoSearch();
    
});
