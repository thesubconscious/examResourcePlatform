import axios, {AxiosError} from 'axios';

export class UserService{
    // private baseUrl :string = "http://localhost:8080"
    private baseUrl :string = "http://1.94.115.38:8080"

    getCookie(name: string): string | null {
        const value = `; ${document.cookie}`;
        console.log(value);
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop()!.split(';').shift()!;
        return null;
    }

    async login(email: string, password: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/users/login', {
                email,
                password
            },{
                withCredentials: true,
            });
            localStorage.setItem('userId', response.data.userId);
            return response.data;
        }catch (err){
            throw err as AxiosError;
        }
    }

    async register(email: string, name: string, password: string, identity: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/users/register', {
                email,
                name,
                password,
                identity
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }

    async getUserDetails(id: string): Promise<any> {
        try{
            const response = await axios.get(this.baseUrl+'/users/'+id);
            console.log(response.data);
            return response.data;
        }catch (err){
            throw err;
        }
    }

    async logout() {
        try{
            const response = await axios.post(this.baseUrl+'/users/logout', {
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }
}