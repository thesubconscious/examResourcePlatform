import axios, {AxiosError} from 'axios';
import {authManager} from "@/services/AuthManager";

export class UserService {
    private apiClient = axios.create({
        baseURL: import.meta.env.VITE_API_BASE,
        timeout: 5000,
        headers: { 'Content-Type': 'application/json' },
        withCredentials: true
    })

    // constructor() {
    //     // 请求前注入token（也许以后会需要）
    //     this.apiClient.interceptors.request.use(config => {
    //         const token = this.getCookie('JWT_TOKEN')
    //         if(token) config.headers.Authorization = `Bearer ${token}`
    //         return config
    //     })
    // }

    getCookie(name:string): string | null {
        // 当前后端使用了httponly，因此暂时用不上
        const value = document.cookie
            .split('; ')
            .find(row => row.startsWith(`${name}=`))
            ?.split('=')[1] ?? null

        // console.log('[Cookie嗅探]', name, value ? '√存在' : '×缺失')
        return value
    }

    async login(email: string, password: string): Promise<any> {
        try{
            const {data} = await this.apiClient.post('/users/login',{email,password})
            localStorage.setItem('userId', data.userId);
            // authManager.userId = data.userId;
            authManager.startPolling()
            return data
        }catch (err){
            throw new Error(`登录失败：${(err as AxiosError).message}`)
        }
    }

    async register(email:string, name:string, password:string, identity:string){
        try {
            const { data } = await this.apiClient.post('/users/register', {
                email, name, password, identity
            })
            return data
        } catch(err) {
            throw new Error(`注册失败：${(err as AxiosError).message}`)
        }
    }

    async getUserDetails(id:string){
        try {
            const { data } = await this.apiClient.get(`/users/${id}`)
            console.debug('用户详情:', data)
            return data
        } catch(err) {
            throw new Error(`获取详情失败：${(err as AxiosError).message}`)
        }
    }

    async logout(){
        try {
            const { data } = await this.apiClient.post('/users/logout')
            localStorage.removeItem('userId')
            authManager.stopPolling()
            return '成功登出'
        } catch(err) {
            throw new Error(`注销失败：${(err as AxiosError).message}`)
        }
    }

    async authCheck() {
        try {
            const { data } = await this.apiClient.get('/users/check')
            return data
        } catch (err) {
            throw new Error(`认证检查失败: ${(err as AxiosError).message}`)
        }
    }
}