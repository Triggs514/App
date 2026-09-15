package com.example.util

import com.example.data.SampleDataRepository
import com.example.model.MembershipState
import com.example.model.PlanType
import kotlinx.coroutines.flow.StateFlow

/**
 * SubscriptionManager handles the logic for verifying user membership tiers
 * and enforcing access restrictions to premium features like sex requests,
 * 18+ vault content, and VIP events.
 */
object SubscriptionManager {

    private val membershipState: StateFlow<MembershipState> = SampleDataRepository.membershipState

    /**
     * Checks if the current user has permission to view 18+ explicit content.
     * This includes private vaults and explicit profiles.
     */
    fun canView18PlusContent(): Boolean {
        val state = membershipState.value
        return state.canView18PlusContent
    }

    /**
     * Checks if the current user can send direct sex requests.
     * This feature is restricted to Standard + Premium Upgrade or VIP members.
     */
    fun canSendSexRequests(): Boolean {
        val state = membershipState.value
        return state.canSendSexRequests
    }

    /**
     * Checks if the user can access VIP-only events or clubs.
     */
    fun canAccessVipFeatures(): Boolean {
        val state = membershipState.value
        return state.isEffectivelyVip
    }

    /**
     * Returns a human-readable reason why a feature is restricted.
     */
    fun getRestrictionReason(feature: PremiumFeature): String {
        val state = membershipState.value
        return when (feature) {
            PremiumFeature.SEX_REQUESTS -> {
                if (state.plan == PlanType.FREE) "Upgrade to Standard + Premium required for Direct Sex Requests."
                else "Premium 18+ Add-on required for Direct Sex Requests."
            }
            PremiumFeature.VAULT_18_PLUS -> {
                if (state.plan == PlanType.FREE) "Standard Membership required to unlock 18+ Vaults."
                else "Premium 18+ Add-on required for uncensored media."
            }
            PremiumFeature.VIP_EVENTS -> "Founder VIP or Standard + Premium required for VIP Events."
        }
    }
}

enum class PremiumFeature {
    SEX_REQUESTS,
    VAULT_18_PLUS,
    VIP_EVENTS
}
