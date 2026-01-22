const API_BASE_URL = 'http://localhost:8080/api';
const userEmail = localStorage.getItem('userEmail') || 'admin@example.com';
const userPassword = localStorage.getItem('userPassword') || 'admin123';
const userRole = localStorage.getItem('userRole'); // duhet ROLE_ADMIN, ROLE_TELLER, ROLE_MANAGER
const userId = localStorage.getItem('userId') || '1';
const authHeader = 'Basic ' + btoa(userEmail + ':' + userPassword);
function formatRole(role){
    return role.replace('ROLE_','');
}

function canAccess(sectionId){
    const role = userRole;
    const accessMap = {
        dashboard: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_TELLER'],
        clients: ['ROLE_ADMIN','ROLE_MANAGER'],
        accounts: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_TELLER'],
        loans: ['ROLE_ADMIN','ROLE_MANAGER'],
        transactions: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_TELLER'],
        users: ['ROLE_ADMIN']
    };
    return accessMap[sectionId]?.includes(role) || false;
}

function canPerform(action){
    const role = userRole;
    const permMap = {
        manageClients: ['ROLE_ADMIN','ROLE_MANAGER'],
        manageAccounts: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_TELLER'],
        manageLoans: ['ROLE_ADMIN','ROLE_MANAGER'],
        manageTransactions: ['ROLE_ADMIN','ROLE_MANAGER','ROLE_TELLER'],
        manageUsers: ['ROLE_ADMIN']
    };
    return permMap[action]?.includes(role) || false;
}

async function apiCall(endpoint, method='GET', body=null){
    const options = {
        method,
        headers: {
            'Authorization': authHeader,
            'Content-Type': 'application/json'
        }
    };
    if(body) options.body = JSON.stringify(body);
    const res = await fetch(API_BASE_URL + endpoint, options);
    if(!res.ok) throw new Error(`HTTP ${res.status}`);
    return method==='DELETE'?null:await res.json();
}

function showAlert(msg,type='success'){
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.textContent = msg;
    document.querySelector('.main-content').prepend(alert);
    setTimeout(()=>alert.remove(),3000);
}

function formatCurrency(amount){
    return new Intl.NumberFormat('en-US',{style:'currency',currency:'USD'}).format(amount);
}

function formatDate(dateString){
    return new Date(dateString).toLocaleDateString('en-US');
}

function formatDateTime(dateString){
    return new Date(dateString).toLocaleString('en-US');
}

function createModal(title,content){
    const modal = document.createElement('div');
    modal.className='modal show';
    modal.innerHTML=`
        <div class="modal-content">
            <div class="modal-header">
                <h2>${title}</h2>
                <span class="close-modal" onclick="closeModal()">×</span>
            </div>
            ${content}
        </div>`;
    return modal;
}

function closeModal(){ document.getElementById('modalContainer').innerHTML=''; }

document.getElementById('sidebarRole').textContent = formatRole(userRole);

function showSection(sectionId){
    if(!canAccess(sectionId)){ showAlert('Access Denied','error'); return; }
    document.querySelectorAll('.content-section').forEach(s=>s.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(n=>n.classList.remove('active'));
    document.getElementById(sectionId).classList.add('active');
    event.target.closest('.nav-item').classList.add('active');

    switch(sectionId){
        case 'dashboard': loadDashboard(); break;
        case 'clients': loadClients(); break;
        case 'accounts': loadAccounts(); break;
        case 'loans': loadLoans(); break;
        case 'transactions': loadTransactions(); break;
        case 'users': loadUsers(); break;
    }
}

function logout(){ localStorage.clear(); window.location.href='login'; }

window.addEventListener('DOMContentLoaded',()=>{
    document.querySelectorAll('.nav-item').forEach(item=>{
        const sectionId = item.getAttribute('onclick').match(/'(\w+)'/)[1];
        if(!canAccess(sectionId)) item.style.display='none';
    });

    if(!canPerform('manageClients')) document.querySelector('button[onclick="openClientModal()"]')?.remove();
    if(!canPerform('manageAccounts')) document.querySelector('button[onclick="openAccountModal()"]')?.remove();
    if(!canPerform('manageLoans')) document.querySelector('button[onclick="openLoanModal()"]')?.remove();
    if(!canPerform('manageUsers')) document.querySelector('button[onclick="openUserModal()"]')?.remove();

    loadDashboard();
    if(canAccess('clients')) loadClients();
    if(canAccess('accounts')) loadAccounts();
    if(canAccess('loans')) loadLoans();
    if(canAccess('transactions')) loadTransactions();
    if(canAccess('users')) loadUsers();

    setInterval(()=>{ if(canAccess('transactions')) loadTransactions(); },30000);
});
