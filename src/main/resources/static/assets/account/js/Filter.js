function addActiveClass(element, listOfElements) {
    if (element.classList.contains("active"))
        return;

    if (listOfElements && listOfElements instanceof NodeList) {
        listOfElements.forEach(e => {
            if (e.classList.contains("active"))
                e.classList.remove("active");
        });
    }
    element.classList.add("active");
};

function addClass(element, className, listOfElements) {
    if (element.classList.contains(className))
        return;

    if (listOfElements && listOfElements instanceof NodeList) {
        listOfElements.forEach(e => {
            if (e.classList.contains(className))
                e.classList.remove(className);
        });
    }
    element.classList.add(className);
};

function removeActiveClass(element) {
    if (!element.classList.contains("active"))
        return;
    element.classList.remove("active");
}

function removeClass(element, className) {
    if (!element.classList.contains(className))
        return;
    element.classList.remove(className);
}

function addActiveClassToParent() {
    this.parentNode.classList.add("active");
}
function removeActiveClassFromParent() {
    if (this.value.length != 0)
        return;
    this.parentNode.classList.remove("active");
}


const searchInput = document.querySelector(".search");
searchInput.addEventListener("focus", addActiveClassToParent);
searchInput.addEventListener("blur", removeActiveClassFromParent);
searchInput.addEventListener("input", controlSearchInput);

function controlSearchInput() {
    console.log("Input: " + this.value);
    serviceButtons[0].click();
    sortBySearchInput(this.value, getAllItems());
}

function sortBySearchInput(input, items) {

    items.forEach(e => {
        const title = e.querySelector(".item-title").textContent.toLowerCase();
        const desc = e.querySelector(".description").textContent.toLowerCase();
        if (title.includes(input.toLowerCase()) || desc.includes(input.toLowerCase()))
            return;
        addClass(e, "hidden");
    });
    checkNonHiddenItemsAmount(getNonHiddenItems());
}

const serviceButtons = document.querySelectorAll(".services ul li");
serviceButtons.forEach(e => e.addEventListener("click", () => {
    addActiveClass(e, serviceButtons)
    filterByService(e.textContent.toLowerCase());
}));


const sortByOptionButtons = document.querySelectorAll(".sort-bar-slide ul li");
sortByOptionButtons.forEach(e => e.addEventListener("click", () => {
    if (e.classList.contains("active"))
        return;
    addActiveClass(e, sortByOptionButtons);
    filterByFilterOptions(e.textContent.toLowerCase());
}));

function filterByFilterOptions(option) {
    const allItems = getAllItems();
    if (allItems.length < 2)
        return;
    if (option === "newest")
        filterByNewest(allItems);
    else if (option === "oldest")
        filterByOldest(allItems);
    else if (option === "higher price")
        filterByHigherPrice(allItems);
    else if (option === "lowest price")
        filterByLowestPrice(allItems);
}

function filterByNewest(items) {
    const parent = items[0].parentNode;
    [...items]
        .sort((a, b) => parseInt(b.getAttribute("data-entity-id")) - parseInt(a.getAttribute("data-entity-id")))
        .forEach(e => parent.append(e));
}
function filterByOldest(items) {
    const parent = items[0].parentNode;
    [...items]
        .sort((a, b) => parseInt(a.getAttribute("data-entity-id")) - parseInt(b.getAttribute("data-entity-id")))
        .forEach(e => parent.append(e));
}
function filterByHigherPrice(items) {
    const parent = items[0].parentNode;
    [...items]
        .sort((a, b) => {
            const valueA = a.querySelector(".price").textContent.toLowerCase();
            const valueB = b.querySelector(".price").textContent.toLowerCase();
            if (valueA === "sold out")
                return 1;
            if (valueB === "sold out")
                return -1;
            return parseFloat(valueB.replace(",", ".")) - parseFloat(valueA.replace(",", "."));
        })
        .forEach(e => parent.append(e));
}
function filterByLowestPrice(items) {
    const parent = items[0].parentNode;
    [...items]
        .sort((a, b) => {
            const valueA = a.querySelector(".price").textContent.toLowerCase();
            const valueB = b.querySelector(".price").textContent.toLowerCase();
            if (valueA === "sold out")
                return 1;
            if (valueB === "sold out")
                return -1;
            return parseFloat(valueA.replace(",", ".")) - parseFloat(valueB.replace(",", "."));
        })
        .forEach(e => parent.append(e));
}

function filterByService(serviceName) {
    getAllItems().forEach(e => {
        if (serviceName === "all")
            removeClass(e, "hidden");
        else if (e.getAttribute("data-service") !== serviceName)
            addClass(e, "hidden");
        else
            removeClass(e, "hidden");
    });

    checkNonHiddenItemsAmount(getNonHiddenItems());
}

function checkAllItemsAmount(items) {
    if (items.length === 0)
        addClass(itemsElement, "empty");
    else
        removeClass(itemsElement, "empty");
}
function checkNonHiddenItemsAmount(items) {
    if (items.length === 0)
        addClass(itemsElement, "empty");
    else
        removeClass(itemsElement, "empty");
}

function getAllItems() {
    return document.querySelectorAll(".items .item");
}
function getNonHiddenItems() {
    return document.querySelectorAll(".items .item:not(.hidden)");
}

const itemsElement = document.querySelector(".items");
const sortByButton = document.querySelector(".sort-by i");
const sortBySlideTab = document.querySelector(".sort-bar-slide");
const sortBySlideTabArrow = document.querySelector(".sort-bar-slide .arrow");
const sortBySlideTabCover = document.querySelector(".sort-by .cover");
sortByButton.addEventListener("click", () => {
    sortBySlideTab.classList.add("active");
    sortBySlideTabCover.classList.add("active");
    sortByButton.classList.add("active");
});
sortBySlideTabArrow.addEventListener("click", () => {
    sortBySlideTab.classList.remove("active");
    sortBySlideTabCover.classList.remove("active");
    sortByButton.classList.remove("active");
})
sortBySlideTabCover.addEventListener("click", () => {
    sortBySlideTab.classList.remove("active");
    sortBySlideTabCover.classList.remove("active");
    sortByButton.classList.remove("active");
});

checkAllItemsAmount(getAllItems());
filterByFilterOptions("newest");
