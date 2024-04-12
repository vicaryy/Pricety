const hamburgerWrapper = document.querySelector(".hamburger-wrapper");
const hamburger = document.querySelector(".hamburger");
const menu = document.querySelectorAll(".popup-menu");
const logOutBtn = document.querySelectorAll(".log-out-button");
hamburgerWrapper.addEventListener("click", popUpMenu);

function popUpMenu() {
    hamburgerAnimationTrigger();
    menu.forEach(e => e.classList.toggle("active"));
}

function hamburgerAnimationTrigger() {
    hamburger.classList.toggle("active");
}

logOutBtn.forEach(e => e.addEventListener("click", event => logOut(event)));
async function logOut(event) {
    event.preventDefault();
    const url = "/join/log-out";
    const options = {
        method: "POST"
    };
    await fetch(url, options);
    window.location.href = "/join?l=true";
}