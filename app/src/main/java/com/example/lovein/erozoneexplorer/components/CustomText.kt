package com.example.lovein.erozoneexplorer.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.lovein.ui.theme.helveticaFontFamily

@Composable
fun CustomText(
    text: String,
    playerNames: List<String>,
) {
    val annotatedText = buildStylizedText(text, playerNames)

    Text(
        text = annotatedText,
        color = Color.White,
        fontSize = 24.sp,
        fontFamily = helveticaFontFamily,
        textAlign = TextAlign.Center
    )
}

fun buildStylizedText(simpleText: String, stylizedTexts: List<String>): AnnotatedString {
    var remainingText = simpleText
    return buildAnnotatedString {
        for (stylizedText in stylizedTexts) {
            val startIndex = remainingText.indexOf(stylizedText)
            if (startIndex >= 0) {
                val endIndex = startIndex + stylizedText.length
                append(remainingText.substring(0, startIndex))

                pushStyle(
                    SpanStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                )
                append(remainingText.substring(startIndex, endIndex))
                pop()

                remainingText = remainingText.substring(endIndex)
            }
        }
        append(remainingText)
    }
}

@Preview
@Composable
private fun CustomTextPreview() {
    val text =
        "Elena, use water-based lubricant and gently insert your fingers into Denis's anus. Gently massage the prostate by making circular motions on the walls of the rectum. It is important to be careful, gentle, and considerate of your partner's comfort."
    val playerNames = listOf("Elena", "Denis")

    CustomText(
        text = text,
        playerNames = playerNames,
    )
}
