package com.example.data

import com.example.R
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.UUID

object SampleDataRepository {

  private val auth: FirebaseAuth? by lazy {
    try {
      FirebaseAuth.getInstance()
    } catch (e: Exception) {
      null
    }
  }

  private val _firebaseUser = MutableStateFlow<FirebaseUser?>(null)
  val firebaseUser: StateFlow<FirebaseUser?> = _firebaseUser.asStateFlow()

  init {
    try {
      auth?.let { authInstance ->
        _firebaseUser.value = authInstance.currentUser
        authInstance.addAuthStateListener {
          _firebaseUser.value = it.currentUser
          // Sync membership state with firebase user if needed
          if (it.currentUser != null) {
            _membershipState.update { state ->
              state.copy(userEmail = it.currentUser?.email ?: state.userEmail)
            }
          }
        }
      }
    } catch (e: Exception) {
      // Firebase might not be initialized yet, this is okay as it will be handled
      // when the auth listener is added or when it's accessed later.
    }
  }

  private val _membershipState = MutableStateFlow(
    MembershipState(
      userEmail = "ilovelcartier11152023@gmail.com",
      plan = PlanType.MONTHLY,
      hasPremium18PlusUpgrade = true,
      isSubscribed = true,
      isLifetimeVip = false
    )
  )
  val membershipState: StateFlow<MembershipState> = _membershipState.asStateFlow()

  private val _currentUserProfile = MutableStateFlow(
    UserProfile(
      id = "user_me",
      email = "ilovelcartier11152023@gmail.com",
      name = "Jordan & Taylor",
      age = "28 & 30",
      userType = UserType.COUPLE,
      sexualOrientation = SexualOrientation.BISEXUAL,
      relationshipStatus = RelationshipStatus.OPEN_RELATIONSHIP,
      cockEndowment = CockEndowment.VERY_WELL_ENDOWED,
      isHungVerified = false,
      hungSizeInches = "7.5\"",
      isDiscreetAffair = false,
      discreetAlias = "",
      fantasiesAndFetishes = listOf(
        "Threesomes (MFF & MMF)",
        "Spit Roasting",
        "Couples Swap & Foursomes",
        "Goth & Emo Dating",
        "Big Cocks & Well Endowed",
        "Hermaphrodite & Intersex Play"
      ),
      tagline = "Open-minded couple exploring new connections & fantasies",
      bio = "We are an easygoing, communicative couple in the city looking to meet fun singles (girls, guys, and trans) and other couples. Open to threesomes, foursomes, orgies, spit roasting, gang banging, and alt/goth dates.",
      distanceMiles = 0,
      mainPhotoRes = R.drawable.couple_profile_hero,
      photos = listOf(
        PhotoItem(id = "p_1", caption = "Us at Downtown Penthouse", isMain = true, drawableRes = R.drawable.couple_profile_hero),
        PhotoItem(id = "p_2", caption = "Alt Club Night", isMain = false, drawableRes = R.drawable.goth_profile_hero),
        PhotoItem(id = "p_3", caption = "VIP Playroom Lounge", isMain = false, drawableRes = R.drawable.vip_vault_banner)
      ),
      lifestylePhotos = listOf(
        PhotoItem("lp_me_1", "Our downtown balcony view", false, R.drawable.couple_profile_hero),
        PhotoItem("lp_me_2", "Goth aesthetic prep", false, R.drawable.goth_profile_hero),
        PhotoItem("lp_me_3", "VIP night out in the city", false, R.drawable.vip_vault_banner)
      ),
      gradientColors = Pair(0xFFFF2A85, 0xFF9D4EDD),
      desireTags = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.THREESOME_MMF,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING,
        DynamicKink.ORGIES,
        DynamicKink.GANG_BANGING,
        DynamicKink.GOTH_EMO,
        DynamicKink.OPEN_POLY
      ),
      preferredGroupActivities = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING,
        DynamicKink.ORGIES
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_FEMALE,
        UserType.SINGLE_MALE,
        UserType.TRANS_FEMALE,
        UserType.TRANS_MALE
      ),
      lookingFor = "Girls, Guys & Trans singles or couples for threesomes, spit roasting, creampie and breeding fantasies",
      boundaries = "Safe, consensual, full communication beforehand. Mutual chemistry first.",
      subculture = "Goth, Alt & Sensual Swinger",
      subcultureTags = listOf(
        SubcultureType.GOTH,
        SubcultureType.ALTERNATIVE,
        SubcultureType.SENSUAL_SWINGER
      )
    )
  )
  val currentUserProfile: StateFlow<UserProfile> = _currentUserProfile.asStateFlow()

  private val _matchPreferences = MutableStateFlow(
    MatchPreferences(
      targetGenders = UserType.values().toSet(),
      targetDynamics = setOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.ORGIES,
        DynamicKink.GANG_BANGING,
        DynamicKink.GOTH_EMO,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING
      ),
      targetSubcultures = setOf(
        SubcultureType.GOTH,
        SubcultureType.EMO,
        SubcultureType.ALTERNATIVE,
        SubcultureType.SENSUAL_SWINGER
      ),
      onlyRealVerifiedMembers = true,
      maxDistanceMiles = 50,
      minAge = 18,
      maxAge = 40,
      onlyWithVault18Plus = false
    )
  )
  val matchPreferences: StateFlow<MatchPreferences> = _matchPreferences.asStateFlow()

  private val initialProfiles = listOf(
    UserProfile(
      id = "prof_1",
      email = "elena.marcus@clubdesire.com",
      name = "Elena & Marcus",
      age = "28 & 31",
      userType = UserType.COUPLE,
      tagline = "Fun couple seeking a girl or trans woman for threesomes, creampie & breeding play",
      bio = "Elena is bi-curious and Marcus loves watching and participating. We have a private downtown loft, respect boundaries, and believe good drinks and chemistry come first! Craving hot threesomes, creampie internal finishes, and breeding fantasies.",
      distanceMiles = 3,
      mainPhotoRes = R.drawable.couple_profile_hero,
      photos = listOf(
        PhotoItem("p1_1", "Our downtown penthouse suite", true, R.drawable.couple_profile_hero),
        PhotoItem("p1_2", "Private VIP lounge evening", false, R.drawable.vip_vault_banner)
      ),
      lifestylePhotos = listOf(
        PhotoItem("lp1_1", "Our private loft balcony", false, R.drawable.couple_profile_hero),
        PhotoItem("lp1_2", "Weekend getaway", false, R.drawable.vip_vault_banner),
        PhotoItem("lp1_3", "Candlelit dinner", false, R.drawable.goth_profile_hero)
      ),
      gradientColors = Pair(0xFFFF2A85, 0xFF7B2CBF),
      desireTags = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.ORGIES,
        DynamicKink.OPEN_POLY
      ),
      preferredGroupActivities = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING,
        DynamicKink.ORGIES
      ),
      targetGendersLookingFor = listOf(
        UserType.SINGLE_FEMALE,
        UserType.TRANS_FEMALE,
        UserType.COUPLE
      ),
      lookingFor = "Single Girls and Trans Women for MFF threesomes, spit roasting, and mutual play",
      boundaries = "Testing verified, no pressure, discreet, hotel or our private loft.",
      subculture = "Sensual Swinger & Alt",
      subcultureTags = listOf(SubcultureType.SENSUAL_SWINGER, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      membershipPlan = PlanType.YEARLY,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_1_1",
          title = "Loft Jacuzzi Session",
          description = "Uncensored couple play video & intimate photos after night out.",
          mediaCategory = "Private Couple Reel",
          isLocked = true,
          durationOrCount = "4:12 HD",
          previewRes = R.drawable.couple_profile_hero,
          blurCoverGradient = Pair(0xFFFF2A85, 0xFF5A189A)
        ),
        VaultMediaItem(
          id = "vault_1_2",
          title = "Elena Spicy Lingerie Album",
          description = "Exclusive high-res gallery with 12 photos.",
          mediaCategory = "18+ Photo Set",
          isLocked = true,
          durationOrCount = "12 Photos",
          previewRes = R.drawable.couple_profile_hero,
          blurCoverGradient = Pair(0xFF7209B7, 0xFFF72585)
        )
      )
    ),

    UserProfile(
      id = "prof_2",
      email = "raven.night@gothdesire.com",
      name = "Raven Nightshade",
      age = "23",
      userType = UserType.SINGLE_FEMALE,
      tagline = "Date a goth emo girl • Open to couples, threesomes & alt kink play",
      bio = "Fishnets, vinyl, dark wave, and bad horror movies. Tattooed goth alt girl looking for attractive couples or singles. Experienced as a 3rd for threesomes, love spit roasting, and down for intense chemistry. Let's grab drinks at the alt bar first.",
      distanceMiles = 5,
      mainPhotoRes = R.drawable.goth_profile_hero,
      photos = listOf(
        PhotoItem("p2_1", "Goth look & corset aesthetic", true, R.drawable.goth_profile_hero),
        PhotoItem("p2_2", "Dungeon photoshoot", false, R.drawable.vip_vault_banner)
      ),
      lifestylePhotos = listOf(
        PhotoItem("lp2_1", "Vinyl collection vibes", false, R.drawable.goth_profile_hero),
        PhotoItem("lp2_2", "Night out at the alt bar", false, R.drawable.vip_vault_banner)
      ),
      gradientColors = Pair(0xFF7209B7, 0xFF4361EE),
      desireTags = listOf(
        DynamicKink.GOTH_EMO,
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.CREAMPIE,
        DynamicKink.DEEPTHROAT_ORAL,
        DynamicKink.GANG_BANGING,
        DynamicKink.BDSM_KINK,
        DynamicKink.ALT_SUBCULTURE
      ),
      preferredGroupActivities = listOf(
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.GANG_BANGING
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_MALE,
        UserType.TRANS_FEMALE
      ),
      lookingFor = "Adventurous couples for threesomes & spit roasting, or a daring alt/goth partner",
      boundaries = "Safe words honored, sober meetup first, aftercare essential.",
      subculture = "Goth & Emo",
      subcultureTags = listOf(SubcultureType.GOTH, SubcultureType.EMO, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      membershipPlan = PlanType.LIFETIME_VIP,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_2_1",
          title = "Goth Room Velvet Clips",
          description = "Solo teaser and outfit changes with dark ambient lighting.",
          mediaCategory = "Alt Goth Reel",
          isLocked = true,
          durationOrCount = "3:30 4K",
          previewRes = R.drawable.goth_profile_hero,
          blurCoverGradient = Pair(0xFF3A0CA3, 0xFF4361EE)
        )
      )
    ),

    UserProfile(
      id = "prof_3",
      email = "chloe.rivera@transallure.com",
      name = "Chloe Rivera",
      age = "26",
      userType = UserType.TRANS_FEMALE,
      tagline = "Stunning trans babe • Passionate, playful & ready for group adventures",
      bio = "Latin trans beauty, model, and party enthusiast. Very comfortable with my body, love joining couples for passionate threesomes, group dynamic parties, and spit roast scenarios. Looking for respectful, generous people.",
      distanceMiles = 7,
      mainPhotoRes = null,
      photos = listOf(
        PhotoItem("p3_1", "Chloe glamour shoot", true, null)
      ),
      gradientColors = Pair(0xFFF72585, 0xFF4CC9F0),
      desireTags = listOf(
        DynamicKink.THREESOME_MMF,
        DynamicKink.THREESOME_MFF,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.ORGIES,
        DynamicKink.OPEN_POLY
      ),
      preferredGroupActivities = listOf(
        DynamicKink.THREESOME_MMF,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.ORGIES
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_MALE,
        UserType.SINGLE_FEMALE
      ),
      lookingFor = "Couples and generous lovers for threesomes, foursomes and upscale private parties",
      boundaries = "Discretion paramount, hygiene and safety required.",
      subculture = "Glam Trans & Play Party",
      subcultureTags = listOf(SubcultureType.SENSUAL_SWINGER, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      membershipPlan = PlanType.MONTHLY,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_3_1",
          title = "Chloe Private Suite Reel",
          description = "Exclusive luxury suite videos and behind the scenes.",
          mediaCategory = "18+ Private Video",
          isLocked = true,
          durationOrCount = "5:15 HD",
          previewRes = null,
          blurCoverGradient = Pair(0xFFF72585, 0xFF3F37C9)
        )
      )
    ),

    UserProfile(
      id = "prof_4",
      email = "scarlett.jax@desiremanor.com",
      name = "Scarlett & Jax",
      age = "30 & 34",
      userType = UserType.COUPLE,
      tagline = "Power couple hosting private orgies, foursomes & consensual gangbangs",
      bio = "We own a secluded manor outside the city equipped for luxury play parties. Looking for attractive couples, hung guys for gangbang showcases, and bi babes. Strict vetting, NDA required, absolute VIP privacy.",
      distanceMiles = 12,
      mainPhotoRes = R.drawable.vip_vault_banner,
      photos = listOf(
        PhotoItem("p4_1", "Manor playroom & stage", true, R.drawable.vip_vault_banner)
      ),
      gradientColors = Pair(0xFF9D4EDD, 0xFFFF0054),
      desireTags = listOf(
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.ORGIES,
        DynamicKink.GANG_BANGING,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.BDSM_KINK
      ),
      preferredGroupActivities = listOf(
        DynamicKink.ORGIES,
        DynamicKink.GANG_BANGING,
        DynamicKink.FOURSOME_MFFM
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_FEMALE,
        UserType.SINGLE_MALE,
        UserType.TRANS_FEMALE
      ),
      lookingFor = "Confident participants for vetted play parties, gangbang showcases and group orgies",
      boundaries = "Pre-screened STI tests required, strict consent rules enforced by dungeon monitor.",
      subculture = "Manor Play & Kink",
      subcultureTags = listOf(SubcultureType.KINKSTER, SubcultureType.SENSUAL_SWINGER),
      verified = true,
      has18PlusVault = true,
      membershipPlan = PlanType.LIFETIME_VIP,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_4_1",
          title = "Manor Orgy Highlights",
          description = "Censored recap clips from our midnight masquerade play party.",
          mediaCategory = "VIP Party Video",
          isLocked = true,
          durationOrCount = "7:45 4K",
          previewRes = R.drawable.vip_vault_banner,
          blurCoverGradient = Pair(0xFF240046, 0xFF9D4EDD)
        )
      )
    ),

    UserProfile(
      id = "prof_5",
      email = "dante.vance@altbull.com",
      name = "Dante Vance",
      age = "29",
      userType = UserType.SINGLE_MALE,
      tagline = "Respectful, athletic third for couples • Down for spit roasting & threesomes",
      bio = "6'2, tattooed, clean and respectful. Experienced with couples who want a safe, passionate male third (MMF or MFF). Open to spit roasting fantasies and high-energy group dynamics. Always attentive to everyone's pleasure.",
      distanceMiles = 4,
      mainPhotoRes = null,
      photos = listOf(
        PhotoItem("p5_1", "Dante portrait", true, null)
      ),
      gradientColors = Pair(0xFF4361EE, 0xFF4CC9F0),
      desireTags = listOf(
        DynamicKink.THREESOME_MMF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.ORGIES,
        DynamicKink.GOTH_EMO
      ),
      preferredGroupActivities = listOf(
        DynamicKink.THREESOME_MMF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.FOURSOME_MFFM
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_FEMALE,
        UserType.TRANS_FEMALE
      ),
      lookingFor = "Couples seeking an athletic, communicative 3rd for threesomes or spit roasting",
      boundaries = "Condoms non-negotiable, relaxed vibes, mutual attraction.",
      subculture = "Alt Athlete & Goth Ally",
      subcultureTags = listOf(SubcultureType.ALTERNATIVE, SubcultureType.GOTH),
      verified = true,
      has18PlusVault = false,
      vaultItems = emptyList()
    ),

    UserProfile(
      id = "prof_6",
      email = "sienna.damian@clubdesire.com",
      name = "Sienna & Damian",
      age = "27 & 29",
      userType = UserType.COUPLE,
      tagline = "Goth-leaning couple looking for foursomes with another couple or trans cutie",
      bio = "We live at the intersection of dark synth wave, velvet sheets, and hedonism. Seeking another couple for couple swaps/foursomes, or a gorgeous trans woman or goth girl for group play.",
      distanceMiles = 8,
      mainPhotoRes = R.drawable.couple_profile_hero,
      photos = listOf(
        PhotoItem("p6_1", "Sienna & Damian velvet lounge", true, R.drawable.couple_profile_hero),
        PhotoItem("p6_2", "Alt fashion shoot", false, R.drawable.goth_profile_hero)
      ),
      gradientColors = Pair(0xFF3F37C9, 0xFFF72585),
      desireTags = listOf(
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.THREESOME_MFF,
        DynamicKink.GOTH_EMO,
        DynamicKink.ORGIES,
        DynamicKink.ALT_SUBCULTURE
      ),
      preferredGroupActivities = listOf(
        DynamicKink.FOURSOME_MFFM,
        DynamicKink.THREESOME_MFF,
        DynamicKink.ORGIES
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.TRANS_FEMALE,
        UserType.SINGLE_FEMALE
      ),
      lookingFor = "Another open-minded couple for foursomes or a goth/trans 3rd for private play",
      boundaries = "Drinks first to check vibe, clean test results, consent and privacy respected.",
      subculture = "Goth & Alt Poly",
      subcultureTags = listOf(SubcultureType.GOTH, SubcultureType.EMO, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_6_1",
          title = "Midnight Candlelit Video",
          description = "Sensual couple teasing and dungeon room tour.",
          mediaCategory = "Goth Couple Reel",
          isLocked = true,
          durationOrCount = "6:00 HD",
          previewRes = R.drawable.couple_profile_hero,
          blurCoverGradient = Pair(0xFF3A0CA3, 0xFF7209B7)
        )
      )
    ),

    UserProfile(
      id = "prof_7",
      email = "maya.sasha@hermaphrodite.app",
      name = "Maya & Sasha",
      age = "26 & 28",
      userType = UserType.INTERSEX_HERMAPHRODITE,
      sexualOrientation = SexualOrientation.PANSEXUAL,
      relationshipStatus = RelationshipStatus.OPEN_RELATIONSHIP,
      cockEndowment = CockEndowment.VERY_WELL_ENDOWED,
      isHungVerified = true,
      hungSizeInches = "7.5\" Dual Beauty",
      isDiscreetAffair = false,
      discreetAlias = "",
      fantasiesAndFetishes = listOf("Hermaphrodite / Intersex Dating", "Bisexual Play", "Sensual Tantra", "Threesomes", "Pegging"),
      tagline = "Hermaphrodite & Intersex beauty • Pansexual sensualist seeking couples & open hearts",
      bio = "Proudly intersex / hermaphrodite. Embracing the divine blend of feminine allure and masculine power. Looking for genuine lovers, threesomes with open couples, and deep intimate exploration without taboos.",
      distanceMiles = 4,
      mainPhotoRes = R.drawable.vip_vault_banner,
      photos = listOf(
        PhotoItem("p7_1", "Sensual silhouette & velvet lights", true, R.drawable.vip_vault_banner)
      ),
      gradientColors = Pair(0xFFFF007F, 0xFF9D4EDD),
      desireTags = listOf(
        DynamicKink.HERMAPHRODITE_INTERSEX_DATING,
        DynamicKink.BISEXUAL_PLAY,
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.PEGGING_PROSTATE,
        DynamicKink.OPEN_POLY
      ),
      preferredGroupActivities = listOf(
        DynamicKink.HERMAPHRODITE_INTERSEX_DATING,
        DynamicKink.THREESOME_MFF,
        DynamicKink.SPIT_ROASTING
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_FEMALE,
        UserType.SINGLE_MALE,
        UserType.TRANS_FEMALE,
        UserType.INTERSEX_HERMAPHRODITE
      ),
      lookingFor = "Couples, single babes, and open-minded partners who celebrate intersex beauty and boundless intimacy",
      boundaries = "Respect and zero fetishization without humanity. Mutual pleasure, safety verified.",
      subculture = "Intersex Allure & Tantra",
      subcultureTags = listOf(SubcultureType.SENSUAL_SWINGER, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_7_1",
          title = "Sensual Dual Beauty Video",
          description = "Intimate glimpse celebrating the complete intersex body.",
          mediaCategory = "Exclusive Vault Reel",
          isLocked = true,
          durationOrCount = "4:45 HD",
          previewRes = R.drawable.vip_vault_banner,
          blurCoverGradient = Pair(0xFFFF007F, 0xFF7209B7)
        )
      )
    ),

    UserProfile(
      id = "prof_8",
      email = "jaxson.steel@hungbull.app",
      name = "Jaxson Steel",
      age = "29",
      userType = UserType.SINGLE_MALE,
      sexualOrientation = SexualOrientation.BISEXUAL,
      relationshipStatus = RelationshipStatus.SINGLE,
      cockEndowment = CockEndowment.BIG_COCKS_ONLY,
      isHungVerified = true,
      hungSizeInches = "8.5\" Thick Monster",
      isDiscreetAffair = false,
      discreetAlias = "",
      fantasiesAndFetishes = listOf("Big Cocks Focus (8.5\")", "Hotwife / Bull", "Spit Roasting", "Cuckold Sessions", "Gangbangs"),
      tagline = "🍆 8.5\" Verified Monster Hung • Attentive Bull for Hotwives & Couples",
      bio = "6'3 athletic frame, well-mannered, and verified heavy 8.5\". Experienced with couples looking for a reliable, respectful bull. Love spit roasting, intense threesomes, and making the woman feel like an absolute goddess while the husband watches or joins.",
      distanceMiles = 6,
      mainPhotoRes = null,
      photos = listOf(
        PhotoItem("p8_1", "Jaxson athletic build", true, null)
      ),
      gradientColors = Pair(0xFFE01E78, 0xFF4361EE),
      desireTags = listOf(
        DynamicKink.BIG_COCKS_FOCUS,
        DynamicKink.CUCKOLD_HOTWIFE,
        DynamicKink.CREAMPIE,
        DynamicKink.BREEDING,
        DynamicKink.RAW_UNPROTECTED_FANTASY,
        DynamicKink.SPIT_ROASTING,
        DynamicKink.THREESOME_MMF,
        DynamicKink.GANG_BANGING,
        DynamicKink.FOURSOME_MFFM
      ),
      preferredGroupActivities = listOf(
        DynamicKink.SPIT_ROASTING,
        DynamicKink.THREESOME_MMF,
        DynamicKink.GANG_BANGING
      ),
      targetGendersLookingFor = listOf(
        UserType.COUPLE,
        UserType.SINGLE_FEMALE,
        UserType.TRANS_FEMALE
      ),
      lookingFor = "Couples, hotwives, and queens who crave an 8.5\" hung bull with endless stamina and discretion",
      boundaries = "Safe sex verified, zero drama, hotel or private residence.",
      subculture = "Hung Bull & Swinger Ally",
      subcultureTags = listOf(SubcultureType.SENSUAL_SWINGER),
      verified = true,
      has18PlusVault = true,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_8_1",
          title = "8.5\" Verified Measurement & Tease",
          description = "Proof measurement clip and athletic physique showcase.",
          mediaCategory = "Verified Hung Vault",
          isLocked = true,
          durationOrCount = "3:20 4K",
          previewRes = null,
          blurCoverGradient = Pair(0xFFE01E78, 0xFF3F37C9)
        )
      )
    ),

    UserProfile(
      id = "prof_9",
      email = "liam.mateo@gaydesire.app",
      name = "Liam & Mateo",
      age = "29 & 32",
      userType = UserType.COUPLE,
      sexualOrientation = SexualOrientation.GAY,
      relationshipStatus = RelationshipStatus.OPEN_RELATIONSHIP,
      cockEndowment = CockEndowment.BIG_COCKS_ONLY,
      isHungVerified = true,
      hungSizeInches = "8\" Verified",
      isDiscreetAffair = false,
      discreetAlias = "",
      fantasiesAndFetishes = listOf("Gay Encounters", "Threesomes (MMM)", "Leather / Kink", "Big Cocks", "Play Parties"),
      tagline = "🏳️‍🌈 Handsome gay couple • Seeking hung 3rd & bi brothers for playroom fun",
      bio = "Athletic gay couple in the city with our own private playroom. Open to hung guys (8\"+), bi guys exploring their cravings, and group events. Clean, tested, high energy.",
      distanceMiles = 5,
      mainPhotoRes = null,
      photos = listOf(
        PhotoItem("p9_1", "Liam & Mateo gym session", true, null)
      ),
      gradientColors = Pair(0xFF3F37C9, 0xFF00F0FF),
      desireTags = listOf(
        DynamicKink.GAY_CRUISE,
        DynamicKink.BISEXUAL_PLAY,
        DynamicKink.BIG_COCKS_FOCUS,
        DynamicKink.LATEX_LEATHER,
        DynamicKink.ORGIES
      ),
      preferredGroupActivities = listOf(
        DynamicKink.GAY_CRUISE,
        DynamicKink.ORGIES
      ),
      targetGendersLookingFor = listOf(
        UserType.SINGLE_MALE,
        UserType.TRANS_MALE,
        UserType.COUPLE
      ),
      lookingFor = "Hung men and bi guys for intense playroom sessions and luxury weekend play",
      boundaries = "Safe play, test results shared, upfront communication.",
      subculture = "Leather & Play Party",
      subcultureTags = listOf(SubcultureType.KINKSTER, SubcultureType.SENSUAL_SWINGER),
      verified = true,
      has18PlusVault = false,
      vaultItems = emptyList()
    ),

    UserProfile(
      id = "prof_10",
      email = "camille.jade@sapphicallure.app",
      name = "Camille & Jade",
      age = "25 & 27",
      userType = UserType.COUPLE,
      sexualOrientation = SexualOrientation.LESBIAN,
      relationshipStatus = RelationshipStatus.OPEN_MARRIAGE,
      cockEndowment = CockEndowment.NOT_APPLICABLE,
      isHungVerified = false,
      hungSizeInches = "",
      isDiscreetAffair = false,
      discreetAlias = "",
      fantasiesAndFetishes = listOf("Lesbian Sapphic Trib", "Goth & Emo Dating", "Pegging & Strap-on", "Fetish Corsets"),
      tagline = "🧡 Married lesbian couple • Seeking bi girls, goth babes & sapphic play",
      bio = "Happily married sapphic couple in an open marriage. We love dark aesthetics, goth music, silk lingerie, and strap-on / pegging sessions. Looking for single women, trans women, or bi babes.",
      distanceMiles = 7,
      mainPhotoRes = R.drawable.goth_profile_hero,
      photos = listOf(
        PhotoItem("p10_1", "Camille & Jade velvet bedroom", true, R.drawable.goth_profile_hero)
      ),
      gradientColors = Pair(0xFFFF5400, 0xFFF72585),
      desireTags = listOf(
        DynamicKink.LESBIAN_TRIB,
        DynamicKink.GOTH_EMO,
        DynamicKink.PEGGING_PROSTATE,
        DynamicKink.OPEN_POLY
      ),
      preferredGroupActivities = listOf(
        DynamicKink.LESBIAN_TRIB,
        DynamicKink.GOTH_EMO
      ),
      targetGendersLookingFor = listOf(
        UserType.SINGLE_FEMALE,
        UserType.TRANS_FEMALE,
        UserType.NON_BINARY
      ),
      lookingFor = "Beautiful women and non-binary sweethearts for passion, laughter and sapphic nights",
      boundaries = "Consent-driven, relaxed chemistry check first.",
      subculture = "Sapphic Goth & Velvet",
      subcultureTags = listOf(SubcultureType.GOTH, SubcultureType.ALTERNATIVE),
      verified = true,
      has18PlusVault = true,
      vaultItems = listOf(
        VaultMediaItem(
          id = "vault_10_1",
          title = "Sapphic Velvet Album",
          description = "Intimate couples shoot with lace, satin, and warm candle glow.",
          mediaCategory = "18+ Photo Gallery",
          isLocked = true,
          durationOrCount = "15 Photos",
          previewRes = R.drawable.goth_profile_hero,
          blurCoverGradient = Pair(0xFFFF5400, 0xFF7209B7)
        )
      )
    ),

    UserProfile(
      id = "prof_11",
      email = "shadow.velvet@discreet.app",
      name = "Shadow Velvet",
      age = "36",
      userType = UserType.SINGLE_MALE,
      sexualOrientation = SexualOrientation.HETERO_FLEXIBLE,
      relationshipStatus = RelationshipStatus.CHEATING_SECRET,
      cockEndowment = CockEndowment.VERY_WELL_ENDOWED,
      isHungVerified = true,
      hungSizeInches = "7.5\" Discreet",
      isDiscreetAffair = true,
      discreetAlias = "Shadow Velvet (Incognito)",
      fantasiesAndFetishes = listOf("Discreet Affair", "Secret Hotel Escapes", "No Attachment", "Confidential Hookups", "Luxury Suites"),
      tagline = "🤫 100% Discreet & Confidential • Cheating / Secret affair looking for private hotel meetups",
      bio = "Successful professional in an unfulfilled marriage seeking a discreet, high-class secret encounter. 100% mutual confidentiality guaranteed. Zero drama, generous, respectful. Photos unblurred after mutual trust.",
      distanceMiles = 9,
      mainPhotoRes = null,
      photos = listOf(
        PhotoItem("p11_1", "Silhouette & luxury watch", true, null)
      ),
      gradientColors = Pair(0xFF00F0FF, 0xFF240046),
      desireTags = listOf(
        DynamicKink.DISCREET_CHEATING,
        DynamicKink.ROLEPLAY_FANTASY,
        DynamicKink.ALL_TYPES
      ),
      preferredGroupActivities = emptyList(),
      targetGendersLookingFor = listOf(
        UserType.SINGLE_FEMALE,
        UserType.COUPLE,
        UserType.TRANS_FEMALE
      ),
      lookingFor = "Discreet women or couples for confidential luxury hotel rendezvous without expectations",
      boundaries = "Absolute secrecy. No personal details, no social media tracking, burner communications.",
      subculture = "Discreet Incognito",
      subcultureTags = listOf(SubcultureType.SENSUAL_SWINGER),
      verified = true,
      has18PlusVault = false,
      vaultItems = emptyList()
    )
  )

  private val _profiles = MutableStateFlow(initialProfiles)
  val profiles: StateFlow<List<UserProfile>> = _profiles.asStateFlow()

  private val initialSexRequests = listOf(
    DirectSexRequest(
      id = "req_1",
      fromUserId = "prof_1",
      fromUserName = "Elena & Marcus",
      toUserId = "user_me",
      toUserName = "Jordan & Taylor",
      category = DynamicKink.SPIT_ROASTING,
      proposalTitle = "Spit Roast & Threesome at Downtown Loft",
      description = "Elena and I would love to invite you over for champagne and explore an intense spit-roast fantasy with you both. We have a private suite and great music.",
      experienceLevel = "Experienced with multiple partners",
      preferredVenue = "Private Penthouse Suite",
      timeAgo = "15m ago",
      status = RequestStatus.PENDING
    ),
    DirectSexRequest(
      id = "req_2",
      fromUserId = "prof_2",
      fromUserName = "Raven Nightshade",
      toUserId = "user_me",
      toUserName = "Jordan & Taylor",
      category = DynamicKink.GOTH_EMO,
      proposalTitle = "Date a Goth Emo Girl + MFF Threesome",
      description = "Hey! Saw your couple profile. I'd love to date you both, wear my best vinyl outfit, and be your playful third for an unforgettable night.",
      experienceLevel = "Very experienced, open & communicative",
      preferredVenue = "Alt Lounge for Drinks then Private Room",
      timeAgo = "1h ago",
      status = RequestStatus.PENDING
    ),
    DirectSexRequest(
      id = "req_3",
      fromUserId = "prof_4",
      fromUserName = "Scarlett & Jax",
      toUserId = "user_me",
      toUserName = "Jordan & Taylor",
      category = DynamicKink.ORGIES,
      proposalTitle = "VIP Invitation: Midnight Masquerade Orgy",
      description = "Exclusive invitation to our vetted 8-couple play party. Private rooms, dungeon equipment, strict consent monitor on site.",
      experienceLevel = "Veterans of play parties",
      preferredVenue = "Secluded Manor Dungeon",
      timeAgo = "3h ago",
      status = RequestStatus.ACCEPTED
    ),
    DirectSexRequest(
      id = "req_4",
      fromUserId = "user_me",
      fromUserName = "Jordan & Taylor",
      toUserId = "prof_3",
      toUserName = "Chloe Rivera",
      category = DynamicKink.THREESOME_MFF,
      proposalTitle = "Threesome with Bi Couple",
      description = "We adore your photos and energy. Would you be down for dinner followed by a private play session at our hotel suite?",
      experienceLevel = "Very experienced couple",
      preferredVenue = "Luxury Hotel Suite",
      timeAgo = "Yesterday",
      status = RequestStatus.PENDING
    )
  )

  private val _sexRequests = MutableStateFlow(initialSexRequests)
  val sexRequests: StateFlow<List<DirectSexRequest>> = _sexRequests.asStateFlow()

  private val initialEvents = listOf(
    GroupEvent(
      id = "evt_1",
      title = "Noir Velvet: Upscale Penthouse Orgy",
      hostName = "Scarlett & Jax",
      kinkType = DynamicKink.ORGIES,
      dateText = "This Saturday • 10:00 PM",
      locationArea = "Downtown Penthouse Lounge",
      attendeesCount = 14,
      maxAttendees = 20,
      description = "Consensual play party for vetted couples and approved singles. Free flowing champagne, themed private suites, and play rooms.",
      isVipOnly = true
    ),
    GroupEvent(
      id = "evt_2",
      title = "Dark Wave & Fishnets: Goth / Emo Dating Mixer",
      hostName = "Raven Nightshade & Club Shadow",
      kinkType = DynamicKink.GOTH_EMO,
      dateText = "Friday • 9:00 PM",
      locationArea = "Underground Vault Alt Lounge",
      attendeesCount = 28,
      maxAttendees = 40,
      description = "Meet attractive goth singles, emo lovers, and couples seeking alt thirds. Dark electro beats, private booths for intimate conversation.",
      isVipOnly = false
    ),
    GroupEvent(
      id = "evt_3",
      title = "Midnight Thunder: Consensual Gangbang Showcase",
      hostName = "REBEL UP Manor",
      kinkType = DynamicKink.GANG_BANGING,
      dateText = "Next Thursday • 11:00 PM",
      locationArea = "Secluded Manor Room 4",
      attendeesCount = 9,
      maxAttendees = 12,
      description = "Pre-screened consensual gangbang experience with designated safety marshals, testing verification, and luxury amenities.",
      isVipOnly = true
    ),
    GroupEvent(
      id = "evt_4",
      title = "Double Chemistry: Foursomes & Couple Swap Social",
      hostName = "Elena & Marcus",
      kinkType = DynamicKink.FOURSOME_MFFM,
      dateText = "Sunday • 8:00 PM",
      locationArea = "Riverfront Loft",
      attendeesCount = 6,
      maxAttendees = 8,
      description = "Intimate social mixer specifically for open-minded couples looking to connect for foursomes, soft swaps, and full group exploration.",
      isVipOnly = false
    )
  )

  private val _events = MutableStateFlow(initialEvents)
  val events: StateFlow<List<GroupEvent>> = _events.asStateFlow()

  private val initialChats = listOf(
    ChatConversation(
      id = "chat_1",
      targetProfileId = "prof_2",
      targetName = "Raven Nightshade",
      targetType = UserType.SINGLE_FEMALE,
      targetPhotoRes = R.drawable.goth_profile_hero,
      lastMessage = "I would love to be your goth third for the weekend 🖤",
      lastMessageTime = "10m ago",
      unreadCount = 1,
      isSecureChannel = true,
      messages = listOf(
        ChatMessage("m1", "Raven Nightshade", "Hey Jordan & Taylor! Stumbled on your profile. Love your vibe.", "10:15 AM", false, isEncrypted = true),
        ChatMessage("m2", "Jordan & Taylor", "Hey Raven! Your goth aesthetic is stunning. Are you in the city this weekend?", "10:18 AM", true, isEncrypted = true),
        ChatMessage("m3", "Raven Nightshade", "Yes! I saw you are into threesomes and spit roasting too. Exactly what I crave.", "10:20 AM", false, isEncrypted = true),
        ChatMessage("m4", "Raven Nightshade", "I would love to be your goth third for the weekend 🖤", "10:25 AM", false, isEncrypted = true)
      )
    ),
    ChatConversation(
      id = "chat_2",
      targetProfileId = "prof_1",
      targetName = "Elena & Marcus",
      targetType = UserType.COUPLE,
      targetPhotoRes = R.drawable.couple_profile_hero,
      lastMessage = "We sent you a Direct Sex Request for the weekend!",
      lastMessageTime = "2h ago",
      unreadCount = 0,
      isSecureChannel = true,
      messages = listOf(
        ChatMessage("m21", "Elena & Marcus", "Hey guys! Elena saw your photos and got super excited.", "12:30 PM", false, isEncrypted = true),
        ChatMessage("m22", "Jordan & Taylor", "Hey Elena & Marcus! Your loft sounds incredible.", "12:35 PM", true, isEncrypted = true),
        ChatMessage("m23", "Elena & Marcus", "We sent you a Direct Sex Request for the weekend!", "12:40 PM", false, isEncrypted = true)
      )
    )
  )

  private val _chats = MutableStateFlow(initialChats)
  val chats: StateFlow<List<ChatConversation>> = _chats.asStateFlow()

  // --- SEX WORKER & ESCORT ADS MARKETPLACE ---
  private val initialSexWorkerAds = listOf(
    SexWorkerAd(
      id = "ad_1",
      providerName = "Mistress Vespera",
      age = "27",
      gender = UserType.SINGLE_FEMALE,
      sexualOrientation = SexualOrientation.QUEER,
      tagline = "Elite Goth Domme & Sensual BDSM Specialist",
      bio = "Sensual dominance, roleplay fantasies, bondage & restraints, spanking/impact, and deep psychological submission in a fully outfitted private downtown dungeon.",
      hourlyRate = "$300/hr",
      twoHoursRate = "$550/2hr",
      overnightRate = "$1,800/overnight",
      quickVisitRate = "$200/quick visit",
      servicesOffered = listOf(
        "BDSM & Bondage",
        "Femdom / Gentle Domme",
        "Sensory Deprivation",
        "Spanking & Discipline",
        "Foot Fetish & Worship",
        "Couples Domination Session",
        "Incall Private Dungeon"
      ),
      location = "Downtown Metro • Private Incall Dungeon",
      contactMethod = "In-App Discreet Encrypted Chat or Signal",
      isVerifiedProvider = true,
      photoRes = R.drawable.goth_profile_hero,
      photoGradient = Pair(0xFF4361EE, 0xFF7209B7),
      reviewsCount = 38,
      rating = 4.95,
      postedTimeAgo = "12m ago",
      outcallAvailable = true,
      incallAvailable = true,
      isDiscreet = true
    ),
    SexWorkerAd(
      id = "ad_2",
      providerName = "Giselle Laurent",
      age = "25",
      gender = UserType.SINGLE_FEMALE,
      sexualOrientation = SexualOrientation.BISEXUAL,
      tagline = "High-Class Companion • GFE & Sensual Escort",
      bio = "Polished, educated, and passionate. Full Girlfriend Experience (GFE), romantic dinner dates, sensual massage, and unhurried intimacy. Couples welcome.",
      hourlyRate = "$350/hr",
      twoHoursRate = "$650/2hr",
      overnightRate = "$2,200/overnight",
      quickVisitRate = "$250/quick visit",
      servicesOffered = listOf(
        "Girlfriend Experience (GFE)",
        "Sensual Full-Body Massage",
        "Dinner & Event Companion",
        "Couples Friendly Play",
        "Luxury Hotel Outcall",
        "Penthouse Incall"
      ),
      location = "Uptown Financial District • Incall & Outcall",
      contactMethod = "In-App Direct Booking / WhatsApp VIP",
      isVerifiedProvider = true,
      photoRes = R.drawable.couple_profile_hero,
      photoGradient = Pair(0xFFFF2A85, 0xFF9D4EDD),
      reviewsCount = 52,
      rating = 5.0,
      postedTimeAgo = "35m ago",
      outcallAvailable = true,
      incallAvailable = true,
      isDiscreet = true
    ),
    SexWorkerAd(
      id = "ad_3",
      providerName = "Chloe Rivera VIP",
      age = "26",
      gender = UserType.TRANS_FEMALE,
      sexualOrientation = SexualOrientation.BISEXUAL,
      tagline = "Glamorous Trans Escort • Threesome Specialist",
      bio = "Stunning trans companion offering sensual encounters, threesome guest appearances for couples, erotic massage, and open-minded fetish exploration.",
      hourlyRate = "$280/hr",
      twoHoursRate = "$500/2hr",
      overnightRate = "$1,600/overnight",
      quickVisitRate = "$190/quick visit",
      servicesOffered = listOf(
        "Threesome Guest (Couples/Singles)",
        "Sensual Erotic Massage",
        "Full GFE & Pampering",
        "Roleplay & Fantasies",
        "Incall Suite / Outcall"
      ),
      location = "Downtown Midtown • Luxury Incall",
      contactMethod = "In-App Verified Chat",
      isVerifiedProvider = true,
      photoRes = null,
      photoGradient = Pair(0xFFF72585, 0xFF4CC9F0),
      reviewsCount = 29,
      rating = 4.9,
      postedTimeAgo = "1h ago",
      outcallAvailable = true,
      incallAvailable = true,
      isDiscreet = true
    ),
    SexWorkerAd(
      id = "ad_4",
      providerName = "Jaxson Steel (Hung Bull)",
      age = "29",
      gender = UserType.SINGLE_MALE,
      sexualOrientation = SexualOrientation.BISEXUAL,
      tagline = "8.5\" Verified Hung Bull • Cuckold & Threesome Escort",
      bio = "Athletic, 8.5\" verified monster hung bull companion. Available for couples seeking a dominant bull, hotwife fantasies, cuckold sessions, or spit roast dynamics.",
      hourlyRate = "$250/hr",
      twoHoursRate = "$450/2hr",
      overnightRate = "$1,400/overnight",
      quickVisitRate = "$180/quick visit",
      servicesOffered = listOf(
        "Hung Bull Escort (8.5\" Verified)",
        "Hotwife & Cuckold Play",
        "Spit Roasting & MMF Threesomes",
        "Gangbang Guest",
        "Hotel Outcall & Incall"
      ),
      location = "Greater Metro Area • Hotel Outcall Preferred",
      contactMethod = "In-App Encrypted Chat",
      isVerifiedProvider = true,
      photoRes = null,
      photoGradient = Pair(0xFF4361EE, 0xFF00F0FF),
      reviewsCount = 41,
      rating = 4.98,
      postedTimeAgo = "2h ago",
      outcallAvailable = true,
      incallAvailable = false,
      isDiscreet = true
    ),
    SexWorkerAd(
      id = "ad_5",
      providerName = "Sasha & Maya (Hermaphrodite Duo)",
      age = "26 & 28",
      gender = UserType.INTERSEX_HERMAPHRODITE,
      sexualOrientation = SexualOrientation.PANSEXUAL,
      tagline = "Sensual Hermaphrodite & Intersex Duo Services",
      bio = "Exclusive dual provider experience. Exploring the full spectrum of sensuality, mutual worship, versatile play, and open-minded couples instruction.",
      hourlyRate = "$400/hr",
      twoHoursRate = "$750/2hr",
      overnightRate = "$2,500/overnight",
      quickVisitRate = "$300/quick visit",
      servicesOffered = listOf(
        "Hermaphrodite & Intersex Intimate Play",
        "Duo Providers / Threesomes",
        "Sensual Tantric Touch",
        "Pegging & Prostate Pleasure",
        "Incall Penthouse Retreat"
      ),
      location = "Waterfront Harbor • Private Penthouse",
      contactMethod = "In-App Discreet Booking",
      isVerifiedProvider = true,
      photoRes = R.drawable.vip_vault_banner,
      photoGradient = Pair(0xFFFF007F, 0xFF9D4EDD),
      reviewsCount = 19,
      rating = 5.0,
      postedTimeAgo = "3h ago",
      outcallAvailable = true,
      incallAvailable = true,
      isDiscreet = true
    )
  )

  private val _sexWorkerAds = MutableStateFlow(initialSexWorkerAds)
  val sexWorkerAds: StateFlow<List<SexWorkerAd>> = _sexWorkerAds.asStateFlow()

  fun postSexWorkerAd(ad: SexWorkerAd) {
    _sexWorkerAds.update { listOf(ad) + it }
  }

  fun deleteSexWorkerAd(adId: String) {
    _sexWorkerAds.update { list -> list.filterNot { it.id == adId } }
  }

  // --- LIFETIME VIP & MEMBERSHIP LOGIC ---

  init {
    // Check initial email if lifetime VIP
    checkAndApplyLifetimeVip(_membershipState.value.userEmail)
  }

  fun setUserEmail(email: String) {
    val cleanEmail = email.trim()
    _membershipState.update { it.copy(userEmail = cleanEmail) }
    _currentUserProfile.update { it.copy(email = cleanEmail) }
    checkAndApplyLifetimeVip(cleanEmail)
  }

  private fun checkAndApplyLifetimeVip(email: String) {
    val isVip = LIFETIME_VIP_EMAILS.any { it.equals(email.trim(), ignoreCase = true) }
    if (isVip) {
      _membershipState.update {
        it.copy(
          plan = PlanType.LIFETIME_VIP,
          hasPremium18PlusUpgrade = true,
          isSubscribed = true,
          isLifetimeVip = true
        )
      }
      _currentUserProfile.update { it.copy(isLifetimeVip = true) }
    }
  }

  fun grantLifetimeVipToEmail(email: String) {
    setUserEmail(email)
  }

  fun updateMembership(plan: PlanType, hasPremium18Plus: Boolean) {
    val isVip = _membershipState.value.isLifetimeVip
    _membershipState.update {
      it.copy(
        plan = if (isVip) PlanType.LIFETIME_VIP else plan,
        hasPremium18PlusUpgrade = isVip || hasPremium18Plus,
        isSubscribed = isVip || (plan != PlanType.FREE)
      )
    }
  }

  fun toggle18PlusUpgrade(enabled: Boolean) {
    _membershipState.update {
      it.copy(hasPremium18PlusUpgrade = it.isLifetimeVip || enabled)
    }
  }

  // --- PHOTO MANAGEMENT FOR COMPREHENSIVE PROFILE SYSTEM ---

  fun addPhotoToCurrentUser(caption: String, drawableRes: Int? = null, localUri: String? = null) {
    val newPhoto = PhotoItem(
      id = "photo_${UUID.randomUUID().toString().take(6)}",
      caption = caption,
      isMain = _currentUserProfile.value.photos.isEmpty(),
      drawableRes = drawableRes,
      localUri = localUri
    )
    _currentUserProfile.update { current ->
      val updatedList = current.photos + newPhoto
      current.copy(photos = updatedList)
    }
  }

  fun removePhotoFromCurrentUser(photoId: String) {
    _currentUserProfile.update { current ->
      val filtered = current.photos.filterNot { it.id == photoId }
      val updated = if (filtered.none { it.isMain } && filtered.isNotEmpty()) {
        filtered.mapIndexed { idx, p -> if (idx == 0) p.copy(isMain = true) else p }
      } else {
        filtered
      }
      current.copy(photos = updated)
    }
  }

  fun setMainPhoto(photoId: String) {
    _currentUserProfile.update { current ->
      val updated = current.photos.map { p ->
        p.copy(isMain = p.id == photoId)
      }
      val mainP = updated.find { it.isMain }
      current.copy(
        photos = updated,
        mainPhotoRes = mainP?.drawableRes ?: current.mainPhotoRes
      )
    }
  }

  fun updateMyProfile(
    name: String,
    age: String,
    userType: UserType,
    tagline: String,
    bio: String,
    desires: List<DynamicKink>,
    groupActivities: List<DynamicKink> = emptyList(),
    targetGenders: List<UserType> = emptyList(),
    lookingFor: String = "",
    boundaries: String = "",
    subcultures: List<SubcultureType> = emptyList(),
    sexualOrientation: SexualOrientation = SexualOrientation.BISEXUAL,
    relationshipStatus: RelationshipStatus = RelationshipStatus.OPEN_RELATIONSHIP,
    cockEndowment: CockEndowment = CockEndowment.NOT_APPLICABLE,
    isHungVerified: Boolean = false,
    hungSizeInches: String = "",
    isDiscreetAffair: Boolean = false,
    discreetAlias: String = "",
    fantasiesAndFetishes: List<String> = emptyList()
  ) {
    _currentUserProfile.update {
      it.copy(
        name = name,
        age = age,
        userType = userType,
        tagline = tagline,
        bio = bio,
        desireTags = desires,
        preferredGroupActivities = groupActivities,
        targetGendersLookingFor = targetGenders,
        lookingFor = lookingFor.ifBlank { it.lookingFor },
        boundaries = boundaries.ifBlank { it.boundaries },
        subcultureTags = subcultures,
        subculture = subcultures.joinToString(", ") { sc -> sc.label }.ifBlank { it.subculture },
        sexualOrientation = sexualOrientation,
        relationshipStatus = relationshipStatus,
        cockEndowment = cockEndowment,
        isHungVerified = isHungVerified,
        hungSizeInches = hungSizeInches,
        isDiscreetAffair = isDiscreetAffair,
        discreetAlias = discreetAlias,
        fantasiesAndFetishes = fantasiesAndFetishes
      )
    }
  }

  // --- MATCHING ALGORITHM ENGINE ---

  fun updateMatchPreferences(newPreferences: MatchPreferences) {
    _matchPreferences.value = newPreferences
  }

  /**
   * Evaluates compatibility score (0 - 100%) based on:
   * 1. Gender identity & Looking-for match (30 pts)
   * 2. Shared dynamics & kinks (threesomes, foursomes, orgies, spit roasting, big cocks, discreet) (30 pts)
   * 3. Subculture alignment (Goth, Emo, Alt, Swinger) (15 pts)
   * 4. Orientation & Relationship Status match (15 pts)
   * 5. Distance and age suitability (10 pts)
   */
  fun calculateMatchScore(
    profile: UserProfile,
    prefs: MatchPreferences,
    myProfile: UserProfile = _currentUserProfile.value
  ): MatchScoreResult {
    // 0. Fake Profile & Bot check
    if (prefs.onlyRealVerifiedMembers && !profile.verified) {
      return MatchScoreResult(
        profile = profile,
        compatibilityPercentage = 0,
        matchedGenders = false,
        sharedKinks = emptyList(),
        sharedSubcultures = emptyList(),
        reasonHighlights = listOf("Filtered out: Unverified / bot check pending")
      )
    }

    // Age restriction: Strict 18 to 40 age limits
    val ageNumbers = Regex("""\d+""").findAll(profile.age).mapNotNull { it.value.toIntOrNull() }.toList()
    val isWithinAge = if (ageNumbers.isNotEmpty()) {
      ageNumbers.all { it in prefs.minAge..prefs.maxAge }
    } else true

    if (!isWithinAge) {
      return MatchScoreResult(
        profile = profile,
        compatibilityPercentage = 0,
        matchedGenders = false,
        sharedKinks = emptyList(),
        sharedSubcultures = emptyList(),
        reasonHighlights = listOf("Filtered: Outside strict 18–40 age policy")
      )
    }

    var score = 0
    val reasons = mutableListOf<String>()

    if (profile.verified) {
      reasons.add("100% Real ID-Verified Member • Zero Fake Profiles / Bots")
    }
    reasons.add("Age Verified (${profile.age} yrs • Within 18–40 Limit)")

    // 1. Gender check
    val genderMatch = prefs.targetGenders.contains(profile.userType)
    if (genderMatch) {
      score += 25
      reasons.add("Matches desired identity (${profile.userType.label})")
    }

    // 2. Kinks & dynamics overlap (threesomes, foursomes, orgies, spit roast, big cocks, intersex)
    val sharedDynamics = profile.desireTags.filter { k ->
      prefs.targetDynamics.contains(k) || myProfile.desireTags.contains(k)
    }.distinct()

    if (sharedDynamics.isNotEmpty()) {
      val dynamicPoints = (sharedDynamics.size * 10).coerceAtMost(30)
      score += dynamicPoints
      val topDynamicsNames = sharedDynamics.take(2).joinToString(" & ") { it.displayName }
      reasons.add("Shared desires: $topDynamicsNames")
    }

    // 3. Subculture alignment (Goth, Emo, Alt, etc.)
    val sharedSubcultures = profile.subcultureTags.filter { sc ->
      prefs.targetSubcultures.contains(sc) || myProfile.subcultureTags.contains(sc)
    }.distinct()

    if (sharedSubcultures.isNotEmpty()) {
      val subPoints = (sharedSubcultures.size * 10).coerceAtMost(15)
      score += subPoints
      val subNames = sharedSubcultures.joinToString(" & ") { it.label }
      reasons.add("Subculture match: $subNames ${sharedSubcultures.first().emoji}")
    } else if (profile.desireTags.contains(DynamicKink.GOTH_EMO)) {
      score += 10
      reasons.add("Open to Goth / Emo dating 🖤")
    }

    // 4. Orientation & Relationship Status
    if (prefs.targetOrientations.isNotEmpty() && prefs.targetOrientations.contains(profile.sexualOrientation)) {
      score += 10
      reasons.add("Orientation: ${profile.sexualOrientation.label}")
    }
    if (prefs.targetRelationshipStatuses.isNotEmpty() && prefs.targetRelationshipStatuses.contains(profile.relationshipStatus)) {
      score += 10
      reasons.add("Relationship Type: ${profile.relationshipStatus.label}")
    }

    // 5. Big Cocks Only filter match
    if (prefs.onlyBigCocks && (profile.cockEndowment == CockEndowment.BIG_COCKS_ONLY || profile.isHungVerified)) {
      score += 15
      reasons.add("🍆 Verified Hung 8\"+ Match (${profile.hungSizeInches})")
    }

    // 6. Discreet filter match
    if (prefs.onlyDiscreet && (profile.isDiscreetAffair || profile.relationshipStatus == RelationshipStatus.CHEATING_SECRET)) {
      score += 15
      reasons.add("🤫 100% Confidential / Discreet Affair")
    }

    // 7. Distance and age
    if (profile.distanceMiles <= prefs.maxDistanceMiles) {
      score += 10
      reasons.add("${profile.distanceMiles} miles nearby")
    }

    val finalScore = score.coerceIn(45, 99)

    return MatchScoreResult(
      profile = profile,
      compatibilityPercentage = finalScore,
      matchedGenders = genderMatch,
      sharedKinks = sharedDynamics,
      sharedSubcultures = sharedSubcultures,
      reasonHighlights = reasons
    )
  }

  fun getRankedMatches(): List<MatchScoreResult> {
    val prefs = _matchPreferences.value
    val all = _profiles.value
    return all.map { profile ->
      calculateMatchScore(profile, prefs)
    }.filter { it.compatibilityPercentage > 0 }
     .sortedByDescending { it.compatibilityPercentage }
  }

  // --- DIRECT REQUESTS & CHATS ---

  fun getOrCreateChatForUser(targetUser: UserProfile): ChatConversation {
    val existing = _chats.value.find { it.targetProfileId == targetUser.id }
    if (existing != null) return existing

    val newChat = ChatConversation(
      id = "chat_${targetUser.id}",
      targetProfileId = targetUser.id,
      targetName = targetUser.name,
      targetType = targetUser.userType,
      targetPhotoRes = targetUser.mainPhotoRes,
      lastMessage = "Started a conversation. Unlimited member chat active.",
      lastMessageTime = "Just now",
      unreadCount = 0,
      messages = listOf(
        ChatMessage(
          id = UUID.randomUUID().toString(),
          senderName = targetUser.name,
          text = "Hey! Saw your profile and desires. What kind of scene are you craving?",
          time = "Just now",
          isFromMe = false
        )
      )
    )
    _chats.update { listOf(newChat) + it }
    return newChat
  }

  fun sendSexRequest(request: DirectSexRequest) {
    _sexRequests.update { listOf(request) + it }
  }

  fun updateRequestStatus(requestId: String, newStatus: RequestStatus) {
    _sexRequests.update { list ->
      list.map { if (it.id == requestId) it.copy(status = newStatus) else it }
    }
  }

  fun sendChatMessage(chatId: String, text: String, isSelfDestruct: Boolean = false) {
    _chats.update { currentList ->
      currentList.map { chat ->
        if (chat.id == chatId) {
          val newMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            senderName = _currentUserProfile.value.name,
            text = text,
            time = "Just now",
            isFromMe = true,
            isEncrypted = true,
            isSelfDestruct = isSelfDestruct
          )
          chat.copy(
            messages = chat.messages + newMsg,
            lastMessage = if (isSelfDestruct) "Encrypted Message" else text,
            lastMessageTime = "Just now"
          )
        } else chat
      }
    }

    // Simulate real-time response after 2 seconds
    val activeChat = _chats.value.find { it.id == chatId }
    if (activeChat != null) {
      CoroutineScope(Dispatchers.Default).launch {
        delay(2000)
        _chats.update { currentList ->
          currentList.map { chat ->
            if (chat.id == chatId) {
              val reply = ChatMessage(
                id = UUID.randomUUID().toString(),
                senderName = chat.targetName,
                text = "Received on secure channel. 🖤",
                time = "Just now",
                isFromMe = false,
                isEncrypted = true
              )
              chat.copy(
                messages = chat.messages + reply,
                lastMessage = reply.text,
                lastMessageTime = "Just now",
                unreadCount = if (chat.id == chatId) 0 else chat.unreadCount // Reset unread if active
              )
            } else chat
          }
        }
      }
    }
  }

  fun signInAnonymously() {
    auth?.signInAnonymously()?.addOnSuccessListener {
      _firebaseUser.value = it.user
    }
  }

  fun loginWithGoogle() {
    auth?.signInAnonymously()?.addOnSuccessListener {
      _firebaseUser.value = it.user
      _membershipState.update { state ->
        state.copy(isSubscribed = true, userEmail = it.user?.email ?: "google_user@rebelup.com")
      }
    }
  }

  fun signOut() {
    auth?.signOut()
    _membershipState.update { 
      it.copy(isSubscribed = false, userEmail = "anonymous@rebelup.com") 
    }
  }

  fun signUp(email: String, password: String = "secure-rebel") {
    // In a real app, this would use auth.createUserWithEmailAndPassword
    auth?.createUserWithEmailAndPassword(email, password)?.addOnSuccessListener {
      _firebaseUser.value = it.user
      _membershipState.update { state ->
        state.copy(userEmail = email, isSubscribed = true) 
      }
    }
  }

  // --- PAYMENT PORTAL (STRIPE, PAYPAL, MOBILE PAY, E-TRANSFER) ---

  private val _paymentReceipts = MutableStateFlow<List<PaymentReceipt>>(
    listOf(
      PaymentReceipt(
        transactionId = "TXN_STRIPE_INIT_904",
        amount = "$30.00",
        paymentMethod = PaymentMethodType.STRIPE_CARD,
        dateText = "Active Billing Period",
        planPurchased = "Standard Monthly + 18+ Premium Upgrade",
        isVipWaiver = false
      )
    )
  )
  val paymentReceipts: StateFlow<List<PaymentReceipt>> = _paymentReceipts.asStateFlow()

  fun processPayment(
    method: PaymentMethodType,
    amount: String,
    plan: PlanType,
    with18PlusUpgrade: Boolean
  ): PaymentReceipt {
    val isVip = _membershipState.value.isLifetimeVip
    val finalAmount = if (isVip) "$0.00 (Lifetime VIP)" else amount
    val txnId = "TXN_${method.name.take(6)}_${UUID.randomUUID().toString().take(8).uppercase()}"

    val receipt = PaymentReceipt(
      transactionId = txnId,
      amount = finalAmount,
      paymentMethod = method,
      dateText = "Just now",
      planPurchased = "${plan.title} ${if (with18PlusUpgrade) "+ 18+ VIP Upgrade" else ""}",
      isVipWaiver = isVip
    )

    _paymentReceipts.update { listOf(receipt) + it }

    updateMembership(
      plan = if (isVip) PlanType.LIFETIME_VIP else plan,
      hasPremium18Plus = isVip || with18PlusUpgrade
    )

    return receipt
  }

  // --- AI GUARDIAN: BOT & SCAMMER DEFENSE + ID VERIFICATION ---

  private val _botScanStatus = MutableStateFlow(
    BotScanStatus(
      safeScore = 99,
      isBotDetected = false,
      scanSummary = "AI Sentinel Shield Active • 0 Scammers / 0 Bots Detected",
      scannedProfilesCount = 142
    )
  )
  val botScanStatus: StateFlow<BotScanStatus> = _botScanStatus.asStateFlow()

  private val _idVerificationState = MutableStateFlow(
    IdVerificationState(
      isVerified = true,
      idType = "Government Photo ID (Verified 18+)",
      verifiedAt = "Verified Today",
      faceMatchConfidence = 99
    )
  )
  val idVerificationState: StateFlow<IdVerificationState> = _idVerificationState.asStateFlow()

  private val _aiGuardianChat = MutableStateFlow(
    listOf(
      AiGuardianMessage(
        id = "ai_msg_1",
        text = "🛡️ Sentinel AI Guardian online. I protect your account by neutralizing fake profiles, bots, cashapp extortionists, and unverified accounts in real time. How can I assist with your safety today?",
        isAi = true,
        time = "Active",
        suggestedActions = listOf(
          "Scan recent direct requests for bots",
          "Verify safety rules for threesomes & orgies",
          "Check my AI Verified ID badge status"
        )
      )
    )
  )
  val aiGuardianChat: StateFlow<List<AiGuardianMessage>> = _aiGuardianChat.asStateFlow()

  fun submitIdVerification(idType: String) {
    _idVerificationState.update {
      it.copy(
        isVerified = true,
        idType = idType,
        verifiedAt = "Just verified by Sentinel AI",
        faceMatchConfidence = 99,
        isPendingVerification = false
      )
    }
    _currentUserProfile.update { it.copy(verified = true) }
  }

  fun sendAiGuardianQuestion(question: String) {
    val userMsg = AiGuardianMessage(
      id = "usr_${UUID.randomUUID().toString().take(6)}",
      text = question,
      isAi = false,
      time = "Just now"
    )

    val aiResponseText = when {
      question.contains("scan", ignoreCase = true) || question.contains("bot", ignoreCase = true) -> {
        "🔍 Full biometric and semantic scan complete. All 6 profiles in your area and active direct requests have passed real-human verification. Zero commercial bots, zero cryptocurrency solicitations, and zero unverified third parties found."
      }
      question.contains("safe", ignoreCase = true) || question.contains("threesome", ignoreCase = true) || question.contains("orgy", ignoreCase = true) -> {
        "🔐 Safety Protocol for Group Activities: 1) Verify gold AI ID checkmarks. 2) Conduct a preliminary video chat in private messages. 3) Meet in a public cocktail bar prior to attending private suites or manors. 4) Clarify consent words and non-negotiables beforehand."
      }
      question.contains("verify", ignoreCase = true) || question.contains("badge", ignoreCase = true) -> {
        "✅ Your account is 100% verified with biometric face-match confidence of 99%. Your profile displays the Gold Shield Verification badge to all members."
      }
      else -> {
        "🛡️ Sentinel AI has analyzed your inquiry. Our automated defense filters are continuously screening incoming direct sex requests, preventing catfishing, bot spoofing, and spam."
      }
    }

    val aiMsg = AiGuardianMessage(
      id = "ai_${UUID.randomUUID().toString().take(6)}",
      text = aiResponseText,
      isAi = true,
      time = "Just now",
      suggestedActions = listOf("Scan area for bots", "Safety guide for play parties")
    )

    _aiGuardianChat.update { it + userMsg + aiMsg }
  }

  // --- AI CREATIVE TOOLS (PORNO MAKER, FACE SWAP, IMAGE EDITOR) ---

  val faceSwapTemplates = listOf(
    FaceSwapTemplate(
      id = "tmpl_1",
      title = "Penthouse VIP Jacuzzi",
      category = "Sensual Luxury",
      previewRes = R.drawable.couple_profile_hero,
      gradientColors = Pair(0xFFFF2A85, 0xFF7B2CBF),
      description = "Step into a luxury downtown jacuzzi suite alongside champagne and candlelit ambience."
    ),
    FaceSwapTemplate(
      id = "tmpl_2",
      title = "Gothic Boudoir & Velvet Bed",
      category = "Goth & Alt",
      previewRes = R.drawable.goth_profile_hero,
      gradientColors = Pair(0xFF7209B7, 0xFF3A0CA3),
      description = "Immerse yourself in lace corsets, dark ambient lighting, velvet cushions, and alt eroticism."
    ),
    FaceSwapTemplate(
      id = "tmpl_3",
      title = "Masquerade Orgy Manor",
      category = "Group & Manor",
      previewRes = R.drawable.vip_vault_banner,
      gradientColors = Pair(0xFF9D4EDD, 0xFFFF0054),
      description = "Gold filigree Venetian mask and opulent royal playroom stage with multiple partners."
    )
  )

  private val initialAiCreations = listOf(
    AiGeneratedMedia(
      id = "gen_1",
      title = "Midnight Velvet Fantasy",
      prompt = "Sensual couple and goth partner in silk sheets, dim candlelight, cinematic lighting, 8k",
      stylePreset = "Noir Velvet",
      toolCategory = AiToolCategory.PORNO_SCENE_MAKER,
      dateText = "Generated Today",
      imageRes = R.drawable.couple_profile_hero,
      gradientColors = Pair(0xFFFF2A85, 0xFF5A189A),
      isAdult18Plus = true
    ),
    AiGeneratedMedia(
      id = "gen_2",
      title = "Raven's Alt Dungeon Shoot",
      prompt = "Goth emo model with lace corset, neon purple underglow, vinyl boots, sensual alt glamour",
      stylePreset = "Gothic Emo Alt",
      toolCategory = AiToolCategory.PORNO_SCENE_MAKER,
      dateText = "Generated Yesterday",
      imageRes = R.drawable.goth_profile_hero,
      gradientColors = Pair(0xFF4361EE, 0xFF7209B7),
      isAdult18Plus = true
    ),
    AiGeneratedMedia(
      id = "gen_3",
      title = "VIP Penthouse Alter-Ego",
      prompt = "Face swapped with luxury penthouse masquerade template",
      stylePreset = "Photoreal Face Swap",
      toolCategory = AiToolCategory.SWAP_FACES,
      dateText = "Generated 2 days ago",
      imageRes = R.drawable.vip_vault_banner,
      gradientColors = Pair(0xFFF72585, 0xFF4CC9F0),
      isAdult18Plus = true
    )
  )

  private val _aiCreations = MutableStateFlow<List<AiGeneratedMedia>>(initialAiCreations)
  val aiCreations: StateFlow<List<AiGeneratedMedia>> = _aiCreations.asStateFlow()

  private val initialLiveStreams = listOf(
    LiveStream(
      id = "stream_1",
      broadcasterName = "Mistress Raven",
      broadcasterType = UserType.SINGLE_FEMALE,
      title = "Goth Dungeon Fetish Party & Q&A",
      viewerCount = 1420,
      priceToJoin = "$5",
      category = "Fetish / BDSM",
      previewRes = R.drawable.goth_profile_hero,
      gradientColors = Pair(0xFF7209B7, 0xFF4361EE),
      messages = listOf(
        StreamMessage("m1", "KinkMaster", "Love the vibe!"),
        StreamMessage("m2", "GothLover", "Sent 10 credits!", isTip = true, tipAmount = "$10"),
        StreamMessage("m3", "DarkSoul", "When is the next scene starting?")
      )
    ),
    LiveStream(
      id = "stream_2",
      broadcasterName = "The Elite Couple",
      broadcasterType = UserType.COUPLE,
      title = "Penthouse VIP Afterparty - Interactive Play",
      viewerCount = 2850,
      priceToJoin = "$10",
      category = "Threesome / Group",
      previewRes = R.drawable.couple_profile_hero,
      gradientColors = Pair(0xFFFF2A85, 0xFFF72585),
      messages = listOf(
        StreamMessage("m4", "VipWatcher", "Incredible penthouse view!"),
        StreamMessage("m5", "LoverGuy", "Tips $25 for a request!", isTip = true, tipAmount = "$25")
      )
    )
  )

  private val _liveStreams = MutableStateFlow<List<LiveStream>>(initialLiveStreams)
  val liveStreams: StateFlow<List<LiveStream>> = _liveStreams.asStateFlow()

  val availableGifts = listOf(
    VirtualGift("g1", "Rose", "🌹", 10, "A simple token of appreciation"),
    VirtualGift("g2", "Champagne", "🍾", 50, "Celebrate the moment"),
    VirtualGift("g3", "VIP Crown", "👑", 200, "For the ultimate broadcaster"),
    VirtualGift("g4", "Luxury Car", "🏎️", 1000, "The ultimate statement gift"),
    VirtualGift("g5", "Heart", "❤️", 5, "Send some love"),
    VirtualGift("g6", "Fire", "🔥", 25, "The stream is heating up!")
  )

  fun sendStreamGift(streamId: String, gift: VirtualGift) {
    _liveStreams.update { streams ->
      streams.map { stream ->
        if (stream.id == streamId) {
          val newMsg = StreamMessage(
            id = UUID.randomUUID().toString(),
            senderName = "Me",
            text = "Sent a ${gift.name}!",
            gift = gift
          )
          stream.copy(messages = stream.messages + newMsg)
        } else stream
      }
    }
  }

  fun sendStreamTip(streamId: String, amount: String) {
    _liveStreams.update { streams ->
      streams.map { stream ->
        if (stream.id == streamId) {
          val newMsg = StreamMessage(
            id = UUID.randomUUID().toString(),
            senderName = "Me",
            text = "Sent a tip!",
            isTip = true,
            tipAmount = amount
          )
          stream.copy(messages = stream.messages + newMsg)
        } else stream
      }
    }
  }

  fun sendStreamMessage(streamId: String, text: String) {
    _liveStreams.update { streams ->
      streams.map { stream ->
        if (stream.id == streamId) {
          val newMsg = StreamMessage(
            id = UUID.randomUUID().toString(),
            senderName = "Me",
            text = text
          )
          stream.copy(messages = stream.messages + newMsg)
        } else stream
      }
    }
  }

  fun generateAdultScene(
    title: String,
    prompt: String,
    stylePreset: String,
    category: AiToolCategory = AiToolCategory.PORNO_SCENE_MAKER
  ): AiGeneratedMedia {
    val sampleRes = when {
      prompt.contains("goth", ignoreCase = true) || prompt.contains("emo", ignoreCase = true) -> R.drawable.goth_profile_hero
      prompt.contains("orgy", ignoreCase = true) || prompt.contains("manor", ignoreCase = true) -> R.drawable.vip_vault_banner
      else -> R.drawable.couple_profile_hero
    }

    val newMedia = AiGeneratedMedia(
      id = "ai_media_${UUID.randomUUID().toString().take(6)}",
      title = title.ifBlank { "Sensual Fantasy Scene" },
      prompt = prompt,
      stylePreset = stylePreset,
      toolCategory = category,
      dateText = "Generated Just Now (Unlimited)",
      imageRes = sampleRes,
      gradientColors = Pair(0xFFFF2A85, 0xFF00F0FF),
      isAdult18Plus = true
    )

    _aiCreations.update { listOf(newMedia) + it }
    return newMedia
  }

  fun performFaceSwap(template: FaceSwapTemplate, userPhotoCaption: String): AiGeneratedMedia {
    val result = AiGeneratedMedia(
      id = "swap_${UUID.randomUUID().toString().take(6)}",
      title = "Face Swap: ${template.title}",
      prompt = "Seamless face-matched identity mapped into ${template.title}",
      stylePreset = "Ultra HD 3D Mesh Swap",
      toolCategory = AiToolCategory.SWAP_FACES,
      dateText = "Generated Just Now (Unlimited)",
      imageRes = template.previewRes,
      gradientColors = template.gradientColors,
      isAdult18Plus = true
    )

    _aiCreations.update { listOf(result) + it }
    return result
  }

  fun applyImageEdit(originalTitle: String, filterName: String): AiGeneratedMedia {
    val editedMedia = AiGeneratedMedia(
      id = "edit_${UUID.randomUUID().toString().take(6)}",
      title = "$originalTitle ($filterName)",
      prompt = "Enhanced with $filterName",
      stylePreset = filterName,
      toolCategory = AiToolCategory.IMAGE_EDITOR,
      dateText = "Edited Just Now (Unlimited)",
      imageRes = R.drawable.couple_profile_hero,
      gradientColors = Pair(0xFF70E000, 0xFF00F0FF),
      isAdult18Plus = true
    )

    _aiCreations.update { listOf(editedMedia) + it }
    return editedMedia
  }
}

