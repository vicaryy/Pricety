const itemEditInputs = {
    id: document.querySelector(".edit-block .item-id-input"),
    photo: document.querySelector(".edit-block img"),
    title: document.querySelector(".edit-block .item-title input"),
    titleValidator: document.querySelector(".edit-block .item-title .mark-wrapper"),
    description: document.querySelector(".edit-block .description input"),
    descriptionValidator: document.querySelector(".edit-block .description .mark-wrapper"),
    variant: document.querySelector(".edit-block .variant input"),
    price: document.querySelector(".edit-block .price input"),
    alert: document.querySelector(".edit-block .alert input"),
    alertValidator: document.querySelector(".edit-block .alert .mark-wrapper")
};

const itemEditButtons = {
    alertOptionButtonAuto: document.querySelector(".alert-options .alert-auto"),
    alertOptionButtonOff: document.querySelector(".alert-options .alert-off"),
    saveButton: document.querySelector(".buttons .save"),
    deleteButton: document.querySelector(".buttons .delete"),
    cancelButton: document.querySelector(".edit-block .cancel")
};

const itemEditBlockCover = document.querySelector(".edit-block-cover");
const itemEditBlock = document.querySelector(".edit-block");
const itemEditButtonsOut = document.querySelectorAll(".item .edit");


itemEditBlockCover.addEventListener("click", closeItemEditBlock);
itemEditButtons.cancelButton.addEventListener("click", closeItemEditBlock);
itemEditInputs.alert.addEventListener("input", () => {
    controlAlertInput();
    controlSaveButton();
});
itemEditInputs.alert.addEventListener("blur", controlAlertInputBlur);
itemEditButtonsOut.forEach(e => e.addEventListener("click", displayEditItemBlock));

itemEditButtons.saveButton.addEventListener("click", () => {
    if (!itemEditButtons.saveButton.classList.contains("active"))
        return;
    const itemEdited = getItemFromEditBlockElement();
    const itemToSaveElement = document.querySelector(`[data-entity-id="${itemEdited.id}"].item`);

    insertEditBlockItemToItemElement(itemEdited, itemToSaveElement);
    closeItemEditBlock();
    displaySuccessPopUp();
});

itemEditButtons.deleteButton.addEventListener("click", () => {
    const itemEdited = getItemFromEditBlockElement();
    const itemToDeleteElement = document.querySelector(`[data-entity-id="${itemEdited.id}"].item`);
    closeItemEditBlock();
    displaySuccessPopUp();

    itemToDeleteElement.classList.add("removed");

    setTimeout(() => itemToDeleteElement.remove(), 1500);
});

itemEditButtons.alertOptionButtonAuto.addEventListener("click", () => {
    addActiveClass(itemEditButtons.alertOptionButtonAuto);
    removeActiveClass(itemEditButtons.alertOptionButtonOff);
    itemEditInputs.alert.value = "AUTO";
    itemEditInputs.alertValidator.classList.remove("active");
    controlSaveButton();
});

itemEditButtons.alertOptionButtonOff.addEventListener("click", () => {
    addActiveClass(itemEditButtons.alertOptionButtonOff);
    removeActiveClass(itemEditButtons.alertOptionButtonAuto);
    itemEditInputs.alert.value = "OFF";
    itemEditInputs.alertValidator.classList.remove("active");
    controlSaveButton();
});

itemEditInputs.title.addEventListener("input", () => {
    const value = itemEditInputs.title.value;
    if (value.length == 0 || value.length > 25)
        itemEditInputs.titleValidator.classList.add("active");
    else
        itemEditInputs.titleValidator.classList.remove("active");
    controlSaveButton();
});
itemEditInputs.description.addEventListener("input", () => {
    const value = itemEditInputs.description.value;
    if (value.length == 0 || value.length > 100)
        itemEditInputs.descriptionValidator.classList.add("active");
    else
        itemEditInputs.descriptionValidator.classList.remove("active");
    controlSaveButton();
});


function controlSaveButton() {
    if (!itemEditInputs.titleValidator.classList.contains("active")
        && !itemEditInputs.descriptionValidator.classList.contains("active")
        && !itemEditInputs.alertValidator.classList.contains("active"))
        itemEditButtons.saveButton.classList.add("active");

    else
        itemEditButtons.saveButton.classList.remove("active");
}

function controlAlertInput() {
    let value = itemEditInputs.alert.value;
    if (value.length === 0) {
        itemEditInputs.alertValidator.classList.add("active");
        return;
    }

    if (value.includes(",")) {
        value = value.replace(",", ".");
        itemEditInputs.alert.value = value;
    }

    if (value.includes(" ")) {
        value = value.split(" ")[0];
        itemEditInputs.alert.value = value;
    }

    if (isNaN(value)) {
        value = value.toUpperCase();
        if (value === "AUTO") {
            itemEditButtons.alertOptionButtonAuto.click();
            return;
        }
        else if (value === "OFF") {
            itemEditButtons.alertOptionButtonOff.click();
            return;
        }
        itemEditInputs.alertValidator.classList.add("active");
        removeActiveClass(itemEditButtons.alertOptionButtonOff);
        removeActiveClass(itemEditButtons.alertOptionButtonAuto);
        return;
    }
    removeActiveClass(itemEditButtons.alertOptionButtonOff);
    removeActiveClass(itemEditButtons.alertOptionButtonAuto);

    value = parseFloat(value).toFixed(2);
    let currentPrice = itemEditInputs.price.value;
    if (currentPrice === "SOLD OUT") {
        if (value > 0 && value < 100000) {
            itemEditInputs.alertValidator.classList.remove("active");
            return;
        }
        itemEditInputs.alertValidator.classList.add("active");
        return;
    }

    currentPrice = +currentPrice.replace(",", ".");
    if (value > 0 && value < 100000 && currentPrice > value) {
        itemEditInputs.alertValidator.classList.remove("active");
        return;
    }

    itemEditInputs.alertValidator.classList.add("active");
}


function controlAlertInputBlur() {
    let value = itemEditInputs.alert.value;
    if (value.length === 0)
        return;
    if (!isNaN(value))
        itemEditInputs.alert.value = parseFloat(value).toFixed(2);
}


function insertEditBlockItemToItemElement(item, element) {
    element.querySelector(".item-title").textContent = item.title;
    element.querySelector(".description").textContent = item.desc;
    element.querySelector(".alert span").textContent = (item.alert === "AUTO" || item.alert === "OFF") ? item.alert : formatPriceToString(item.alert) + " " + element.querySelector(".currency").textContent;
    if (item.alert === "AUTO")
        addActiveClass(element.querySelector(".tooltip"));
    else
        removeActiveClass(element.querySelector(".tooltip"));

}

function insertItemIntoEditBlock(item) {
    itemEditInputs.id.value = item.id;
    itemEditInputs.photo.src = item.photoSrc;
    itemEditInputs.title.value = item.title;
    itemEditInputs.description.value = item.desc;
    itemEditInputs.variant.value = item.variant;
    itemEditInputs.price.value = item.price;
    itemEditInputs.alert.value = item.alert;
}
function getItemFromEditBlockElement() {
    return new Item(
        itemEditBlock.dataset.entityId,
        itemEditBlock.querySelector("img").getAttribute("src"),
        itemEditInputs.title.value,
        itemEditInputs.description.value,
        itemEditInputs.variant.value,
        itemEditInputs.price.value,
        null,
        itemEditInputs.alert.value);
}
function getItemFromItemElement(element) {
    let price = element.querySelector(".price span:first-of-type").textContent;
    if (price !== "SOLD OUT")
        price = parseFloat(price.split(" ")[0].replace(",", ".")).toFixed(2);
    return new Item(
        element.dataset.entityId,
        element.querySelector("img").getAttribute("src"),
        element.querySelector(".item-title").textContent,
        element.querySelector(".description").textContent,
        element.getAttribute("data-variant"),
        price,
        element.querySelector(".currency").textContent,
        element.querySelector(".alert span").textContent);
}

function formatPriceToString(price) {
    return price.replace(".", ",");
}

function displaySuccessPopUp() {
    const success = document.querySelector(".pop-up-success");
    success.classList.add("active");

    setTimeout(() => success.classList.remove("active"), 1500);
}


function displayEditItemBlock() {
    addActiveClass(itemEditBlock);
    addActiveClass(itemEditBlockCover);
    const itemElement = this.parentNode;

    const item = getItemFromItemElement(itemElement);

    itemEditBlock.dataset.entityId = item.id;
    insertItemIntoEditBlock(item);
    controlAlertInput();
}

function closeItemEditBlock() {
    removeActiveClass(itemEditBlockCover);
    removeActiveClass(itemEditBlock);
}



