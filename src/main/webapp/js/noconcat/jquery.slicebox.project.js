$(function () {
    var Page = (function () {

        var $navArrows = $('#nav-arrows').hide(),
                $shadow = $('#slicebox-shadow').hide(),
                slicebox = $('#sb-slider').slicebox({
            onReady: function () {

                $navArrows.show();
                $shadow.show();

            },
            orientation: 'r',
            cuboidsRandom: true,
            autoplay: true
        }),
        init = function () {


            initEvents();

        },
        initEvents = function () {

            $navArrows.children("first").on('click', function () {
                slicebox.next();
                return false;
            });


            $navArrows.children(':last').on('click', function () {

                slicebox.previous();
                return false;

            });

        };

        return {init: init};

    })();
    $(window).on('statechangecomplete', Page.init());
    $(document).ready(Page.init());
});