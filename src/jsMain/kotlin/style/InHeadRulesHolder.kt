package style

import kotlinx.browser.document
import org.jetbrains.compose.web.css.CSSRuleDeclaration
import org.jetbrains.compose.web.css.CSSRuleDeclarationList
import org.jetbrains.compose.web.css.CSSRulesHolder
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.css.CSSStyleSheet


class InHeadRulesHolder : CSSRulesHolder {
    private val style: HTMLStyleElement = document.createElement("style") as HTMLStyleElement
    private val rules = ArrayList<CSSRuleDeclaration>()
    override val cssRules: CSSRuleDeclarationList get() = rules

    init {
        document.head!!.append(style)
    }

    override fun add(cssRule: CSSRuleDeclaration) {
        rules.add(cssRule)

        (style.sheet as? CSSStyleSheet)?.let { cssStylesheet ->
            setCSSRules(cssStylesheet, cssRules)
        }
    }
}
