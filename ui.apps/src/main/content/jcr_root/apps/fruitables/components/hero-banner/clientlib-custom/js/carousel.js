document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll('.hero-banner').forEach(el => {
        let bg = el.getAttribute('data-bg');
        if (bg) {
            bg = bg + "/_jcr_content/renditions/original";
            el.style.setProperty('--bg-image', `url(${bg})`);
        }
    });
    const carousel = document.querySelector(".carousel");
    if (!carousel) return;
    const slides = carousel.querySelectorAll(".slide");
    const nextBtn = carousel.querySelector(".next");
    const prevBtn = carousel.querySelector(".prev");
    let currentIndex = 0;
    if (slides.length === 0) return;
    slides[currentIndex].classList.add("active");
    function showSlide(index) {
        slides[currentIndex].classList.remove("active");
        currentIndex = index;
        slides[currentIndex].classList.add("active");
    }
    function nextSlide() {
        showSlide((currentIndex + 1) % slides.length);
    }
    function prevSlide() {
        showSlide((currentIndex - 1 + slides.length) % slides.length);
    }
    nextBtn?.addEventListener("click", nextSlide);
    prevBtn?.addEventListener("click", prevSlide);
    let autoSlide = setInterval(nextSlide, 3000);
    carousel.addEventListener("mouseenter", () => clearInterval(autoSlide));
    carousel.addEventListener("mouseleave", () => {
        autoSlide = setInterval(nextSlide, 3000);
    });

});