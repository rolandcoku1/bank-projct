async function loadUsers(){
    if(!canPerform('manageUsers')) return;
    try{
        const users = await apiCall('/users');
        const tbody = document.getElementById('usersTableBody');
        tbody.innerHTML = users.map(u=>`
            <tr>
                <td>${u.userId}</td>
                <td>${u.fullName}</td>
                <td>${u.email}</td>
                <td>${formatRole(u.role)}</td>
                <td class="action-buttons">
                    ${canPerform('manageUsers')?`<button class="btn btn-small btn-primary" onclick="openUserModal(${u.userId})">Edit</button>`:''}
                    ${canPerform('manageUsers')?`<button class="btn btn-small btn-danger" onclick="deleteUser(${u.userId})">Delete</button>`:''}
                </td>
            </tr>`).join('');
    }catch(e){ console.error(e); }
}

function openUserModal(userId=null){
    if(!canPerform('manageUsers')) return;
    const modal=createModal('User Form',`
        <form onsubmit="saveUser(event, ${userId})">
            <div class="form-group"><label>Full Name *</label><input type="text" id="userFullName" required></div>
            <div class="form-group"><label>Email *</label><input type="email" id="userEmail" required></div>
            <div class="form-group"><label>Password *</label><input type="password" id="userPassword" ${userId?'':'required'}></div>
            <div class="form-group"><label>Role *</label>
                <select id="userRole">
                    <option value="ROLE_ADMIN">ADMIN</option>
                    <option value="ROLE_MANAGER">MANAGER</option>
                    <option value="ROLE_TELLER">TELLER</option>
                    <option value="ROLE_USER">USER</option>
                </select>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">Save User</button>
                <button type="button" class="btn" onclick="closeModal()">Cancel</button>
            </div>
        </form>`);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveUser(event,userId){
    if(!canPerform('manageUsers')) return;
    event.preventDefault();
    const data={
        fullName:document.getElementById('userFullName').value,
        email:document.getElementById('userEmail').value,
        password:document.getElementById('userPassword').value,
        role:document.getElementById('userRole').value
    };
    try{
        if(userId) await apiCall(`/users/${userId}`,'PUT',data);
        else await apiCall('/users','POST',data);
        closeModal();
        loadUsers();
        showAlert('User saved successfully!');
    }catch{ showAlert('Error saving user','error'); }
}

async function deleteUser(id){
    if(!canPerform('manageUsers')) return;
    if(confirm('Delete this user?')){
        try{
            await apiCall(`/users/${id}`,'DELETE');
            loadUsers();
            showAlert('User deleted successfully!');
        }catch{ showAlert('Error deleting user','error'); }
    }
}
