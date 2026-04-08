document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("billingForm");
    if (!form) return;
    const fields = form.querySelectorAll("input, textarea");
    fields.forEach(field => {
        field.addEventListener("blur", () => validateField(field));
        field.addEventListener("input", () => clearError(field));

    });

    function validateField(field) {
        const error = document.getElementById(field.id + "Error");
        if (!error) return;
        let value = field.value.trim();
        error.textContent = "";
        if (field.hasAttribute("required") && !value) {
            error.textContent = "This field is required";
            return;
        }
        if (field.type === "email" && value) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(value)) {
                error.textContent = "Enter valid email";
                return;
            }
        }
        if (field.name === "mobile" && value) {
            const mobileRegex = /^[0-9]{10}$/;
            if (!mobileRegex.test(value)) {
                error.textContent = "Enter valid 10-digit mobile number";
                return;
            }
        }
    }
    function clearError(field) {
        const error = document.getElementById(field.id + "Error");
        if (error) {
            error.textContent = "";
        }
    }

});