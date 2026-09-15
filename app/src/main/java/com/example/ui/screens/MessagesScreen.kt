package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleDataRepository
import com.example.model.ChatConversation
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun MessagesScreen(
  onOpenUpgrade: () -> Unit
) {
  val chats by SampleDataRepository.chats.collectAsState()
  var activeChatId by remember { mutableStateOf<String?>(null) }

  val activeChat = chats.find { it.id == activeChatId }

  if (activeChat != null) {
    ChatConversationDetail(
      chat = activeChat,
      onBack = { activeChatId = null }
    )
  } else {
    ChatListView(
      chats = chats,
      onChatSelected = { activeChatId = it.id }
    )
  }
}

@Composable
fun ChatListView(
  chats: List<ChatConversation>,
  onChatSelected: (ChatConversation) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Top Bar
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(DesireDarkSurface)
        .padding(16.dp)
    ) {
      Column {
        Text(
          text = "DISCREET MESSAGING",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = NeonMagenta
        )
        Text(
          text = "Conversations & Matches",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
      }
    }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 90.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(chats, key = { it.id }) { chat ->
        ChatItemRow(chat = chat, onClick = { onChatSelected(chat) })
      }
    }
  }
}

@Composable
fun ChatItemRow(
  chat: ChatConversation,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(DesireCardSurface)
      .border(1.dp, DesireBorder, RoundedCornerShape(16.dp))
      .clickable(onClick = onClick)
      .padding(14.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      // Avatar
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(Brush.radialGradient(listOf(NeonMagenta, VelvetPurple)))
      ) {
        if (chat.targetPhotoRes != null) {
          Image(
            painter = painterResource(id = chat.targetPhotoRes),
            contentDescription = chat.targetName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(chat.targetType.icon, fontSize = 24.sp)
          }
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = chat.targetName,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
          )
          Text(
            text = chat.lastMessageTime,
            fontSize = 11.sp,
            color = TextMuted
          )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = chat.lastMessage,
            fontSize = 13.sp,
            color = if (chat.unreadCount > 0) TextPrimary else TextSecondary,
            fontWeight = if (chat.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1,
            modifier = Modifier.weight(1f)
          )

          if (chat.unreadCount > 0) {
            Box(
              modifier = Modifier
                .size(20.dp)
                .background(NeonMagenta, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${chat.unreadCount}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun ChatConversationDetail(
  chat: ChatConversation,
  onBack: () -> Unit
) {
  var inputText by remember { mutableStateOf("") }
  val listState = rememberLazyListState()
  val coroutineScope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(DesireBlack)
  ) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(DesireDarkSurface)
        .padding(horizontal = 8.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = onBack) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
      }

      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Brush.radialGradient(listOf(NeonMagenta, VelvetPurple)))
      ) {
        if (chat.targetPhotoRes != null) {
          Image(
            painter = painterResource(id = chat.targetPhotoRes),
            contentDescription = chat.targetName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        } else {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(chat.targetType.icon, fontSize = 18.sp)
          }
        }
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column {
        Text(
          text = chat.targetName,
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = TextPrimary
        )
        Text(
          text = "Active Now • Discreet Channel",
          fontSize = 11.sp,
          color = SuccessGreen
        )
      }
    }

    // Message List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(vertical = 12.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(chat.messages) { msg ->
        val isMe = msg.isFromMe
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
        ) {
          Box(
            modifier = Modifier
              .widthIn(max = 280.dp)
              .clip(
                RoundedCornerShape(
                  topStart = 16.dp,
                  topEnd = 16.dp,
                  bottomStart = if (isMe) 16.dp else 2.dp,
                  bottomEnd = if (isMe) 2.dp else 16.dp
                )
              )
              .background(
                if (isMe) Brush.horizontalGradient(listOf(NeonMagentaDark, NeonMagenta))
                else Brush.linearGradient(listOf(DesireCardSurfaceElevated, DesireCardSurfaceElevated))
              )
              .padding(12.dp)
          ) {
            Column {
              Text(
                text = msg.text,
                fontSize = 13.sp,
                color = Color.White,
                lineHeight = 18.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = msg.time,
                fontSize = 10.sp,
                color = if (isMe) Color.White.copy(alpha = 0.7f) else TextMuted,
                modifier = Modifier.align(Alignment.End)
              )
            }
          }
        }
      }
    }

    // Input Bar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(DesireDarkSurface)
        .navigationBarsPadding()
        .padding(horizontal = 12.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = inputText,
        onValueChange = { inputText = it },
        placeholder = { Text("Send discreet message...", fontSize = 13.sp, color = TextMuted) },
        modifier = Modifier.weight(1f),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = DesireCardSurface,
          unfocusedContainerColor = DesireCardSurface,
          focusedBorderColor = NeonMagenta,
          unfocusedBorderColor = DesireBorder,
          focusedTextColor = TextPrimary,
          unfocusedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(20.dp),
        maxLines = 3
      )

      Spacer(modifier = Modifier.width(8.dp))

      IconButton(
        onClick = {
          if (inputText.isNotBlank()) {
            SampleDataRepository.sendChatMessage(chat.id, inputText.trim())
            inputText = ""
            coroutineScope.launch {
              listState.animateScrollToItem(chat.messages.size)
            }
          }
        },
        modifier = Modifier
          .size(44.dp)
          .background(NeonMagenta, CircleShape)
      ) {
        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
      }
    }
  }
}
