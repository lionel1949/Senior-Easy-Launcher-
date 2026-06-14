package com.oldman.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oldman.launcher.ui.theme.LunarDateBlue
import com.oldman.launcher.utils.LunarCalendarUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * 日期时间栏 Composable
 *
 * 显示三行内容，垂直居中排列:
 * - 第一行: 当前时间 HH:mm (64sp 加粗，最醒目)
 * - 第二行: 阳历日期 "yyyy年MM月dd日 星期X" (32sp)
 * - 第三行: 农历日期 "农历 丙午年 四月廿九" (28sp，深蓝色)
 *
 * **刷新机制**: 使用 LaunchedEffect + ticker Flow，每 60 秒自动刷新一次。
 * 刷新由 Composable 生命周期自动管理——离开屏幕时自动停止，返回时重新开始。
 */
@Composable
fun DateTimeBar(modifier: Modifier = Modifier) {
    // 存储当前时间戳，每次更新触发重组
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    // ── 60秒定时器 ──
    // tickerFlow 每60秒发射一次，触发 UI 刷新
    LaunchedEffect(Unit) {
        flow {
            while (true) {
                emit(System.currentTimeMillis())
                delay(60_000L)  // 60秒
            }
        }.collect { millis ->
            currentTimeMillis = millis
        }
    }

    // ── 计算当前时间 ──
    val calendar = remember(currentTimeMillis) {
        Calendar.getInstance().also { it.timeInMillis = currentTimeMillis }
    }

    val timeFormatter = remember {
        SimpleDateFormat("HH:mm", Locale.CHINA)
    }
    val dateFormatter = remember {
        SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
    }

    val timeString = remember(currentTimeMillis) {
        timeFormatter.format(calendar.time)
    }
    val dateString = remember(currentTimeMillis) {
        dateFormatter.format(calendar.time)
    }
    val weekdayName = remember(currentTimeMillis) {
        LunarCalendarUtils.getWeekdayName(calendar)
    }

    // ── 计算农历 ──
    val lunarDate = remember(currentTimeMillis) {
        LunarCalendarUtils.gregorianToLunar(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── 第一行: 时间 (64sp) ──
        Text(
            text = timeString,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // ── 第二行: 阳历日期 + 星期 (32sp) ──
        Text(
            text = "$dateString $weekdayName",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // ── 第三行: 农历日期 (28sp, 深蓝色) ──
        Text(
            text = lunarDate.fullName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = LunarDateBlue,
            textAlign = TextAlign.Center
        )
    }
}
