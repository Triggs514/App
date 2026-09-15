package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.data.SampleDataRepository
import com.example.model.*
import com.example.ui.theme.*
import com.example.util.SubscriptionManager
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells

@Composable
fun LiveStreamingScreen(
    membership: MembershipState,
    onOpenUpgrade: () -> Unit
) {
    val streams by SampleDataRepository.liveStreams.collectAsState()
    var activeStream by remember { mutableStateOf<LiveStream?>(null) }

    if (activeStream != null) {
        ActiveStreamView(
            stream = activeStream!!,
            onClose = { activeStream = null }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "LIVE PAY-AND-WATCH",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = GoldVip,
                letterSpacing = 2.sp
            )
            Text(
                text = "Join live sessions, send tips, and interact with top broadcasters",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(streams) { stream ->
                    StreamListCard(
                        stream = stream,
                        membership = membership,
                        onJoin = {
                            if (membership.canView18PlusContent) {
                                activeStream = it
                            } else {
                                onOpenUpgrade()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StreamListCard(
    stream: LiveStream,
    membership: MembershipState,
    onJoin: (LiveStream) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DesireCardSurface),
        onClick = { onJoin(stream) }
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            // Live Stream Background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(stream.gradientColors.first), Color(stream.gradientColors.second))
                        )
                    )
            ) {
                if (stream.previewRes != null) {
                    Image(
                        painter = painterResource(id = stream.previewRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alpha = 0.6f
                    )
                }
            }

            // Overlay Info
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = SpicyRed,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("${stream.viewerCount}", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }

                Column {
                    Text(
                        text = stream.title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "with ${stream.broadcasterName} • ${stream.category}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Lock Overlay if needed
            if (!membership.canView18PlusContent) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = GoldVip, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "UNLOCK TO JOIN",
                            color = GoldVip,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Entry: ${stream.priceToJoin}",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveStreamView(
    stream: LiveStream,
    onClose: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var showGiftSheet by remember { mutableStateOf(false) }
    val currentStream by SampleDataRepository.liveStreams.collectAsState()
    val streamData = currentStream.find { it.id == stream.id } ?: stream

    if (showGiftSheet) {
        ModalBottomSheet(
            onDismissRequest = { showGiftSheet = false },
            containerColor = DesireCardSurface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White.copy(alpha = 0.3f)) }
        ) {
            GiftSelectionContent(
                onGiftSelected = { gift ->
                    SampleDataRepository.sendStreamGift(stream.id, gift)
                    showGiftSheet = false
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(DesireBlack)) {
        // Active Stream Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(stream.gradientColors.first), Color(stream.gradientColors.second))
                    )
                )
        ) {
             if (stream.previewRes != null) {
                Image(
                    painter = painterResource(id = stream.previewRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.4f
                )
            }
        }

        // Overlay Controls
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GoldVip)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.align(Alignment.Center))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(stream.broadcasterName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${streamData.viewerCount} Viewers", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Chat & Actions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Chat Messages
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp),
                    reverseLayout = true
                ) {
                    items(streamData.messages.reversed()) { msg ->
                        StreamMessageBubble(msg)
                    }
                }

                // Input Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Interact with stream...", fontSize = 13.sp, color = Color.White.copy(alpha = 0.6f)) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.Black.copy(alpha = 0.5f),
                            unfocusedContainerColor = Color.Black.copy(alpha = 0.5f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedBorderColor = NeonMagenta,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                SampleDataRepository.sendStreamMessage(stream.id, messageText)
                                messageText = ""
                            }
                        },
                        modifier = Modifier.background(NeonMagenta, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { showGiftSheet = true },
                        modifier = Modifier.background(GoldVip, CircleShape)
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = "Gift", tint = DesireBlack)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                             SampleDataRepository.sendStreamTip(stream.id, "$10")
                        },
                        modifier = Modifier.background(GoldVip, CircleShape)
                    ) {
                        Icon(Icons.Default.AttachMoney, contentDescription = "Tip", tint = DesireBlack)
                    }
                }
            }
        }
    }
}

@Composable
fun GiftSelectionContent(onGiftSelected: (VirtualGift) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "SEND A GIFT",
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = GoldVip,
            letterSpacing = 1.sp
        )
        Text(
            text = "Support the broadcaster with tokens & virtual gifts",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(SampleDataRepository.availableGifts.size) { index ->
                val gift = SampleDataRepository.availableGifts[index]
                GiftCard(gift = gift, onClick = { onGiftSelected(gift) })
            }
        }
    }
}

@Composable
fun GiftCard(gift: VirtualGift, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(gift.icon, fontSize = 28.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(gift.name, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text("${gift.tokenValue} TKN", color = GoldVip, fontSize = 10.sp)
        }
    }
}

@Composable
fun StreamMessageBubble(msg: StreamMessage) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
    ) {
        val isGift = msg.gift != null
        val bubbleColor = when {
            msg.isTip -> GoldVip.copy(alpha = 0.9f)
            isGift -> NeonMagenta.copy(alpha = 0.8f)
            else -> Color.Black.copy(alpha = 0.4f)
        }
        val textColor = if (msg.isTip || isGift) DesireBlack else Color.White

        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = msg.senderName,
                        color = if (msg.isTip || isGift) DesireBlack else GoldVip,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    if (isGift) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(msg.gift!!.icon, fontSize = 14.sp)
                    }
                }
                Text(
                    text = when {
                        msg.isTip -> "Tipped ${msg.tipAmount}! ${msg.text}"
                        isGift -> "Sent ${msg.gift!!.name}! ${msg.text}"
                        else -> msg.text
                    },
                    color = textColor,
                    fontSize = 13.sp
                )
            }
        }
    }
}
