async function loadAccounts(){
    if(!canPerform('manageAccounts')) return;
    try{
        const accounts = await apiCall('/accounts');
        const tbody = document.getElementById('accountsTableBody');
        tbody.innerHTML = accounts.map(a=>`
            <tr>
                <td>${a.accountId}</td>
                <td>${a.clientName}</td>
                <td>${a.accountType}</td>
                <td>${formatCurrency(a.currentBalance)}</td>
                <td>${a.status}</td>
                <td class="action-buttons">
                    ${canPerform('manageAccounts')?`<button class="btn btn-small btn-primary" onclick="editAccount(${a.accountId})">Edit</button>`:''}
                    ${canPerform('manageAccounts')?`<button class="btn btn-small btn-danger" onclick="deleteAccount(${a.accountId})">Delete</button>`:''}
                </td>
            </tr>`).join('');
    }catch(e){ console.error(e); }
}

function openAccountModal(accountId=null){
    if(!canPerform('manageAccounts')) return;
    const modal=createModal('Account Form',`
        <form onsubmit="saveAccount(event, ${accountId})">
            <div class="form-group"><label>Client ID *</label><input type="number" id="accountClientId" required></div>
            <div class="form-group"><label>Account Type *</label><input type="text" id="accountType" required></div>
            <div class="form-group"><label>Balance *</label><input type="number" step="0.01" id="accountBalance" required></div>
            <div class="form-group"><label>Status</label>
                <select id="accountStatus">
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="INACTIVE">INACTIVE</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Account</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>`);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveAccount(event,accountId){
    if(!canPerform('manageAccounts')) return;
    event.preventDefault();
    const data={
        clientId:Number(document.getElementById('accountClientId').value),
        accountType:document.getElementById('accountType').value,
        currentBalance:Number(document.getElementById('accountBalance').value),
        status:document.getElementById('accountStatus').value
    };
    try{
        if(accountId) await apiCall(`/accounts/${accountId}`,'PUT',data);
        else await apiCall('/accounts','POST',data);
        closeModal();
        loadAccounts();
        showAlert('Account saved successfully!');
    }catch(e){ showAlert('Error saving account: '+e.message,'error'); }
}

async function editAccount(accountId){
    if(!canPerform('manageAccounts')) return;
    try{
        const account = await apiCall(`/accounts/${accountId}`);
        openAccountModal(accountId);
        document.getElementById('accountClientId').value=account.clientId;
        document.getElementById('accountType').value=account.accountType;
        document.getElementById('accountBalance').value=account.currentBalance;
        document.getElementById('accountStatus').value=account.status;
    }catch(e){ showAlert('Error loading account','error'); }
}

async function deleteAccount(id){
    if(!canPerform('manageAccounts')) return;
    if(confirm('Delete this account?')){
        try{
            await apiCall(`/accounts/${id}`,'DELETE');
            loadAccounts();
            showAlert('Account deleted successfully!');
        }catch{ showAlert('Error deleting account','error'); }
    }
}
