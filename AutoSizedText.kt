import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun AutoSizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    scaleDownUntil: Double? = null,
    scaleUpUntil: Double? = null,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val resizedFontSize = remember {
        if (fontSize != TextUnit.Unspecified) {
            mutableStateOf(fontSize)
        } else {
            mutableStateOf(style.fontSize)
        }
    }
    val fontScale = LocalDensity.current.fontScale

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = resizedFontSize.value,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        onTextLayout = {
            if (it.hasVisualOverflow && ((scaleDownUntil != null && scaleDownUntil < resizedFontSize.value.value / fontScale) || scaleDownUntil == null)) {
                resizedFontSize.value *= 0.9f
            } else if (
                (fontSize != TextUnit.Unspecified && fontSize.isSp && fontSize.value < resizedFontSize.value.value)
                || (fontSize == TextUnit.Unspecified && style.fontSize.value < resizedFontSize.value.value)
                && ((scaleUpUntil != null && scaleUpUntil > resizedFontSize.value.value / fontScale) || scaleUpUntil == null)
            ) {
                resizedFontSize.value *= 1.1f
            }
            onTextLayout(it)
        },
        style = style
    )
}

@Composable
fun AutoSizedText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    scaleDownUntil: Double? = null,
    scaleUpUntil: Double? = null,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val resizedFontSize = remember {
        if (fontSize != TextUnit.Unspecified) {
            mutableStateOf(fontSize)
        } else {
            mutableStateOf(style.fontSize)
        }
    }
    val fontScale = LocalDensity.current.fontScale

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = resizedFontSize.value,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            if (it.hasVisualOverflow && ((scaleDownUntil != null && scaleDownUntil < resizedFontSize.value.value / fontScale) || scaleDownUntil == null)) {
                resizedFontSize.value *= 0.9f
            } else if (
                (fontSize != TextUnit.Unspecified && fontSize.isSp && fontSize.value < resizedFontSize.value.value)
                || (fontSize == TextUnit.Unspecified && style.fontSize.value < resizedFontSize.value.value)
                && ((scaleUpUntil != null && scaleUpUntil > resizedFontSize.value.value / fontScale) || scaleUpUntil == null)
            ) {
                resizedFontSize.value *= 1.1f
            }
            onTextLayout(it)
        },
        style = style
    )
}
