const menuButtons = document.querySelectorAll(".menu-bar ul li span");
const mainContents = document.querySelectorAll(".content-bar-wrapper .content")

menuButtons.forEach(e => e.addEventListener("click", addActiveClassToMenuButtons));

function addActiveClassToMenuButtons() {
        addActiveClass(this, menuButtons);
        const option = this.textContent.trim().toLowerCase();
        if (option === "products")
                addActiveClass(mainContents[0], mainContents);
        else if (option === "profile")
                addActiveClass(mainContents[1], mainContents);
        else if (option === "settings")
                addActiveClass(mainContents[2], mainContents);

}
