package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AuraRepository
import com.example.model.BankAccount
import com.example.model.Quote
import com.example.model.Transaction
import com.example.model.DepositAddress
import com.example.model.DepositAsset
import com.example.model.DepositNetwork
import com.example.model.SecurityState
import com.example.model.RewardEvent
import com.example.model.SetupTask
import com.example.model.TaskStatus
import com.example.model.TourState
import com.example.model.TransactionStatus
import com.example.model.UserAccount
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppNavDestination {
    HOME,
    SELL_USDT,
    REVIEW_SALE,
    PROCESSING_SALE,
    COMPLETED_SALE,
    FAILED_SALE,
    TRANSACTION_DETAIL,
    LOGIN,
    RECEIVE,
    PIN_LOCK,
    CREATE_PIN,
    WELCOME,
    REWARDS,
    SETUP_JOURNEY
}

enum class BottomNavTab {
    HOME,
    SETUPS,
    WALLET,
    ACTIVITY,
    PROFILE
}

enum class ActiveBottomSheet {
    NONE,
    RATE_DETAILS,
    FEE_DETAILS,
    SETTLEMENT_DETAILS,
    BANK_SELECTOR,
    QUOTE_EXPIRED,
    SUPPORT_MODAL,
    ADD_BANK
}

enum class ActivityFilter {
    ALL,
    COMPLETED,
    PROCESSING,
    FAILED
}

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

class AuraViewModel(
    private val repository: AuraRepository = AuraRepository()
) : ViewModel() {

    val userAccount: StateFlow<UserAccount> = repository.userAccount
    val bankAccounts: StateFlow<List<BankAccount>> = repository.bankAccounts
    val selectedBank: StateFlow<BankAccount> = repository.selectedBank
    val currentRate: StateFlow<Double> = repository.currentRate
    val isRateUpdating: StateFlow<Boolean> = repository.isRateUpdating
    val isOffline: StateFlow<Boolean> = repository.isOffline
    val quoteTimerSeconds: StateFlow<Int> = repository.quoteTimerSeconds
    val isQuoteExpired: StateFlow<Boolean> = repository.isQuoteExpired
    val previousExpiredRate: StateFlow<Double?> = repository.previousExpiredRate
    val allTransactions: StateFlow<List<Transaction>> = repository.transactions

    val setupTasks: StateFlow<List<SetupTask>> = repository.setupTasks
    val rewardEvents: StateFlow<List<RewardEvent>> = repository.rewardEvents
    val tourState: StateFlow<TourState> = repository.tourState

    val auraPoints: StateFlow<Int> = rewardEvents.combine(setupTasks) { events, _ ->
        events.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val nextRecommendedTask: StateFlow<SetupTask?> = setupTasks.combine(userAccount) { tasks, _ ->
        tasks.firstOrNull { it.status == TaskStatus.INCOMPLETE }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Navigation & View States
    private val _currentDestination = MutableStateFlow(AppNavDestination.HOME)
    val currentDestination: StateFlow<AppNavDestination> = _currentDestination.asStateFlow()

    private val _selectedBottomTab = MutableStateFlow(BottomNavTab.HOME)
    val selectedBottomTab: StateFlow<BottomNavTab> = _selectedBottomTab.asStateFlow()

    private val _activeBottomSheet = MutableStateFlow(ActiveBottomSheet.NONE)
    val activeBottomSheet: StateFlow<ActiveBottomSheet> = _activeBottomSheet.asStateFlow()

    // USDT Input State
    private val _inputUsdtAmountString = MutableStateFlow("10000")
    val inputUsdtAmountString: StateFlow<String> = _inputUsdtAmountString.asStateFlow()

    private val _amountValidationError = MutableStateFlow<String?>(null)
    val amountValidationError: StateFlow<String?> = _amountValidationError.asStateFlow()

    // Active in-progress transaction
    private val _activeTransaction = MutableStateFlow<Transaction?>(null)
    val activeTransaction: StateFlow<Transaction?> = _activeTransaction.asStateFlow()

    // Selected transaction for details view
    private val _selectedDetailTransaction = MutableStateFlow<Transaction?>(null)
    val selectedDetailTransaction: StateFlow<Transaction?> = _selectedDetailTransaction.asStateFlow()

    // Confirmation / Auth state
    private val _isConfirmingSale = MutableStateFlow(false)
    val isConfirmingSale: StateFlow<Boolean> = _isConfirmingSale.asStateFlow()

    private val _isBiometricSheetVisible = MutableStateFlow(false)
    val isBiometricSheetVisible: StateFlow<Boolean> = _isBiometricSheetVisible.asStateFlow()

    // Activity filtering & search
    private val _activityFilter = MutableStateFlow(ActivityFilter.ALL)
    val activityFilter: StateFlow<ActivityFilter> = _activityFilter.asStateFlow()

    private val _activitySearchQuery = MutableStateFlow("")
    val activitySearchQuery: StateFlow<String> = _activitySearchQuery.asStateFlow()

    // Toast message for user actions (e.g. copied UTR, switched account)
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    // Theme State
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    // Computed live quote
    val currentQuote: StateFlow<Quote> = combine(
        _inputUsdtAmountString,
        currentRate,
        quoteTimerSeconds,
        isQuoteExpired,
        previousExpiredRate
    ) { inputStr, rate, timer, expired, oldRate ->
        val amount = inputStr.toDoubleOrNull() ?: 0.0
        val quote = Quote.calculate(amount, rate, oldRate)
        quote.copy(
            secondsRemaining = timer,
            isExpired = expired
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = Quote.calculate(10000.0, 84.72)
    )

    private fun handlePinKeyPress(key: String) {
        val current = _pinBuffer.value
        when (key) {
            "⌫", "DEL", "BACKSPACE" -> {
                if (current.isNotEmpty()) _pinBuffer.value = current.dropLast(1)
            }
            else -> {
                if (current.length < 6) {
                    val newPin = current + key
                    _pinBuffer.value = newPin
                    checkPinAutoSubmit(newPin)
                }
            }
        }
    }

    private fun checkPinAutoSubmit(pin: String) {
        if (pin.length == 4) { // Assuming 4-digit PIN for demo
            viewModelScope.launch {
                delay(200)
                if (_currentDestination.value == AppNavDestination.PIN_LOCK) {
                    if (pin == _savedPin.value || pin == "0000") { // 0000 master for demo
                        _securityState.update { it.copy(isAppLocked = false) }
                        _currentDestination.value = AppNavDestination.HOME
                        _pinBuffer.value = ""
                    } else {
                        showToast("Incorrect PIN")
                        _pinBuffer.value = ""
                    }
                } else if (_currentDestination.value == AppNavDestination.CREATE_PIN) {
                    _savedPin.value = pin
                    _securityState.update { it.copy(hasPinSet = true, isAppLocked = false) }
                    showToast("App PIN set successfully")
                    _currentDestination.value = AppNavDestination.HOME
                    _pinBuffer.value = ""
                }
            }
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isConfirmingSale.value = true // Reusing loading state for demo
            delay(1000)
            _isConfirmingSale.value = false
            
            if (email.isNotBlank() && pass.isNotBlank()) {
                _securityState.update { it.copy(isAuthenticated = true) }
                if (!_securityState.value.hasPinSet) {
                    _currentDestination.value = AppNavDestination.CREATE_PIN
                } else {
                    _currentDestination.value = AppNavDestination.HOME
                }
            } else {
                showToast("Please enter credentials")
            }
        }
    }

    fun logout() {
        _securityState.update { it.copy(isAuthenticated = false, isAppLocked = true) }
        _currentDestination.value = AppNavDestination.LOGIN
    }

    fun openReceive(initialAsset: String? = null) {
        val asset = when(initialAsset) {
            "USDC" -> DepositAsset("USDC", "USD Coin")
            else -> DepositAsset("USDT", "Tether")
        }
        selectDepositAsset(asset)
        _currentDestination.value = AppNavDestination.RECEIVE
    }

    fun selectDepositAsset(asset: DepositAsset) {
        _selectedDepositAsset.value = asset
        _selectedDepositNetwork.value = null
        _depositAddress.value = null
        
        // Auto-select first network if available for demo
        val networks = getMockNetworks(asset.symbol)
        if (networks.isNotEmpty()) {
            selectDepositNetwork(networks.first())
        }
    }

    fun selectDepositNetwork(network: DepositNetwork) {
        _selectedDepositNetwork.value = network
        fetchDepositAddress(network)
    }

    private fun fetchDepositAddress(network: DepositNetwork) {
        viewModelScope.launch {
            _isAddressLoading.value = true
            _depositAddress.value = null
            delay(800)
            val address = when (network.protocol) {
                "TRC-20" -> "TX8a9Jk2LpNmQ4vW1Rz9PxY6tB3cD7eFgH"
                "ERC-20" -> "0x742d35Cc6634C0532925a3b844Bc454e4438f44e"
                "SOL" -> "7xKX3z12JdMNpQ4vW1Rz9PxY6tB3cD7eFgH5i"
                else -> "0xAddressFor${network.name}"
            }
            _depositAddress.value = DepositAddress(
                address = address,
                qrPayload = address
            )
            _isAddressLoading.value = false
        }
    }

    fun getMockNetworks(symbol: String): List<DepositNetwork> {
        return listOf(
            DepositNetwork("tron", "Tron", "TRC-20", "2 min", "Free", "1 USDT"),
            DepositNetwork("eth", "Ethereum", "ERC-20", "5 min", "1.2 USDT", "20 USDT"),
            DepositNetwork("sol", "Solana", "SOL", "Instant", "Free", "0.1 USDT"),
            DepositNetwork("bsc", "BNB Chain", "BEP-20", "2 min", "0.5 USDT", "1 USDT")
        )
    }
    val filteredTransactions: StateFlow<List<Transaction>> = combine(
        allTransactions,
        _activityFilter,
        _activitySearchQuery
    ) { txns, filter, query ->
        txns.filter { txn ->
            val matchesFilter = when (filter) {
                ActivityFilter.ALL -> true
                ActivityFilter.COMPLETED -> txn.status == TransactionStatus.COMPLETED
                ActivityFilter.PROCESSING -> txn.status == TransactionStatus.PROCESSING || txn.status == TransactionStatus.SUBMITTED
                ActivityFilter.FAILED -> txn.status == TransactionStatus.FAILED || txn.status == TransactionStatus.CANCELLED
            }
            val matchesQuery = if (query.isBlank()) true else {
                txn.id.contains(query, ignoreCase = true) ||
                txn.settlementBankName.contains(query, ignoreCase = true) ||
                (txn.utrNumber?.contains(query, ignoreCase = true) == true)
            }
            matchesFilter && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )

    fun navigateTo(destination: AppNavDestination) {
        _currentDestination.value = destination
    }

    fun selectBottomTab(tab: BottomNavTab) {
        _selectedBottomTab.value = tab
        if (_currentDestination.value != AppNavDestination.HOME && tab == BottomNavTab.HOME) {
            _currentDestination.value = AppNavDestination.HOME
        }
    }

    fun openBottomSheet(sheet: ActiveBottomSheet) {
        _activeBottomSheet.value = sheet
    }

    fun closeBottomSheet() {
        _activeBottomSheet.value = ActiveBottomSheet.NONE
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _toastMessage.value = message
            delay(2500)
            if (_toastMessage.value == message) {
                _toastMessage.value = null
            }
        }
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    // Security & Auth State
    private val _securityState = MutableStateFlow(SecurityState())
    val securityState: StateFlow<SecurityState> = _securityState.asStateFlow()

    private val _pinBuffer = MutableStateFlow("")
    val pinBuffer: StateFlow<String> = _pinBuffer.asStateFlow()
    
    private val _savedPin = MutableStateFlow<String?>(null) // Should be secure storage in prod

    // Receive State
    private val _selectedDepositAsset = MutableStateFlow<DepositAsset?>(null)
    val selectedDepositAsset: StateFlow<DepositAsset?> = _selectedDepositAsset.asStateFlow()

    private val _selectedDepositNetwork = MutableStateFlow<DepositNetwork?>(null)
    val selectedDepositNetwork: StateFlow<DepositNetwork?> = _selectedDepositNetwork.asStateFlow()

    private val _depositAddress = MutableStateFlow<DepositAddress?>(null)
    val depositAddress: StateFlow<DepositAddress?> = _depositAddress.asStateFlow()

    private val _isAddressLoading = MutableStateFlow(false)
    val isAddressLoading: StateFlow<Boolean> = _isAddressLoading.asStateFlow()

    private val _receiveCoachMarkSeen = MutableStateFlow(false)
    val receiveCoachMarkSeen: StateFlow<Boolean> = _receiveCoachMarkSeen.asStateFlow()

    // Keypad and Input Handling
    fun onKeyPress(key: String) {
        if (_currentDestination.value == AppNavDestination.PIN_LOCK || 
            _currentDestination.value == AppNavDestination.CREATE_PIN) {
            handlePinKeyPress(key)
            return
        }
        val current = _inputUsdtAmountString.value
        val newString = when (key) {
            "⌫", "DEL", "BACKSPACE" -> {
                if (current.length <= 1) "0" else current.dropLast(1)
            }
            "." -> {
                if (current.contains(".")) current else "$current."
            }
            else -> {
                if (current == "0") key else {
                    if (current.length < 9) current + key else current
                }
            }
        }
        setAmountString(newString)
    }

    fun setAmountString(amountStr: String) {
        _inputUsdtAmountString.value = amountStr
        validateAmount(amountStr)
    }

    fun setPercentageAmount(percent: Int) {
        val balance = userAccount.value.usdtBalance
        val targetAmount = when (percent) {
            25 -> balance * 0.25
            50 -> balance * 0.50
            75 -> balance * 0.75
            100 -> balance
            else -> balance
        }
        val formatted = if (targetAmount % 1.0 == 0.0) {
            targetAmount.toLong().toString()
        } else {
            String.format(java.util.Locale.US, "%.2f", targetAmount)
        }
        setAmountString(formatted)
    }

    private fun validateAmount(amountStr: String): Boolean {
        val amount = amountStr.toDoubleOrNull()
        val available = userAccount.value.usdtBalance

        return when {
            amount == null || amount <= 0.0 -> {
                _amountValidationError.value = "Please enter an amount greater than 0"
                false
            }
            amount < 10.0 -> {
                _amountValidationError.value = "Minimum sale amount is 10 USDT"
                false
            }
            amount > available -> {
                _amountValidationError.value = "Insufficient balance (Available: ${available.toLong()} USDT)"
                false
            }
            else -> {
                _amountValidationError.value = null
                true
            }
        }
    }

    fun startSellFlow(initialAmount: Double? = null) {
        if (initialAmount != null) {
            setAmountString(initialAmount.toLong().toString())
        }
        validateAmount(_inputUsdtAmountString.value)
        _currentDestination.value = AppNavDestination.SELL_USDT
    }

    fun proceedToReview() {
        if (validateAmount(_inputUsdtAmountString.value)) {
            _currentDestination.value = AppNavDestination.REVIEW_SALE
        }
    }

    fun triggerBiometricAuth() {
        _isBiometricSheetVisible.value = true
    }

    fun cancelBiometricAuth() {
        _isBiometricSheetVisible.value = false
    }

    fun confirmSaleWithBiometrics() {
        _isBiometricSheetVisible.value = false
        confirmSale()
    }

    fun confirmSale() {
        val amount = _inputUsdtAmountString.value.toDoubleOrNull() ?: return
        val quote = currentQuote.value
        val bank = selectedBank.value

        viewModelScope.launch {
            _isConfirmingSale.value = true
            delay(600) // Calm deliberate transition
            _isConfirmingSale.value = false

            val txn = repository.createSellTransaction(
                usdtAmount = amount,
                rate = quote.ratePerUsdt,
                fee = quote.feeInr,
                gross = quote.grossInr,
                net = quote.netInr,
                bank = bank
            )
            _activeTransaction.value = txn
            _currentDestination.value = AppNavDestination.PROCESSING_SALE

            // Progression simulation: Step 1 -> Step 2
            delay(1200)
            repository.advanceTransactionToProcessing(txn.id)
            _activeTransaction.value = _activeTransaction.value?.copy(
                status = TransactionStatus.PROCESSING,
                currentStepIndex = 2
            )

            // Progression simulation: Step 2 -> Step 3 (Completed)
            delay(2800)
            repository.completeTransaction(txn.id)
            val completedTxn = repository.transactions.value.firstOrNull { it.id == txn.id }
            _activeTransaction.value = completedTxn
            _currentDestination.value = AppNavDestination.COMPLETED_SALE
        }
    }

    fun simulateFailActiveTransaction(reason: String = "Bank IMPS node timeout. USDT refunded.") {
        val txnId = _activeTransaction.value?.id ?: return
        repository.failTransaction(txnId, reason)
        _activeTransaction.value = repository.transactions.value.firstOrNull { it.id == txnId }
        _currentDestination.value = AppNavDestination.FAILED_SALE
    }

    fun selectTransactionForDetail(txn: Transaction) {
        _selectedDetailTransaction.value = txn
        _currentDestination.value = AppNavDestination.TRANSACTION_DETAIL
    }

    fun selectBank(bank: BankAccount) {
        repository.selectBankAccount(bank)
        closeBottomSheet()
        showToast("Settlement account set to ${bank.bankName} (${bank.accountNumberMasked})")
    }

    fun refreshRateManually() {
        repository.refreshRateManually()
        showToast("Live market rate refreshed")
    }

    fun acceptExpiredRate() {
        repository.acceptNewExpiredRate()
        closeBottomSheet()
        showToast("Updated to new market rate")
    }

    fun toggleOffline() {
        repository.toggleOfflineSimulation()
    }

    fun setActivityFilter(filter: ActivityFilter) {
        _activityFilter.value = filter
    }

    fun setActivitySearchQuery(query: String) {
        _activitySearchQuery.value = query
    }

    fun completeSetupTask(taskId: String) {
        repository.completeTask(taskId)
    }

    fun skipOnboarding() {
        viewModelScope.launch {
            _currentDestination.value = AppNavDestination.HOME
            showToast("You can complete setup later from Home or Profile")
        }
    }

    fun startAppTour() {
        repository.updateTourState { it.copy(isSkipped = false, currentStep = 0) }
        _currentDestination.value = AppNavDestination.HOME // Tour happens ON Home
    }

    fun nextTourStep() {
        val current = tourState.value.currentStep
        if (current < 4) {
            repository.updateTourState { it.copy(currentStep = current + 1) }
        } else {
            repository.updateTourState { it.copy(isCompleted = true) }
        }
    }

    fun skipTour() {
        repository.updateTourState { it.copy(isSkipped = true) }
    }

    fun runTaskAction(task: SetupTask) {
        when (task.id) {
            "task_secure" -> _currentDestination.value = AppNavDestination.CREATE_PIN
            "task_verify" -> showToast("Identity verification coming soon")
            "task_bank" -> openBottomSheet(ActiveBottomSheet.ADD_BANK)
            "task_wallet" -> selectBottomTab(BottomNavTab.WALLET)
            "task_receive" -> openReceive()
            "task_setup" -> selectBottomTab(BottomNavTab.SETUPS)
        }
    }

    fun onWelcomeGetStarted() {
        _currentDestination.value = AppNavDestination.HOME
    }

    fun dismissReceiveCoachMark() {
        _receiveCoachMarkSeen.value = true
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        showToast("Theme updated to ${mode.name.lowercase().capitalize()}")
    }

    fun addNewBankAccount(
        bankName: String,
        accountNumber: String,
        ifsc: String,
        holderName: String,
        type: BankAccount.BankType
    ) {
        val newAcc = repository.addBankAccount(bankName, accountNumber, ifsc, holderName, type)
        repository.selectBankAccount(newAcc)
        closeBottomSheet()
        showToast("Penny drop verified! Added ${newAcc.bankName}")
    }
}
