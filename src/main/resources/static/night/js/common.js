layui.config({base:getProjectUrl()+ "/qiantai/module/"}).extend({notice: "notice/notice"}).use(["jquery", "element", "util", "admin"], function () {
    var f = layui.jquery;
    var e = layui.element;
    var d = layui.util;
    var c = layui.admin;
    c.removeLoading();
    if (f(".ew-header").length > 0) {
        var b = [];
        f("[nav-id]").each(function () {
            b.push(f(this).attr("nav-id"))
        });
        e.on("nav(ew-header-nav)", function (g) {
            var j = f(g).attr("lay-href");
            if (j) {
                if (b.length == 0) {
                    location.href = j
                } else {
                    if (j.indexOf("#") != -1) {
                        var i = j.substring(j.indexOf("#") + 1);
                        var h = f('[nav-id="' + i + '"]');
                        if (h.length > 0) {
                            f("html,body").animate({scrollTop: h.offset().top - 70}, 300)
                        }
                    } else {
                        f("html").animate({scrollTop: 0}, 300)
                    }
                }
            }
        });
        if (b.length > 0) {
            f(window).on("scroll", function () {
                a()
            });
            if (location.hash) {
                f('.ew-header a[lay-href$="' + location.hash.substring(1) + '"]').trigger("click")
            } else {
                a()
            }
            f(document).on("click", "[nav-scroll]", function () {
                var h = f(this).attr("nav-scroll");
                var g = f('[nav-id="' + h + '"]');
                if (g.length > 0) {
                    f(".ew-header .layui-nav-item").removeClass("layui-this");
                    f('.ew-header a[lay-href$="#' + h + '"]').parent().addClass("layui-this");
                    f("html,body").animate({scrollTop: g.offset().top - 70}, 300)
                }
            })
        }
    }

    function a() {
        var g = f(window).scrollTop();
        for (var h = b.length - 1; h >= 0; h--) {
            if (g >= (f('[nav-id="' + b[h] + '"]').offset().top - 75)) {
                f(".ew-header .layui-nav-item").removeClass("layui-this");
                f('.ew-header a[lay-href$="#' + b[h] + '"]').parent().addClass("layui-this");
                return
            }
        }
        f(".ew-header .layui-nav-item").removeClass("layui-this");
        f('.ew-header a[lay-href="/index"]').parent().addClass("layui-this")
    }

    f("body").on("click", ".ew-nav-group .layui-nav-item", function (g) {
        if (c.getPageWidth() < 935) {
            f(".ew-nav-group .layui-nav-item>.layui-nav-child").removeClass("layui-anim layui-anim-upbit");
            var h = f(this).children(".layui-nav-child");
            if (h.hasClass("layui-show")) {
                h.removeClass("layui-show");
                f(this).find(".layui-nav-more").removeClass("layui-nav-mored")
            } else {
                f(".ew-nav-group .layui-nav-item>.layui-nav-child").removeClass("layui-show");
                f(".ew-nav-group .layui-nav-item>a>.layui-nav-more").removeClass("layui-nav-mored");
                h.addClass("layui-show");
                f(this).find(".layui-nav-more").addClass("layui-nav-mored")
            }
        }
    })
});

function getProjectUrl() {
    var c = layui.cache.dir;
    if (!c) {
        var e = document.scripts, b = e.length - 1, f;
        for (var a = b; a > 0; a--) {
            if (e[a].readyState === "interactive") {
                f = e[a].src;
                break
            }
        }
        var d = f || e[b].src;
        c = d.substring(0, d.lastIndexOf("/") + 1)
    }
    return c.substring(0, c.indexOf("qiantai"))
};