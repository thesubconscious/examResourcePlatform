import axios from 'axios';

export class UserService{
    private baseUrl :string = "http://localhost:8080/users"

    async login(email: string, password: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/login', {
                email,
                password
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }

    async register(email: string, name: string, password: string, identity: string): Promise<any> {
        try{
            const response = await axios.post(this.baseUrl+'/register', {
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
            const response = await axios.get(this.baseUrl+'/users/'+id, {
            });
            return response.data;
        }catch (err){
            throw err;
        }
    }

    getCookie(name: string): string | null {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop()!.split(';').shift()!;
        return null;
    }
}