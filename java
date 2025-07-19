// Espera que todo o conteúdo HTML seja carregado antes de executar o script
document.addEventListener('DOMContentLoaded', () => {

    // --- Seletores de Elementos do DOM ---
    const balanceValueEl = document.getElementById('balance-value');
    const transactionListEl = document.getElementById('transaction-list');
    const openModalBtn = document.getElementById('open-modal-btn');
    const closeModalBtn = document.getElementById('close-modal-btn');
    const modalOverlay = document.getElementById('add-transaction-modal');
    const transactionForm = document.getElementById('transaction-form');
    const typeExpenseBtn = document.getElementById('type-expense');
    const typeIncomeBtn = document.getElementById('type-income');

    // --- Estado da Aplicação (os nossos dados) ---
    let transactions = [
        { id: 1, category: 'Lazer', description: 'Cinema com amigos', value: 55.90, type: 'expense' },
        { id: 2, category: 'Salário', description: 'Adiantamento', value: 1800.00, type: 'income' },
        { id: 3, category: 'Alimentação', description: 'Almoço no restaurante', value: 35.00, type: 'expense' },
    ];

    const categoryIcons = {
        'Lazer': '🎬', 'Salário': '💰', 'Alimentação': '🍔', 'Transporte': '🚌', 'Moradia': '🏠', 'Outra': '🛒', 'Compras': '🛍️'
    };
    
    // --- Funções ---

    /**
     * Calcula o saldo total com base nas transações.
     * Receitas somam, despesas subtraem.
     */
    const calculateBalance = () => {
        return transactions.reduce((acc, transaction) => {
            return transaction.type === 'income' ? acc + transaction.value : acc - transaction.value;
        }, 0);
    };

    /**
     * Renderiza uma única transação na lista do HTML.
     * @param {object} transaction - O objeto da transação a ser adicionado.
     */
    const renderTransactionItem = (transaction) => {
        const { id, category, description, value, type } = transaction;
        const isIncome = type === 'income';
        const sign = isIncome ? '+' : '-';
        const valueClass = isIncome ? 'income' : 'expense';

        const item = document.createElement('li');
        item.classList.add('transaction-item');
        item.innerHTML = `
            <div class="transaction-details">
                <div class="transaction-icon">${categoryIcons[category] || '❓'}</div>
                <div>
                    <p class="transaction-category">${category}</p>
                    <p class="transaction-description">${description}</p>
                </div>
            </div>
            <p class="transaction-value ${valueClass}">${sign} R$ ${value.toFixed(2).replace('.', ',')}</p>
        `;
        transactionListEl.prepend(item); // Adiciona no início da lista
    };

    /**
     * Atualiza toda a interface (UI) com os dados mais recentes.
     * Limpa a lista e o saldo e os renderiza novamente.
     */
    const updateUI = () => {
        // Atualiza o saldo
        const balance = calculateBalance();
        balanceValueEl.textContent = `R$ ${balance.toFixed(2).replace('.', ',')}`;

        // Limpa a lista de transações antes de renderizar
        transactionListEl.innerHTML = '';

        // Renderiza cada transação
        transactions.forEach(renderTransactionItem);
    };

    /**
     * Adiciona uma nova transação ao estado e atualiza a UI.
     * @param {Event} e - O evento de submissão do formulário.
     */
    const addTransaction = (e) => {
        e.preventDefault(); // Impede o recarregamento da página

        const amountInput = document.getElementById('amount');
        const descriptionInput = document.getElementById('description');
        const categoryInput = document.getElementById('category');

        const value = parseFloat(amountInput.value.replace('R$', '').replace(',', '.').trim());
        const description = descriptionInput.value;
        const category = categoryInput.value;
        const type = typeExpenseBtn.classList.contains('active') ? 'expense' : 'income';

        // Validação simples
        if (isNaN(value) || value <= 0) {
            alert('Por favor, insira um valor válido.');
            return;
        }

        const newTransaction = {
            id: Date.now(), // ID único baseado no tempo
            value,
            description: description || category,
            category,
            type
        };

        transactions.push(newTransaction);
        updateUI();
        closeModal();
        transactionForm.reset(); // Limpa o formulário
    };

    // --- Funções do Modal ---
    const openModal = () => modalOverlay.classList.remove('hidden');
    const closeModal = () => modalOverlay.classList.add('hidden');

    // --- Event Listeners ---
    openModalBtn.addEventListener('click', openModal);
    closeModalBtn.addEventListener('click', closeModal);
    modalOverlay.addEventListener('click', (e) => {
        if (e.target === modalOverlay) {
            closeModal();
        }
    });
    
    transactionForm.addEventListener('submit', addTransaction);

    // Lógica para o seletor de tipo (Despesa/Receita)
    typeExpenseBtn.addEventListener('click', () => {
        typeExpenseBtn.classList.add('active');
        typeIncomeBtn.classList.remove('active');
    });

    typeIncomeBtn.addEventListener('click', () => {
        typeIncomeBtn.classList.add('active');
        typeExpenseBtn.classList.remove('active');
    });

    // --- Inicialização da Aplicação ---
    updateUI();
});
