package com.example.data

import com.example.model.BankAccount
import com.example.model.Quote
import com.example.model.RewardEvent
import com.example.model.SetupTask
import com.example.model.TaskStatus
import com.example.model.TourState
import com.example.model.Transaction
import com.example.model.TransactionStatus
import com.example.model.UserAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class AuraRepository(private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {

    private val _userAccount = MutableStateFlow(
        UserAccount(
            name = "Aarav Sharma",
            usdtBalance = 12450.00,
            inrBalance = 34500.00
        )
    )
    val userAccount: StateFlow<UserAccount> = _userAccount.asStateFlow()

    private val _bankAccounts = MutableStateFlow<List<BankAccount>>(
        listOf(
            BankAccount(
                id = "bank_hdfc_01",
                bankName = "HDFC Bank",
                accountNumberMasked = "•••• 4821",
                rawAccountNumber = "50100482914821",
                ifsc = "HDFC0001234",
                accountHolder = "AARAV SHARMA",
                isPrimary = true,
                isPennyDropVerified = true,
                bankLogoType = BankAccount.BankType.HDFC
            ),
            BankAccount(
                id = "bank_icici_02",
                bankName = "ICICI Bank",
                accountNumberMasked = "•••• 9032",
                rawAccountNumber = "002401569032",
                ifsc = "ICIC0000024",
                accountHolder = "AARAV SHARMA",
                isPrimary = false,
                isPennyDropVerified = true,
                bankLogoType = BankAccount.BankType.ICICI
            ),
            BankAccount(
                id = "bank_kotak_03",
                bankName = "Kotak Mahindra Bank",
                accountNumberMasked = "•••• 1156",
                rawAccountNumber = "771122331156",
                ifsc = "KKBK0000958",
                accountHolder = "AARAV SHARMA",
                isPrimary = false,
                isPennyDropVerified = true,
                bankLogoType = BankAccount.BankType.KOTAK
            )
        )
    )
    val bankAccounts: StateFlow<List<BankAccount>> = _bankAccounts.asStateFlow()

    private val _selectedBank = MutableStateFlow(_bankAccounts.value.first())
    val selectedBank: StateFlow<BankAccount> = _selectedBank.asStateFlow()

    // Base market rate (fluctuates around ₹84.72)
    private var baseMarketRate = 84.72
    private val _currentRate = MutableStateFlow(baseMarketRate)
    val currentRate: StateFlow<Double> = _currentRate.asStateFlow()

    private val _isRateUpdating = MutableStateFlow(false)
    val isRateUpdating: StateFlow<Boolean> = _isRateUpdating.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    private val _quoteTimerSeconds = MutableStateFlow(30)
    val quoteTimerSeconds: StateFlow<Int> = _quoteTimerSeconds.asStateFlow()

    private val _isQuoteExpired = MutableStateFlow(false)
    val isQuoteExpired: StateFlow<Boolean> = _isQuoteExpired.asStateFlow()

    private val _previousExpiredRate = MutableStateFlow<Double?>(null)
    val previousExpiredRate: StateFlow<Double?> = _previousExpiredRate.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(
        listOf(
            Transaction(
                id = "TXN_7829104",
                usdtAmount = 1000.0,
                ratePerUsdt = 84.70,
                grossInr = 84700.0,
                feeInr = 127.05,
                netInr = 84572.95,
                settlementBankName = "HDFC Bank",
                settlementAccountMasked = "•••• 4821",
                ifsc = "HDFC0001234",
                status = TransactionStatus.COMPLETED,
                createdAtMillis = System.currentTimeMillis() - 2 * 60 * 60 * 1000L, // 2h ago
                completedAtMillis = System.currentTimeMillis() - 2 * 60 * 60 * 1000L + 12 * 60 * 1000L,
                utrNumber = "UTR-HDFC-9928174628",
                txHash = "0x8fa928...4b82c1",
                currentStepIndex = 3
            ),
            Transaction(
                id = "TXN_6639102",
                usdtAmount = 500.0,
                ratePerUsdt = 84.65,
                grossInr = 42325.0,
                feeInr = 63.48,
                netInr = 42261.52,
                settlementBankName = "HDFC Bank",
                settlementAccountMasked = "•••• 4821",
                ifsc = "HDFC0001234",
                status = TransactionStatus.COMPLETED,
                createdAtMillis = System.currentTimeMillis() - 28 * 60 * 60 * 1000L, // Yesterday
                completedAtMillis = System.currentTimeMillis() - 28 * 60 * 60 * 1000L + 15 * 60 * 1000L,
                utrNumber = "UTR-HDFC-4491028371",
                txHash = "0x3bc194...12d77a",
                currentStepIndex = 3
            ),
            Transaction(
                id = "TXN_5129481",
                usdtAmount = 220.0,
                ratePerUsdt = 84.58,
                grossInr = 18607.60,
                feeInr = 27.91,
                netInr = 18579.69,
                settlementBankName = "ICICI Bank",
                settlementAccountMasked = "•••• 9032",
                ifsc = "ICIC0000024",
                status = TransactionStatus.COMPLETED,
                createdAtMillis = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L, // 3 days ago
                completedAtMillis = System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000L + 8 * 60 * 1000L,
                utrNumber = "UTR-ICICI-8819203941",
                txHash = "0xaa9823...8841fa",
                currentStepIndex = 3
            )
        )
    )
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    // Onboarding & Rewards State
    private val _setupTasks = MutableStateFlow<List<SetupTask>>(
        listOf(
            SetupTask("task_account", "Account created", "Your Aura account is ready.", TaskStatus.COMPLETED, 100, "Done"),
            SetupTask("task_secure", "Protect your account", "Create your Aura PIN and enable biometric protection.", TaskStatus.INCOMPLETE, 150, "Secure account"),
            SetupTask("task_verify", "Complete verification", "Finish the required account verification steps.", TaskStatus.INCOMPLETE, 300, "Verify identity"),
            SetupTask("task_bank", "Add a settlement account", "Add and verify the bank account where your INR settlements can be received.", TaskStatus.INCOMPLETE, 300, "Add bank account"),
            SetupTask("task_wallet", "Create your wallet", "Get your receiving address ready for supported crypto assets.", TaskStatus.INCOMPLETE, 250, "Open wallet"),
            SetupTask("task_receive", "Receive crypto", "Learn how Aura deposit addresses and network selection work.", TaskStatus.INCOMPLETE, 100, "Open receive"),
            SetupTask("task_setup", "Explore an A+ setup", "Learn how Aura explains an A+ market setup.", TaskStatus.INCOMPLETE, 100, "Explore setups")
        )
    )
    val setupTasks: StateFlow<List<SetupTask>> = _setupTasks.asStateFlow()

    private val _rewardEvents = MutableStateFlow<List<RewardEvent>>(
        listOf(
            RewardEvent("rev_01", "task_account", "Welcome Reward", 100)
        )
    )
    val rewardEvents: StateFlow<List<RewardEvent>> = _rewardEvents.asStateFlow()

    private val _tourState = MutableStateFlow(TourState())
    val tourState: StateFlow<TourState> = _tourState.asStateFlow()

    init {
        startQuoteTicker()
    }

    private fun startQuoteTicker() {
        scope.launch {
            while (true) {
                delay(1000)
                if (_isOffline.value) continue

                val currentSec = _quoteTimerSeconds.value
                if (currentSec > 1) {
                    _quoteTimerSeconds.value = currentSec - 1
                } else {
                    // Timer reached 0 -> rate updates!
                    refreshLiveRateInternal()
                    _quoteTimerSeconds.value = 30
                }
            }
        }
    }

    fun selectBankAccount(bank: BankAccount) {
        _selectedBank.value = bank
    }

    fun toggleOfflineSimulation() {
        _isOffline.update { !it }
    }

    fun setOffline(isOffline: Boolean) {
        _isOffline.value = isOffline
    }

    fun refreshRateManually() {
        scope.launch {
            _isRateUpdating.value = true
            delay(400)
            refreshLiveRateInternal()
            _quoteTimerSeconds.value = 30
            _isQuoteExpired.value = false
            _isRateUpdating.value = false
        }
    }

    fun acceptNewExpiredRate() {
        _isQuoteExpired.value = false
        _previousExpiredRate.value = null
        _quoteTimerSeconds.value = 30
    }

    private fun refreshLiveRateInternal() {
        val oldRate = _currentRate.value
        // Small realistic micro fluctuation (+/- 0.08)
        val delta = (Random.nextDouble() - 0.48) * 0.08
        val newRate = ((baseMarketRate + delta) * 100.0).toLong() / 100.0
        
        if (Math.abs(newRate - oldRate) > 0.02) {
            _previousExpiredRate.value = oldRate
        }
        _currentRate.value = newRate
    }

    fun createSellTransaction(
        usdtAmount: Double,
        rate: Double,
        fee: Double,
        gross: Double,
        net: Double,
        bank: BankAccount
    ): Transaction {
        val txnId = "TXN_" + Random.nextInt(1000000, 9999999)
        val txn = Transaction(
            id = txnId,
            usdtAmount = usdtAmount,
            ratePerUsdt = rate,
            feeInr = fee,
            grossInr = gross,
            netInr = net,
            settlementBankName = bank.bankName,
            settlementAccountMasked = bank.accountNumberMasked,
            ifsc = bank.ifsc,
            status = TransactionStatus.SUBMITTED,
            createdAtMillis = System.currentTimeMillis(),
            network = "TRON (TRC-20)",
            currentStepIndex = 1
        )

        // Deduct USDT from balance immediately
        _userAccount.update { current ->
            current.copy(usdtBalance = (current.usdtBalance - usdtAmount).coerceAtLeast(0.0))
        }

        // Add to transactions list at top
        _transactions.update { list ->
            listOf(txn) + list
        }

        return txn
    }

    fun advanceTransactionToProcessing(txnId: String) {
        _transactions.update { list ->
            list.map { item ->
                if (item.id == txnId) {
                    item.copy(
                        status = TransactionStatus.PROCESSING,
                        currentStepIndex = 2
                    )
                } else item
            }
        }
    }

    fun completeTransaction(txnId: String) {
        val utr = "UTR-${_selectedBank.value.bankLogoType.name}-${Random.nextLong(1000000000L, 9999999999L)}"
        val txHash = "0x" + UUID.randomUUID().toString().replace("-", "").take(16)
        
        var netInrAdded = 0.0
        _transactions.update { list ->
            list.map { item ->
                if (item.id == txnId) {
                    netInrAdded = item.netInr
                    item.copy(
                        status = TransactionStatus.COMPLETED,
                        completedAtMillis = System.currentTimeMillis(),
                        utrNumber = utr,
                        txHash = txHash,
                        currentStepIndex = 3
                    )
                } else item
            }
        }

        // Add INR to user's settled balance
        _userAccount.update { current ->
            current.copy(
                inrBalance = current.inrBalance + netInrAdded,
                dailyUsedInr = current.dailyUsedInr + netInrAdded
            )
        }
    }

    fun failTransaction(txnId: String, reason: String) {
        var refundUsdt = 0.0
        _transactions.update { list ->
            list.map { item ->
                if (item.id == txnId) {
                    refundUsdt = item.usdtAmount
                    item.copy(
                        status = TransactionStatus.FAILED,
                        failureReason = reason
                    )
                } else item
            }
        }

        // Refund USDT balance
        if (refundUsdt > 0) {
            _userAccount.update { current ->
                current.copy(usdtBalance = current.usdtBalance + refundUsdt)
            }
        }
    }

    fun addBankAccount(
        bankName: String,
        accountNumber: String,
        ifsc: String,
        holderName: String,
        type: BankAccount.BankType
    ): BankAccount {
        val masked = "•••• " + accountNumber.takeLast(4)
        val newAccount = BankAccount(
            id = "bank_" + UUID.randomUUID().toString().take(8),
            bankName = bankName,
            accountNumberMasked = masked,
            rawAccountNumber = accountNumber,
            ifsc = ifsc.uppercase(),
            accountHolder = holderName.uppercase(),
            isPrimary = false,
            isPennyDropVerified = true,
            bankLogoType = type
        )
        _bankAccounts.update { it + newAccount }
        
        completeTask("task_bank")
        
        return newAccount
    }

    fun completeTask(taskId: String) {
        val task = _setupTasks.value.find { it.id == taskId } ?: return
        if (task.status == TaskStatus.COMPLETED) return

        _setupTasks.update { tasks ->
            tasks.map {
                if (it.id == taskId) it.copy(status = TaskStatus.COMPLETED) else it
            }
        }

        // Award Reward
        val reward = RewardEvent(
            id = "rev_" + UUID.randomUUID().toString().take(8),
            taskId = taskId,
            title = task.title,
            amount = task.rewardAmount
        )
        _rewardEvents.update { it + reward }
    }

    fun updateTourState(update: (TourState) -> TourState) {
        _tourState.update(update)
    }
}
