const forgotPassElements = {
    inputFields: document.querySelectorAll(".forgot-pass-form .input-wrapper input"),
    emailInput: document.querySelector(".forgot-pass-form .input-wrapper .input-email"),
    forgotPassButton: document.querySelector(".forgot-pass-form .forgot-pass-btn")
};

forgotPassElements.inputFields.forEach(input => input.addEventListener("focus", addActiveClass));
forgotPassElements.inputFields.forEach(input => input.addEventListener("blur", removeActiveClass));

function addActiveClass() {
    this.parentNode.classList.add("active");
}
function removeActiveClass() {
    if (this.value.length != 0)
        return;
    this.parentNode.classList.remove("active");
}


forgotPassElements.forgotPassButton.addEventListener("click", logIn);

function logIn() {
    console.log(`email: ${forgotPassElements.emailInput.value}`);
}