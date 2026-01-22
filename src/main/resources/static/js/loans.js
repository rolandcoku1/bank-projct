async function loadLoans(){
    if(!canPerform('manageLoans')) return;
    try{
        const loans = await apiCall('/loans');
        const tbody = document.getElementById('loansTableBody');
        tbody.innerHTML = loans.map(l=>`
            <tr>
                <td>${l.loanId}</td>
                <td>${l.clientName}</td>
                <td>${formatCurrency(l.amount)}</td>
                <td>${l.interestRate}%</td>
                <td>${formatDate(l.startDate)}</td>
                <td>${l.status}</td>
                <td class="action-buttons">
                    ${canPerform('manageLoans')?`<button class="btn btn-small btn-primary" onclick="openLoanModal(${l.loanId})">Edit</button>`:''}
                    ${canPerform('manageLoans')?`<button class="btn btn-small btn-danger" onclick="deleteLoan(${l.loanId})">Delete</button>`:''}
                </td>
            </tr>`).join('');
    }catch(e){ console.error(e); }
}

function openLoanModal(loanId=null){
    if(!canPerform('manageLoans')) return;
    const modal=createModal('Loan Form',`
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
        </form>`);
    document.getElementById('modalContainer').appendChild(modal);
}

async function saveLoan(event,loanId){
    if(!canPerform('manageLoans')) return;
    event.preventDefault();
    const data={
        clientId:Number(document.getElementById('loanClientId').value),
        amount:Number(document.getElementById('loanAmount').value),
        interestRate:Number(document.getElementById('loanInterestRate').value),
        startDate:document.getElementById('loanStartDate').value,
        status:document.getElementById('loanStatus').value
    };
    try{
        if(loanId) await apiCall(`/loans/${loanId}`,'PUT',data);
        else await apiCall('/loans','POST',data);
        closeModal();
        loadLoans();
        showAlert('Loan saved successfully!');
    }catch{ showAlert('Error saving loan','error'); }
}

async function deleteLoan(id){
    if(!canPerform('manageLoans')) return;
    if(confirm('Delete this loan?')){
        try{
            await apiCall(`/loans/${id}`,'DELETE');
            loadLoans();
            showAlert('Loan deleted successfully!');
        }catch{ showAlert('Error deleting loan','error'); }
    }
}
