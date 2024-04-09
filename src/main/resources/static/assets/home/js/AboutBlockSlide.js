const slideElements = document.querySelectorAll(".about-services .wrapper .slider-wrapper");
let isActive = false;
function slideAboutBlocks() {
    const aboutServicesHeight = document.querySelector(".about-services").getBoundingClientRect().top - window.innerHeight;

    if (!isActive && aboutServicesHeight < -100) {
        isActive = true;
        slideElements.forEach(element => element.classList.add("active"));

    }
    else if (isActive && aboutServicesHeight > 500) {
        isActive = false;
        slideElements.forEach(element => element.classList.remove("active"));
    }
}