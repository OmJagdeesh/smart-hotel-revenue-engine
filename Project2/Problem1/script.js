document.getElementById("registrationForm").addEventListener("submit", function(e) {
    e.preventDefault();

    // Clear previous errors
    document.querySelectorAll(".error").forEach(el => el.textContent = "");

    let isValid = true;

    // Username
    const username = document.getElementById("username").value.trim();
    if (username === "") {
        document.getElementById("usernameError").textContent = "Username cannot be empty.";
        isValid = false;
    }

    // Email
    const email = document.getElementById("email").value.trim();
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z]{3,}\.[a-zA-Z]{2,3}$/;
    if (!emailPattern.test(email)) {
        document.getElementById("emailError").textContent = "Invalid email format.";
        isValid = false;
    }

    // Phone
    const phone = document.getElementById("phone").value.trim();
    const phonePattern = /^[0-9]{10}$/;
    if (!phonePattern.test(phone)) {
        document.getElementById("phoneError").textContent = "Phone number must be 10 digits.";
        isValid = false;
    }

    // Password
    const password = document.getElementById("password").value;
    const passwordPattern = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[&$#@]).{7,}$/;
    if (!passwordPattern.test(password)) {
        document.getElementById("passwordError").textContent = "Password must be at least 7 characters, include one capital letter, one digit, and one special character (&,$,#,@).";
        isValid = false;
    }

    // Confirm Password
    const confirmPassword = document.getElementById("confirmPassword").value;
    if (password !== confirmPassword) {
        document.getElementById("confirmPasswordError").textContent = "Passwords do not match.";
        isValid = false;
    }

    if (isValid) {
        alert("Registration successful!");
        document.getElementById("registrationForm").reset();
    }
});
