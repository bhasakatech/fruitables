document.addEventListener("DOMContentLoaded", function () {
    const tabs = document.querySelectorAll(".pd-tab");
    const contents = document.querySelectorAll(".pd-tab-content");
    tabs.forEach(tab => {
        tab.addEventListener("click", function () {
            const tabId = this.getAttribute("data-tab");
            tabs.forEach(btn => btn.classList.remove("active"));
            contents.forEach(content => content.classList.remove("active"));
            this.classList.add("active");
            document.getElementById(tabId).classList.add("active");
        });
    });
});