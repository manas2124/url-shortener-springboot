let countdownInterval;

/**
 * Shorten URL
 */
function shortenUrl() {
    const longUrl = document.getElementById("longUrl").value;
    const result = document.getElementById("result");

    if (!longUrl) {
        result.innerText = "Please enter URL";
        return;
    }

    fetch("/api/shorten", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ longUrl })
    })
        .then(res => {
            if (!res.ok) {
                return res.text().then(t => { throw new Error(t); });
            }
            return res.json();
        })
        .then(data => {
            const expiryTime = new Date(data.expiryTime).getTime();

            // store slug so analytics can auto-refresh later
            localStorage.setItem("lastSlug", data.slug);

            result.innerHTML = `
            <p><b>Short URL:</b></p>
            <a href="/r/${data.slug}" target="_blank">
                ${data.shortUrl}
            </a>

            <p id="analyticsBox">📊 Access Count: <b>0</b></p>

            <p id="countdown"></p>
        `;

            startCountdown(expiryTime);
            loadAnalytics(data.slug);
        })
        .catch(err => {
            result.innerHTML = `<span style="color:red;">❌ ${err.message}</span>`;
        });
}

/**
 * Load analytics and show inside UI
 */
function loadAnalytics(slug) {
    const analyticsBox = document.getElementById("analyticsBox");
    if (!analyticsBox) return;

    analyticsBox.innerHTML = "⏳ Loading analytics...";

    fetch(`/api/analytics/${slug}`)
        .then(res => {
            if (!res.ok) throw new Error();
            return res.json();
        })
        .then(data => {
            analyticsBox.innerHTML =
                `📊 Access Count: <b>${data.accessCount}</b>`;
        })
        .catch(() => {
            analyticsBox.innerHTML =
                `<span style="color:red;">📊 Analytics not available</span>`;
        });
}

/**
 * Countdown timer
 */
function startCountdown(expiryTime) {
    const countdownEl = document.getElementById("countdown");

    if (countdownInterval) {
        clearInterval(countdownInterval);
    }

    countdownInterval = setInterval(() => {
        const diff = expiryTime - Date.now();

        if (diff <= 0) {
            clearInterval(countdownInterval);
            countdownEl.innerHTML = "❌ Link Expired";
            return;
        }

        countdownEl.innerHTML =
            `⏳ Expires in: <b>${Math.floor(diff / 1000)}s</b>`;
    }, 1000);
}

/**
 * Auto-refresh analytics when user comes back to page
 * (after clicking short URL in new tab)
 */
document.addEventListener("visibilitychange", () => {
    if (document.visibilityState === "visible") {
        const slug = localStorage.getItem("lastSlug");
        if (slug) {
            loadAnalytics(slug);
        }
    }
});

function clearInput() {
    // clear input field
    document.getElementById("longUrl").value = "";

    // clear result area
    document.getElementById("result").innerHTML = "";

    // clear countdown timer if running
    if (countdownInterval) {
        clearInterval(countdownInterval);
        countdownInterval = null;
    }

    // optional: clear last slug
    localStorage.removeItem("lastSlug");
}

