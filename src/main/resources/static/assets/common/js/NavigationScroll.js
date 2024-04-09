const nav = document.querySelector("nav");

const homeSection = document.querySelector("section.home");
const howToUseSection = document.querySelector("section.how-to-use");
const aboutSection = document.querySelector("section.about");
const supportedSection = document.querySelector("section.about-services");
const contactSection = document.querySelector("section.contact");

const homeButton = document.querySelector(".home-btn");
const howToUseButton = document.querySelector(".how-to-use-btn");
const aboutButton = document.querySelector(".about-btn");
const contactButton = document.querySelector(".contact-btn");

homeButton.addEventListener("click", scrollToHome);
howToUseButton.addEventListener("click", scrollToHowToUse);
aboutButton.addEventListener("click", scrollToAbout);
contactButton.addEventListener("click", scrollToContact);


let homeActive = true;
let howToUseActive = false;
let aboutActive = false;
let contactActive = false;

const navHeight = document.querySelector("nav").getBoundingClientRect().height + 20;

function scrollToHome() {
    window.scrollTo(scrollToOptionsFactory(homeSection));
}
function scrollToHowToUse() {
    window.scrollTo(scrollToOptionsFactory(howToUseSection));
}
function scrollToAbout() {
    window.scrollTo(scrollToOptionsFactory(aboutSection));
}
function scrollToContact() {
    window.scrollTo(scrollToOptionsFactory(contactSection));
}

function disableButtons() {
    if (homeActive) {
        homeActive = false;
        homeButton.classList.remove("active");
    }
    if (howToUseActive) {
        howToUseActive = false;
        howToUseButton.classList.remove("active");
    }
    if (aboutActive) {
        aboutActive = false;
        aboutButton.classList.remove("active");
    }
    if (contactActive) {
        contactActive = false;
        contactButton.classList.remove("active");
    }
}

function getHomeY() {
    return document.querySelector(".home-btn").getBoundingClientRect().top;
}


function controlNav() {
    controlNavColor();
    controlNavButtons();
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

function controlNavButtons() {
    let homeY = homeSection.getBoundingClientRect().top;
    let homeHeight = homeSection.getBoundingClientRect().height * 0.7;

    let howY = howToUseSection.getBoundingClientRect().top;
    let howHeight = howToUseSection.getBoundingClientRect().height * 0.7;

    let aboutY = aboutSection.getBoundingClientRect().top;
    let aboutHeight = (aboutSection.getBoundingClientRect().height + supportedSection.getBoundingClientRect().height) * 0.7;

    let contactY = contactSection.getBoundingClientRect().top;
    let contactHeight = contactSection.getBoundingClientRect().height * 0.7;

    if (!homeActive && homeY < 0 + navHeight + 10 && (homeY * -1) < homeHeight) {
        disableButtons();
        homeActive = true;
        homeButton.classList.add("active");
    }

    else if (!howToUseActive && howY < 0 + navHeight + 10 && (howY * -1) < howHeight) {
        disableButtons();
        howToUseActive = true;
        howToUseButton.classList.add("active");
    }

    else if (!aboutActive && aboutY < 0 + navHeight + 10 && (aboutY * -1) < aboutHeight) {
        disableButtons();
        aboutActive = true;
        aboutButton.classList.add("active");
    }

    else if (!contactActive && contactY < 0 + navHeight + 10 && (contactY * -1) < contactHeight) {
        disableButtons();
        contactActive = true;
        contactButton.classList.add("active");
    }
}

function scrollToOptionsFactory(scrollToElement) {
    return {
        top: scrollToElement.getBoundingClientRect().top + window.scrollY - navHeight,
        behavior: "smooth"
    }
}