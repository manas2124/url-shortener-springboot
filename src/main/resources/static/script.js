function shortenUrl() {
    const longUrl = document.getElementById("longUrl").value;
    const resultDiv = document.getElementById("result");

    if (!longUrl) {
        resultDiv.innerHTML = "❌ Please enter a URL";
        return;
    }

    fetch("/api/shorten", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ longUrl: longUrl })
    })
    .then(response => response.json())
    .then(data => {
        const shortUrl = data.shortUrl;

        resultDiv.innerHTML = `
            Short URL:<br>
            <a href="${shortUrl}" target="_blank">${shortUrl}</a>
            <p>⏳ Link expires in 1 minute</p>
        `;
    })
    .catch(error => {
        resultDiv.innerHTML = " Error shortening URL";
        console.error(error);
    });
}
