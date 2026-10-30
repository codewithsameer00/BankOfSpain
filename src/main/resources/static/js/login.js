document.addEventListener("DOMContentLoaded", function () {
    const passwordInput = document.getElementById("password");
    const toggleButton = document.getElementById("togglePassword");

    if (!passwordInput || !toggleButton) {
        return;
    }

    toggleButton.addEventListener("click", function () {
        const isHidden = passwordInput.type === "password";

        passwordInput.type = isHidden ? "text" : "password";
        toggleButton.textContent = isHidden ? "🙈" : "👁";
        toggleButton.setAttribute(
            "aria-label",
            isHidden ? "Hide password" : "Show password"
        );

        passwordInput.focus();
    });
});
