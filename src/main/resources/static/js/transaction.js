
async function loadTransactions(){
    if(!canPerform('manageTransactions')) return;
    const tbody = document.getElementById('transactionsTableBody');
    try{
        const txs = await apiCall('/transactions');
        if(txs.length===0){
            tbody.innerHTML='<tr><td colspan="7" style="text-align:center; padding:30px;">No transactions found</td></tr>';
            return;
        }
        txs.sort((a,b)=>new Date(b.transactionDate)-new Date(a.transactionDate));
        tbody.innerHTML = txs.map(tx=>{
            const isPositive = tx.transactionType==='DEPOSIT'||tx.transactionType==='TRANSFER_IN';
            const prefix = isPositive?'+':'-';
            const cls = isPositive?'amount-positive':'amount-negative';
            return `
            <tr>
                <td>#${tx.transactionId}</td>
                <td>${tx.accountId}</td>
                <td>${tx.transactionType.replace('_',' ')}</td>
                <td class="${cls}">${prefix}${formatCurrency(Math.abs(tx.amount))}</td>
                <td>${tx.description||'N/A'}</td>
                <td>${formatDateTime(tx.transactionDate)}</td>
                <td>${tx.userName||'System'}</td>
            </tr>`;
        }).join('');
    }catch(e){ console.error(e); }
}

async function makeDeposit(event){
    if(!canPerform('manageTransactions')) return;
    event.preventDefault();
    const accountId=Number(document.getElementById('depositAccountId').value);
    const amount=Number(document.getElementById('depositAmount').value);
    const description=document.getElementById('depositDescription').value||'Deposit';
    try{
        await apiCall('/transactions/deposit','POST',{accountId,amount,userId:Number(userId),description});
        document.getElementById('depositAccountId').value='';
        document.getElementById('depositAmount').value='';
        document.getElementById('depositDescription').value='';
        await loadTransactions();
        showAlert(`Deposit of ${formatCurrency(amount)} successful!`);
    }catch(e){ showAlert('Deposit failed: '+e.message,'error'); }
}

async function makeWithdrawal(event){
    if(!canPerform('manageTransactions')) return;
    event.preventDefault();
    const accountId=Number(document.getElementById('withdrawAccountId').value);
    const amount=Number(document.getElementById('withdrawAmount').value);
    const description=document.getElementById('withdrawDescription').value||'Withdrawal';
    try{
        await apiCall('/transactions/withdraw','POST',{accountId,amount,userId:Number(userId),description});
        document.getElementById('withdrawAccountId').value='';
        document.getElementById('withdrawAmount').value='';
        document.getElementById('withdrawDescription').value='';
        await loadTransactions();
        showAlert(`Withdrawal of ${formatCurrency(amount)} successful!`);
    }catch(e){ showAlert('Withdrawal failed: '+e.message,'error'); }
}

async function makeTransfer(event){
    if(!canPerform('manageTransactions')) return;
    event.preventDefault();
    const fromAccountId=Number(document.getElementById('transferFromAccountId').value);
    const toAccountId=Number(document.getElementById('transferToAccountId').value);
    const amount=Number(document.getElementById('transferAmount').value);
    const description=document.getElementById('transferDescription').value||'Transfer';

    if(fromAccountId===toAccountId){ showAlert('Cannot transfer to same account','error'); return; }

    try{
        await apiCall('/transactions/transfer','POST',{fromAccountId,toAccountId,amount,userId:Number(userId),description});
        document.getElementById('transferFromAccountId').value='';
        document.getElementById('transferToAccountId').value='';
        document.getElementById('transferAmount').value='';
        document.getElementById('transferDescription').value='';
        await loadTransactions();
        showAlert(`Transfer of ${formatCurrency(amount)} successful!`);
    }catch(e){ showAlert('Transfer failed: '+e.message,'error'); }
}
