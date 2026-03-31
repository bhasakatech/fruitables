function toggleMenu() {
   const nav = document.getElementById("mobileNav");
    const hamburger = document.querySelector(".fruitables-hamburger");

    nav.classList.toggle("active");
    hamburger.classList.toggle("active"); // 👈 important
    
}

function toggleSubMenu(el) {

    const parent = el.closest(".fruitables-nav-item");
    if (parent.classList.contains("open")) {
        parent.classList.remove("open");
        return; // 👈 IMPORTANT (stop here)
    }

    document.querySelectorAll(".fruitables-nav-item").forEach(item => {
        item.classList.remove("open");
    });

    parent.classList.add("open");
}


function openSearch() {
    document.getElementById("fruitables-search-overlay")
        .classList.add("active");
}

function closeSearch() {
    document.getElementById("fruitables-search-overlay")
        .classList.remove("active");
}



document.addEventListener("DOMContentLoaded", function () {

    console.log("working");
    const header = document.querySelector(
        ".header-wrapper"
    );

    if (!header) return;

    window.addEventListener("scroll", function () {

        if (window.scrollY > 50) {
            header.classList.add("scrolled");
        } else {
            header.classList.remove("scrolled");
        }

    });

});