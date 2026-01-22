async function loadClients(){
    if(!canPerform('manageClients')) return;
    try{
        const clients = await apiCall('/clients');
        const tbody = document.getElementById('clientsTableBody');
        tbody.innerHTML = clients.map(c=>`
            <tr>
                <td>${c.clientId}</td>
                <td>${c.fullName}</td>
                <td>${c.email||'N/A'}</td>
                <td>${c.phone||'N/A'}</td>
                <td>${formatDate(c.createdAt)}</td>
                <td class="action-buttons">
                    <button class="btn btn-small btn-primary" onclick="openClientModal(${c.clientId})">Edit</button>
                    <button class="btn btn-small btn-danger" onclick="deleteClient(${c.clientId})">Delete</button>
                </td>
            </tr>`).join('');
    }catch(e){ console.error(e); }
}

function openClientModal(clientId=null){
    if(!canPerform('manageClients')) return;
    const modal=createModal('Client Form',`
        <form onsubmit="saveClient(event, ${clientId})">
            <div class="form-group"><label>Full Name *</label><input type="text" id="clientFullName" required></div>
            <div class="form-group"><label>Email</label><input type="email" id="clientEmail"></div>
            <div class="form-group"><label>Phone</label><input type="tel" id="clientPhone"></div>
            <div class="form-group"><label>Address</label><textarea id="clientAddress" rows="3"></textarea></div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save Client</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>`);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveClient(event,clientId){
    if(!canPerform('manageClients')) return;
    event.preventDefault();
    const data={
        fullName: document.getElementById('clientFullName').value,
        email: document.getElementById('clientEmail').value,
        phone: document.getElementById('clientPhone').value,
        address: document.getElementById('clientAddress').value
    };
    try{
        if(clientId) await apiCall(`/clients/${clientId}`,'PUT',data);
        else await apiCall('/clients','POST',data);
        closeModal();
        loadClients();
        showAlert('Client saved successfully!');
    }catch{ showAlert('Error saving client','error'); }
}

async function deleteClient(id){
    if(!canPerform('manageClients')) return;
    if(confirm('Delete this client?')){
        try{
            await apiCall(`/clients/${id}`,'DELETE');
            loadClients();
            showAlert('Client deleted successfully!');
        }catch{ showAlert('Error deleting client','error'); }
    }
}
