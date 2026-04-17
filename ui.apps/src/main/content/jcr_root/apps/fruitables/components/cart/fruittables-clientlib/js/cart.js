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
    console.warn("Invalid cart cookie. Resetting...", e);
    document.cookie = CART_COOKIE + "=[]; path=/";
    return [];
  }
}


function saveCart(cart) {
  document.cookie =
    CART_COOKIE + "=" + encodeURIComponent(JSON.stringify(cart)) + "; path=/";
}


function getProductUrl(path) {
  if (path.includes("jcr:content")) {
    return path + ".json";
  }

  return path + "/jcr:content/data/master.json";
}


function updateQty(productPath, change) {
  let cart = getCart();

  const item = cart.find(i => i.path === productPath);
  if (!item) return;

  item.qty += change;

  if (item.qty <= 0) {
    cart = cart.filter(i => i.path !== productPath);
  }

  saveCart(cart);
  location.reload();
}

function deleteItem(productPath) {
  let cart = getCart();
  cart = cart.filter(i => i.path !== productPath);
  saveCart(cart);
  location.reload();
}


document.addEventListener("DOMContentLoaded", function () {

  const container = document.getElementById("cart-items");
  if (!container) return;

  const cart = getCart();

  if (cart.length === 0) {
    container.innerHTML = "<p>Your cart is empty</p>";
    return;
  }

  let subtotal = 0;

  cart.forEach(item => {

    const url = getProductUrl(item.path);

    fetch(url)
      .then(res => {
        if (!res.ok) throw new Error("HTTP " + res.status);
        return res.json();
      })
      .then(p => {

        if (!p) {
          console.warn("Invalid product:", item.path);
          return;
        }

        const price = Number(p.productPrice) || 0;
        const total = price * item.qty;

        subtotal += total;

        container.innerHTML += `
          <div class="cart-row" data-path="${item.path}">
            <img src="${p.productImage || ''}" />
            <h3>${p.productName || 'No Name'}</h3>
            <p>₹ ${price}</p>

            <div class="qty-box">
              <button class="qty-btn minus">-</button>
              <span class="qty-value">${item.qty}</span>
              <button class="qty-btn plus">+</button>
            </div>

            <p class="total">₹ ${total}</p>
            <button class="delete-btn">✖</button>
          </div>
        `;

       
        const subEl = document.getElementById("subtotal");
        if (subEl) subEl.innerText = "₹ " + subtotal;

        const totalEl = document.getElementById("total");
        if (totalEl) totalEl.innerText = "₹ " + subtotal;

      })
      .catch(err => {
        console.error("Fetch error:", url, err);
      });

  });


  container.addEventListener("click", function (e) {

    const row = e.target.closest(".cart-row");
    if (!row) return;

    const path = row.dataset.path;

    if (e.target.classList.contains("plus")) {
      updateQty(path, 1);
    }

    if (e.target.classList.contains("minus")) {
      updateQty(path, -1);
    }

    if (e.target.classList.contains("delete-btn")) {
      deleteItem(path);
    }

  });

});