const logInElements = {
    inputFields: document.querySelectorAll(".log-in-form .input-wrapper input"),
    passwordEye: document.querySelector(".log-in-form .input-wrapper .fa-eye"),
    passwordInput: document.querySelector(".log-in-form .input-wrapper .input-password"),
    emailInput: document.querySelector(".log-in-form .input-wrapper .input-email"),
    logInButton: document.querySelector(".log-in-form .log-in-btn"),
};

logInElements.inputFields.forEach(input => input.addEventListener("focus", addActiveClass));
logInElements.inputFields.forEach(input => input.addEventListener("blur", removeActiveClass));

function addActiveClass() {
    this.parentNode.classList.add("active");
}
function removeActiveClass() {
    if (this.value.length != 0)
        return;
    this.parentNode.classList.remove("active");
}


logInElements.passwordEye.addEventListener("click", displayEyeSlashed);

function displayEyeSlashed() {
    if (!logInElements.passwordEye.classList.contains("active")) {
        logInElements.passwordEye.classList.add("active");
        logInElements.passwordInput.type = "text";
    }
    else {
        logInElements.passwordEye.classList.remove("active");
        logInElements.passwordInput.type = "password";
    }
}

logInElements.logInButton.addEventListener("click", logIn);

function logIn() {
        console.log(`email: ${logInElements.emailInput.value}
        password: ${logInElements.passwordInput.value}`);
}



