(function () {
    "use strict";

    var AUTO_DELAY = 10000;

    function getVisibleSlides() {
        if (window.innerWidth <= 576) return 1;
        if (window.innerWidth <= 991) return 2;
        return 4;
    }

    function initCarousel(section) {
        var wrapper = section.querySelector("[data-carousel]");
        var track = section.querySelector(".product-container");
        var slides = section.querySelectorAll(".product-card");
        var prevBtn = section.querySelector(".carousel-btn.prev");
        var nextBtn = section.querySelector(".carousel-btn.next");

        var currentIndex = 0;
        var timer = null;

        if (!wrapper || !track || slides.length === 0) return;

        function getMaxIndex() {
            return Math.max(0, slides.length - getVisibleSlides());
        }

        function updatePosition() {
            if (!slides.length) return;

            var slideWidth = slides[0].offsetWidth;
            var gap = 20;
            var offset = currentIndex * (slideWidth + gap);
            track.style.transform = "translateX(-" + offset + "px)";
        }

        function moveNext() {
            var maxIndex = getMaxIndex();
            currentIndex = currentIndex < maxIndex ? currentIndex + 1 : 0;
            updatePosition();
        }

        function movePrev() {
            var maxIndex = getMaxIndex();
            currentIndex = currentIndex > 0 ? currentIndex - 1 : maxIndex;
            updatePosition();
        }

        function startAuto() {
            stopAuto();
            timer = setInterval(moveNext, AUTO_DELAY);
        }

        function stopAuto() {
            if (timer) clearInterval(timer);
        }

        if (nextBtn) {
            nextBtn.addEventListener("click", function () {
                moveNext();
                startAuto();
            });
        }

        if (prevBtn) {
            prevBtn.addEventListener("click", function () {
                movePrev();
                startAuto();
            });
        }

        window.addEventListener("resize", updatePosition);

        updatePosition();
        startAuto();
    }

    function initCategoryFilter(section) {

        var buttons = section.querySelectorAll(".category-tab");
        var cards = section.querySelectorAll(".product-card");
        var message = section.querySelector(".no-products-message");

        if (!buttons.length || !cards.length) return;

        buttons.forEach(function (btn) {

            btn.addEventListener("click", function () {

                buttons.forEach(function (b) {
                    b.classList.remove("active");
                });
                btn.classList.add("active");

                var selectedCategory = btn.getAttribute("data-category");
                var visibleCount = 0;

                cards.forEach(function (card) {
                    var cardCategory = card.getAttribute("data-category");

                    if (selectedCategory === "all" || selectedCategory === cardCategory) {
                        card.style.display = "block";
                        visibleCount++;
                    } else {
                        card.style.display = "none";
                    }
                });

                if (message) {
                    message.style.display = visibleCount === 0 ? "block" : "none";
                }

            });

        });
    }

    document.addEventListener("DOMContentLoaded", function () {

        var carouselSections = document.querySelectorAll(".product-section.fresh-vegetables");
        carouselSections.forEach(function (section) {
            initCarousel(section);
        });

        var filterSections = document.querySelectorAll(".product-section.organic-products");
        filterSections.forEach(function (section) {
            initCategoryFilter(section);
        });

    });

})();