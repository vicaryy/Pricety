class Item {
    constructor(id, photoSrc, title, desc, variant, price, currency, alert) {
        this.id = id;
        this.photoSrc = photoSrc;
        this.title = title;
        this.desc = desc;
        this.variant = variant;
        this.price = price;
        this.currency = currency;
        this.alert = alert;
    }
}

const notifyButtons = document.querySelectorAll(".notify");

notifyButtons.forEach(e => e.addEventListener("click", () => {
    addClass(e, "hide");
    setTimeout(() => e.remove(), 1800);
}));