const submitBtn = document.querySelector(".home-wrapper");

submitBtn.addEventListener("click", e => {
    if (e.target.classList.contains("submit-btn")) {
        e.target.value = "";
        e.target.nextElementSibling.classList.add("active");
    }
});

setInterval(() => {
    const variants = document.querySelector(".variants-wrapper");
    if (variants && !variants.classList.contains("clicked")) {
        variants.classList.add("active");
        variants.querySelectorAll("button").forEach(e => {
            e.addEventListener("click", () => {
                variants.classList.add("clicked");
                setTimeout(() => variants.remove(), 1500);
            });
        });
    }
}, 1000);

function toLogIn(e) {
e.preventDefault();
window.location.href = "/join?l=true";
}
