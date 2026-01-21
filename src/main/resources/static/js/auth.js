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
                showMessage(registerForm, result.message || 'Registration failed', 'error');
            } else {
                setTimeout(() => {
                    window.location.href = '/login';
                }, 1500);
            }
        } catch (err) {
            console.error(err);
            showMessage(registerForm, 'Error connecting to server', 'error');
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
                showMessage(loginForm, result.message || 'Login failed', 'error');
            } else {
                localStorage.setItem('user', JSON.stringify(result));
                setTimeout(() => {
                    window.location.href = '/dashboard';
                }, 1000);
            }
        } catch (err) {
            console.error(err);
            showMessage(loginForm, 'Error connecting to server', 'error');
        }
    });
}
