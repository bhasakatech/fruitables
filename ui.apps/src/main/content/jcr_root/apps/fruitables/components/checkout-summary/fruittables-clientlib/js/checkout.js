const CART_COOKIE = "cart";

function getCart() {
  const cookie = document.cookie
    .split("; ")
    .find(row => row.startsWith(CART_COOKIE + "="));

  if (!cookie) return [];

  try {
    const value = cookie.split("=")[1];
    if (!value || value === "undefined") return [];

    return JSON.parse(decodeURIComponent(value));
  } catch (e) {
    document.cookie = CART_COOKIE + "=[]; path=/";
    return [];
  }
}

function getProductUrl(path) {
  return path.includes("jcr:content")
    ? path + ".json"
    : path + "/jcr:content/data/master.json";
}

function getSubtotalValue() {
  const el = document.getElementById("subtotal");
  return el ? Number(el.innerText.replace(/[^\d.]/g, "")) || 0 : 0;
}

function updateTotal() {
  const subtotal = getSubtotalValue();

  const selected = document.querySelector("input[name='shipping']:checked");
  if (!selected) return;

  let shipping = 0;

  if (selected.value !== "free") {
    shipping = Number(selected.parentElement.innerText.replace(/[^\d.]/g, "")) || 0;
  }

  const total = subtotal + shipping;

  const totalEl = document.getElementById("total");
  if (totalEl) totalEl.innerText = "₹" + total;
}

function initPaymentToggle() {

  const contents = document.querySelectorAll(".payment-content");
  const radios = document.querySelectorAll("input[name='payment']");

  function hideAll() {
    contents.forEach(el => el.style.display = "none");
  }

  hideAll();

  const checked = document.querySelector("input[name='payment']:checked");
  if (checked) {
    const el = document.getElementById(checked.value + "-content");
    if (el) el.style.display = "block";
  }

  radios.forEach(radio => {
    radio.addEventListener("change", function () {
      hideAll();
      const el = document.getElementById(this.value + "-content");
      if (el) el.style.display = "block";
    });
  });
}

function initPlaceOrder() {
  const btn = document.querySelector(".place-order");
  if (!btn) return;

  btn.addEventListener("click", function () {

    const cart = getCart();

    if (cart.length === 0) {
      alert("Your cart is empty!");
      return;
    }

    const shipping = document.querySelector("input[name='shipping']:checked");
    if (!shipping) {
      alert("Please select shipping option");
      return;
    }

    const payment = document.querySelector("input[name='payment']:checked");
    if (!payment) {
      alert("Please select payment option");
      return;
    }

    const requiredFields = [
      "firstName",
      "lastName",
      "address",
      "city",
      "country",
      "zip",
      "mobile",
      "email"
    ];

    for (let id of requiredFields) {
      const el = document.getElementById(id);
      if (!el || !el.value.trim()) {
        alert("Please fill all required fields");
        el && el.focus();
        return;
      }
    }

    document.cookie = CART_COOKIE + "=[]; path=/";

    const container = document.getElementById("checkout-items");
    if (container) container.innerHTML = "";

    const emptyMsg = document.getElementById("empty-cart-msg");
    if (emptyMsg) emptyMsg.style.display = "block";

    const subEl = document.getElementById("subtotal");
    if (subEl) subEl.innerText = "₹0";

    const totalEl = document.getElementById("total");
    if (totalEl) totalEl.innerText = "₹0";

    const popup = document.getElementById("orderPopup");
    if (popup) popup.style.display = "flex";
  });
}

document.addEventListener("DOMContentLoaded", function () {

  const container = document.getElementById("checkout-items");
  if (!container) return;

  const cart = getCart();

  if (cart.length === 0) {
    const empty = document.getElementById("empty-cart-msg");
    if (empty) empty.style.display = "block";
    return;
  }

  let subtotal = 0;

  cart.forEach(item => {

    fetch(getProductUrl(item.path))
      .then(res => res.json())
      .then(p => {

        const price = Number(p.productPrice) || 0;
        const total = price * item.qty;

        subtotal += total;

        container.innerHTML += `
          <div class="cart-row">
            <img src="${p.productImage || ''}" width="90"/>
            <span>${p.productName || ''}</span>
            <span>₹${price}</span>
            <span>${item.qty}</span>
            <span>₹${total}</span>
          </div>
        `;

        const subEl = document.getElementById("subtotal");
        if (subEl) subEl.innerText = "₹" + subtotal;

        const totalEl = document.getElementById("total");
        if (totalEl) totalEl.innerText = "₹" + subtotal;
      });
  });

  document.querySelectorAll("input[name='shipping']").forEach(radio => {
    radio.addEventListener("change", updateTotal);
  });

  initPaymentToggle();
  initPlaceOrder();
});

function closePopup() {
  const popup = document.getElementById("orderPopup");
  if (popup) popup.style.display = "none";
  location.reload();
}