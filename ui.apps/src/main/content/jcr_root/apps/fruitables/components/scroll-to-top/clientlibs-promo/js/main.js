document.addEventListener("DOMContentLoaded", function () {

    const btn = document.getElementById("scrollTopBtn");
    if (!btn) return;

    // Show button after scroll
    window.addEventListener("scroll", function () {
        if (window.scrollY > 200) {
            btn.classList.add("show");
        } else {
            btn.classList.remove("show");
        }
    });

    
    btn.addEventListener("click", function () {
``
        // Add click effect
        btn.classList.add("clicked");

        // Remove effect after short time
        setTimeout(function () {
            btn.classList.remove("clicked");
        }, 200);

        // Scroll after delay
        setTimeout(function () {
            window.scrollTo({
                top: 0,
                behavior: "smooth"
            });
        }, 400);

    });

});