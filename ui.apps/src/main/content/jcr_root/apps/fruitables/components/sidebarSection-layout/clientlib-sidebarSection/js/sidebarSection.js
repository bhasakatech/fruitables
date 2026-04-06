(function (document) {
    "use strict";

    function revealFeaturedProducts(container) {
        var hiddenItems = container.querySelectorAll("[data-featured-product-item][hidden]");

        hiddenItems.forEach(function (item) {
            item.hidden = false;
        });
    }

    function bindFeaturedProducts(section) {
        var container = section.querySelector("[data-featured-products]");
        var toggle = section.querySelector("[data-featured-products-toggle]");

        if (!container || !toggle) {
            return;
        }

        toggle.addEventListener("click", function () {
            revealFeaturedProducts(container);
            toggle.setAttribute("aria-expanded", "true");
            toggle.parentElement.hidden = true;
        });
    }

    function updatePriceRange(container) {
        var input = container.querySelector("[data-price-range-input]");
        var output = container.querySelector("[data-price-range-output]");
        var example = container.querySelector("[data-price-range-example]");

        if (!input || !output) {
            return;
        }

        var value = input.value;
        output.value = value;
        output.textContent = value;

        if (example) {
            example.textContent = "Example: showing products up to Rs. " + value;
        }
    }

    function bindPriceRange(container) {
        var input = container.querySelector("[data-price-range-input]");

        if (!input) {
            return;
        }

        updatePriceRange(container);
        input.addEventListener("input", function () {
            updatePriceRange(container);
        });
        input.addEventListener("change", function () {
            updatePriceRange(container);
        });
    }

    function init() {
        var featuredProductsSections = document.querySelectorAll("[data-featured-products-section]");
        var priceRanges = document.querySelectorAll("[data-price-range]");

        featuredProductsSections.forEach(bindFeaturedProducts);
        priceRanges.forEach(bindPriceRange);
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
        return;
    }

    init();
}(document));
