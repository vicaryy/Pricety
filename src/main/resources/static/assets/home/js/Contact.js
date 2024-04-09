document.querySelector(".contact-wrapper").addEventListener("click", e => {
    if (e.target.classList.contains("send-btn")) {
        const spin = e.target.nextElementSibling;
        if (spin != null) {
            e.target.value = "";
            spin.classList.add("active");
        }
    }
});