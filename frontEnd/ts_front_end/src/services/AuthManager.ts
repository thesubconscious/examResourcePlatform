import {UserService} from "@/services/userService";

class AuthManager {
    private _checkTimer?: number;

    // 核心方法：带状态缓存的认证检查
    public async checkAuthStatus() {
        try {
            const userService = new UserService();
            const { expiresIn, isAuthenticated }  = await userService.authCheck();
            const serverTime = Date.now() + (expiresIn * 1000); // 假设expiresIn单位是秒
            const clientTime = Date.now() + 300_000;
            const actualExpire = Math.min(serverTime, clientTime);
            localStorage.setItem('auth_expire', actualExpire.toString());
            return isAuthenticated;
        } catch (error) {
            localStorage.removeItem('auth_expire');
            return false;
        }
    }

    // 启动定时检测（在登录成功后调用）
    public startPolling() {
        this.stopPolling(); // 防止重复启动

// 立即执行首次检查
        this.checkAuthStatus();

// 设置5分钟间隔
        this._checkTimer = window.setInterval(
            () => this.checkAuthStatus(),
            300_000
        );
    }

    // 停止检测（登出时调用）
    public stopPolling() {
        if (this._checkTimer) {
            localStorage.removeItem('auth_expire');
            clearInterval(this._checkTimer);
            this._checkTimer = undefined;
        }
    }

    // 实时登录态判断
    public get isLoggedIn() {
        return Number(localStorage.getItem('auth_expire')) > Date.now();
    }
}

export const authManager = new AuthManager();