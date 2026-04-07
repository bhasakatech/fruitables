(function (document) {
    "use strict";

    function setFeaturedProductsExpanded(container, expanded) {
        var extraItems = container.querySelectorAll("[data-featured-product-extra]");

        extraItems.forEach(function (item) {
            item.hidden = !expanded;
        });
    }

    function bindFeaturedProducts(section) {
        var container = section.querySelector("[data-featured-products]");
        var toggle = section.querySelector("[data-featured-products-toggle]");
        var viewMoreLabel = toggle && toggle.getAttribute("data-view-more-label");
        var viewLessLabel = toggle && toggle.getAttribute("data-view-less-label");

        if (!container || !toggle) {
            return;
        }

        setFeaturedProductsExpanded(container, false);
        toggle.textContent = viewMoreLabel || toggle.textContent;
        toggle.setAttribute("aria-expanded", "false");

        toggle.addEventListener("click", function () {
            var expanded = toggle.getAttribute("aria-expanded") === "true";
            var nextExpandedState = !expanded;

            setFeaturedProductsExpanded(container, nextExpandedState);
            toggle.setAttribute("aria-expanded", String(nextExpandedState));
            toggle.textContent = nextExpandedState
                ? (viewLessLabel || "View Less")
                : (viewMoreLabel || "View More");
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
