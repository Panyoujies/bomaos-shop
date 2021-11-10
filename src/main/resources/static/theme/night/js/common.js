layui.config({
    base: getProjectUrl() + 'night/module/'
}).use(['jquery', 'element', 'util', 'admin'], function () {
    var $ = layui.jquery;
    var element = layui.element;
    var util = layui.util;
    var admin = layui.admin;
    admin.removeLoading();

    if ($('.ew-header').length > 0) {
        // 获取当前页面所有的导航id
        var navIds = [];
        $('[nav-id]').each(function () {
            navIds.push($(this).attr('nav-id'));
        });

        // 导航点击事件
        element.on('nav(ew-header-nav)', function (elem) {
            var layHref = $(elem).attr('lay-href');
            if (layHref) {
                if (navIds.length == 0) {
                    location.href = layHref;
                } else if (layHref.indexOf('#') != -1) {
                    var hash = layHref.substring(layHref.indexOf('#') + 1);
                    var $section = $('[nav-id="' + hash + '"]');
                    if ($section.length > 0) {
                        $('html,body').animate({scrollTop: $section.offset().top - 70}, 300);
                    }
                } else {
                    $('html').animate({scrollTop: 0}, 300);
                }
            }
        });

        // 滚动监听，节流处理
        if (navIds.length > 0) {
            // nav-scroll事件
            $(document).on('click', '[nav-scroll]', function () {
                var hash = $(this).attr('nav-scroll');
                var $section = $('[nav-id="' + hash + '"]');
                if ($section.length > 0) {
                    $('.ew-header .layui-nav-item').removeClass('layui-this');
                    $('.ew-header a[lay-href$="#' + hash + '"]').parent().addClass('layui-this');
                    $('html,body').animate({scrollTop: $section.offset().top - 70}, 300);
                }
            });
        }
    }

    // 移动设备下头部导航多级菜单展开折叠
    $('body').on('click', '.ew-nav-group .layui-nav-item', function (e) {
        if (admin.getPageWidth() < 935) {
            $('.ew-nav-group .layui-nav-item>.layui-nav-child').removeClass('layui-anim layui-anim-upbit');
            var $navChild = $(this).children('.layui-nav-child');
            if ($navChild.hasClass('layui-show')) {
                $navChild.removeClass('layui-show');
                $(this).find('.layui-nav-more').removeClass('layui-nav-mored');
            } else {
                $('.ew-nav-group .layui-nav-item>.layui-nav-child').removeClass('layui-show');
                $('.ew-nav-group .layui-nav-item>a>.layui-nav-more').removeClass('layui-nav-mored');
                $navChild.addClass('layui-show');
                $(this).find('.layui-nav-more').addClass('layui-nav-mored');
            }
        }
    });

});

// 获取当前项目的根路径
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('night'));
}