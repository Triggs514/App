package com.example.model

import androidx.annotation.DrawableRes

enum class UserType(val label: String, val icon: String, val description: String = "") {
  COUPLE("Couple", "👫", "Couples seeking individuals or other couples"),
  SINGLE_FEMALE("Single Woman", "👩", "Solo female explorer"),
  SINGLE_MALE("Single Man", "👨", "Solo male explorer"),
  TRANS_FEMALE("Trans Woman", "⚧️", "Transgender woman explorer"),
  TRANS_MALE("Trans Man", "⚧️", "Transgender man explorer"),
  INTERSEX_HERMAPHRODITE("Hermaphrodite / Intersex", "⚧️", "Hermaphrodite & intersex singles / lovers"),
  NON_BINARY("Non-Binary / Queer", "✨", "Gender-expansive explorer"),
  AGENDER_ANDROGYNOUS("Agender / Androgynous", "💫", "Fluid or neutral gender identity"),
  TWO_SPIRIT("Two-Spirit", "🪶", "Two-spirit / indigenous queer"),
  THROUPLE_GROUP("Throuple / Polycule", "👥", "3+ partners exploring together")
}

enum class SexualOrientation(val label: String, val badge: String, val description: String) {
  BISEXUAL("Bisexual", "💖💜💙", "Attracted to two or more genders"),
  GAY("Gay", "🏳️‍🌈", "Men attracted to men"),
  LESBIAN("Lesbian", "🧡🤍💖", "Women attracted to women"),
  PANSEXUAL("Pansexual", "💗💛💙", "Attraction regardless of gender"),
  STRAIGHT("Straight", "👫", "Heterosexual attraction"),
  QUEER("Queer", "✨", "Queer / non-normative sexuality"),
  HETERO_FLEXIBLE("Heteroflexible", "⚡", "Predominantly straight with curiosity"),
  HOMO_FLEXIBLE("Homoflexible", "🌈", "Predominantly gay with exceptions"),
  ASEXUAL_KINK("Kinky Asexual", "🖤", "Asexual spectrum with kink / fetish interest")
}

enum class RelationshipStatus(val label: String, val icon: String, val description: String) {
  OPEN_MARRIAGE("Open Marriage", "💍", "Married couple with consensual open agreements"),
  OPEN_RELATIONSHIP("Open Relationship", "🔓", "Dating partners exploring ethical non-monogamy"),
  SINGLE("Single", "🖤", "Unattached solo explorer"),
  CHEATING_SECRET("Cheating / Discreet Affair", "🤫", "Discreet extramarital hookup looking for 100% confidentiality"),
  POLYAMOROUS("Polyamorous", "♾️", "Multiple consensual loving / sexual partnerships"),
  SWINGER_COUPLE("Swinger Couple", "🥂", "Couples lifestyle swaps, play parties & soft/hard swaps"),
  CASUAL_FWB("Casual / FWB", "⚡", "Friends with benefits, no emotional commitment"),
  MONOGAMISH("Monogamish", "✨", "Mostly monogamous with selective spicy adventures"),
  SEPARATED_DIVORCED("Separated / Divorced", "🕊️", "Recently single, rediscovering desires"),
  COMPLICATED_OTHER("It's Complicated / Other", "🌀", "Custom relationship arrangement")
}

enum class CockEndowment(val label: String, val icon: String, val badgeText: String) {
  BIG_COCKS_ONLY("Big Cocks Only (8\"+ / Hung VIP)", "🍆", "8\"+ HUNG VIP"),
  VERY_WELL_ENDOWED("Hung (7\" - 8\")", "🔥", "7\"-8\" HUNG"),
  AVERAGE_PLUS("Average / Versatile (5.5\" - 6.5\")", "✨", "VERSATILE"),
  NOT_APPLICABLE("Not Applicable / Female / Trans", "💫", "ALL SIZES")
}

enum class DynamicKink(
  val displayName: String,
  val badgeColorHex: Long,
  val isGroupActivity: Boolean = false
) {
  THREESOME_MFF("Threesome (MFF)", 0xFFFF2A85, isGroupActivity = true),
  THREESOME_MMF("Threesome (MMF)", 0xFF9D4EDD, isGroupActivity = true),
  FOURSOME("Foursomes & Couples Swap", 0xFF00F0FF, isGroupActivity = true),
  SPIT_ROASTING("Spit Roasting", 0xFFFF5400, isGroupActivity = true),
  ORGIES("Orgies & Play Parties", 0xFFFF0054, isGroupActivity = true),
  GANG_BANGING("Gang Banging", 0xFF7209B7, isGroupActivity = true),
  GOTH_EMO("Date a Goth / Emo", 0xFF4361EE, isGroupActivity = false),
  BDSM_KINK("BDSM & Dominance/Submission", 0xFFE01E78, isGroupActivity = false),
  OPEN_POLY("Open & Polyamory", 0xFF4CC9F0, isGroupActivity = false),
  ALT_SUBCULTURE("Alt, Punk & Pierced", 0xFF70E000, isGroupActivity = false),
  BIG_COCKS_FOCUS("Big Cocks & Monster Size", 0xFFE01E78, isGroupActivity = false),
  HERMAPHRODITE_INTERSEX_DATING("Hermaphrodite & Intersex Play", 0xFFFF007F, isGroupActivity = false),
  BISEXUAL_PLAY("Bisexual & Bi-Curious Play", 0xFF9D4EDD, isGroupActivity = false),
  GAY_CRUISE("Gay & Men-Only Encounters", 0xFF3F37C9, isGroupActivity = false),
  LESBIAN_TRIB("Lesbian & Sapphic Fantasies", 0xFFFF5400, isGroupActivity = false),
  OPEN_MARRIAGE_SWAP("Open Marriage & Wife Sharing", 0xFFFFD166, isGroupActivity = false),
  DISCREET_CHEATING("Discreet Affairs & Taboo Hookups", 0xFF00F0FF, isGroupActivity = false),
  CUCKOLD_HOTWIFE("Cuckold, Bull & Hotwife", 0xFF70E000, isGroupActivity = false),
  PEGGING_PROSTATE("Pegging & Strap-on Play", 0xFFFF2A85, isGroupActivity = false),
  FOOT_FETISH("Foot Fetish, Worship & Nylon", 0xFF4CC9F0, isGroupActivity = false),
  LATEX_LEATHER("Latex, Leather & Rubber", 0xFF7209B7, isGroupActivity = false),
  EXHIBITION_VOYEUR("Exhibitionism & Voyeurism", 0xFFFF0054, isGroupActivity = false),
  CHASTITY_EDGING("Chastity, Edging & Denial", 0xFF4361EE, isGroupActivity = false),
  ROLEPLAY_FANTASY("Taboo Roleplay & Fantasies", 0xFFFFB703, isGroupActivity = false),
  CREAMPIE("Creampie & Internal Climax", 0xFFFF007F, isGroupActivity = false),
  BREEDING("Breeding & Impregnation Fantasies", 0xFFFF2A85, isGroupActivity = false),
  RAW_UNPROTECTED_FANTASY("Bareback & Raw Desires", 0xFFFF5400, isGroupActivity = false),
  CUM_TRIBUTE_FACIAL("Facials & Cum Tributes", 0xFF00F0FF, isGroupActivity = false),
  DEEPTHROAT_ORAL("Deepthroat & Oral Fixation", 0xFF7209B7, isGroupActivity = false),
  ALL_TYPES("All Other Types", 0xFFFFD166, isGroupActivity = false)
}

enum class SubcultureType(val label: String, val emoji: String) {
  GOTH("Goth", "🖤"),
  EMO("Emo", "🥀"),
  ALTERNATIVE("Alternative / Punk", "⚡"),
  VAMPIRE("Vampire / Dark Aesthetic", "🩸"),
  CYBERPUNK("Cyberpunk & Rave", "👾"),
  SENSUAL_SWINGER("Sensual Swinger", "🥂"),
  KINKSTER("Kinkster & Leather", "⛓️")
}

enum class RequestStatus {
  PENDING,
  ACCEPTED,
  DECLINED
}

data class DirectSexRequest(
  val id: String,
  val fromUserId: String,
  val fromUserName: String,
  val toUserId: String,
  val toUserName: String,
  val category: DynamicKink,
  val proposalTitle: String,
  val description: String,
  val experienceLevel: String,
  val preferredVenue: String,
  val timeAgo: String,
  val status: RequestStatus = RequestStatus.PENDING,
  val isExplicit18Plus: Boolean = true
)

data class VaultMediaItem(
  val id: String,
  val title: String,
  val description: String,
  val mediaCategory: String, // e.g. "Couple Intimate Reel", "Goth Bedroom Shoot", "Party Play Clip"
  val isLocked: Boolean,
  val durationOrCount: String,
  @DrawableRes val previewRes: Int? = null,
  val blurCoverGradient: Pair<Long, Long>
)

data class PhotoItem(
  val id: String,
  val caption: String = "",
  val isMain: Boolean = false,
  @DrawableRes val drawableRes: Int? = null,
  val localUri: String? = null
)

data class UserProfile(
  val id: String,
  val email: String = "",
  val name: String,
  val age: String,
  val userType: UserType,
  val sexualOrientation: SexualOrientation = SexualOrientation.BISEXUAL,
  val relationshipStatus: RelationshipStatus = RelationshipStatus.OPEN_RELATIONSHIP,
  val cockEndowment: CockEndowment = CockEndowment.NOT_APPLICABLE,
  val isHungVerified: Boolean = false,
  val hungSizeInches: String = "",
  val isDiscreetAffair: Boolean = false,
  val discreetAlias: String = "",
  val fantasiesAndFetishes: List<String> = emptyList(),
  val tagline: String,
  val bio: String,
  val distanceMiles: Int,
  @DrawableRes val mainPhotoRes: Int? = null,
  val photos: List<PhotoItem> = emptyList(),
  val gradientColors: Pair<Long, Long>,
  val desireTags: List<DynamicKink>,
  val preferredGroupActivities: List<DynamicKink> = emptyList(),
  val targetGendersLookingFor: List<UserType> = emptyList(),
  val lookingFor: String,
  val boundaries: String,
  val subculture: String,
  val subcultureTags: List<SubcultureType> = emptyList(),
  val verified: Boolean = true,
  val has18PlusVault: Boolean = true,
  val vaultItems: List<VaultMediaItem> = emptyList(),
  val isLifetimeVip: Boolean = false
)

data class GroupEvent(
  val id: String,
  val title: String,
  val hostName: String,
  val kinkType: DynamicKink,
  val dateText: String,
  val locationArea: String,
  val attendeesCount: Int,
  val maxAttendees: Int,
  val description: String,
  val isVipOnly: Boolean
)

data class SexWorkerAd(
  val id: String,
  val providerName: String,
  val age: String,
  val gender: UserType,
  val sexualOrientation: SexualOrientation = SexualOrientation.BISEXUAL,
  val tagline: String,
  val bio: String,
  val hourlyRate: String,
  val twoHoursRate: String,
  val overnightRate: String,
  val quickVisitRate: String,
  val servicesOffered: List<String>,
  val location: String,
  val contactMethod: String,
  val isVerifiedProvider: Boolean = true,
  @DrawableRes val photoRes: Int? = null,
  val photoGradient: Pair<Long, Long> = Pair(0xFFFF2A85, 0xFF7B2CBF),
  val reviewsCount: Int = 24,
  val rating: Double = 4.9,
  val postedTimeAgo: String = "Just now",
  val outcallAvailable: Boolean = true,
  val incallAvailable: Boolean = true,
  val isDiscreet: Boolean = true
)

enum class PlanType(
  val title: String,
  val priceText: String,
  val billingPeriod: String,
  val saveLabel: String?,
  val description: String
) {
  FREE(
    "Free Explorer",
    "$0",
    "Forever",
    null,
    "Basic profile browsing and public feed access."
  ),
  MONTHLY(
    "Standard Monthly",
    "$15",
    "/month",
    null,
    "Full messaging, matches, party invites, and discovery algorithm."
  ),
  YEARLY(
    "Standard Annual",
    "$45",
    "/year",
    "Save 75%",
    "Best value. $3.75/mo equivalent. Full standard access for 1 full year."
  ),
  LIFETIME_VIP(
    "Founder VIP Lifetime",
    "$0",
    "Lifetime Free",
    "FOUNDER VIP",
    "Permanent zero-cost access to all standard, sex requests, and 18+ vault features."
  )
}

val LIFETIME_VIP_EMAILS = setOf(
  "triggz2905@gmail.com",
  "laurencartier6@gmail.com",
  "ilovelcartier11152023@gmail.com"
)

data class MembershipState(
  val userEmail: String = "member@clubdesire.com",
  val plan: PlanType = PlanType.FREE,
  val hasPremium18PlusUpgrade: Boolean = false, // $15 Upgrade for sex requests and 18+ content
  val isSubscribed: Boolean = false,
  val isLifetimeVip: Boolean = false
) {
  val canSendSexRequests: Boolean
    get() = isLifetimeVip || hasPremium18PlusUpgrade

  val canView18PlusContent: Boolean
    get() = isLifetimeVip || hasPremium18PlusUpgrade

  // All registered adult members enjoy 100% unlimited member chat
  val canUnlimitedChat: Boolean
    get() = true

  val isEffectivelyVip: Boolean
    get() = isLifetimeVip || (plan != PlanType.FREE && hasPremium18PlusUpgrade)
}

// Matching Algorithm Models
data class MatchPreferences(
  val targetGenders: Set<UserType> = UserType.values().toSet(),
  val targetDynamics: Set<DynamicKink> = emptySet(),
  val targetSubcultures: Set<SubcultureType> = emptySet(),
  val targetOrientations: Set<SexualOrientation> = emptySet(),
  val targetRelationshipStatuses: Set<RelationshipStatus> = emptySet(),
  val onlyBigCocks: Boolean = false,
  val onlyDiscreet: Boolean = false,
  val onlyRealVerifiedMembers: Boolean = true, // Filter out fake profiles & bots
  val maxDistanceMiles: Int = 60,
  val minAge: Int = 18,
  val maxAge: Int = 40, // Strict 18 to 40 age limits
  val onlyWithVault18Plus: Boolean = false
)

data class MatchScoreResult(
  val profile: UserProfile,
  val compatibilityPercentage: Int,
  val matchedGenders: Boolean,
  val sharedKinks: List<DynamicKink>,
  val sharedSubcultures: List<SubcultureType>,
  val reasonHighlights: List<String>
)

data class ChatMessage(
  val id: String,
  val senderName: String,
  val text: String,
  val time: String,
  val isFromMe: Boolean,
  val attachedSexRequest: DirectSexRequest? = null
)

data class ChatConversation(
  val id: String,
  val targetProfileId: String,
  val targetName: String,
  val targetType: UserType,
  @DrawableRes val targetPhotoRes: Int? = null,
  val lastMessage: String,
  val lastMessageTime: String,
  val unreadCount: Int = 0,
  val messages: List<ChatMessage> = emptyList()
)

// --- PAYMENT PORTAL MODELS ---
enum class PaymentMethodType(
  val displayName: String,
  val iconEmoji: String,
  val description: String
) {
  STRIPE_CARD("Stripe Credit / Debit Card", "💳", "Visa, Mastercard, Amex, Discover (Stripe Secure)"),
  PAYPAL("PayPal", "🅿️", "One-touch PayPal checkout & Buyer Protection"),
  MOBILE_WALLET("Mobile Pay (Apple / Google / Cash App)", "📱", "Instant biometric mobile authorization"),
  E_TRANSFER("Interac e-Transfer / Bank Wire", "🏦", "Direct bank wire & instant auto-deposit e-transfer")
}

data class PaymentReceipt(
  val transactionId: String,
  val amount: String,
  val paymentMethod: PaymentMethodType,
  val dateText: String,
  val planPurchased: String,
  val isVipWaiver: Boolean = false
)

// --- AI GUARDIAN & BOT / SCAMMER DEFENSE MODELS ---
data class BotScanStatus(
  val safeScore: Int = 99,
  val isBotDetected: Boolean = false,
  val scanSummary: String = "AI Guardian Active • 0 Scammers Detected",
  val scannedProfilesCount: Int = 142
)

data class IdVerificationState(
  val isVerified: Boolean = false,
  val idType: String = "Passport / Government Photo ID",
  val verifiedAt: String? = null,
  val faceMatchConfidence: Int = 98,
  val isPendingVerification: Boolean = false
)

data class AiGuardianMessage(
  val id: String,
  val text: String,
  val isAi: Boolean,
  val time: String,
  val suggestedActions: List<String> = emptyList()
)

// --- AI CREATIVE TOOLS (PORNO MAKER, FACE SWAP, IMAGE EDITOR) ---
enum class AiToolCategory(val label: String, val icon: String, val subtitle: String) {
  PORNO_SCENE_MAKER("Adult Scene & Fantasy Maker", "🔞", "Generate custom intimate couple, threesome, and goth fantasy scenes"),
  SWAP_FACES("AI Face Swap Studio", "🎭", "Swap your face or persona into luxury playrooms and alter-ego templates"),
  IMAGE_EDITOR("AI Glam & Privacy Editor", "✨", "Enhance lighting, add mood filters, and apply discreet privacy blur masks"),
  GENERATOR_GALLERY("My AI Creations", "🖼️", "All generated high-res scenes with unlimited downloads")
}

data class AiGeneratedMedia(
  val id: String,
  val title: String,
  val prompt: String,
  val stylePreset: String,
  val toolCategory: AiToolCategory,
  val dateText: String,
  @DrawableRes val imageRes: Int? = null,
  val gradientColors: Pair<Long, Long>,
  val isAdult18Plus: Boolean = true
)

data class FaceSwapTemplate(
  val id: String,
  val title: String,
  val category: String,
  @DrawableRes val previewRes: Int? = null,
  val gradientColors: Pair<Long, Long>,
  val description: String
)

