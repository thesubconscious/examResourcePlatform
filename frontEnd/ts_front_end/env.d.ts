/// <reference types="vite/client" />
interface ImportMetaEnv {
    readonly VITE_API_BASE: string;
    // 其他自定义环境变量...
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}