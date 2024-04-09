const contentWrapper = document.querySelector(".content-wrapper");
const supportBtn = document.querySelector(".support");
const supportText = document.querySelector(".support .support-text");
const supportTab = document.querySelector(".support-tab");
const supportTabArrow = document.querySelector(".support-tab .arrow");

document.querySelector(".support-exit").addEventListener("click", () => supportBtn.classList.add("disable"));

supportText.addEventListener("click", () => supportTab.classList.toggle("active"));
supportTabArrow.addEventListener("click", () => supportTab.classList.remove("active"));


const copyButtons = document.querySelectorAll(".support .support-tab .support-content > .copy p");

copyButtons.forEach(e => e.addEventListener("click", event => copyValue(event, e)));

function copyValue(event, element) {
    const value = element.getAttribute("data-address");

    const textarea = document.querySelector(".copy-area");
    textarea.textContent = value;
    textarea.select();
    document.execCommand("copy");
    textarea.blur();
    displayCopiedText(event);
}


let copiedInfoIsActive = false;
function displayCopiedText(event) {
    if (copiedInfoIsActive)
        return;

    const copiedInfo = createCopiedInfoElement();
    contentWrapper.append(copiedInfo);
    copiedInfo.style.top = `${event.clientY - 40}px`;
    copiedInfo.style.left = event.clientX + "px";
    copiedInfoIsActive = true;

    setTimeout(() => {
        contentWrapper.removeChild(copiedInfo);
        copiedInfoIsActive = false;
    }, 1500)
}

function createCopiedInfoElement() {
    const element = document.createElement("div");
    element.className = "copied-info";
    element.innerHTML = `<span>copied to clipboard</span><i class="fa-regular fa-circle-check"></i>`;
    return element;
}
