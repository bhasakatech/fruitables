document.addEventListener("DOMContentLoaded", function () {
    const sections = document.querySelectorAll(".product-section.grid-75-view");
 
    sections.forEach(function (section) {
        const container = section.querySelector(".product-container");
        if (!container) return;
 
        const cards = Array.from(container.querySelectorAll(".product-card"));
        const itemsPerPage = 9;
        const totalPages = Math.ceil(cards.length / itemsPerPage);
 
        if (totalPages <= 1) return;
 
        let currentPage = 1;
 
        const oldPagination = section.querySelector(".product-pagination");
        if (oldPagination) {
            oldPagination.remove();
        }
 
        const pagination = document.createElement("div");
        pagination.className = "product-pagination";
 
        const prevBtn = document.createElement("button");
        prevBtn.className = "page-btn prev-btn";
        prevBtn.type = "button";
        prevBtn.innerHTML = "&laquo;";
 
        const numbersWrap = document.createElement("div");
        numbersWrap.className = "page-numbers";
 
        const nextBtn = document.createElement("button");
        nextBtn.className = "page-btn next-btn";
        nextBtn.type = "button";
        nextBtn.innerHTML = "&raquo;";
 
        pagination.appendChild(prevBtn);
        pagination.appendChild(numbersWrap);
        pagination.appendChild(nextBtn);
 
        section.appendChild(pagination);
 
        function renderPage(page) {
            const start = (page - 1) * itemsPerPage;
            const end = start + itemsPerPage;
 
            cards.forEach(function (card, index) {
                card.style.display = index >= start && index < end ? "" : "none";
            });
 
            numbersWrap.innerHTML = "";
 
            for (let i = 1; i <= totalPages; i++) {
                const pageBtn = document.createElement("button");
                pageBtn.className = "page-btn" + (i === page ? " active" : "");
                pageBtn.type = "button";
                pageBtn.textContent = i;
 
                pageBtn.addEventListener("click", function () {
                    currentPage = i;
                    renderPage(currentPage);
                });
 
                numbersWrap.appendChild(pageBtn);
            }
 
            prevBtn.disabled = page === 1;
            nextBtn.disabled = page === totalPages;
        }
 
        prevBtn.addEventListener("click", function () {
            if (currentPage > 1) {
                currentPage--;
                renderPage(currentPage);
            }
        });
 
        nextBtn.addEventListener("click", function () {
            if (currentPage < totalPages) {
                currentPage++;
                renderPage(currentPage);
            }
        });
 
        renderPage(currentPage);
    });
});