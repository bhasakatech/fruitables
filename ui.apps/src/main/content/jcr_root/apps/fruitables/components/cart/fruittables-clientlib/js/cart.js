document.addEventListener("DOMContentLoaded", function () {

  fetch("/libs/granite/csrf/token.json")
    .then(res => res.json())
    .then(data => {

      const token = data.token;

      document.querySelectorAll(".cart-row").forEach(row => {

        const productPath = row.dataset.path;


        row.querySelector(".plus").addEventListener("click", () => {
          updateCart(productPath, "inc");
        });


        row.querySelector(".minus").addEventListener("click", () => {
          updateCart(productPath, "dec");
        });


        row.querySelector(".delete-btn").addEventListener("click", () => {
          updateCart(productPath, "delete");
        });

      });

      function updateCart(productPath, action) {

        fetch("/bin/cart/update", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "CSRF-Token": token
          },
          body:
            "productPath=" + encodeURIComponent(productPath) +
            "&action=" + action
        })
        .then(() => location.reload());
      }

    });

});

function goToCheckout() {
    window.location.href = "/content/fruitables/us/en/Fruitables-Homepage/Pages/checkout.html";
}