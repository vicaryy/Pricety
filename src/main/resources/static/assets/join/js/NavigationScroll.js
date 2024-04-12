const nav = document.querySelector("nav");

function controlNav() {
    controlNavColor();
}

function controlNavColor() {
    const y = window.scrollY;
    let opacity = 0;

    if (y >= 75 && y <= 400)
        opacity = (y - 75) / 325 * 0.65 + 0.05;
    else if (y > 400)
        opacity = 0.7;

    nav.style.backgroundColor = `rgba(0, 0, 0, ${opacity})`;
}