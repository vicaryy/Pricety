const emailToggle = document.querySelector(".notification .toggle");
const languageCaret = document.querySelector(".language .caret");
const languageSlider = document.querySelector(".language .slider");
const languageOptions = document.querySelectorAll(".language .slider div > div");
const languageTab = document.querySelector(".language");
const languageCover = document.querySelector(".settings .cover");
const getFromTelegram = document.querySelector(".get-telegram");
const sendToTelegram = document.querySelector(".send-telegram");

getFromTelegram.addEventListener("click", () => {
    if (getFromTelegram.classList.contains("active"))
        return;
    addActiveClass(getFromTelegram.querySelector(".loading"));
});

sendToTelegram.addEventListener("click", () => {
    if (sendToTelegram.classList.contains("active"))
        return;
    addActiveClass(sendToTelegram.querySelector(".loading"));
});

languageCaret.addEventListener("click", () => {
    addActiveClass(languageTab);
})
languageCover.addEventListener("click", () => {
    removeActiveClass(languageTab);
});

languageOptions.forEach(e => e.addEventListener("click", () => addActiveClass(e, languageOptions)));
emailToggle.addEventListener("click", switchEmailNotification);

function switchEmailNotification() {
    if (this.classList.contains("active"))
        fetch("/account/emailNotifications?n=false", {
            method: 'PATCH'
        });
    else
        fetch("/account/emailNotifications?n=true", {
            method: 'PATCH'
        });
    this.classList.toggle("active");
}
