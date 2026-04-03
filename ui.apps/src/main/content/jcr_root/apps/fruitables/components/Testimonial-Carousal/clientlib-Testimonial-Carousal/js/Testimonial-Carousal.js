(function () {
    "use strict";

    var AUTO_DELAY = 5000;

    function getVisibleSlides() {
        return window.innerWidth <= 991 ? 1 : 2;
    }

    function buildStars(rating) {
        var count = Math.max(0, Math.min(5, parseInt(rating, 10) || 0));
        var output = "";
        var i;

        for (i = 1; i <= 5; i++) {
            if (i <= count) {
                output += '<span class="testimonial-card__star testimonial-card__star--filled">&#9733;</span>';
            } else {
                output += '<span class="testimonial-card__star testimonial-card__star--empty">&#9733;</span>';
            }
        }

        return output;
    }

    function setRatings(component) {
        var ratingElements = component.querySelectorAll(".testimonial-card__stars");

        ratingElements.forEach(function (element) {
            element.innerHTML = buildStars(element.getAttribute("data-rating"));
        });
    }

    function initCarousel(component) {
        var track = component.querySelector("[data-track]");
        var slides = component.querySelectorAll(".testimonial-carousel__slide");
        var prevButton = component.querySelector('[data-direction="prev"]');
        var nextButton = component.querySelector('[data-direction="next"]');
        var currentIndex = 0;
        var direction = "next";
        var timer = null;

        if (!track || slides.length === 0) {
            return;
        }

        function getMaxIndex() {
            return Math.max(0, slides.length - getVisibleSlides());
        }

        function updatePosition() {
            var firstSlide = slides[0];
            var slideWidth = firstSlide.offsetWidth;
            var trackStyle = window.getComputedStyle(track);
            var gap = parseInt(trackStyle.columnGap || trackStyle.gap || 0, 10);
            var offset = currentIndex * (slideWidth + gap);

            track.style.transform = "translateX(-" + offset + "px)";
        }

        function stopLoop() {
            if (timer) {
                window.clearInterval(timer);
                timer = null;
            }
        }

        function moveNext() {
            var maxIndex = getMaxIndex();

            if (currentIndex < maxIndex) {
                currentIndex += 1;
                updatePosition();
            } else {
                stopLoop();
            }
        }

        function movePrev() {
            if (currentIndex > 0) {
                currentIndex -= 1;
                updatePosition();
            } else {
                stopLoop();
            }
        }

        function runByDirection() {
            if (direction === "prev") {
                movePrev();
            } else {
                moveNext();
            }
        }

        function startLoop(newDirection) {
            direction = newDirection;
            stopLoop();
            timer = window.setInterval(runByDirection, AUTO_DELAY);
        }

        function setActiveButton(button) {
            if (prevButton) {
                prevButton.classList.remove("is-active");
            }

            if (nextButton) {
                nextButton.classList.remove("is-active");
            }

            if (button) {
                button.classList.add("is-active");
            }
        }

        if (slides.length <= getVisibleSlides()) {
            if (prevButton) {
                prevButton.style.display = "none";
            }

            if (nextButton) {
                nextButton.style.display = "none";
            }

            return;
        }

        if (prevButton) {
            prevButton.addEventListener("click", function () {
                setActiveButton(prevButton);
                movePrev();

                if (currentIndex > 0) {
                    startLoop("prev");
                }
            });
        }

        if (nextButton) {
            nextButton.addEventListener("click", function () {
                var maxIndex = getMaxIndex();

                setActiveButton(nextButton);
                moveNext();

                if (currentIndex < maxIndex) {
                    startLoop("next");
                }
            });
        }

        window.addEventListener("resize", function () {
            var maxIndex = getMaxIndex();

            if (currentIndex > maxIndex) {
                currentIndex = maxIndex;
            }

            updatePosition();

            if (currentIndex === 0 || currentIndex === maxIndex) {
                stopLoop();
            }
        });

        updatePosition();
        startLoop("next");
    }

    document.addEventListener("DOMContentLoaded", function () {
        var components = document.querySelectorAll('[data-cmp-is="testimonial-carousel"]');

        components.forEach(function (component) {
            setRatings(component);
            initCarousel(component);
        });
    });
})();
