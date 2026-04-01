document.addEventListener("DOMContentLoaded", function () {

    const btn = document.getElementById("scrollTopBtn");
    if (!btn) return;

    window.addEventListener("scroll", function () {
        if (window.scrollY > 200) {
            btn.classList.add("show");
        } else {
            btn.classList.remove("show");
        }
    });

    
    btn.addEventListener("click", function () {
``
        btn.classList.add("clicked");

        setTimeout(function () {
            btn.classList.remove("clicked");
        }, 200);

        setTimeout(function () {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        }, 400);

    });

});