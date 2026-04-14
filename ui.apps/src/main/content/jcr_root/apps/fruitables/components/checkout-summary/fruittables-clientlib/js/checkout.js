(function () {

    function parseAmount(text) {
        if (!text) return 0;
        return parseFloat(text.replace(/[^0-9.]/g, "")) || 0;
    }

    function initCheckout() {

        const subtotalEl = document.getElementById("subtotal");
        const totalEl = document.getElementById("total");

        if (!subtotalEl || !totalEl) return;

        const subtotal = parseAmount(subtotalEl.innerText);

        const flatRadio = document.querySelector("input[name='shipping'][value='flat']");
        const pickupRadio = document.querySelector("input[name='shipping'][value='pickup']");

        let flatRate = 0;
        let pickupRate = 0;

        if (flatRadio) {
            const text = flatRadio.parentElement.innerText;
            flatRate = parseAmount(text);
        }

        if (pickupRadio) {
            const text = pickupRadio.parentElement.innerText;
            pickupRate = parseAmount(text);
        }

        
        const shippingInputs = document.querySelectorAll("input[name='shipping']");

        shippingInputs.forEach(function (input) {
            input.addEventListener("change", function () {

                let shipping = 0;
               
                if (this.value === "pickup") {
                    shipping = pickupRate;
                } else {
                    shipping = flatRate;
                }
                 if (this.value === "free") {
                    shipping = 0;
                }

                const newTotal = subtotal + shipping;

                totalEl.innerText = "$" + newTotal.toFixed(2);
            });
        });

        const paymentInputs = document.querySelectorAll("input[name='payment']");
        const paymentContents = document.querySelectorAll(".payment-content");

        function hideAllPaymentContent() {
            paymentContents.forEach(function (div) {
                div.style.display = "none";
            });
        }

        paymentInputs.forEach(function (input) {
            input.addEventListener("change", function () {

                hideAllPaymentContent();

                const selectedId = this.value + "-content";
                const selectedDiv = document.getElementById(selectedId);

                if (selectedDiv) {
                    selectedDiv.style.display = "block";
                }
            });
        });

      
        hideAllPaymentContent();

        const checkedPayment = document.querySelector("input[name='payment']:checked");
        if (checkedPayment) {
            const selectedDiv = document.getElementById(checkedPayment.value + "-content");
            if (selectedDiv) {
                selectedDiv.style.display = "block";
            }
        }
    }

    document.addEventListener("DOMContentLoaded", initCheckout);

})();

document.addEventListener("DOMContentLoaded", function () {

    const btn = document.querySelector(".place-order");
    if (!btn) return;

    btn.addEventListener("click", function () {

        const cartItems = document.querySelectorAll(".cart-row");
        const shipping = document.querySelector("input[name='shipping']:checked");
        const payment = document.querySelector("input[name='payment']:checked");
        const popup = document.getElementById("orderPopup");

        if (cartItems.length === 0) {
            alert("Cart is empty!");
            return;
        }

        if (!shipping) {
            alert("Select shipping option");
            return;
        }

        if (!payment) {
            alert("Select payment option");
            return;
        }

        const firstName = document.getElementById("firstName");
        const lastName = document.getElementById("lastName");
        const address = document.getElementById("address");
        const city = document.getElementById("city");
        const country = document.getElementById("country");
        const zip = document.getElementById("zip");
        const mobile = document.getElementById("mobile");
        const email = document.getElementById("email");

        if (!firstName.value.trim()) {
            alert("Enter First Name");
            firstName.focus();
            return;
        }

        if (!lastName.value.trim()) {
            alert("Enter Last Name");
            lastName.focus();
            return;
        }

        if (!address.value.trim()) {
            alert("Enter Address");
            address.focus();
            return;
        }

        if (!city.value.trim()) {
            alert("Enter City");
            city.focus();
            return;
        }

        if (!country.value.trim()) {
            alert("Enter Country");
            country.focus();
            return;
        }

        if (!zip.value.trim()) {
            alert("Enter ZIP Code");
            zip.focus();
            return;
        }

        if (!mobile.value.trim()) {
            alert("Enter Mobile Number");
            mobile.focus();
            return;
        }

        if (!email.value.trim()) {
            alert("Enter Email");
            email.focus();
            return;
        }

        if (popup) {
            popup.style.display = "flex";
        }
    });

});

function closePopup() {
     document.getElementById('orderPopup').style.display = 'none';
      location.reload();

 }