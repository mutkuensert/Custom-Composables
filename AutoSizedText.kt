import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.createFontFamilyResolver
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
    val density = LocalDensity.current
    val context = LocalContext.current

    val calculateIntrinsics = {
        ParagraphIntrinsics(
            text = text,
            style = style,
            density = density,
            fontFamilyResolver = createFontFamilyResolver(context),
        )
    }

    var intrinsics = calculateIntrinsics()

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
            if (it.hasVisualOverflow) {
                if (intrinsics.maxIntrinsicWidth > it.size.width) {
                    resizedFontSize.value *= 0.9f
                    intrinsics = calculateIntrinsics()
                }
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
    val density = LocalDensity.current
    val context = LocalContext.current

    val calculateIntrinsics = {
        ParagraphIntrinsics(
            text = text.text,
            style = style,
            density = density,
            fontFamilyResolver = createFontFamilyResolver(context),
        )
    }

    var intrinsics = calculateIntrinsics()

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
            if (it.hasVisualOverflow) {
                if (intrinsics.maxIntrinsicWidth > it.size.width) {
                    resizedFontSize.value *= 0.9f
                    intrinsics = calculateIntrinsics()
                }
            }
            onTextLayout(it)
        },
        style = style
    )
}
