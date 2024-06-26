export interface LoginForm {
    email: string;
    password: string;
    isLoading: { value: boolean };
}

export interface RegisterForm {
    name: string;
    email: string;
    password: string;
    confirmPassword: string;
    identity: string;
    isLoading: { value: boolean };
}