const signUpElements = {
    inputFields: document.querySelectorAll(".sign-up-form .input-wrapper input"),
    passwordEye: document.querySelector(".sign-up-form .input-wrapper .fa-eye"),
    passwordInput: document.querySelector(".sign-up-form .input-wrapper .input-password"),
    emailInput: document.querySelector(".sign-up-form .input-wrapper .input-email"),
    createAccountButton: document.querySelector(".sign-up-form .create-btn"),
    validEmail: document.querySelector(".valid-email"),
    minLetters: document.querySelector(".min-letters"),
    specialChar: document.querySelector(".special-char")
};

signUpElements.inputFields.forEach(input => input.addEventListener("focus", addActiveClass));
signUpElements.inputFields.forEach(input => input.addEventListener("blur", removeActiveClass));

function addActiveClass() {
    this.parentNode.classList.add("active");
}
function removeActiveClass() {
    if (this.value.length != 0)
        return;
    this.parentNode.classList.remove("active");
}


signUpElements.passwordEye.addEventListener("click", displayEyeSlashed);

function displayEyeSlashed() {
    if (!signUpElements.passwordEye.classList.contains("active")) {
        signUpElements.passwordEye.classList.add("active");
        signUpElements.passwordInput.type = "text";
    }
    else {
        signUpElements.passwordEye.classList.remove("active");
        signUpElements.passwordInput.type = "password";
    }
}


const specialCharacters = [
    "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "=", "+",
    "[", "{", "]", "}", ";", ":", "'", "\"", ",", "<", ".", ">", "/", "?", "\\",
    "|", "`", "~"
];
signUpElements.passwordInput.addEventListener("input", e => checkPasswordInput(e));
signUpElements.emailInput.addEventListener("input", e => checkEmailInput(e));


const signUpValidators = {
    minLetter: false,
    specialChar: false,
    validEmail: false,
    btnActive: false,

    areInputsValid() {
        return this.minLetter && this.specialChar && this.validEmail;
    }
};

function checkPasswordInput() {
    const password = signUpElements.passwordInput.value;
    checkPasswordMinLetters(password);
    checkPasswordSpecialCharacter(password);
    checkButton();
}

function checkEmailInput() {
    const email = signUpElements.emailInput.value;
    checkEmailValidation(email);
    checkButton();
}


function checkPasswordMinLetters(password) {
    if (password.length > 7 && password.length < 24) {
        signUpValidators.minLetter = true;
        signUpElements.minLetters.classList.add("active");
    }
    else {
        signUpValidators.minLetter = false;
        signUpElements.minLetters.classList.remove("active");
    }
}

function checkPasswordSpecialCharacter(password) {
    for (let i = 0; i < specialCharacters.length; i++) {
        if (password.includes(specialCharacters[i])) {
            signUpValidators.specialChar = true;
            signUpElements.specialChar.classList.add("active");
            return;
        }
    }

    if (signUpValidators.specialChar) {
        signUpValidators.specialChar = false;
        signUpElements.specialChar.classList.remove("active");
    }
}

function checkEmailValidation(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailRegex.test(email)) {
        signUpValidators.validEmail = true;
        signUpElements.validEmail.classList.add("active");
        return;
    }

    if (signUpValidators.validEmail) {
        signUpValidators.validEmail = false;
        signUpElements.validEmail.classList.remove("active");
    }
}

function checkButton() {
    if (signUpValidators.areInputsValid()) {
        signUpValidators.btnActive = true;
        signUpElements.createAccountButton.classList.add("active");
        signUpElements.createAccountButton.disabled = false;
        return;
    }

    if (signUpValidators.btnActive) {
        signUpValidators.btnActive = false;
        signUpElements.createAccountButton.classList.remove("active");
        signUpElements.createAccountButton.disabled = true;
    }
}


signUpElements.createAccountButton.addEventListener("click", createAccount);

function createAccount() {
    if (signUpValidators.areInputsValid()) {
        console.log(`email: ${signUpElements.emailInput.value}
        password: ${signUpElements.passwordInput.value}`);
    }
}



