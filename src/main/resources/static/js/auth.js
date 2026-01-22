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

const registerForm = document.getElementById('register-form');
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const data = Object.fromEntries(new FormData(registerForm).entries());
        data.role = "teller";

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
                let role = result.role;
                if (role && !role.startsWith('ROLE_')) {
                    role = 'ROLE_' + role.toUpperCase();
                }
                localStorage.setItem("token", result.token);
                localStorage.setItem("userRole", role);
                localStorage.setItem("userEmail", data.email);
                localStorage.setItem("userId", result.id || result.userId || '1');
                localStorage.setItem("user", JSON.stringify(result));


                setTimeout(() => {
                    window.location.href = 'dashboard';
                }, 500);
            }
        } catch (err) {
            console.error('Login error:', err);
            showMessage(loginForm, 'Server error');
        }
    });
}
function getToken() {
    return localStorage.getItem("token");
}

function getRole() {
    return localStorage.getItem("userRole");
}

function logout() {
    localStorage.clear();
    window.location.href = 'login';
}
