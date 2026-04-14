document.addEventListener("DOMContentLoaded", function () {
    const productLinks = document.querySelectorAll(".clickable-product");
    productLinks.forEach(function (link) {
        link.addEventListener("click", function (e) {
            e.preventDefault();
            let name = this.getAttribute("data-name");
            if (!name) {
                return;
            }
            let slug = name
                .toLowerCase()
                .trim()
                .replace(/\s+/g, '-')
                .replace(/[^a-z0-9-]/g, '')
                .replace(/-+/g, '-');
            let basePath = "/content/fruitables/us/en/pages/shop-details";
            let url;
            if (window.location.pathname.startsWith("/editor.html")) {
                url = "/editor.html" + basePath + "." + slug + ".html";
            } else {
                url = basePath + "." + slug + ".html";
            }
            window.location.href = url;
        });
    });
});