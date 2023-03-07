import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

/**
 * This composable decreases font size
 * if it can't fit in its parent with given font size.
 *
 * @param autoSizeMinTextSize Stops decreasing font size when the font size is detected under or equal to
 * this value if this parameter is not equal to TextUnit.Unspecified.
 * @param autoSizeMaxTextSize If the composable decreased the font size to fit in the parent composable earlier
 * if there is place again, it increases font size until this value if this value is not TextUnit.Unspecified,
 */
@Composable
fun AutoSizedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    autoSizeMinTextSize: TextUnit = TextUnit.Unspecified,
    autoSizeMaxTextSize: TextUnit = TextUnit.Unspecified,
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
    val resizedFontSize = rememberResizedFontSize(fontSize, style)

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
            resizedFontSize.value = getAdjustedFontSize(
                hasVisualOverflow = it.hasVisualOverflow,
                currentFontSize = resizedFontSize.value,
                autoSizeMinTextSize = autoSizeMinTextSize,
                autoSizeMaxTextSize = autoSizeMaxTextSize,
                specifiedFontSize = getSpecifiedFontSize(fontSize, style),
            )
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
    autoSizeMinTextSize: TextUnit = TextUnit.Unspecified,
    autoSizeMaxTextSize: TextUnit = TextUnit.Unspecified,
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
    val resizedFontSize = rememberResizedFontSize(fontSize, style)

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
            resizedFontSize.value = getAdjustedFontSize(
                hasVisualOverflow = it.hasVisualOverflow,
                currentFontSize = resizedFontSize.value,
                autoSizeMinTextSize = autoSizeMinTextSize,
                autoSizeMaxTextSize = autoSizeMaxTextSize,
                specifiedFontSize = getSpecifiedFontSize(fontSize, style),
            )
            onTextLayout(it)
        },
        style = style
    )
}

@Composable
private fun rememberResizedFontSize(
    fontSize: TextUnit,
    style: TextStyle,
): MutableState<TextUnit> {
    return remember {
        if (fontSize != TextUnit.Unspecified) {
            mutableStateOf(fontSize)
        } else {
            mutableStateOf(style.fontSize)
        }
    }
}

private fun getSpecifiedFontSize(
    fontSize: TextUnit,
    style: TextStyle,
): TextUnit? {
    return if (fontSize != TextUnit.Unspecified) {
        fontSize
    } else if (style.fontSize != TextUnit.Unspecified) {
        style.fontSize
    } else null
}

private fun getAdjustedFontSize(
    hasVisualOverflow: Boolean,
    currentFontSize: TextUnit,
    autoSizeMinTextSize: TextUnit,
    autoSizeMaxTextSize: TextUnit,
    specifiedFontSize: TextUnit?,
): TextUnit {
    return if (hasVisualOverflow) {
        getDecreasedFontSize(
            currentFontSize = currentFontSize,
            autoSizeMinTextSize = autoSizeMinTextSize
        )
    } else {
        getIncreasedFontSize(
            currentFontSize = currentFontSize,
            autoSizeMaxTextSize = autoSizeMaxTextSize,
            specifiedFontSize = specifiedFontSize,
        )
    }
}

/**
 * @return If autoSizeMinTextSize is specified and lower than currentFontSize or
 * if autoSizeMinTextSize is not specified, returns a decreased font size.
 * Else, returns currentFontSize.
 */
private fun getDecreasedFontSize(
    currentFontSize: TextUnit,
    autoSizeMinTextSize: TextUnit,
): TextUnit {
    return if ((autoSizeMinTextSize != TextUnit.Unspecified && autoSizeMinTextSize < currentFontSize) || autoSizeMinTextSize == TextUnit.Unspecified) {
        currentFontSize * 0.9f
    } else currentFontSize
}

/**
 * @return If autoSizeMaxTextSize is specified and higher than currentFontSize,
 * it returns an increased value.
 *
 * @return If a font size is specified via fontSize or style parameters
 * and current font size is smaller than specified font size, it returns
 * an increased value. Else, it returns specifiedFontSize.
 */
private fun getIncreasedFontSize(
    currentFontSize: TextUnit,
    autoSizeMaxTextSize: TextUnit,
    specifiedFontSize: TextUnit?,
): TextUnit {
    if (specifiedFontSize != null && currentFontSize > specifiedFontSize) return specifiedFontSize

    if ((autoSizeMaxTextSize != TextUnit.Unspecified && autoSizeMaxTextSize > currentFontSize) || autoSizeMaxTextSize == TextUnit.Unspecified) {
        currentFontSize * 1.1f
    }

    return currentFontSize
}
