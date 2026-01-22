const API_BASE_URL = 'http://localhost:8080/api';
const userEmail = localStorage.getItem('userEmail') || 'admin@example.com';
const userPassword = localStorage.getItem('userPassword') || 'admin123';
const userRole = localStorage.getItem('userRole') || 'ROLE_ADMIN';
const userId = localStorage.getItem('userId') || '1';
const authHeader = 'Basic ' + btoa(userEmail + ':' + userPassword);

document.getElementById('sidebarRole').textContent = userRole.replace('ROLE_', '');
function showSection(sectionId) {
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    event.target.closest('.nav-item').classList.add('active');

    if (sectionId === 'dashboard') loadDashboard();
    if (sectionId === 'clients') loadClients();
    if (sectionId === 'accounts') loadAccounts();
    if (sectionId === 'loans') loadLoans();
    if (sectionId === 'transactions') loadTransactions();
    if (sectionId === 'users') loadUsers();
}

function logout() {
    localStorage.clear();
    window.location.href = 'login';
}
async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    };
    if (body) options.body = JSON.stringify(body);
    const response = await fetch(API_BASE_URL + endpoint, options);
    if (!response.ok) throw new Error('API call failed');
    return method === 'DELETE' ? null : await response.json();
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amount);
}
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString('en-US');
}

function createModal(title, content) {
    const modal = document.createElement('div');
    modal.className = 'modal show';
    modal.innerHTML = `
        <div class="modal-content">
            <div class="modal-header">
                <h2>${title}</h2>
                <span class="close-modal" onclick="closeModal()">×</span>
            </div>
            ${content}
        </div>
    `;
    return modal;
}
function closeModal() {
    document.getElementById('modalContainer').innerHTML = '';
}
function showAlert(message, type = 'success') {
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = message;
    document.querySelector('.main-content').prepend(alert);
    setTimeout(() => alert.remove(), 3000);
}

async function loadDashboard() {
    try {
        const data = await apiCall('/dashboard');
        const statsGrid = document.getElementById('statsGrid');
        statsGrid.innerHTML = `
            <div class="stat-card clients"><h3>Total Clients</h3><div class="stat-value">${data.totalClients || 0}</div></div>
            <div class="stat-card accounts"><h3>Total Accounts</h3><div class="stat-value">${data.totalAccounts || 0}</div></div>
            <div class="stat-card loans"><h3>Active Loans</h3><div class="stat-value">${data.totalLoans || 0}</div></div>
            <div class="stat-card transactions"><h3>Today's Transactions</h3><div class="stat-value">${data.totalTransactionsToday || 0}</div></div>
        `;
        const txList = document.getElementById('recentTransactions');
        if (data.recentTransactions && data.recentTransactions.length > 0) {
            txList.innerHTML = '<h2 style="margin-bottom: 20px;">Recent Transactions</h2>' +
                data.recentTransactions.map(tx => `
                    <div class="transaction-card">
                        <div class="transaction-info">
                            <h4>${tx.clientName}</h4>
                            <p>${tx.transactionType} - ${formatDate(tx.transactionDate)}</p>
                        </div>
                        <div class="transaction-amount ${tx.transactionType === 'DEPOSIT' ? 'positive' : 'negative'}">
                            ${tx.transactionType === 'DEPOSIT' ? '+' : '-'}${formatCurrency(tx.amount)}
                        </div>
                    </div>
                `).join('');
        }
    } catch (e) {
        console.error(e);
    }
}

async function loadClients() {
    try {
        const clients = await apiCall('/clients');
        const tbody = document.getElementById('clientsTableBody');
        tbody.innerHTML = clients.map(c => `
            <tr>
                <td>${c.clientId}</td>
                <td>${c.fullName}</td>
                <td>${c.email || 'N/A'}</td>
                <td>${c.phone || 'N/A'}</td>
                <td>${formatDate(c.createdAt)}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="openClientModal(${c.clientId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteClient(${c.clientId})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}
function openClientModal(clientId = null) {
    const modal = createModal('Client Form', `
        <form onsubmit="saveClient(event, ${clientId})">
            <div class="form-group"><label>Full Name *</label><input type="text" id="clientFullName" required></div>
            <div class="form-group"><label>Email</label><input type="email" id="clientEmail"></div>
            <div class="form-group"><label>Phone</label><input type="tel" id="clientPhone"></div>
            <div class="form-group"><label>Address</label><textarea id="clientAddress" rows="3"></textarea></div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Client</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>
    `);
    document.getElementById('modalContainer').appendChild(modal);
}
async function saveClient(event, clientId) {
    event.preventDefault();
    const data = {
        fullName: document.getElementById('clientFullName').value,
        email: document.getElementById('clientEmail').value,
        phone: document.getElementById('clientPhone').value,
        address: document.getElementById('clientAddress').value
    };
    try {
        if (clientId) await apiCall(`/clients/${clientId}`, 'PUT', data);
        else await apiCall('/clients', 'POST', data);
        closeModal();
        loadClients();
        showAlert('Client saved successfully!');
    } catch { showAlert('Error saving client', 'error'); }
}
async function deleteClient(id) {
    if (confirm('Delete this client?')) {
        try {
            await apiCall(`/clients/${id}`, 'DELETE');
            loadClients();
            showAlert('Client deleted successfully!');
        } catch { showAlert('Error deleting client', 'error'); }
    }
}
async function loadAccounts() {
    try {
        const accounts = await apiCall('/accounts');
        const tbody = document.getElementById('accountsTableBody');
        tbody.innerHTML = accounts.map(a => `
            <tr>
                <td>${a.accountId}</td
                <td>${a.clientName}</td>
                <td>${a.accountType}</td>
                <td>${formatCurrency(a.balance)}</td>
                <td>${a.status}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="editAccount(${a.accountId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteAccount(${a.accountId})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}

async function deleteAccount(id) {
    if (confirm('Delete this account?')) {
        try {
            await apiCall(`/accounts/${id}`, 'DELETE');
            loadAccounts();
            showAlert('Account deleted successfully!', 'success');
        } catch { showAlert('Error deleting account', 'error'); }
    }
}
async function loadLoans() {
    try {
        const loans = await apiCall('/loans');
        const tbody = document.getElementById('loansTableBody');
        tbody.innerHTML = loans.map(l => `
            <tr>
                <td>${l.loanId}</td>
                <td>${l.clientName}</td>
                <td>${formatCurrency(l.amount)}</td>
                <td>${l.interestRate}%</td>
                <td>${formatDate(l.startDate)}</td>
                <td>${l.status}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="openLoanModal(${l.loanId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteLoan(${l.loanId})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}

function openLoanModal(loanId = null) {
    const modal = createModal('Loan Form', `
        <form onsubmit="saveLoan(event, ${loanId})">
            <div class="form-group"><label>Client ID *</label><input type="number" id="loanClientId" required></div>
            <div class="form-group"><label>Amount *</label><input type="number" step="0.01" id="loanAmount" required></div>
            <div class="form-group"><label>Interest Rate (%) *</label><input type="number" step="0.01" id="loanInterestRate" required></div>
            <div class="form-group"><label>Start Date *</label><input type="date" id="loanStartDate" required></div>
            <div class="form-group"><label>Status</label>
                <select id="loanStatus">
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="CLOSED">CLOSED</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Loan</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>
    `);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveLoan(event, loanId) {
    event.preventDefault();
    const data = {
        clientId: document.getElementById('loanClientId').value,
        amount: document.getElementById('loanAmount').value,
        interestRate: document.getElementById('loanInterestRate').value,
        startDate: document.getElementById('loanStartDate').value,
        status: document.getElementById('loanStatus').value
    };
    try {
        if (loanId) await apiCall(`/loans/${loanId}`, 'PUT', data);
        else await apiCall('/loans', 'POST', data);
        closeModal();
        loadLoans();
        showAlert('Loan saved successfully!', 'success');
    } catch { showAlert('Error saving loan', 'error'); }
}

async function deleteLoan(id) {
    if (confirm('Delete this loan?')) {
        try {
            await apiCall(`/loans/${id}`, 'DELETE');
            loadLoans();
            showAlert('Loan deleted successfully!', 'success');
        } catch { showAlert('Error deleting loan', 'error'); }
    }
}
async function loadTransactions() {
    try {
        const txs = await apiCall('/transactions');
        const tbody = document.getElementById('transactionsTableBody');
        tbody.innerHTML = txs.map(t => `
            <tr>
                <td>${t.transactionId}</td>
                <td>${t.accountId}</td>
                <td>${t.transactionType}</td>
                <td>${formatCurrency(t.amount)}</td>
                <td>${formatDate(t.transactionDate)}</td>
                <td>${t.description || ''}</td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}

async function makeDeposit(e) {
    e.preventDefault();
    try {
        await apiCall('/transactions/deposit', 'POST', {
            accountId: depositAccountId.value,
            amount: depositAmount.value,
            description: depositDescription.value
        });
        loadTransactions();
        showAlert('Deposit successful!', 'success');
    } catch { showAlert('Deposit failed', 'error'); }
}

async function makeWithdrawal(e) {
    e.preventDefault();
    try {
        await apiCall('/transactions/withdraw', 'POST', {
            accountId: withdrawAccountId.value,
            amount: withdrawAmount.value,
            description: withdrawDescription.value
        });
        loadTransactions();
        showAlert('Withdrawal successful!', 'success');
    } catch { showAlert('Withdrawal failed', 'error'); }
}

async function makeTransfer(e) {
    e.preventDefault();
    try {
        await apiCall('/transactions/transfer', 'POST', {
            fromAccountId: transferFromAccountId.value,
            toAccountId: transferToAccountId.value,
            amount: transferAmount.value,
            description: transferDescription.value
        });
        loadTransactions();
        showAlert('Transfer successful!', 'success');
    } catch { showAlert('Transfer failed', 'error'); }
}
async function loadUsers() {
    try {
        const users = await apiCall('/users');
        const tbody = document.getElementById('usersTableBody');
        tbody.innerHTML = users.map(u => `
            <tr>
                <td>${u.userId}</td>
                <td>${u.fullName}</td>
                <td>${u.email}</td>
                <td>${u.role.replace('ROLE_', '')}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="openUserModal(${u.userId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteUser(${u.userId})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (e) { console.error(e); }
}

function openUserModal(userId = null) {
    const modal = createModal('User Form', `
        <form onsubmit="saveUser(event, ${userId})">
            <div class="form-group"><label>Full Name *</label><input type="text" id="userFullName" required></div>
            <div class="form-group"><label>Email *</label><input type="email" id="userEmail" required></div>
            <div class="form-group"><label>Password *</label><input type="password" id="userPassword" ${userId ? '' : 'required'}></div>
            <div class="form-group"><label>Role *</label>
                <select id="userRole">
                    <option value="ROLE_ADMIN">ADMIN</option>
                    <option value="ROLE_USER">USER</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save User</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>
    `);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveUser(event, userId) {
    event.preventDefault();
    const data = {
        fullName: document.getElementById('userFullName').value,
        email: document.getElementById('userEmail').value,
        password: document.getElementById('userPassword').value,
        role: document.getElementById('userRole').value
    };
    try {
        if (userId) await apiCall(`/users/${userId}`, 'PUT', data);
        else await apiCall('/users', 'POST', data);
        closeModal();
        loadUsers();
        showAlert('User saved successfully!', 'success');
    } catch { showAlert('Error saving user', 'error'); }
}

async function deleteUser(id) {
    if (confirm('Delete this user?')) {
        try {
            await apiCall(`/users/${id}`, 'DELETE');
            loadUsers();
            showAlert('User deleted successfully!', 'success');
        } catch { showAlert('Error deleting user', 'error'); }
    }
}
/* =======================
   ACCOUNTS
======================= */

async function loadAccounts() {
    try {
        const accounts = await apiCall('/accounts');
        const tbody = document.getElementById('accountsTableBody');
        tbody.innerHTML = accounts.map(a => `
            <tr>
                <td>${a.accountId}</td>
                <td>${a.clientName}</td>
                <td>${a.accountType}</td>
                <td>${formatCurrency(a.currentBalance)}</td>
                <td>${a.status}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="editAccount(${a.accountId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteAccount(${a.accountId})">Delete</button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        console.error(e);
    }
}

// Open modal for add/edit account
function openAccountModal(accountId = null) {
    const modal = createModal('Account Form', `
        <form onsubmit="saveAccount(event, ${accountId})">
            <div class="form-group">
                <label>Client ID *</label>
                <input type="number" id="accountClientId" required>
            </div>
            <div class="form-group">
                <label>Account Type *</label>
                <input type="text" id="accountType" required>
            </div>
            <div class="form-group">
                <label>Balance *</label>
                <input type="number" step="0.01" id="accountBalance" required>
            </div>
            <div class="form-group">
                <label>Status</label>
                <select id="accountStatus">
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="INACTIVE">INACTIVE</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Account</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>
    `);
    document.getElementById('modalContainer').appendChild(modal);
}

// Save account (POST or PUT)
async function saveAccount(event, accountId) {
    event.preventDefault();

    const data = {
        clientId: Number(document.getElementById('accountClientId').value),
        accountType: document.getElementById('accountType').value,
        currentBalance: Number(document.getElementById('accountBalance').value),
        status: document.getElementById('accountStatus').value
    };

    try {
        let response;
        if (accountId) {
            response = await fetch(`${API_BASE_URL}/accounts/${accountId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': authHeader,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
        } else {
            response = await fetch(`${API_BASE_URL}/accounts`, {
                method: 'POST',
                headers: {
                    'Authorization': authHeader,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
        }

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP ${response.status}: ${errorText}`);
        }

        closeModal();
        loadAccounts();
        showAlert('Account saved successfully!', 'success');

    } catch (error) {
        console.error('Error saving account:', error);
        showAlert('Error saving account: ' + error.message, 'error');
    }
}

async function editAccount(accountId) {
    try {
        const account = await apiCall(`/accounts/${accountId}`);
        openAccountModal(accountId);

        document.getElementById('accountClientId').value = account.clientId;
        document.getElementById('accountType').value = account.accountType;
        document.getElementById('accountBalance').value = account.currentBalance;
        document.getElementById('accountStatus').value = account.status;
    } catch (e) {
        console.error(e);
        showAlert('Error loading account', 'error');
    }
}

async function deleteAccount(id) {
    if (confirm('Delete this account?')) {
        try {
            await apiCall(`/accounts/${id}`, 'DELETE');
            loadAccounts();
            showAlert('Account deleted successfully!', 'success');
        } catch (e) {
            showAlert('Error deleting account', 'error');
        }
    }
}


loadDashboard();
loadClients();
loadAccounts();
loadLoans();
loadTransactions();
loadUsers();
