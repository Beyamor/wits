document.addEventListener("DOMContentLoaded", () => {
    let fullpage = document.getElementById("fullpage-image");
    fullpage.addEventListener("click", () => {
        fullpage.style.display = "none";
    });

    document.querySelectorAll("#blog img").forEach(img => {
        img.addEventListener("click", () => {
            fullpage.style.backgroundImage = `url(${img.src})`;
            fullpage.style.display = "block";
        });
    });
});