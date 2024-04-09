const logInBtn = document.querySelectorAll(".switch-to-log-in");
const signUpBtn = document.querySelector(".switch-to-sign-up");
const forgotPassBtn = document.querySelector(".switch-to-forgot-pass");
const logInWrapper = document.querySelector(".log-in-wrapper");
const signUpWrapper = document.querySelector(".sign-up-wrapper");
const forgotPassWrapper = document.querySelector(".forgot-pass-wrapper");

logInBtn.forEach(e => e.addEventListener("click", switchToLogIn));
signUpBtn.addEventListener("click", switchToSignUp);
forgotPassBtn.addEventListener("click", switchToForgotPass);

function switchToLogIn() {
    forgotPassWrapper.classList.remove("active");
    signUpWrapper.classList.remove("active");
    logInWrapper.classList.add("active");
}

function switchToSignUp() {
    forgotPassWrapper.classList.remove("active");
    logInWrapper.classList.remove("active");
    signUpWrapper.classList.add("active");
}

function switchToForgotPass() {
    logInWrapper.classList.remove("active");
    signUpWrapper.classList.remove("active");
    forgotPassWrapper.classList.add("active");
}