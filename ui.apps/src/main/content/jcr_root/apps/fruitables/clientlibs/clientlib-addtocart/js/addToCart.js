
const CART_COOKIE = "cart";

function getCart() {
  const cookie = document.cookie
    .split("; ")
    .find(row => row.startsWith("cart="));

  if (!cookie) return [];

  try {
    const value = cookie.split("=")[1];

    if (!value || value === "undefined") return [];

    return JSON.parse(decodeURIComponent(value));
  } catch (e) {
    console.warn("Invalid cart cookie, resetting...", e);


    document.cookie = "cart=[]; path=/";

    return [];
  }
}


function saveCart(cart) {
  document.cookie =
    CART_COOKIE + "=" + encodeURIComponent(JSON.stringify(cart)) + "; path=/";
}


function addToCart(productPath) {
  let cart = getCart();

  const existing = cart.find(item => item.path === productPath);

  if (existing) {
    existing.qty += 1;
  } else {
    cart.push({ path: productPath, qty: 1 });
  }

  saveCart(cart);
}


document.addEventListener("DOMContentLoaded", function () {

  document.querySelectorAll(".add-to-cart").forEach(btn => {

    btn.addEventListener("click", function () {

      const productPath = this.dataset.path;
      const button = this;

      addToCart(productPath);

      button.disabled = true;
      button.innerText = "Added ✓";
      button.style.backgroundColor = "#28a745";

      alert("✅ Product added to cart!");
    });

  });

});