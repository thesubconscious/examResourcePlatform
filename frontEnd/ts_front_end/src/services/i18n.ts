import { createI18n } from 'vue-i18n'

const loadLocaleMessages = async () => {
    const modules = import.meta.glob('@/assets/i18n/*.json')
    const messages: Record<string, any> = {}

    for (const path in modules) {
        const matched = path.match(/([A-Za-z-]+)\.json$/i)
        if (matched && matched[1]) {
            const locale = matched[1]
            messages[locale] = await modules[path]()
        }
    }

    return messages
}

const savedLang = localStorage.getItem('userLang')
const browserLang = navigator.language.startsWith('zh') ? 'zh-CN' : 'en'
const i18n = createI18n({
    legacy: false,
    locale: savedLang || browserLang || 'zh-CN', // 优先级：存储 > 浏览器 > 默认
    fallbackLocale: 'en',
    messages: await loadLocaleMessages()
})

export default i18n