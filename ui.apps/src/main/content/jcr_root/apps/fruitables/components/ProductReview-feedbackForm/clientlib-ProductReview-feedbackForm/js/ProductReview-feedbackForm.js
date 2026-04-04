(function () {
    var forms = document.querySelectorAll(".review-form__form");

    forms.forEach(function (form) {
        var stars = form.querySelectorAll(".review-form__star");
        var ratingInput = form.querySelector(".review-form__rating-input");
        var message = form.querySelector(".review-form__message");

        function paintStars(value) {
            stars.forEach(function (star) {
                var starValue = parseInt(star.getAttribute("data-value"), 10);
                if (starValue <= value) {
                    star.classList.add("is-active");
                } else {
                    star.classList.remove("is-active");
                }
            });
        }

        stars.forEach(function (star) {
            star.addEventListener("click", function () {
                var selectedValue = parseInt(star.getAttribute("data-value"), 10);
                ratingInput.value = selectedValue;
                paintStars(selectedValue);
            });
        });

        form.addEventListener("submit", function (event) {
            event.preventDefault();

            var name = form.querySelector('input[name="name"]').value.trim();
            var email = form.querySelector('input[name="email"]').value.trim();
            var review = form.querySelector('textarea[name="review"]').value.trim();
            var rating = ratingInput.value;

            if (!name || !email || !review || rating === "0") {
                message.hidden = false;
                message.textContent = "Please fill all fields and select a rating.";
                message.classList.add("is-error");
                return;
            }

            message.hidden = false;
            message.textContent = "Form is ready to submit.";
            message.classList.remove("is-error");

            console.log({
                name: name,
                email: email,
                review: review,
                rating: rating
            });
        });
    });
})();
