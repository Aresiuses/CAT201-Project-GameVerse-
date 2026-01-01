document.addEventListener("DOMContentLoaded", () => {
    fetch("wishlist")
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load wishlist");
            }
            return response.json();
        })
        .then(games => renderWishlist(games))
        .catch(err => {
            console.error(err);
            document.getElementById("empty-msg").classList.remove("hidden");
        });
});

function renderWishlist(games) {
    const container = document.getElementById("wishlist-container");
    const emptyMsg = document.getElementById("empty-msg");

    if (!games || games.length === 0) {
        emptyMsg.classList.remove("hidden");
        return;
    }

    games.forEach(game => {
        const card = document.createElement("div");
        card.className = "card p-5 rounded-xl shadow-lg";

        card.innerHTML = `
            <h2 class="text-xl font-bold text-white mb-2">
                ${game.title}
            </h2>

            <p class="text-sm text-gray-400 mb-1">
                Platform: ${game.platform}
            </p>

            <p class="text-sm text-gray-400 mb-3">
                Genre: ${game.genre}
            </p>

            <p class="text-red-400 font-semibold">
                $${game.price}
            </p>
        `;

        container.appendChild(card);
    });
}
