let csrfToken = "";

document.addEventListener("DOMContentLoaded", function () {
  
  
  fetch("/libs/granite/csrf/token.json")
    .then(res => res.json())
    .then(data => {
      csrfToken = data.token;

    
      document.querySelectorAll(".add-to-cart").forEach(btn => {
        
        btn.addEventListener("click", function () {
          
          const productPath = this.dataset.path;
          const button = this;   // reference to current button

          console.log("Sending productPath:", productPath);

         
          button.disabled = true;
          button.innerText = "Adding...";

          fetch("/bin/cart/add", {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
              "CSRF-Token": csrfToken
            },
            body: "productId=" + encodeURIComponent(productPath)
          })
          .then(res => {
            if (res.ok) {
              return res.text();
            } else {
              throw new Error("Failed to add to cart");
            }
          })
          .then(() => {
            
            alert("✅ Product has been added to your cart!");

           
            button.innerText = "Added ✓";
            button.style.backgroundColor = "#28a745"; 
          })
          .catch(err => {
            console.error("Cart error:", err);
            alert("❌ Failed to add product to cart. Please try again.");
            
            // Reset button on error
            button.disabled = false;
            button.innerText = "Add to Cart";
          });
        });
      });
    })
    .catch(err => console.error("CSRF Token fetch failed:", err));
});