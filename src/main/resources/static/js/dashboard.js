async function loadDashboard(){
    if(!canAccess('dashboard')) return;
    try{
        const data = await apiCall('/dashboard');
        const statsGrid = document.getElementById('statsGrid');
        statsGrid.innerHTML=`
            <div class="stat-card clients"><h3>Total Clients</h3><div class="stat-value">${data.totalClients||0}</div></div>
            <div class="stat-card accounts"><h3>Total Accounts</h3><div class="stat-value">${data.totalAccounts||0}</div></div>
            <div class="stat-card loans"><h3>Active Loans</h3><div class="stat-value">${data.totalLoans||0}</div></div>
            <div class="stat-card transactions"><h3>Today's Transactions</h3><div class="stat-value">${data.totalTransactionsToday||0}</div></div>
        `;

        const txList = document.getElementById('recentTransactions');
        if(data.recentTransactions && data.recentTransactions.length>0){
            txList.innerHTML='<h2 style="margin-bottom:20px;">Recent Transactions</h2>'+
                data.recentTransactions.map(tx=>`
                    <div class="transaction-card">
                        <div class="transaction-info">
                            <h4>${tx.clientName}</h4>
                            <p>${tx.transactionType} - ${formatDate(tx.transactionDate)}</p>
                        </div>
                        <div class="transaction-amount ${tx.transactionType==='DEPOSIT'?'positive':'negative'}">
                            ${tx.transactionType==='DEPOSIT'?'+':'-'}${formatCurrency(tx.amount)}
                        </div>
                    </div>`).join('');
        }

    }catch(e){ console.error(e); }
}
