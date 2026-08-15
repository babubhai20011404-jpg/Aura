package com.example

import kotlin.OptIn
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.GlassSurface
import com.example.ui.components.sheets.AddBankSheet
import com.example.ui.components.sheets.BankSelectorSheet
import com.example.ui.components.sheets.BiometricAuthSheet
import com.example.ui.components.sheets.FeeDetailsSheet
import com.example.ui.components.sheets.RateDetailsSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReviewSaleScreen
import com.example.ui.screens.SaleCompletedScreen
import com.example.ui.screens.SaleFailedScreen
import com.example.ui.screens.SaleProcessingScreen
import com.example.ui.screens.SellUsdtScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.TransactionDetailScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.PinScreen
import com.example.ui.screens.ReceiveScreen
import com.example.ui.screens.WelcomeScreen
import com.example.ui.screens.SetupJourneyScreen
import com.example.ui.screens.AuraRewardsScreen
import com.example.ui.components.AppTourOverlay
import com.example.model.TaskStatus
import com.example.ui.theme.AuraEmerald
import com.example.ui.theme.AuraTextMuted
import com.example.ui.theme.AuraTheme
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.ActiveBottomSheet
import com.example.viewmodel.AppNavDestination
import com.example.viewmodel.AuraViewModel
import com.example.viewmodel.BottomNavTab

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AuraApp()
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AuraApp(viewModel: AuraViewModel = viewModel()) {
    val context = LocalContext.current

    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedBottomTab.collectAsStateWithLifecycle()
    val activeBottomSheet by viewModel.activeBottomSheet.collectAsStateWithLifecycle()

    val userAccount by viewModel.userAccount.collectAsStateWithLifecycle()
    val bankAccounts by viewModel.bankAccounts.collectAsStateWithLifecycle()
    val selectedBank by viewModel.selectedBank.collectAsStateWithLifecycle()
    val currentRate by viewModel.currentRate.collectAsStateWithLifecycle()
    val isRateUpdating by viewModel.isRateUpdating.collectAsStateWithLifecycle()
    val quoteTimerSeconds by viewModel.quoteTimerSeconds.collectAsStateWithLifecycle()
    val isOffline by viewModel.isOffline.collectAsStateWithLifecycle()
    
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val inputAmountString by viewModel.inputUsdtAmountString.collectAsStateWithLifecycle()
    val validationError by viewModel.amountValidationError.collectAsStateWithLifecycle()
    val currentQuote by viewModel.currentQuote.collectAsStateWithLifecycle()
    val activeTransaction by viewModel.activeTransaction.collectAsStateWithLifecycle()
    val isConfirmingSale by viewModel.isConfirmingSale.collectAsStateWithLifecycle()
    val isBiometricVisible by viewModel.isBiometricSheetVisible.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    val securityState by viewModel.securityState.collectAsStateWithLifecycle()
    val pinBuffer by viewModel.pinBuffer.collectAsStateWithLifecycle()

    val setupTasks by viewModel.setupTasks.collectAsStateWithLifecycle()
    val rewardEvents by viewModel.rewardEvents.collectAsStateWithLifecycle()
    val auraPoints by viewModel.auraPoints.collectAsStateWithLifecycle()
    val nextTask by viewModel.nextRecommendedTask.collectAsStateWithLifecycle()
    val tourState by viewModel.tourState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Handle Back Press
    BackHandler(enabled = currentDestination != AppNavDestination.HOME) {
        viewModel.navigateTo(AppNavDestination.HOME)
    }

    // Toast Handling
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Auth Check
    LaunchedEffect(securityState.isAuthenticated) {
        if (!securityState.isAuthenticated) {
            viewModel.navigateTo(AppNavDestination.LOGIN)
        } else if (currentDestination == AppNavDestination.LOGIN) {
            viewModel.navigateTo(AppNavDestination.WELCOME)
        }
    }

    // Auto-complete tasks based on navigation
    LaunchedEffect(selectedTab) {
        if (selectedTab == BottomNavTab.SETUPS) {
            viewModel.completeSetupTask("task_setup")
        }
        if (selectedTab == BottomNavTab.WALLET) {
            viewModel.completeSetupTask("task_wallet")
        }
    }

    LaunchedEffect(currentDestination) {
        if (currentDestination == AppNavDestination.RECEIVE) {
            viewModel.completeSetupTask("task_receive")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = com.example.ui.theme.AuraBackground,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (currentDestination == AppNavDestination.HOME) {
                    AuraBottomNavigationBar(
                        selectedTab = selectedTab,
                        onTabSelected = { viewModel.selectBottomTab(it) }
                    )
                }
            }
        ) { innerPadding ->
            SharedTransitionLayout {
                AnimatedContent(
                    targetState = currentDestination,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                    },
                    label = "main_nav_transition",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) { destination ->
                    when (destination) {
                        AppNavDestination.LOGIN -> LoginScreen(
                            onLogin = { email, pass -> viewModel.login(email, pass) }
                        )
                        AppNavDestination.PIN_LOCK -> PinScreen(
                            title = "Unlock Anteprox",
                            subtitle = "Enter your secure app PIN",
                            pinBuffer = pinBuffer,
                            isBiometricEnabled = userAccount.biometricEnabled,
                            onKeyPressed = { viewModel.onKeyPress(it) }
                        )
                        AppNavDestination.CREATE_PIN -> PinScreen(
                            title = "Create app PIN",
                            subtitle = "Set a 4-digit PIN for device security",
                            pinBuffer = pinBuffer,
                            onKeyPressed = { 
                                viewModel.onKeyPress(it)
                                if (pinBuffer.length == 4) viewModel.completeSetupTask("task_secure")
                            }
                        )
                        AppNavDestination.WELCOME -> WelcomeScreen(
                            onGetStarted = { viewModel.onWelcomeGetStarted() },
                            onSkip = { viewModel.skipOnboarding() }
                        )
                        AppNavDestination.SETUP_JOURNEY -> SetupJourneyScreen(
                            tasks = setupTasks,
                            onTaskClick = { viewModel.runTaskAction(it) },
                            onBackClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                        )
                        AppNavDestination.REWARDS -> AuraRewardsScreen(
                            totalPoints = auraPoints,
                            rewardEvents = rewardEvents,
                            onBackClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                        )
                        AppNavDestination.HOME -> {
                            when (selectedTab) {
                                BottomNavTab.HOME -> HomeScreen(
                                    userAccount = userAccount,
                                    currentRate = currentRate,
                                    quoteTimerSeconds = quoteTimerSeconds,
                                    isRateUpdating = isRateUpdating,
                                    recentTransactions = allTransactions,
                                    selectedBank = selectedBank,
                                    isOffline = isOffline,
                                    nextTask = nextTask,
                                    onSellUsdtClick = { viewModel.startSellFlow() },
                                    onReceiveClick = { viewModel.openReceive() },
                                    onRateDetailsClick = { viewModel.openBottomSheet(ActiveBottomSheet.RATE_DETAILS) },
                                    onTransactionClick = { viewModel.selectTransactionForDetail(it) },
                                    onViewAllActivityClick = { viewModel.selectBottomTab(BottomNavTab.ACTIVITY) },
                                    onManageBanksClick = { viewModel.openBottomSheet(ActiveBottomSheet.BANK_SELECTOR) },
                                    onSetupJourneyClick = { viewModel.navigateTo(AppNavDestination.SETUP_JOURNEY) },
                                    onRewardsClick = { viewModel.navigateTo(AppNavDestination.REWARDS) }
                                )
                                BottomNavTab.WALLET -> WalletScreen(
                                    userAccount = userAccount,
                                    bankAccounts = bankAccounts,
                                    currentRate = currentRate,
                                    onSellUsdtClick = { viewModel.startSellFlow() },
                                    onReceiveClick = { viewModel.openReceive(it) },
                                    onManageBanksClick = { viewModel.openBottomSheet(ActiveBottomSheet.BANK_SELECTOR) },
                                    onCopyAddress = { viewModel.showToast("Address copied: $it") }
                                )
                                BottomNavTab.ACTIVITY -> ActivityScreen(
                                    transactions = allTransactions,
                                    currentFilter = viewModel.activityFilter.collectAsStateWithLifecycle().value,
                                    searchQuery = viewModel.activitySearchQuery.collectAsStateWithLifecycle().value,
                                    onFilterChange = { viewModel.setActivityFilter(it) },
                                    onSearchChange = { viewModel.setActivitySearchQuery(it) },
                                    onTransactionClick = { viewModel.selectTransactionForDetail(it) },
                                    onStartSellClick = { viewModel.startSellFlow() }
                                )
                                BottomNavTab.PROFILE -> ProfileScreen(
                                    userAccount = userAccount,
                                    isOffline = isOffline,
                                    onToggleOffline = { viewModel.toggleOffline() },
                                    onOpenRewards = { viewModel.navigateTo(AppNavDestination.REWARDS) },
                                    onOpenSetupJourney = { viewModel.navigateTo(AppNavDestination.SETUP_JOURNEY) },
                                    onOpenSupport = { viewModel.openBottomSheet(ActiveBottomSheet.SUPPORT_MODAL) },
                                    onStartTour = { viewModel.startAppTour() }
                                )
                                else -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Setups Screen - Coming Soon", color = AuraTextMuted)
                                }
                            }
                        }
                        AppNavDestination.SELL_USDT -> SellUsdtScreen(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent,
                            userAccount = userAccount,
                            quote = currentQuote,
                            inputAmountString = inputAmountString,
                            selectedBank = selectedBank,
                            validationError = validationError,
                            isOffline = isOffline,
                            onKeyPressed = { viewModel.onKeyPress(it) },
                            onPercentClick = { viewModel.setPercentageAmount(it) },
                            onContinueClick = { viewModel.proceedToReview() },
                            onRateDetailsClick = { viewModel.openBottomSheet(ActiveBottomSheet.RATE_DETAILS) },
                            onFeeDetailsClick = { viewModel.openBottomSheet(ActiveBottomSheet.FEE_DETAILS) },
                            onBackClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                        )
                        AppNavDestination.REVIEW_SALE -> ReviewSaleScreen(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this@AnimatedContent,
                            quote = currentQuote,
                            selectedBank = selectedBank,
                            isConfirming = isConfirmingSale,
                            isOffline = isOffline,
                            onConfirmClick = { viewModel.triggerBiometricAuth() },
                            onEditClick = { viewModel.navigateTo(AppNavDestination.SELL_USDT) },
                            onBankChangeClick = { viewModel.openBottomSheet(ActiveBottomSheet.BANK_SELECTOR) },
                            onFeeInfoClick = { viewModel.openBottomSheet(ActiveBottomSheet.FEE_DETAILS) },
                            onBackClick = { viewModel.navigateTo(AppNavDestination.SELL_USDT) }
                        )
                        AppNavDestination.PROCESSING_SALE -> SaleProcessingScreen(
                            transaction = activeTransaction,
                            onReturnHomeClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                        )
                        AppNavDestination.COMPLETED_SALE -> SaleCompletedScreen(
                            transaction = activeTransaction,
                            onViewDetailClick = { viewModel.selectTransactionForDetail(it) },
                            onDoneClick = { viewModel.navigateTo(AppNavDestination.HOME) },
                            onCopyUtr = { viewModel.showToast("UTR copied: $it") }
                        )
                        AppNavDestination.FAILED_SALE -> SaleFailedScreen(
                            transaction = activeTransaction,
                            onTryAgainClick = { viewModel.navigateTo(AppNavDestination.SELL_USDT) },
                            onNeedHelpClick = { viewModel.openBottomSheet(ActiveBottomSheet.SUPPORT_MODAL) },
                            onReturnHomeClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                        )
                        AppNavDestination.TRANSACTION_DETAIL -> {
                            val selectedTxn = viewModel.selectedDetailTransaction.collectAsStateWithLifecycle().value
                            TransactionDetailScreen(
                                transaction = selectedTxn,
                                onCopyText = { viewModel.showToast("Copied to clipboard") },
                                onNeedHelpClick = { viewModel.openBottomSheet(ActiveBottomSheet.SUPPORT_MODAL) },
                                onBackClick = { viewModel.navigateTo(AppNavDestination.HOME) }
                            )
                        }
                        AppNavDestination.RECEIVE -> ReceiveScreen(
                            selectedAsset = viewModel.selectedDepositAsset.collectAsStateWithLifecycle().value,
                            selectedNetwork = viewModel.selectedDepositNetwork.collectAsStateWithLifecycle().value,
                            depositAddress = viewModel.depositAddress.collectAsStateWithLifecycle().value,
                            isLoading = viewModel.isAddressLoading.collectAsStateWithLifecycle().value,
                            onAssetSelect = { viewModel.selectDepositAsset(it) },
                            onNetworkSelect = { viewModel.selectDepositNetwork(it) },
                            onCopyAddress = { viewModel.showToast("Address copied: $it") },
                            onShareAddress = { viewModel.showToast("Sharing address: $it") },
                            onBackClick = { viewModel.navigateTo(AppNavDestination.HOME) },
                            networks = viewModel.getMockNetworks("USDT"),
                            showCoachMark = !viewModel.receiveCoachMarkSeen.collectAsStateWithLifecycle().value,
                            onDismissCoachMark = { viewModel.dismissReceiveCoachMark() }
                        )
                    }
                }
            }
        }

        AppTourOverlay(
            step = tourState.currentStep,
            onNext = { viewModel.nextTourStep() },
            onSkip = { viewModel.skipTour() },
            visible = !tourState.isCompleted && !tourState.isSkipped && currentDestination == AppNavDestination.HOME
        )
    }

    // Bottom Sheets System
    when (activeBottomSheet) {
        ActiveBottomSheet.RATE_DETAILS -> RateDetailsSheet(
            rate = currentRate,
            secondsRemaining = quoteTimerSeconds,
            onDismiss = { viewModel.closeBottomSheet() },
            onManualRefresh = { viewModel.refreshRateManually() }
        )
        ActiveBottomSheet.FEE_DETAILS -> FeeDetailsSheet(
            quote = currentQuote,
            onDismiss = { viewModel.closeBottomSheet() }
        )
        ActiveBottomSheet.BANK_SELECTOR -> BankSelectorSheet(
            bankAccounts = bankAccounts,
            selectedBank = selectedBank,
            onSelectBank = { viewModel.selectBank(it) },
            onAddNewBankClick = { viewModel.openBottomSheet(ActiveBottomSheet.ADD_BANK) },
            onDismiss = { viewModel.closeBottomSheet() }
        )
        ActiveBottomSheet.ADD_BANK -> AddBankSheet(
            onAddBank = { name, acc, ifsc, holder, type -> 
                viewModel.addNewBankAccount(name, acc, ifsc, holder, type)
            },
            onDismiss = { viewModel.closeBottomSheet() }
        )
        else -> {}
    }

    // Biometric Confirmation
    if (isBiometricVisible) {
        BiometricAuthSheet(
            usdtAmount = currentQuote.usdtAmount,
            netInr = currentQuote.netInr,
            onAuthenticated = { viewModel.confirmSaleWithBiometrics() },
            onDismiss = { viewModel.cancelBiometricAuth() }
        )
    }
}

@Composable
private fun AuraBottomNavigationBar(
    selectedTab: BottomNavTab,
    onTabSelected: (BottomNavTab) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        GlassSurface(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            color = Color.White.copy(alpha = 0.95f),
            shape = RoundedCornerShape(AuraTheme.Radius.pill),
            shadowElevation = 16.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(
                    icon = Icons.Default.Home,
                    label = "Home",
                    isSelected = selectedTab == BottomNavTab.HOME,
                    onClick = { onTabSelected(BottomNavTab.HOME) }
                )
                BottomNavItem(
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    label = "Setups",
                    isSelected = selectedTab == BottomNavTab.SETUPS,
                    onClick = { onTabSelected(BottomNavTab.SETUPS) }
                )
                BottomNavItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = "Wallet",
                    isSelected = selectedTab == BottomNavTab.WALLET,
                    onClick = { onTabSelected(BottomNavTab.WALLET) }
                )
                BottomNavItem(
                    icon = Icons.Default.History,
                    label = "Activity",
                    isSelected = selectedTab == BottomNavTab.ACTIVITY,
                    onClick = { onTabSelected(BottomNavTab.ACTIVITY) }
                )
                BottomNavItem(
                    icon = Icons.Default.Person,
                    label = "Profile",
                    isSelected = selectedTab == BottomNavTab.PROFILE,
                    onClick = { onTabSelected(BottomNavTab.PROFILE) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) AuraEmerald else AuraTextMuted,
        animationSpec = tween(300),
        label = "nav_item_color"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "nav_item_scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(8.dp)
            .scale(scale)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.sp
            ),
            color = contentColor
        )
    }
}
