function showMessage(form, text, type = 'error') {
    let msgDiv = form.querySelector('.message');
    if (!msgDiv) {
        msgDiv = document.createElement('div');
        msgDiv.classList.add('message');
        form.prepend(msgDiv);
    }
    msgDiv.textContent = text;
    msgDiv.className = 'message ' + type;
}

/* =========================
   REGISTER
========================= */
const registerForm = document.getElementById('register-form');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(registerForm).entries());
        data.role = "teller"; // default role

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (!response.ok) {
                showMessage(registerForm, result.message || 'Registration failed');
            } else {
                showMessage(registerForm, 'Registration successful!', 'success');
                setTimeout(() => {
                    window.location.href = 'login.html';
                }, 1500);
            }
        } catch (err) {
            showMessage(registerForm, 'Server error');
        }
    });
}

/* =========================
   LOGIN
========================= */
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(loginForm).entries());

        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            });

            const result = await response.json();

            if (!response.ok) {
                showMessage(loginForm, result.message || 'Login failed');
            } else {
                // 🔑 RUAN SAKTË TOKEN + ROLE
                localStorage.setItem("token", result.token);
                localStorage.setItem("role", result.role);
                localStorage.setItem("user", JSON.stringify(result));

                window.location.href = 'dashboard';
            }
        } catch (err) {
            showMessage(loginForm, 'Server error');
        }
    });
}

/* =========================
   AUTH HELPERS (GLOBAL)
========================= */
function getToken() {
    return localStorage.getItem("token");
}

function getRole() {
    return localStorage.getItem("role");
}

function logout() {
    localStorage.clear();
    window.location.href = 'login';
}
